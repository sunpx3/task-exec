package com.sunpx.quartz_one.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.groovy.transform.tailrec.ReturnStatementToIterationConverter;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.sunpx.quartz_one.article.ArticleFetchUrl;
import com.sunpx.quartz_one.bean.AppArticle;
import com.sunpx.quartz_one.bean.AppAuthorUpdateEntity;
import com.sunpx.quartz_one.bean.ArticleUpdateStatusEntity;
import com.sunpx.quartz_one.bean.ReturnMsg;
import com.sunpx.quartz_one.util.Keys;
import com.sunpx.quartz_one.util.UUIDUtils;
import com.xxl.job.core.log.XxlJobLogger;

import junit.framework.Assert;

@RestController
public class IndexController {
	
	@Value("${xxl.job.executor.ip}")
	private String ip;

	@Value("${server.port}")
	private int port;
	
	
	@Autowired
	private JdbcTemplate JdbcTemplate;

	@RequestMapping("/")
	public String index() {
		
		return "Hello XX-Job!";
	}
	
	/**
	 * 检查该作者文章是否更新
	 */
	@RequestMapping("/checkAutorArticleUpdateStatus")
	@ResponseBody
	public String checkAuthorArticleUpdateStatus(HttpServletRequest request) {
		
		String appId = request.getParameter("appId");
		String articleMd5 = request.getParameter("articleMd5");
		
		ReturnMsg returnMsg = new ReturnMsg();
		
		if(StringUtils.isEmpty(appId)) {
			returnMsg.setSuccess(Keys.ERROR);
			returnMsg.setMsg("Method:checkAuthorArticleUpdateStatus AppId不能为空！");
			XxlJobLogger.log("Method:checkAuthorArticleUpdateStatus AppId不能为空！");
			
		}
		else if(StringUtils.isEmpty(articleMd5)) {
			returnMsg.setSuccess(Keys.ERROR);
			returnMsg.setMsg("Method:articleMd5 articleMd5 不能为空！");
			XxlJobLogger.log("Method:articleMd5 articleMd5 不能为空！");
		}
		
		RowMapper<ArticleUpdateStatusEntity> rowMapper = new BeanPropertyRowMapper<ArticleUpdateStatusEntity>(ArticleUpdateStatusEntity.class);
		
		String sql = "select id ,app_id , last_article_hash ,timestamp , remark from app_article_update_status where app_id = ?";
		List<ArticleUpdateStatusEntity> articleStatus = JdbcTemplate.query(sql , rowMapper,appId);
		
		
		if(articleStatus.size() > 0) {
			ArticleUpdateStatusEntity articleEntity = articleStatus.get(0);
			String articleHash = articleEntity.getLastArticleHash();
			if("".equals(articleHash) || articleHash == null) {
				updateHash(articleMd5  , appId);
			}else {
				//判断文章是否更新
				if(articleMd5.equals(articleEntity.getLastArticleHash())) {
					//无更新
					System.out.println("-------------当前文章无更新----------");
				}else {
					//有更新，调用爬虫开始抓取
					System.out.println("-------------当前文章有更新，请调用抓取程序----------");
					
					
					ArticleFetchUrl fetchObj;
					try {
						fetchObj = new ArticleFetchUrl(appId , ip , port);
						Thread thread = new Thread(fetchObj);
						thread.start();
						//抓取后调用更新程序
						updateHash(articleMd5  , appId);
					} catch (UnknownHostException e) {
						System.out.println("IP获取出现异常,请检查....");
						e.printStackTrace();
					}
					
				}
			}
		}else {
			
			String uuid = UUIDUtils.getUUID();
			long timestamp = System.currentTimeMillis();
			//程序首次启动，初始化数据。
			String sqlInsert = "insert into app_article_update_status(id ,app_id , last_article_hash ,timestamp , remark) values(?,?,?,?,?)";
			int res = JdbcTemplate.update(sqlInsert , uuid , appId , articleMd5 , String.valueOf(timestamp),"");
			if(res > 0) {
				System.out.println("初始化数据中...插入成功,Id:" + appId);
			}else {
				System.out.println("初始化数据中...插入失败,Id:" + appId);
			}
		}
		
		return JSONObject.toJSON(returnMsg).toString();
		
	}
	
	public void updateHash(String articleHash , String appId) {
		//更新当前hash数据库
		int res = JdbcTemplate.update("update app_article_update_status set last_article_hash = ? where app_id = ?",articleHash , appId);
		if(res > 0) {
			System.out.println("更新当前Hash成功,Hash:" + articleHash);
		}else {
			System.out.println("更新当前Hash失败,Hash:" + articleHash);
		}
	}
	
	
	
	@RequestMapping("/checkArticleHashIsExists")
	@ResponseBody
	public boolean checkArticleHashIsExists(HttpServletRequest request) {
		String articleHash = request.getParameter("articleHash");
		
		String sql = "select count(1) from app_article where article_title_hash = ?";
		String[] params = {articleHash};
		
		int ret = JdbcTemplate.queryForObject(sql , params ,Integer.class);
		if(ret > 0) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping("/saveArticleInfo")
	@ResponseBody
	public void saveArticleInfo(@RequestBody AppArticle appArticle) {
		
		String sql = "insert into app_article(id , title , url , read_nums , comment_nums ,article_title_hash , timestamp,app_id) values(?,?,?,?,?,?,?,?)";
		try {
			if(appArticle != null) {
				int ret = JdbcTemplate.update(sql, appArticle.getId(),appArticle.getTitle(),appArticle.getUrl(),appArticle.getReadNums() , appArticle.getCommentNums() , appArticle.getArticleTitleHash() , appArticle.getTimeStamp(),appArticle.getAppId());
				if(ret > 0) {
					System.out.println("Title:" + appArticle.getTitle() +"----保存成功!");
				}else {
					System.out.println("Title:" + appArticle.getTitle() +"----保存失败!");
				}
			}
		} catch (Exception e) {
			System.out.println("发生异常:" + e.getMessage());
		}
		
	}
	
	@RequestMapping("/updateAuthorName")
	@ResponseBody
	public void updateAuthorName(@RequestParam("authorName") String authorName,
			@RequestParam("appId") String appId) {
		
		String updateSql ="update  app_author_name_consult set author_name = ? where app_id = ?";
		String insertSql = "insert into app_author_name_consult (id , app_id , author_name , timestamp) values(?,?,?,?)";
		String selectSql = "select count(1) from app_author_name_consult where app_id = ?";
		
		try {			
			String[] params = {appId};
			if(!authorName.equals("") && !appId.equals("")) {
				int ret = JdbcTemplate.queryForObject(selectSql, params , Integer.class);
				if(ret > 0) {
					int updateRet = JdbcTemplate.update(updateSql, authorName ,appId);
					if(updateRet > 0) {
						System.out.println("----作者名称更新成功-----" + authorName);
					}else {
						System.out.println("----作者名称更新失败-----" + authorName);
					}
				}else {
					int insertRet = JdbcTemplate.update(insertSql, UUIDUtils.getUUID() , appId , authorName , System.currentTimeMillis());
					if(insertRet > 0) {
						System.out.println("------添加新作者名称---:" + authorName);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}

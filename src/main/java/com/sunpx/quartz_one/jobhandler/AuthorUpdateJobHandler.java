package com.sunpx.quartz_one.jobhandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sunpx.quartz_one.config.JobContext;
import com.sunpx.quartz_one.httputil.HttpGetArticleToMd5Util;
import com.sunpx.quartz_one.httputil.HttpUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value="authorUpdateName")
@Component
public class AuthorUpdateJobHandler extends IJobHandler{

	
	 @Value("${xxl.job.executor.ip}")
     private String ip;

     @Value("${server.port}")
     private int port;
	
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		
		ServletContext sc = JobContext.getInstance().getContext();
		Set<String>  authorSets = (Set<String>) sc.getAttribute("authorUidList");
		
		for (String appId : authorSets) {
			String reString = HttpUtil.sendHttpGetHtml("https://author.baidu.com/profile?context=%7B%22from%22:0,%22app_id%22:%22" + appId + "%22%7D&pagelets[]=root", null);
			String reString2 = reString.substring(reString.indexOf("{"), reString.lastIndexOf("}") + 1);
			String html = JSONObject.parseObject(reString2).get("html").toString();
			String html2 = new String(html.getBytes("ISO-8859-1"),"UTF-8");
			Document document = Jsoup.parse(html2);
			String authorName = document.select("div[class='name-item'] div[class='name']").text();
			
			Map<String, String> params = new HashMap<>();
			params.put("appId", appId);
			params.put("authorName", authorName);
			
			if(ip.equals("")) {
				InetAddress addr = InetAddress.getLocalHost();  
		        ip  = addr.getHostAddress().toString(); //获取本机ip			
			}
			HttpUtil.sendHttpRequest("http://" + ip + ":" + port + "/updateAuthorName",params);
			
		}
		return SUCCESS;
	}

}

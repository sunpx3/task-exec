package com.sunpx.quartz_one.article;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.sunpx.quartz_one.bean.AppArticle;
import com.sunpx.quartz_one.httputil.HttpGetArticleToMd5Util;
import com.sunpx.quartz_one.httputil.HttpUtil;
import com.sunpx.quartz_one.util.UUIDUtils;

public class ArticleFetchUrl implements Runnable {


	private String ip;
	private int port;

	private String appId;

	public ArticleFetchUrl(String appId , String ip , int port) throws UnknownHostException {
		this.appId = appId;
		if(ip.equals("")) {
			InetAddress addr = InetAddress.getLocalHost();  
	        this.ip  = addr.getHostAddress().toString(); //获取本机ip			
		}
		this.port = port;
	}

	public String getAppId() {
		return appId;
	}

	@Override
	public void run() {
		System.out.println("正在抓取数据....当前抓取AppId为:" + appId);

		String jsonHtml3 = HttpGetArticleToMd5Util.httpGet(getAppId());

		Document doc = Jsoup.parse(jsonHtml3);
		Elements rows = doc.select("ul[class='c-news-text'] li");

		for (int i = 0; i < rows.size(); i++) {
			String title = rows.get(i).select("h2").text();
			String titleHash = Hashing.md5().newHasher().putString(title, Charsets.UTF_8).hash().toString();
			Map<String, String> params = new HashMap<>();
			String reString = HttpUtil.sendHttpRequest(ip, String.valueOf(port), "checkArticleHashIsExists", params);
			// 远程调用数据库，判断该文章是否存在。
			if (reString.trim().equals("true")) {
				System.out.println(reString);
			} else {
				// 保存文章信息
				String readNums = rows.get(i).select("div[class='pv']").text();
				String commentNums = rows.get(i).select("div[class='comment']").text();
				String articleUrl = rows.get(i).select("a").attr("href");

				AppArticle articleObj = new AppArticle();
				articleObj.setId(UUIDUtils.getUUID());
				articleObj.setTitle(title);
				articleObj.setUrl(articleUrl);
				articleObj.setReadNums(readNums);
				articleObj.setCommentNums(commentNums);
				articleObj.setArticleTitleHash(titleHash);
				articleObj.setTimeStamp(String.valueOf(System.currentTimeMillis()));
				
				articleObj.setAppId(appId);

				try {
					HttpUtil.sendHttpRequestPost(ip, String.valueOf(port), "saveArticleInfo",JSONObject.toJSON(articleObj));
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}

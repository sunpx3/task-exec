package com.sunpx.quartz_one.httputil;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class HttpGetArticleToMd5Util {

	public static String getArticleToMd5(String appId) throws UnsupportedEncodingException {
		
		String jsonHtml3 = httpGet(appId);

		Document doc = Jsoup.parse(jsonHtml3);
    	Elements rows = doc.select("h2");
    	String abstractStr = rows.get(0).text();
		String md5Str = Hashing.md5().newHasher().putString(abstractStr, Charsets.UTF_8).hash().toString();
		
		return md5Str;
	}

	public static String httpGet(String appId) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "https://author.baidu.com/pipe?context=%7B%22app_id%22%3A%22" + appId + "%22%7D&pagelets%5B%5d=article&reqID=1";

		HttpGet getObj = new HttpGet(url);
		getObj.setHeader("Cookie", CookiesUtil.getCookies());
		//getObj.setHeader("Accept","text/html");
		getObj.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Redmi Note 4 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/10.7 light/1.0 baiduboxapp/10.7.0.10 (Baidu; P1 6.0)");
		getObj.setHeader("Host","author.baidu.com");
 
		
		try {
			CloseableHttpResponse response = httpClient.execute(getObj);
			if(response.getStatusLine().toString().contains("200")) {
				String jsonRes = EntityUtils.toString(response.getEntity());
				String jsonHtml = jsonRes.substring(jsonRes.indexOf("{"), jsonRes.lastIndexOf("}") + 1);
				String jsonHtml2 = JSONObject.parseObject(jsonHtml).get("html").toString();
				String jsonHtml3 = new String(jsonHtml2.getBytes("ISO-8859-1"),"utf-8");
				
				return jsonHtml3;
			}
		} catch (ClassCastException e) {
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
}

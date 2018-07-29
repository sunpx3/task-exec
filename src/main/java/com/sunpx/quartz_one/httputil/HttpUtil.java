package com.sunpx.quartz_one.httputil;

import java.io.IOException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;


public class HttpUtil {

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param method
	 * @param params 无参数给NULL即可
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String sendHttpRequest(String ip , String port , String method ,Map<String ,String> params) {
		
		int paramsIndex = 0;
		StringBuffer buffer = new StringBuffer();
		
		String url = "http://" + ip + ":" + port + "/" + method;
		buffer.append(url);
		
		if(params != null) {
			for (String key : params.keySet()) {
				if(paramsIndex == 0) {
					buffer.append("?" + key + "=" + params.get(key));
				}else {
					buffer.append("&" + key + "=" + params.get(key));
				}
				
				
				paramsIndex++;
			}
		}
		
		
		return buffer.toString(); 
	}  
	
	public static String sendHttpRequest(String url ,Map<String ,String> params) throws ClientProtocolException, IOException {
		
		int paramsIndex = 0;
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(url);
		
		if(params != null) {
			for (String key : params.keySet()) {
				if(paramsIndex == 0) {
					buffer.append("?" + key + "=" + params.get(key));
				}else {
					buffer.append("&" + key + "=" + params.get(key));
				}
				paramsIndex++;
			}
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(buffer.toString());
		httpClient.execute(httpGet);
		
		return buffer.toString(); 
	} 
	
	public static void sendHttpRequestPost(String ip , String port , String method ,Object objEntity) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://" + ip + ":" + port + "/saveArticleInfo");
		
		StringEntity entity = new StringEntity(JSONObject.toJSONString(objEntity) , "UTF-8");
		entity.setContentEncoding("UTF-8");
		post.setEntity(entity);
		entity.setContentType("application/json");//发送json数据需要设置contentType
		CloseableHttpResponse response = httpClient.execute(post);
		
	}
	
	/**
	 * 默认80端口port参数为null即可。
	 * @param url
	 * @param port
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendHttpGetHtml(String url , String port) throws ClientProtocolException, IOException {
		
		if(!url.contains("http")) {
			url  = "http://" + url;
		}
		if(port != null) {
			url += ":" + port;
		}
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
		httpGet.setHeader("Host","author.baidu.com");
		httpGet.setHeader("Cookie","BAIDUID=C1C83F43186EF803CBEFC664C5806DFB:FG=1; BIDUPSID=C1C83F43186EF803CBEFC664C5806DFB; PSTM=1521962780; BDUSS=F1d2c3QXpnaEZVTDgteVRSZnN6NnhKVkZCZzVHdWlJTzBrMjlHV0xadWMyVkJiQVFBQUFBJCQAAAAAAAAAAAEAAADtLYY1yfHW3bulwapmcmVlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJxMKVucTClbeX; H_PS_PSSID=1445_21098_18560_20697_20927; PSINO=2");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		
		return EntityUtils.toString(response.getEntity());
	}

}

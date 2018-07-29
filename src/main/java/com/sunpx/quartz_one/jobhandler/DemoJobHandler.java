package com.sunpx.quartz_one.jobhandler;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunpx.quartz_one.config.JobContext;
import com.sunpx.quartz_one.httputil.HttpGetArticleToMd5Util;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="demoJobHandler")
@Component
public class DemoJobHandler extends IJobHandler {

	 @Value("${xxl.job.executor.ip}")
     private String ip;

     @Value("${server.port}")
     private int port;
	
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		
		ServletContext sc = JobContext.getInstance().getContext();
		Set<String>  authorSets = (Set<String>) sc.getAttribute("authorUidList");
		
		for (String appId : authorSets) {
			String articleMd5Str = HttpGetArticleToMd5Util.getArticleToMd5(appId);
			execRemoteCheckArticleUpdateStatus(appId , articleMd5Str);
		}
		return SUCCESS;
	}
	
	public void execRemoteCheckArticleUpdateStatus(String appId ,String articleHash) throws ClientProtocolException, IOException {
		if(ip.equals("")) {
			InetAddress addr = InetAddress.getLocalHost();  
	        ip  = addr.getHostAddress().toString(); //获取本机ip			
		}
		String url = "http://" + ip + ":" + port +"/checkAutorArticleUpdateStatus?appId=" + appId
				+"&articleMd5=" + articleHash;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpClient.execute(httpGet);
   
		System.out.println("查询中....当前请求:" + url);
	}

}

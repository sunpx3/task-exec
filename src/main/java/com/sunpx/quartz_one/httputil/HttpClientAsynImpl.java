//package com.sunpx.quartz_one.httputil;
//
//import java.io.IOException;
//import java.util.concurrent.CountDownLatch;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.util.EntityUtils;
//
//import com.google.common.util.concurrent.FutureCallback;
//
//public class HttpClientAsynImpl {
//
//	public void runAsynHttpGet() {
//		 	CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
//	        httpclient.start();
//
//	        final CountDownLatch latch = new CountDownLatch(1);
//	        final HttpGet request = new HttpGet("https://www.alipay.com/");
//
//	        System.out.println(" caller thread id is : " + Thread.currentThread().getId());
//
//	        httpclient.execute(request, new FutureCallback<HttpResponse>() {
//
//	            public void completed(final HttpResponse response) {
//	                latch.countDown();
//	                System.out.println(" callback thread id is : " + Thread.currentThread().getId());
//	                System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
//	                try {
//	                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
//	                    System.out.println(" response content is : " + content);
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//
//	            public void failed(final Exception ex) {
//	                latch.countDown();
//	                System.out.println(request.getRequestLine() + "->" + ex);
//	                System.out.println(" callback thread id is : " + Thread.currentThread().getId());
//	            }
//
//	            public void cancelled() {
//	                latch.countDown();
//	                System.out.println(request.getRequestLine() + " cancelled");
//	                System.out.println(" callback thread id is : " + Thread.currentThread().getId());
//	            }
//
//				@Override
//				public void onSuccess(HttpResponse result) {
//					// TODO Auto-generated method stub
//					
//				}
//
//				@Override
//				public void onFailure(Throwable t) {
//					// TODO Auto-generated method stub
//					
//				}
//
//	        });
//	        try {
//	            latch.await();
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//
//	        try {
//	            httpclient.close();
//	        } catch (IOException ignore) {
//
//	        }
//	    }
//	
//}

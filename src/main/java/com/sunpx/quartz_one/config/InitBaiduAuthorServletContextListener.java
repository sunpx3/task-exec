package com.sunpx.quartz_one.config;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitBaiduAuthorServletContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		Set<String> authorUidList = new HashSet<>();
		authorUidList.add("1569367671639834");
		authorUidList.add("1588035128419781");
		authorUidList.add("1589565597040333");
		authorUidList.add("1549419995112995");
		authorUidList.add("1536769836689790");
		authorUidList.add("1570179901067133");
		authorUidList.add("1582674260175929");
		authorUidList.add("1565343144901875");
		sc.getServletContext().setAttribute("authorUidList", authorUidList);
		//Servlet存放到静态变量中
		JobContext.getInstance().setContext(sc.getServletContext());  
	}

}

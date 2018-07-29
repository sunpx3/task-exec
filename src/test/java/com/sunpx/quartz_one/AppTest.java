package com.sunpx.quartz_one;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;
import com.sunpx.quartz_one.httputil.CookiesUtil;
import com.sunpx.quartz_one.httputil.HttpUtil;

public class AppTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {

//		String URL = "jdbc:mysql://127.0.0.1:3306/xxl-job?useUnicode=true&characterEncoding=utf-8";
//		String USER = "root";
//		String PASSWORD = "root";
//		// 1.加载驱动程序
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			// 2.获得数据库链接
//			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
//			Statement st = conn.createStatement();
//			ResultSet rs = st.executeQuery("select * from app_article;");
//			String title = new String("".getBytes(),"GB2312");
//			st.execute("insert into app_article (id , title , url , read_nums , comment_nums , article_title_hash , timestamp) values('1XXASDFASDFHHG' , '杨幂傻逼，怒怼范丞丞，范冰冰神回应。','http://www.baidu.com','100人','10评论','ASDFASDF234523ASDFSA','100232323');");
//			// 4.处理数据库的返回结果(使用ResultSet类)
////			while (rs.next()) {
////				System.out.println(rs.getString("title"));
////			}
//
//			// 关闭资源
//			rs.close();
//			st.close();
//			conn.close();
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
	
		
//		String reString = HttpUtil.sendHttpGetHtml("https://author.baidu.com/profile?context=%7B%22from%22:0,%22app_id%22:%22" + "1582674260175929" + "%22%7D&pagelets[]=root", null);
//		String reString2 = reString.substring(reString.indexOf("{"), reString.lastIndexOf("}") + 1);
//		String html = JSONObject.parseObject(reString2).get("html").toString();
//		String html2 = new String(html.getBytes("ISO-8859-1"),"UTF-8");
//		Document document = Jsoup.parse(html2);
//		String authorName = document.select("div[class='name-item'] div[class='name']").text();
//		
//		Map<String, String> params = new HashMap<>();
//		params.put("appId", "1582674260175929");
//		params.put("authorName", authorName);
//		HttpUtil.sendHttpRequest("192.168.89.2", String.valueOf(8080), "updateAuthorName", params);
		
		long timestamp = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(timestamp);
		System.out.println(time);
		
		
	}
}

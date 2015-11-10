package com.linpeng.netutil;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 网络操作工具类
 * @author linpeng123l
 *
 */
public class NetUtil { 
	public final static  String UESRAGENT_PHONE = "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A405 Safari/8536.25"; 
	
	public static String postAndGetDaet(String url){
		String response=null;
		System.out.println(url);
		try{
			HttpPost httpPost=new HttpPost(url);
			httpPost.setHeader("User-Agent", UESRAGENT_PHONE);
			DefaultHttpClient httpClient=new DefaultHttpClient();
			HttpResponse httpResponse=httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				response=EntityUtils.toString(httpResponse.getEntity());
			}
		}catch (Exception e) {
			System.out.println("error ");
			response="connect_error";
			e.printStackTrace();
		}
		return response;
	}

}

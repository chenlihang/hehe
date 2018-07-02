package cn.com.boomhope.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.com.boomhope.common.exception.LogicException;

import com.google.gson.Gson;

public class HttpRequestUtil {
	private static Gson gson = new Gson();
	public static int HTTP_POST = 0;
	public static int HTTP_GET = 1;
	public static int HTTP_DEFAULT_TIMEOUT = 2000;
	private static Logger log = Logger.getLogger(HttpRequestUtil.class);
	
	public static String request(String requestUrl, Map<String, Object> requestMap, int method, long timeout, String encode) throws LogicException {
		HttpClient client = null;
		try {
			//创建客户端实例
			client = new DefaultHttpClient();
			//设置请求参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", gson.toJson(requestMap)));
			//绑定请求
			if(method == HTTP_POST){
				return doHttpPost(client, requestUrl, requestMap, method, timeout, encode);
			}else{
				return doHttpGet(client, requestUrl, requestMap, method, timeout, encode);
			}
		} catch (Exception e) {
			log.error("请求失败", e);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	private static String doHttpPost(HttpClient client, String requestUrl, Map<String, Object> requestMap, int method, long timeout, String encode) throws Exception {
        //创建客户端实例
		HttpPost post = new HttpPost(requestUrl);
        //设置请求参数
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("params", gson.toJson(requestMap)));
        //设置超时时间
        // client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//连接时间
        // client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);//数据传输时间
		//绑定请求
        post.setEntity(new UrlEncodedFormEntity(nvps, encode));
        //进行请求
    	HttpResponse httpResponse = client.execute(post); 
    	if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    		return EntityUtils.toString(httpResponse.getEntity());
		}
		return null;
	}

	private static String doHttpGet(HttpClient client, String requestUrl, Map<String, Object> requestMap, int method, long timeout, String encode) throws Exception{
		 //创建客户端实例
		HttpGet get = new HttpGet(requestUrl);
        //设置超时时间
        //client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);//连接时间
       // client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);//数据传输时间
        //进行请求
    	HttpResponse httpResponse = client.execute(get); 
    	if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    		return EntityUtils.toString(httpResponse.getEntity());
		}
		return null;
	}
}

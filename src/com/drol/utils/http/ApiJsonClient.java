package com.drol.utils.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.drol.utils.JsonUtils;

public class ApiJsonClient {

	public static ApiJsonClient httpApiClient;

	private Map<String, String> httpHeader;

	private ApiJsonClient() {}

	public static ApiJsonClient getInstance() {
		if (httpApiClient == null)
			httpApiClient = new ApiJsonClient();
		
		return httpApiClient;
	}
	
	public void setHeader(String key, String val) {
		if(this.httpHeader == null) {
			this.httpHeader = new HashMap<String, String>();
		}
		
		this.httpHeader.put(key, val);
		
	}
	
	public void cleanHeader() {
		this.httpHeader.clear();
	}
	
	private void initHeader(AbstractHttpMessage abhm) {
		if(this.httpHeader != null) {
			this.httpHeader.put("charset", "UTF-8");
			String[]keyArray = this.httpHeader.keySet().toArray(new String[]{});
			for(String key : keyArray) {
				abhm.addHeader(key, httpHeader.get(key));
			}
		}
	}

	public Map<String, Object> post(String api, Map<String, String> parameters) {

		Map<String, Object> result = new HashMap<String, Object>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		// 创建httppost
		HttpPost httpPost = new HttpPost(api);
		initHeader(httpPost);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		String[] keyArray = parameters.keySet().toArray(new String[]{});
		for(String key : keyArray) {
			formparams.add(new BasicNameValuePair(key, parameters.get(key)));  
		}
		
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);
//			System.out.println("executing request " + httpPost.getURI());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					
					String restr = EntityUtils.toString(entity, "UTF-8");
					result = JsonUtils.Json2Map(restr);
					
//					System.out.println("--------------------------------------");
//					System.out.println("Response content: " + restr);
//					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/** 
     * 发送 get请求 
     */  
    public Map<String, Object> get(String api, Map<String, String> parameters) {  
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	
        try {  
            
            StringBuffer sb = new StringBuffer(api);
            String[] keyArray = parameters.keySet().toArray(new String[]{});
            for(int i=0; i < keyArray.length; i++) {
            	String key = keyArray[i];
            	if(api.indexOf("?") == -1 && i == 0)
            		sb.append(String.format("?%s=%s", key, parameters.get(key))); 
            	else
            		sb.append(String.format("&%s=%s", key, parameters.get(key))); 
    		}
            
            // 创建httpget.    
            HttpGet httpGet = new HttpGet(sb.toString());
            initHeader(httpGet);
//            System.out.println("executing request " + httpGet.getURI()); 
    		
            // 执行get请求.    
            CloseableHttpResponse response = httpClient.execute(httpGet);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
//                System.out.println("--------------------------------------");  
                // 打印响应状态    
//                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                	String restr = EntityUtils.toString(entity, "UTF-8");
					result = JsonUtils.Json2Map(restr);
                	
                    // 打印响应内容长度    
//                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
//                    System.out.println("Response content: " + restr);  
                }  
//                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpClient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
        
        return result;
        
    } 
    
    /** 
     * 上传文件 
     */  
    public Map<String, Object> upload(String api, Map<String, Object> parameters) {  
    	
    	Map<String, Object> result = new HashMap<String, Object>();
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        
        try {  
            HttpPost httpPost = new HttpPost(api);  
            initHeader(httpPost);
            
            String[] keyArray = parameters.keySet().toArray(new String[]{});
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    		for(String key : keyArray) {
    			Object val = parameters.get(key);
    			if(val instanceof File) {
    				FileBody fileBody = new FileBody((File)val);
    				multipartEntityBuilder.addPart(key, fileBody);
    				
    			} else if(val instanceof String) {
    				StringBody stringBody = new StringBody(String.valueOf(val), ContentType.TEXT_PLAIN); 
    				multipartEntityBuilder.addPart(key, stringBody);
    			}
    		}
    		
            HttpEntity multipartEntity = multipartEntityBuilder.build();  
            httpPost.setEntity(multipartEntity);  
  
//            System.out.println("executing request " + httpPost.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute(httpPost);  
            try {  
            	HttpEntity resEntity = response.getEntity();
				
//                System.out.println("----------------------------------------");  
//                System.out.println(response.getStatusLine());  
                  
                if (resEntity != null) { 
                	String restr = EntityUtils.toString(resEntity, "UTF-8");
    				result = JsonUtils.Json2Map(restr);
    				
//                    System.out.println("Response content length: " + resEntity.getContentLength()); 
//                    System.out.println("Response content: " + restr);  
                }  
                EntityUtils.consume(resEntity);  
//                System.out.println("----------------------------------------");  
                
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        
        return result;
        
    } 

}

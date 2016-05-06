package com.app.linj.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by zhangshenglan on 16/5/3.
 */
public class HttpUtil {

    public static JSONObject sendGetResquest(String path) {
        JSONObject result = null;
        String strResult;
        HttpGet httpGet = new HttpGet(path);                           //创建一个GET方式的HttpRequest对象
        DefaultHttpClient httpClient = new DefaultHttpClient();        //创建一个默认的HTTP客户端
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);               //执行GET方式的HTTP请求
            int reponseCode = httpResponse.getStatusLine().getStatusCode();        //获得服务器的响应码
            if (reponseCode == HttpStatus.SC_OK) {
                strResult  = EntityUtils.toString(httpResponse.getEntity());
                result = new JSONObject(strResult);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject sendPostResquest(String path) {
        JSONObject result = null;
        String strResult;
        HttpPost httpPost = new HttpPost(path);                           //创建一个GET方式的HttpRequest对象
        DefaultHttpClient httpClient = new DefaultHttpClient();        //创建一个默认的HTTP客户端
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);               //执行GET方式的HTTP请求
            int reponseCode = httpResponse.getStatusLine().getStatusCode();        //获得服务器的响应码
            if (reponseCode == HttpStatus.SC_OK) {
                strResult  = EntityUtils.toString(httpResponse.getEntity());
                result = new JSONObject(strResult);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args){

    }
}

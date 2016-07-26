package com.example.katherine_qj.news;

import android.net.http.HttpResponseCache;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Katherine-qj on 2016/7/24.
 */
public class NetWorkClass {
    public String getDataByGet(String url){
        Log.e("qwe","content");
        String content ="";
        HttpClient httpClient = new DefaultHttpClient();
        Log.e("qwe","content1");
        /*使用HttpClient发送请求、接收响应很简单，一般需要如下几步即可。
        1. 创建HttpClient对象。
        2. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
        3. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HetpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
        4. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
        5. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。程序可通过该对象获取服务器的响应内容。
        6. 释放连接。无论执行方法是否成功，都必须释放连接*/
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
           // HttpReponse是服务器接收到浏览器的请求后，处理返回结果常用的一个类。
            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                /*getStatusLine()
               获得此响应的状态行。状态栏可以设置使用setstatusline方法之一，也可以在构造函数初始化*/
                InputStream is = httpResponse.getEntity().getContent();
                /*getEntity()
                获取此响应的消息实体，如果有。实体是通过调用setentity提供。*/
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null){
                    content += line;
                }
            }
        }catch (IOException e)
        {
            Log.e("http",e.toString());
        }
        return  content;
    }
}

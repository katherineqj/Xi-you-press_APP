package com.example.katherine_qj.news;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PullListView pullListView;
    private List<News> list;
    private MainAdapter mainAdapter;
    private String newsUrl;
    private String nextPage;
    private String url = "http://www.xiyou.edu.cn/index/xy21";
    private  String content;
    private int num ;      //总页数
    private boolean isGetMore = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        MainThread mt = new MainThread(newsUrl);
        final Thread t = new Thread(mt, "MainThread");
        t.start();

        pullListView.setOnRefreshListener(new PullListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isGetMore = false;
                MainThread mt = new MainThread(newsUrl);
                Thread t = new Thread(mt, "MainThread");
                t.start();

            }
        });

        pullListView.setOnGetMoreListener(new PullListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
                isGetMore = true;
                if (num > 1) {
                    MainThread mt = new MainThread(nextPage);
                    Thread t = new Thread(mt, "MainThread");
                    t.start();
                }

            }
        });
        pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("url",list.get(position-1).getUrl());
                startActivity(intent);

            }
        });

    }
    public void InitView(){
        pullListView = (PullListView)findViewById(R.id.pulllist);
        list = new ArrayList<News>();
        newsUrl = url+".htm";
    }
     private final android.os.Handler handler = new android.os.Handler(){
       public  void  handleMessage(Message msg){
           switch (msg.what){
               case 111:
                   analyseHTML();
                   if(isGetMore){
                        mainAdapter.notifyDataSetChanged();
           /*每一次notifyDataSetChange()都会引起界面的重绘。当需要修改界面上View的相关属性的时候，
             最后先设置完成再调用notifyDataSetChange()来重绘界面。*/
                   }else {
                       mainAdapter = new MainAdapter(MainActivity.this, list);
                       pullListView.setAdapter(mainAdapter);
                   }
                   pullListView.refreshComplete();
                   pullListView.getMoreComplete();
                   break;
           }
       }
     };
    public class MainThread implements  Runnable{
        private String url;
        public MainThread(String url){
            this.url = url;
        }
        @Override
        public void run() {
            NetWorkClass netWorkClass =new NetWorkClass();
            content = netWorkClass.getDataByGet(url);
            Log.e("qwe",content);
            handler.sendEmptyMessage(111);
        }
    }
    public void analyseHTML(){
         if(content!=null){
             int x= 0;
             Document  document = Jsoup.parse(content);
             //解析HTML字符串
             if (!isGetMore) {
                 list.clear();
                 Element element = document.getElementById("fanye3942");
                 String text = element.text();
                 System.out.print(text);
                 num = Integer.parseInt(text.substring(text.lastIndexOf('/') + 1, text.length() - 1));
                 System.out.print(num);
             }
                 Elements elements = document.getElementsByClass("c3942");
                 while(true){
                    if(x==elements.size()){
                        System.out.print(elements.size());
                        break;
                    }
                     News news = new News();
                     news.setTitle(elements.get(x).attr("title"));
                     news.setUrl(elements.get(x).attr("href"));
                    // list.add(news);
                     if (!isGetMore||x>10){
                         list.add(news);
                         if(x>=25){
                             break;
                         }
                     }
                     x++;

                 }
                 if (num>1){
                     nextPage = url+"/"+ --num+".htm";
                     System.out.println("qqqqqqqqqqq"+nextPage);
                 }

             }
         }

}

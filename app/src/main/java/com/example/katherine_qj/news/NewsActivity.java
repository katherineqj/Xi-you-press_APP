package com.example.katherine_qj.news;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Katherine-qj on 2016/7/25.
 */
public class NewsActivity extends Activity {
    private TextView textTitle;
    private TextView textEdit;
    private TextView textDetail;
    private String title;
    private String edit;
    private String  detail;
    private StringBuilder text;
    private String url;
    private Document document;
    private String  content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        InitView();
        url=getIntent().getStringExtra("url");
        Log.e("qqq",url);
        NewsThread newsThread = new NewsThread(url);

        final Thread t = new Thread(newsThread,"NewsActivity");
        t.start();

    }
    public void InitView(){
        textTitle =(TextView)findViewById(R.id.textTitle);
        textEdit =(TextView)findViewById(R.id.textEdit);
        textDetail = (TextView)findViewById(R.id.textDetail);
    }
    private final android.os.Handler handler = new android.os.Handler(){
      public  void handleMessage(Message msg){
          if(msg.what==1001){
              document = Jsoup.parse(content);
              analyseHTML(document);
              textTitle.setText(title);
              textEdit.setText(edit);
              textDetail.setText(text);
          }

      }
    };
    public class  NewsThread implements  Runnable{
        String url;
        public NewsThread(String url){
            this.url = url;
        }
        @Override
        public void run() {

            NetWorkClass netWorkClass = new NetWorkClass();
            content = netWorkClass.getDataByGet(url);
            System.out.print("qqq"+content);
            handler.sendEmptyMessage(1001);
        }


    }
    public void analyseHTML(Document document){
        if (document!=null){
            Element element = document.getElementById("nrys");
            Elements elements = element.getAllElements();
            title = elements.get(1).text();
            edit = elements.get(4).text();
            Element mElement = document.getElementById("vsb_content_1031");
            if(mElement != null) {
                Elements mElements = mElement.getAllElements();
                text = new StringBuilder();
                for (Element melement : mElements) {
                    if(melement.className().equals("nrzwys") || melement.tagName().equals("strong")){
                        continue;
                    }

                    if(!melement.text().equals(" ") && !melement.text().equals(""));{
                        text.append("  ").append(melement.text()).append("\n");
                    }
                    if (melement.className().equals("vsbcontent_end")) {
                        break;
                    }
                }
            }


        }
    }
}

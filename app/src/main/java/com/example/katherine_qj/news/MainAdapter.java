package com.example.katherine_qj.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Katherine-qj on 2016/7/24.
 */
public class MainAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<News> list;

    public MainAdapter(Context context, List<News> list){
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout linear;
        ViewHolder viewHolder;
        if(convertView == null){
            linear = (FrameLayout)layoutInflater.inflate(R.layout.news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) linear.findViewById(R.id.newsTitle);
            linear.setTag(viewHolder);
        } else {
            linear = (FrameLayout)convertView;
            viewHolder = (ViewHolder)linear.getTag();
        }
        viewHolder.title.setText(list.get(position).getTitle());
        return linear;
    }

    class ViewHolder{
        TextView title;
    }
}

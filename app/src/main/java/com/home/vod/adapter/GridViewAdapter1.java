package com.home.vod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.home.vod.R;
import com.home.vod.activity.ImageLoader;

import java.util.ArrayList;

public class GridViewAdapter1 extends BaseAdapter {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private static LayoutInflater inflater=null;
    private ArrayList<String> urls = new ArrayList<>();
    public ImageLoader imageLoader;

    public GridViewAdapter1(Context mContext, ArrayList<String> url, int layoutResourceId) {


        this.mContext = mContext;
        this.urls = url;
        this.layoutResourceId = layoutResourceId;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(layoutResourceId, null);

            holder = new ViewHolder();

            holder.imageView=(ImageView)vi.findViewById(R.id.itemImage);
            holder.titleTextView=(TextView)vi.findViewById(R.id.itemTitle);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );


            //holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);

        } else {
            holder=(ViewHolder)vi.getTag();
        }

        //GridItem item = mGridData.get(position);
        //holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        //Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        imageLoader.DisplayImage(urls.get(position), holder.imageView);
        holder.titleTextView.setVisibility(View.GONE);
        return vi;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
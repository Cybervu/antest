package com.home.vod.adapter;

/**
 * Created by MUVI on 10/6/2017.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.model.SeasonModel;
import com.home.vod.util.FontUtls;

import java.util.ArrayList;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SeasonModel> data = new ArrayList<SeasonModel>();
    private int layoutResourceId;
    private  OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.seasonTitle);
            FontUtls.loadFont(mContext, mContext.getResources().getString(R.string.regular_fonts), title);
            thumbnail = (ImageView) view.findViewById(R.id.seasonImageView);
        }
    }

    public SeasonAdapter(Context context, int layoutResourceId,
                         ArrayList<SeasonModel> data) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.listener=listener;
    }


    public interface OnItemClickListener {
        void onItemClick(SeasonModel item,int pos);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_card_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SeasonModel album = data.get(position);
        holder.title.setText(album.getSeasonName());
        holder.thumbnail.setImageResource(album.getSeasonImage());
       /* holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(position),position);
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
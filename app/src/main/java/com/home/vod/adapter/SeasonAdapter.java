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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.model.SeasonModel;

import java.util.ArrayList;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SeasonModel> data = new ArrayList<SeasonModel>();
    private int layoutResourceId;
    private  OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout card_view;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.seasonTitle);
            thumbnail = (ImageView) view.findViewById(R.id.seasonImageView);
            card_view = (RelativeLayout) view.findViewById(R.id.cardLayout);

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
        void onItemClick(SeasonModel item, int pos);

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
       /* if(album.isSelected() == true){
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

            //  holder.card_view.setBackground(R.drawable.cardborder);

        }
        else {
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardunselectborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.seasonTextColor));
        }*/
        if(album.isSelected() == true){
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardunselectborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.seasonTextColor));
        }
        // holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardunselectborder));
           // holder.card_view.setCardBackgroundColor(sparseArray.valueAt(i).isSelected() ? Color.LTGRAY : Color.WHITE);
       // holder.card_view.setCardBackgroundColor(Color.WHITE);

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
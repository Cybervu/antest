package com.home.vod.adapter;

/**
 * Created by MUVI on 10/6/2017.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.model.WeekModel;
import com.home.vod.preferences.LanguagePreference;

import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<WeekModel> data = new ArrayList<WeekModel>();
    private int layoutResourceId;
    private  OnItemClickListener listener;
    LanguagePreference languagePreference;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout card_view;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.weekTitle);
            card_view = (RelativeLayout) view.findViewById(R.id.circularLayout);

        }
    }

    public WeekAdapter(Context context, int layoutResourceId,
                       ArrayList<WeekModel> data) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.listener=listener;
    }


    public interface OnItemClickListener {
        void onItemClick(WeekModel item, int pos);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_card_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        WeekModel album = data.get(position);
        holder.title.setText(album.getWeekName());
     //   holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_circle));
        languagePreference = LanguagePreference.getLanguagePreference(mContext);
//        holder.thumbnail.setImageResource(album.getSeasonImage());


        if (position  == 0) {
//            holder.card_view.setCardBackgroundColor(mContext.getResources().getDrawable(R.drawable.button_radious));
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_blue_circle));
        }else {
//            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_circle));

            try {

                int lineNum = position / 3;
                if(lineNum == 0){
                    holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_blue_circle));

                }else
                {
                    if(lineNum % 2 == 0){

                        holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_blue_circle));
                    }
                    else{
                        holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.week_circle));
                    }
                }
            }catch (Exception e){

            }

        }

        // For selection
       /* if(album.isSelected() == true){
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.card_view.setBackground(mContext.getResources().getDrawable(R.drawable.cardunselectborder));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.seasonTextColor));
        }*/

        // For selection

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
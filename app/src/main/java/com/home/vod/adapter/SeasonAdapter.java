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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.model.SeasonModel;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;

import java.util.ArrayList;

import static com.home.vod.preferences.LanguagePreference.DAYS_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DAYS_TITLE;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SeasonModel> data = new ArrayList<SeasonModel>();
    private int layoutResourceId;
    private  OnItemClickListener listener;
    LanguagePreference languagePreference;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout card_view;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.seasonTitle);
//            thumbnail = (ImageView) view.findViewById(R.id.seasonImageView);
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
        languagePreference = LanguagePreference.getLanguagePreference(mContext);
//        holder.thumbnail.setImageResource(album.getSeasonImage());

        Log.v("SUBHASS","pos ==== "+ position);

        if (position % 2 == 0) {
//            holder.card_view.setCardBackgroundColor(mContext.getResources().getDrawable(R.drawable.button_radious));
            holder.card_view.setBackgroundColor(mContext.getResources().getColor(R.color.week_background));
        }else{
            holder.card_view.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        FontUtls.loadFont(mContext,mContext.getResources().getString(R.string.semibold_fonts),holder.title);

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
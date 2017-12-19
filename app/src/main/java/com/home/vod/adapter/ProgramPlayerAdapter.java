package com.home.vod.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;

public class ProgramPlayerAdapter extends RecyclerView.Adapter<ProgramPlayerAdapter.ViewHolder> {

    int posT = 0;
    private String bannerImageUrl = "";
    private String showTitle = "";
    private String showStory = "";
    private String movieTrailerUrl = "";
    private String movieUniqueId;
    private int contentTypesId = 0;


    private Context context;
    private int layoutResourceId;
    private int isThirdParty = 0;
    private int pos = 0;
    private int nextPosition = 1;




    private  OnItemClickListener listener;
    private ArrayList<EpisodesListModel> data = new ArrayList<EpisodesListModel>();



    public interface OnItemClickListener {
        void onItemClick(EpisodesListModel item, int position);

    }


    public ProgramPlayerAdapter(Context context, int layoutResourceId,
                                ArrayList<EpisodesListModel> data,int pos,int nextPosition) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.movieUniqueId = movieUniqueId;
        this.pos = pos;
        this.nextPosition = nextPosition;


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView episodeTitleTextView;
        public TextView episodeNameTextView;
        public TextView episodeDateTextView,belowlineTextView,middleLineTextView;
        public View iView;
        public TextView episodeNowTextView;
        public ViewHolder(View view) {
            super(view);
            iView = view;
            episodeTitleTextView = (TextView) view.findViewById(R.id.itemTitle);
            belowlineTextView = (TextView) view.findViewById(R.id.belowlineTextView);
            middleLineTextView = (TextView) view.findViewById(R.id.middleLineTextView);
            FontUtls.loadFont(context,context.getResources().getString(R.string.regular_fonts),episodeTitleTextView);

           /* Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
            episodeTitleTextView.setTypeface(castDescriptionTypeface);*/
            //  episodeNameTextView = (TextView) view.findViewById(R.id.episodeNameTextView);
            //episodeDateTextView = (TextView) view.findViewById(R.id.itemImage);

            episodeNowTextView = (TextView) view.findViewById(R.id.itemNowTitle);


            //episodeImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView,episodeImageView.getDrawable().getIntrinsicWidth(),episodeImageView.getDrawable().getIntrinsicHeight()));
        }

        public void bind(final EpisodesListModel item, final OnItemClickListener listener) {
            episodeTitleTextView.setText(item.getEpisodeTitle());
            // episodeNameTextView.setText(item.getEpisodeNumber());

            String imageId = item.getEpisodeThumbnailImageView();
            LogUtil.showLog("MUVI","kjshdvuih");


           /* if(imageId.matches("") || imageId.matches(LanguagePreference.getLanguagePreference(context).getTextofLanguage(NO_DATA,DEFAULT_NO_DATA))){
                episodeImageView.setImageResource(R.drawable.logo);

            }else {
                Picasso.with(context)
                        .load(item.getEpisodeThumbnailImageView()).error(R.drawable.logo).placeholder(R.drawable.logo)
                        .into(episodeImageView);
            }
*/
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item,item.getTag());
                    LogUtil.showLog("Subhalaxmi","f"+item.getTag());
                }
            });*/
        }
    }

    public boolean isHeader(int position) {
        return position == 0;
    }






    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
        param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        // groupViewHolder.mImage.setText(labels.get(position - 1));
        posT = position;
        //int nextPosInt = nextPosition;

        Log.v("Subhalaxmi","position"+position);
        Log.v("Subhalaxmi","pos"+pos);
        Log.v("Subhalaxmi", "position" + nextPosition);

        holder.itemView.setLayoutParams(param);
        holder.itemView.setVisibility(View.VISIBLE);
        Log.v("SUBHASS","POs === "+pos + nextPosition + position);
        if (pos == position || nextPosition == position) {
            holder.itemView.setLayoutParams(param);
            holder.itemView.setVisibility(View.VISIBLE);


            if(pos == position){
                holder.episodeNowTextView.setText(" NOW ");
                holder.episodeNowTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.episodeTitleTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.middleLineTextView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.belowlineTextView.setVisibility(View.VISIBLE);

            }
            else if(nextPosition == position){
                holder.episodeNowTextView.setText(" NEXT ");
                holder.episodeNowTextView.setTextColor(context.getResources().getColor(R.color.player_next_linecolor));
                holder.episodeTitleTextView.setTextColor(context.getResources().getColor(R.color.player_next_linecolor));
                holder.middleLineTextView.setBackgroundColor(context.getResources().getColor(R.color.player_next_linecolor));
                holder.belowlineTextView.setVisibility(View.GONE);

            }

//            ((ViewHolder) holder).episodeNowTextView.setTextColor(R.color.colorPrimary);

        } else {
            param.height = 0;
            param.width = 0;
            Log.v("Subhalaxmi", "VISIBLE" + position);
            holder.itemView.setLayoutParams(param);
            holder.itemView.setVisibility(View.GONE);
        }
        holder.bind(data.get(position), listener);
    }


    @Override
    public int getItemCount() {
        int itemcount;
      /*  if (data.size()>4){
            itemcount = 4;
        }else{
            itemcount = data.size();
        }*/
        return  data.size();
    }
    public static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight){
        final int height = opt.outHeight;
        final int width = opt.outWidth;
        int sampleSize=1;
        if (height > reqHeight || width > reqWidth){
            final int halfWidth = width/2;
            final int halfHeight = height/2;
            while ((halfHeight/sampleSize) > reqHeight && (halfWidth/sampleSize) > reqWidth){
                sampleSize *=2;
            }

        }
        return sampleSize;
    }
}




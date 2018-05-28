package com.home.vod.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.home.vod.R;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ShowWithEpisodesActivity;
import com.home.vod.model.SingleItemModel;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    int layoutname = 0;



    PreferenceManager preferenceManager;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ProgressDialog videoPDialog;
    String videoUrlStr;
    //Register Dialog


    private EditText regEmailIdEditText;
    private EditText regPasswordEditText;
    private EditText regFullNameEditText;

    //Forgot Password Dialog


    private TextView validationIndicatorTextView;



    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    public static int sampleSize=100;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList, int layoutname) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutname = layoutname;
        preferenceManager =  PreferenceManager.getPreferenceManager(context);

    }
   /* @Override
    public int getItemViewType(int position) {
        return position;
    }*/
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    /*    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false));

        if (i == 2) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        }else{*/
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutname, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        //}
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SingleItemModel singleItem = itemsList.get(i);
        FontUtls.loadFont(mContext,mContext.getResources().getString(R.string.regular_fonts),holder.itemTitle);

      /*  Typeface castDescriptionTypeface = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.regular_fonts));
        holder.itemTitle.setTypeface(castDescriptionTypeface);*/

        Log.v("BIBHU12", "section adapter ==============" + i);

        holder.itemTitle.setText(singleItem.getTitle());
        holder.position = i;
        // holder.temPV.setTag(singleItem.get(i)); //For passing the list item index

        try{
            if (singleItem.getImage()!=null) {
           /* if (singleItem.getImage().equalsIgnoreCase("transparent")) {
                Picasso.with(mContext)
                        .load(R.drawable.logo)
                        .placeholder(R.drawable.transparent)   // optional
                        .error(R.drawable.transparent)      // optional
                        .into(holder.itemImage);
            }else {*/
                /*Picasso.with(mContext)
                        .load(singleItem.getImage())
                        .placeholder(R.drawable.logo)   // optional
                        .error(R.drawable.logo)      // optional
                        .into(holder.itemImage);*/


                Glide.with(mContext)
                        .load(singleItem.getImage())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .thumbnail(0.3f)
                        .into(holder.itemImage);
//            }
            }else{
                /*Picasso.with(mContext)
                        .load(R.drawable.logo)
                        .placeholder(R.drawable.logo)   // optional
                        .error(R.drawable.logo)      // optional
                        .into(holder.itemImage);*/

                Glide.with(mContext)
                        .load(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.itemImage);
            }
        }catch (Exception e){ Log.v("BIBHU12", "section adapter Exception==============" + e.toString());}


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected ImageView itemImage;
        protected View temPV;
        protected  int position;

        public SingleItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.itemImage.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), R.id.movieImageView,this.itemImage.getDrawable().getIntrinsicWidth(),this.itemImage.getDrawable().getIntrinsicHeight()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String moviePermalink = itemsList.get(position).getPermalink();
                    String movieTypeId = itemsList.get(position).getVideoTypeId();

                    LogUtil.showLog("MUVI","HHH"+moviePermalink + movieTypeId);
                    if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                        final Intent detailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(detailsIntent);

                    } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                        final Intent detailsIntent = new Intent(mContext, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(detailsIntent);

                    }


                }
            });


        }

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options opt =new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        opt.inSampleSize=sampleSize;
        BitmapFactory.decodeResource(res, resId, opt);
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight);
        opt.inJustDecodeBounds=true;
        return BitmapFactory.decodeResource(res, resId, opt);
    }
    public static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight){
        final int height = opt.outHeight*sampleSize;
        final int width = opt.outWidth*sampleSize;
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
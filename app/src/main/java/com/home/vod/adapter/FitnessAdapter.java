package com.home.vod.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.R;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.YogaPlayerActivity;
import com.home.vod.model.YogaItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WATCH_NOW;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.WATCH_NOW;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

/**
 * Created by Android on 12/6/2017.
 */

public class FitnessAdapter extends ArrayAdapter<YogaItem> {


    private Context context;
    private int layoutResourceId;
    private ArrayList<YogaItem> data = new ArrayList<YogaItem>();
    PreferenceManager preferenceManager;
    private String loggedInStr;
    LanguagePreference languagePreference;
    private ProgressBarHandler PDialog;


    public FitnessAdapter(Context context, int layoutResourceId,
                             ArrayList<YogaItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        preferenceManager = PreferenceManager.getPreferenceManager(context);
        PDialog = new ProgressBarHandler(context);

    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        int type = getItemViewType(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        holder = new ViewHolder();
        holder.title = (TextView) row.findViewById(R.id.movieTitle);
        holder.movieStory = (TextView) row.findViewById(R.id.movieStory);
        holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);

        languagePreference = LanguagePreference.getLanguagePreference(context);

        FontUtls.loadFont(context, context.getResources().getString(R.string.medium_fonts), holder.title);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), holder.movieStory);

        if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));

        } else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));


        } else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
            holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));

        } else {
            holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.videoImageview.getDrawable().getIntrinsicWidth(), holder.videoImageview.getDrawable().getIntrinsicHeight()));
        }
        row.setTag(holder);


        try {

            final YogaItem item = data.get(position);
            holder.title.setText(item.getTitle());
            holder.movieStory.setText(item.getStory());
            String imageId = item.getImage();

            Log.v("SUUBHA","movie story == "+ item.getStory());


            if (imageId.matches("") || imageId.matches(LanguagePreference.getLanguagePreference(context).getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                holder.videoImageview.setImageResource(R.drawable.logo);

            } else {

                Picasso.with(context)
                        .load(item.getImage()).error(R.drawable.no_image).placeholder(R.drawable.no_image)
                        .into(holder.videoImageview);

            }

/////////////////////////////////////////////////////////end////////////////////////////////////////////////////////////////


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

        }

        return row;
    }


    static class ViewHolder {
        public TextView title, movieStory;
        public ImageView videoImageview;

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opt);
        opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, opt);
    }

    public static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight) {
        final int height = opt.outHeight;
        final int width = opt.outWidth;
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth) {
                sampleSize *= 2;
            }

        }
        return sampleSize;
    }

}

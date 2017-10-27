package com.home.vod.adapter;

/**
 * Created by MUVI on 10/11/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.model.EpisodesListModel;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;


public class Tutorial_List_Adapter extends ArrayAdapter<EpisodesListModel> {


    private String bannerImageUrl = "";
    private String showTitle = "";
    private String showStory = "";
    private String movieTrailerUrl = "";
    private String movieUniqueId;
    private int contentTypesId = 0;


    private Context context;
    private int layoutResourceId;
    private int isThirdParty = 0;
    private LanguagePreference languagePreference;



    private ArrayList<EpisodesListModel> data=new ArrayList<EpisodesListModel>();




    public Tutorial_List_Adapter(Context context, int layoutResourceId,
                                 ArrayList<EpisodesListModel> data) {
        super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

     //   languagePreference = LanguagePreference.getLanguagePreference(context);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.movieTitle = (TextView) row.findViewById(R.id.movieTitle);
            FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), holder.movieTitle );
            holder.movieImageView = (ImageView) row.findViewById(R.id.movieImageView);

           /* int height = holder.videoImageview.getDrawable().getIntrinsicHeight();
            int width = holder.videoImageview.getDrawable().getIntrinsicWidth();

            holder.videoImageview.getLayoutParams().height = height;
            holder.videoImageview.getLayoutParams().width = width;*/

            if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                holder.movieImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.movieImageView.getDrawable().getIntrinsicWidth(), holder.movieImageView.getDrawable().getIntrinsicHeight()));

            } else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                holder.movieImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.movieImageView.getDrawable().getIntrinsicWidth(), holder.movieImageView.getDrawable().getIntrinsicHeight()));


            } else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
                holder.movieImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.movieImageView.getDrawable().getIntrinsicWidth(), holder.movieImageView.getDrawable().getIntrinsicHeight()));


            } else {
                holder.movieImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView, holder.movieImageView.getDrawable().getIntrinsicWidth(), holder.movieImageView.getDrawable().getIntrinsicHeight()));


            }
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
        try {

            EpisodesListModel item = data.get(position);
            LogUtil.showLog("Tutorial","ABhishek" +item.getEpisodeTitle());
            holder.movieTitle.setText(item.getEpisodeTitle());
            String imageId = item.getEpisodeThumbnailImageView();


            if (imageId.matches("") || imageId.matches(LanguagePreference.getLanguagePreference(context).getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                holder.movieImageView.setImageResource(R.drawable.logo);

            } else {

                Picasso.with(context)
                        .load(item.getEpisodeThumbnailImageView()).error(R.drawable.no_image).placeholder(R.drawable.no_image)
                        .into(holder.movieImageView);


          /*  ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.no_thumbnail)
                    .showImageOnFail(R.drawable.no_thumbnail)
                    .showImageOnLoading(R.drawable.no_thumbnail).build();
            ImageAware imageAware = new ImageViewAware(holder.videoImageview, false);
            imageLoader.displayImage(imageId, imageAware,options);*/
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

        }

        return row;
    }

    static class ViewHolder {
        public TextView movieTitle;

        public ImageView movieImageView;

    }


        /*    Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
            episodeTitleTextView.setTypeface(castDescriptionTypeface);

            Typeface episodeNameTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
            episodeNameTextView.setTypeface(episodeNameTypeface);

            Typeface episodeDateTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
            episodeDateTextView.setTypeface(episodeDateTypeface);*/


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

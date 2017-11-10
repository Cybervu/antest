
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.AddToFavAsync;
import com.home.apisdk.apiController.DeleteFavAsync;
import com.home.apisdk.apiController.GetRelatedContentAsynTask;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;
import com.home.apisdk.apiModel.RelatedContentInput;
import com.home.vod.Episode_Programme_Handler;
import com.home.vod.R;
import com.home.vod.Single_Part_Programme_Handler;
import com.home.vod.activity.MovieDetailsActivity;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.activity.YogaPlayerActivity;
import com.home.vod.fragment.VideosListFragment;
import com.home.vod.model.GridItem;
import com.home.vod.model.YogaItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WATCH_NOW;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.WATCH_NOW;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;
import static com.home.vod.util.Constant.authTokenStr;

public class YogaFilterAdapter extends ArrayAdapter<YogaItem> implements DeleteFavAsync.DeleteFavListener, AddToFavAsync.AddToFavListener {
    private Context context;
    private int layoutResourceId;
    private ArrayList<YogaItem> data = new ArrayList<YogaItem>();
    PreferenceManager preferenceManager;
    private String loggedInStr;
    LanguagePreference languagePreference;
    private int isFavoritePos;

    int i = -1;
    private ArrayList<ViewHolder> viewstoreHolder = new ArrayList<>();
    private ProgressBarHandler PDialog;

    public YogaFilterAdapter(Context context, int layoutResourceId,
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

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
//            viewstoreHolder.add(position, holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        languagePreference = LanguagePreference.getLanguagePreference(context);

        holder.title = (TextView) row.findViewById(R.id.movieTitle);
        holder.movieStory = (TextView) row.findViewById(R.id.movieStory);
        holder.Playnow = (Button) row.findViewById(R.id.play_button);
        holder.share = (ImageView) row.findViewById(R.id.share_click_view);

        holder.share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareIt();
            }
        });

        /////////////////////////////////////////////////////////start////////////////////////////////////////////////////////////////
        holder.fav_click_view = (ImageView) row.findViewById(R.id.fav_click_view);



        if (data.get(position).isFavoriteClicked()) {
            holder.fav_click_view.setImageResource(R.drawable.favorite_red);
        }else{
            holder.fav_click_view.setImageResource(R.drawable.favorite);
        }

        if (data.get(position).getIsFavorite() == 1) {
            holder.fav_click_view.setImageResource(R.drawable.favorite_red);
        }

        holder.Playnow.setText(languagePreference.getTextofLanguage(WATCH_NOW, DEFAULT_WATCH_NOW));

        FontUtls.loadFont(context, context.getResources().getString(R.string.fonts), holder.title);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), holder.movieStory);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), holder.Playnow);
        holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);


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


            if (imageId.matches("") || imageId.matches(LanguagePreference.getLanguagePreference(context).getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                holder.videoImageview.setImageResource(R.drawable.logo);

            } else {

                Picasso.with(context)
                        .load(item.getImage()).error(R.drawable.no_image).placeholder(R.drawable.no_image)
                        .into(holder.videoImageview);


                holder.Playnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((item.getVideoTypeId().trim().equalsIgnoreCase("1")) || (item.getVideoTypeId().trim().equalsIgnoreCase("2")) || (item.getVideoTypeId().trim().equalsIgnoreCase("4"))) {
                       /* final Intent detailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                        detailsIntent.putExtra(PERMALINK_INTENT_KEY, moviePermalink);
                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(detailsIntent);*/
//                      new Single_Part_Programme_Handler(getContext()).handleIntent(PERMALINK_INTENT_KEY,item.getPermalink());
                            final Intent movieDetailsIntent = new Intent(context, YogaPlayerActivity.class);
                            movieDetailsIntent.putExtra(PERMALINK_INTENT_KEY, item.getPermalink());
                            movieDetailsIntent.putExtra("CONTENT_ID", item.getContent_id());
                            movieDetailsIntent.putExtra("CONTENT_STREAM_ID", item.getContent_stream_id());
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(movieDetailsIntent);
                                }
                            });
                        } else if ((item.getVideoTypeId().trim().equalsIgnoreCase("3"))) {
                            new Episode_Programme_Handler(context).handleIntent(PERMALINK_INTENT_KEY, item.getPermalink());

                        }


                    }
                });
            }
            if (Util.favPosition > -1 && Util.favPosition == position){
                AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                addToFavInputModel.setAuthToken(authTokenStr);
                addToFavInputModel.setMovie_uniq_id(item.getMovieUniqueId());
                addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                         /* if (item.getContentTypesId().equals("3")){
                              addToFavInputModel.setIsEpisodeStr("0");
                          }else{
                              addToFavInputModel.setIsEpisodeStr("1");

                          }*/
                addToFavInputModel.setIsEpisodeStr("0");

                AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, YogaFilterAdapter.this, context);
                asynFavoriteAdd.execute();
            }
            ///////

            holder.fav_click_view.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    if (preferenceManager != null) {
                        loggedInStr = preferenceManager.getUseridFromPref();
                    }
                    i = position;
                    if (loggedInStr != null) {
                        if (data.get(position).getIsFavorite() == 1) {

                            DeleteFavInputModel deleteFavInputModel = new DeleteFavInputModel();
                            deleteFavInputModel.setAuthTokenStr(authTokenStr);
                            deleteFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                            deleteFavInputModel.setMovieUniqueId(item.getMovieUniqueId());
                            if (item.getContentTypesId().equals("3")) {
                                deleteFavInputModel.setIsEpisode("0");
                            } else {
                                deleteFavInputModel.setIsEpisode("0");

                            }
                            DeleteFavAsync deleteFavAsync = new DeleteFavAsync(deleteFavInputModel, YogaFilterAdapter.this, context);
                            deleteFavAsync.execute();


                        } else {
                            LogUtil.showLog("Nihar_sdk", item.getMovieUniqueId());
                            LogUtil.showLog("Nihar_sdk", item.getContentTypesId()
                                    + "favorite   ===" + item.getMovieUniqueId() + preferenceManager.getUseridFromPref());
                            AddToFavInputModel addToFavInputModel = new AddToFavInputModel();
                            addToFavInputModel.setAuthToken(authTokenStr);
                            addToFavInputModel.setMovie_uniq_id(item.getMovieUniqueId());
                            addToFavInputModel.setLoggedInStr(preferenceManager.getUseridFromPref());
                         /* if (item.getContentTypesId().equals("3")){
                              addToFavInputModel.setIsEpisodeStr("0");
                          }else{
                              addToFavInputModel.setIsEpisodeStr("1");

                          }*/
                            addToFavInputModel.setIsEpisodeStr("0");

                            AddToFavAsync asynFavoriteAdd = new AddToFavAsync(addToFavInputModel, YogaFilterAdapter.this, context);
                            asynFavoriteAdd.execute();


                        }
                    } else {
                        Util.favorite_clicked = true;
                        Util.favPosition = position;
                        final Intent registerActivity = new Intent(context, RegisterActivity.class);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                registerActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                registerActivity.putExtra("from", this.getClass().getName());
                                ((Activity) context).startActivityForResult(registerActivity, 30060);

                            }
                        });

                    }


                }
            });
/////////////////////////////////////////////////////////end////////////////////////////////////////////////////////////////


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

        }

        return row;
    }

    @Override
    public void onDeleteFavPreExecuteStarted() {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                PDialog.show();

            }
        });

    }

    @Override
    public void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg) {
        // isFavorite = 0;
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                PDialog.hide();

            }
        });

        if (status == 200) {
            //  data.get(i).setFavbtnClicked(true);
            data.get(i).setIsFavorite(0);
            //notifyDataSetChanged();*/
            data.get(i).setFavoriteClicked(false);
            notifyDataSetChanged();
        }
        showToast(sucessMsg, context);


    }

    public void showToast(String successmsg, Context context) {


        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(successmsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public void onAddToFavPreExecuteStarted() {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                PDialog.show();

            }
        });
    }

    @Override
    public void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg) {

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                PDialog.hide();

            }
        });

        if (status == 200) {
            // data.get(i).setFavbtnClicked(true);

            data.get(i).setIsFavorite(1);
            data.get(i).setFavoriteClicked(true);
            notifyDataSetChanged();

        }
        showToast(sucessMsg, context);
        Util.favPosition = -1;

    }

    static class ViewHolder {
        public TextView title, movieStory;
        public ImageView videoImageview, share, fav_click_view;
        public Button Playnow;

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

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
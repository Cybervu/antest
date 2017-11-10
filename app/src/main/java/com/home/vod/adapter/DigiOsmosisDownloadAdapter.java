package com.home.vod.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.activity.DigiOsmosisDownloads;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import player.model.ContactModel1;
import player.utils.DBHelper;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.home.vod.preferences.LanguagePreference.CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CANCEL_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DELETE_BTN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WANT_TO_DELETE;
import static com.home.vod.preferences.LanguagePreference.DELETE_BTN;
import static com.home.vod.preferences.LanguagePreference.WANT_TO_DELETE;

/**
 * Created by Muvi on 1/16/2017.
 */
public class DigiOsmosisDownloadAdapter extends ArrayAdapter<ContactModel1> {
    DigiOsmosisDownloads activity;
    ArrayList<ContactModel1> downloadModel;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;
    List<String[]> allElements;
    //CSVReader readers = null;
    String[] nextLine;
    SharedPreferences pref;
    String emailIdStr = "";
    ContactModel1 audio;
    //MydownloadModel mydownloadModel;
    DBHelper dbHelper;
    public boolean downloading;
    DownloadManager downloadManager;
    //Downloadlistdb downloadlistdb;
    public DigiOsmosisDownloadAdapter(DigiOsmosisDownloads activity, int simple_dropdown_item_1line, ArrayList<ContactModel1> downloadModel) {
        super(activity, simple_dropdown_item_1line, downloadModel);

        LogUtil.showLog("MUVI","DOWNLOAD MODEL=="+downloadModel.get(0).toString());
        this.activity = activity;
        this.downloadModel = downloadModel;
        dbHelper = new DBHelper(activity);
        downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
        preferenceManager = PreferenceManager.getPreferenceManager(activity);
        languagePreference = LanguagePreference.getLanguagePreference(activity);
       // pref = activity.getSharedPreferences(Util.LOGIN_PREF, 0);
        if (preferenceManager != null) {
            emailIdStr = preferenceManager.getEmailIdFromPref();


        } else {
            emailIdStr = "";


        }



    }

    @Override
    public int getCount() {
        return downloadModel.size();
    }

    /*@Override
    public Object getItem(int position) {
        return position;
    }
*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = activity.getLayoutInflater();
        v = inflater.inflate(R.layout.custom_offlist, null);
        TextView title = (TextView) v.findViewById(R.id.textView);
        TextView realise_date = (TextView) v.findViewById(R.id.textView2);
        TextView genre = (TextView) v.findViewById(R.id.textView3);
        TextView duration = (TextView) v.findViewById(R.id.textView4);


        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        ImageView image1 = (ImageView) v.findViewById(R.id.imageView1);
//
        image.setImageBitmap(decodeSampledBitmapFromResource(this.activity.getResources(), R.id.imageView,image.getDrawable().getIntrinsicWidth(),image.getDrawable().getIntrinsicHeight()));

        Picasso.with(activity)
                .load(downloadModel.get(position).getPoster())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(image);
        title.setText(downloadModel.get(position).getMUVIID());
//        realise_date.setText("");
        genre.setText(downloadModel.get(position).getGenere());
        String dd = downloadModel.get(position).getDuration();
        LogUtil.showLog("SUBHA", dd);
        duration.setText(dd);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);
                dlgAlert.setTitle("");
                dlgAlert.setMessage(languagePreference.getTextofLanguage(WANT_TO_DELETE,DEFAULT_WANT_TO_DELETE));

                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(DELETE_BTN,DEFAULT_DELETE_BTN), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(DELETE_BTN,DEFAULT_DELETE_BTN),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                DownloadManager downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.remove(downloadModel.get(position).getDOWNLOADID());

                                String path1 = downloadModel.get(position).getPath().trim();
                                File file = new File(path1);
                                if (file != null && file.exists()) {

                                    file.delete();

                                }


                                audio = dbHelper.getContact(downloadModel.get(position).getUniqueId().trim());

                                downloadModel.remove(position);
                                notifyDataSetChanged();
                                activity.visible();

                                if (audio != null) {


                                    dbHelper.deleteRecord(audio);

                                }

                            }
                        });
                dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(CANCEL_BUTTON,DEFAULT_CANCEL_BUTTON),null);
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(CANCEL_BUTTON,DEFAULT_CANCEL_BUTTON),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();



                            }
                        });

                dlgAlert.create().show();


            }


        });

        return v;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options opt =new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res, resId, opt);
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight);
        opt.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res, resId, opt);
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

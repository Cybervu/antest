package player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;


import com.home.vod.R;

import java.util.ArrayList;

public class DownloadOptionAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList List_Of_FileSize ;
    private ArrayList ResolutionFormat ;

    int selected_option = 0;
    private String[] resolutionName = {"144p", "240p", "360p", "480p","640p", "720p", "1080p", "1440p", "2048p", "4096p", "7680p", "best", "auto"};
    private int[] resolutionId = {
            R.id.download_144,
            R.id.download_240,
            R.id.download_360,
            R.id.download_480,
            R.id.download_640,
            R.id.download_720,
            R.id.download_1080,
            R.id.download_1440,
            R.id.download_2048,
            R.id.download_4096,
            R.id.download_7680,
            R.id.download_best,
            R.id.download_auto};

    public DownloadOptionAdapter(Context mContext, ArrayList List_Of_FileSize , ArrayList ResolutionFormat) {
        this.mContext = mContext;
        this.List_Of_FileSize = List_Of_FileSize;
        this.ResolutionFormat = ResolutionFormat;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return List_Of_FileSize.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_download_resolution_list, null);
        } else {
            view = convertView;
        }



        final RadioButton radioButton = (RadioButton)view.findViewById(R.id.selected_resolution_option);
//        radioButton.setText("hello");
        radioButton.setText("  "+ResolutionFormat.get(position)+" "+List_Of_FileSize.get(position));

        // Kushal- Set id to download Layout
        setIdToDownloadLayout(view,radioButton);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton.setChecked(true);
                selected_option = position;
                notifyDataSetChanged();

                Intent intent = new Intent("UrlPosition");
                // add data
                intent.putExtra("position", ""+selected_option);
                mContext.sendBroadcast(intent);
            }
        });

        if(selected_option==position)
        {
            radioButton.setChecked(true);
        }
        else
        {
            radioButton.setChecked(false);
        }


        return view;
    }

    private void setIdToDownloadLayout(View convertView, RadioButton radioButton) {

        if (convertView instanceof LinearLayout){
            LinearLayout l= (LinearLayout)convertView;
            for(int j=0; j<resolutionName.length; j++){
                if (radioButton.getText().toString().toLowerCase().contains(resolutionName[j])){
                    l.setId(resolutionId[j]);
                }
            }
        }
       /* LinearLayout layout= (LinearLayout)convertView.findViewById(R.id.layout);
        for (int i=0; i<layout.getChildCount();i++){
            View v= layout.getChildAt(i);
            if (v instanceof LinearLayout){
                LinearLayout l= (LinearLayout)v;
                for(int j=0; j<resolutionName.length; j++){
                    if (radioButton.getText().toString().toLowerCase().contains(resolutionName[j])){
                        l.setId(resolutionId[j]);
                    }
                }
            }

        }*/
    }
}
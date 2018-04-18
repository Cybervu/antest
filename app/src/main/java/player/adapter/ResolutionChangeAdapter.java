package player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.vod.R;
import com.home.vod.util.FontUtls;

import java.util.ArrayList;

import player.utils.Util;

/**
 * Created by MUVI on 3/10/2017.
 */


public class ResolutionChangeAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<String> items; //data source of the list adapter
    private String[] resolutionName = {"144p", "240p", "360p", "480p", "720p", "1080p", "1440p", "2048p", "4096p", "7680p", "best", "auto"};
    private int[] resolutionId = {
            R.id.resolution_144,
            R.id.resolution_240,
            R.id.resolution_360,
            R.id.resolution_480,
            R.id.resolution_720,
            R.id.resolution_1080,
            R.id.resolution_1440,
            R.id.resolution_2048,
            R.id.resolution_4096,
            R.id.resolution_7680,
            R.id.resolution_Best,
            R.id.resolution_Auto};

    //public constructor
    public ResolutionChangeAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.resolution_list_layout, parent, false);
        }


        // get the TextView for item name and item description
        TextView title = (TextView) convertView.findViewById(R.id.title);

        //sets the text for item name and item description from the current item object
        title.setText(items.get(position));

        // Kushal- set Id to layout
        setIdToResolutionLayout(convertView, title);



        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), title);

        /*Typeface typeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        title.setTypeface(typeface);*/

        ImageView imageView = (ImageView) convertView.findViewById(R.id.selected_resolution);
        if (items.get(position).contains(Util.VideoResolution)) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }


        // returns the view for the current row
        return convertView;
    }

    private void setIdToResolutionLayout(View convertView, TextView title) {
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof LinearLayout) {
                LinearLayout l= (LinearLayout)v;
                for (int j=0; j<resolutionName.length;j++) {
                    if (title.getText().toString().toLowerCase().equalsIgnoreCase(resolutionName[j])){
                        l.setId(resolutionId[j]);
                    }
                }
            }

        }
    }
}

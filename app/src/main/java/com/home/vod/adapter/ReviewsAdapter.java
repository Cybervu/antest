package com.home.vod.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


import com.home.apisdk.apiModel.ViewContentRatingOutputModel;
import com.home.vod.R;
import com.home.vod.model.ReviewsItem;

import java.util.ArrayList;

/**
 * Created by User on 28-06-2017.
 */
public class ReviewsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ViewContentRatingOutputModel.Rating> reviewsItems ;

    public ReviewsAdapter(Context mContext, ArrayList<ViewContentRatingOutputModel.Rating> reviewsItems) {
        this.mContext = mContext;
        this.reviewsItems = reviewsItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return reviewsItems.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            grid = new View(mContext);
            grid = inflater.inflate(R.layout.review_layout, null);





        } else {
            grid = convertView;

        }
        TextView userNameTextView = (TextView) grid.findViewById(R.id.userNameTextView);
        TextView userReviewTextView = (TextView) grid.findViewById(R.id.userreviewTextView);
        RatingBar rating = (RatingBar)grid.findViewById(R.id.ratingBar);
        Typeface custom_name = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.light_fonts));
        userNameTextView.setTypeface(custom_name);

        Typeface castDescriptionTypeface = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.light_fonts));
        userReviewTextView.setTypeface(castDescriptionTypeface);

        userNameTextView.setText(reviewsItems.get(position).getDisplay_name());
        userReviewTextView.setText(reviewsItems.get(position).getReview());
        rating.setRating(Float.parseFloat(reviewsItems.get(position).getRating()));
        rating.setFocusable(false);

        rating.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
//            imageView.setImageResource(castCrewItems.get(position).getCastImage());


        return grid;
    }
}

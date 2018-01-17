package com.home.vod;

import android.content.Context;
import android.content.Intent;

import com.home.vod.activity.ExpertsDetailsActivity;
import com.home.vod.activity.ProgrammeActivity;
import com.home.vod.activity.YogaPlayerActivity;

import static com.home.vod.util.Constant.PERMALINK_FORCONTENT_KEY;
import static com.home.vod.util.Constant.PERMALINK_INTENT_KEY;

/**
 * Created by MUVI on 10/6/2017.
 */

public class Single_Part_Programme_Handler {

    Context context;

    public Single_Part_Programme_Handler(Context context){
        this.context=context;
    }

    public void handleIntent(String permalinkForContent,String permalink){

        Intent intent;
        if(permalinkForContent.equalsIgnoreCase("experts")){
           intent=new Intent(context, ExpertsDetailsActivity.class);


        }else{
           intent=new Intent(context, YogaPlayerActivity.class);
        }


        intent.putExtra(PERMALINK_INTENT_KEY,permalink);
        intent.putExtra(PERMALINK_FORCONTENT_KEY,permalinkForContent);
        context.startActivity(intent);

    }

}

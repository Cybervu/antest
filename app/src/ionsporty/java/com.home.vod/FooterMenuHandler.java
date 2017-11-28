package com.home.vod;

import android.app.Activity;

import com.home.apisdk.apiModel.MenusOutputModel;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BISHAL on 20-10-2017.
 */

public class FooterMenuHandler {
    private Activity context;
    public FooterMenuHandler(Activity context){
        this.context=context;
    }
    public void addFooterMenu(MenusOutputModel menusOutputModel,ArrayList<String> titleArray,HashMap<String, ArrayList<String>> expandableListDetail ){

       /* for (MenusOutputModel.FooterMenu menuListOutput : menusOutputModel.getFooterMenuModel()) {
            LogUtil.showLog("Alok", "footermenuListOutputList ::" + menuListOutput.getPermalink());
                    menuList.add(new NavDrawerItem(menuListOutput.getDisplay_name(), menuListOutput.getPermalink(), menuListOutput.isEnable(), menuListOutput.getLink_type(), menuListOutput.getUrl()));

        }*/

        for (int k = 0; k < menusOutputModel.getFooterMenuModel().size (); k++) {
            titleArray.add(menusOutputModel.getFooterMenuModel().get(k).getDisplay_name());
            ArrayList<String> childArray = new ArrayList<>();
            expandableListDetail.put(menusOutputModel.getFooterMenuModel().get(k).getDisplay_name(), childArray);

        }

    }
}

package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.GetMenusInputModel;
import com.home.apisdk.apiModel.GetMenusOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 12-12-2016.
 */
public class GetMenusAsynTask extends AsyncTask<GetMenusInputModel, Void, Void> {

    GetMenusInputModel getMenusInputModel;
    String responseStr,movieUniqueId;
    int status;
    String message,permalink,PACKAGE_NAME; String title, Permalink, ID, TitleChild, PermalinkChild, IDChild, ClasChild, UserTitleChild,
            UserIDChild, UserParentIdChild, UserPermalinkChild, UserClasChild, fdomain, flink_type, fid, fdisplay_name,
            fpermalink, furl, ParentIdChild, LinkTypeChild, ParentId, Clas, LinkType, UserTitle, UserPermalink, UserID,
            UserParentId, UserClas;

    String str = "#", abs;

    public interface GetMenusListner {
        void onGetMenusPreExecuteStarted();
        void onGetMenusPostExecuteCompleted(GetMenusOutputModel getMenusOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetMenusListner listener;
    private Context context;
    GetMenusOutputModel getMenusOutputModel;
    GetMenusOutputModel.MainMenu mainMenu ;
    GetMenusOutputModel.UserMenu userMenu ;
    GetMenusOutputModel.FooterMenu footerMenu ;
    GetMenusOutputModel.MainMenu.MainMenuChild mainMenuChild ;
    GetMenusOutputModel.UserMenu.UserMenuChild userMenuChild ;
    ArrayList<GetMenusOutputModel.MainMenu.MainMenuChild> mainMenuChildArrayList;
    ArrayList<GetMenusOutputModel.UserMenu.UserMenuChild> userMenuChildArrayList;
    ArrayList<GetMenusOutputModel.MainMenu> mainMenuArrayList;
    ArrayList<GetMenusOutputModel.UserMenu> userMenuArrayList;
    ArrayList<GetMenusOutputModel.FooterMenu> footerMenuArrayList;

    public GetMenusAsynTask(GetMenusInputModel getMenusInputModel, GetMenusListner listener, Context context) {
        this.listener=listener;
        this.context=context;
        this.getMenusInputModel = getMenusInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK", "GetMenusAsynTask");

    }

    @Override
    protected Void doInBackground(GetMenusInputModel... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetMenusUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.getMenusInputModel.getAuthToken());
            httppost.addHeader(HeaderConstants.LANG_CODE,this.getMenusInputModel.getLang_code());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e){

                status = 0;
                message = "Error";

            }catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject myJson =null;
            if(responseStr!=null){
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");

            }

            if (status > 0) {

                if (status == 200) {
                    getMenusOutputModel = new GetMenusOutputModel();

                    if ((myJson.has("msg")) && myJson.optString("msg").trim() != null && !myJson.optString("msg").trim().isEmpty() && !myJson.optString("msg").trim().equals("null") && !myJson.optString("msg").trim().matches("")) {
                        getMenusOutputModel.setMsg(myJson.optString("msg"));

                    }


                    try {
                        JSONObject jsonMain = myJson.optJSONObject ("menus");
                        JSONArray jsonMainMenu = jsonMain.optJSONArray ("mainmenu");
                        mainMenuArrayList = new ArrayList<>();



                        for (int i = 0; i < jsonMainMenu.length (); i++) {
                            mainMenu = new GetMenusOutputModel().new MainMenu();

                            title = jsonMainMenu.getJSONObject (i).optString ("title").toString ().trim ();
                            mainMenu.setTitle (title);
                            Permalink = jsonMainMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
                            mainMenu.setPermalink (Permalink);
                            ID = jsonMainMenu.getJSONObject (i).optString ("id").toString ().trim ();
                            mainMenu.setId (ID);
                            ParentId = jsonMainMenu.getJSONObject (i).optString ("parent_id").toString ().trim ();
                            mainMenu.setParent_id (ParentId);
                            LinkType = jsonMainMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
                            mainMenu.setLink_type (LinkType);


                            try {


                                JSONArray jsonChildNode = jsonMainMenu.getJSONObject (i).getJSONArray ("children");
                                for (int j = 0; j < jsonChildNode.length (); j++) {

                                    mainMenuChild = mainMenu.new MainMenuChild();
                                    TitleChild = jsonChildNode.getJSONObject (j).optString ("title").toString ().trim ();
                                    mainMenuChild.setTitle (TitleChild);
                                    PermalinkChild = jsonChildNode.getJSONObject (j).optString ("permalink").toString ().trim ();
                                    mainMenuChild.setPermalink (PermalinkChild);
                                    IDChild = jsonChildNode.getJSONObject (j).optString ("id").toString ().trim ();
                                    mainMenuChild.setId (IDChild);
                                    ParentIdChild = jsonChildNode.getJSONObject (j).optString ("parent_id").toString ().trim ();
                                    mainMenuChild.setParent_id (ParentIdChild);

                                    LinkTypeChild = jsonChildNode.getJSONObject (j).optString ("link_type").toString ().trim ();
                                    mainMenuChild.setLink_type (LinkTypeChild);
                                    mainMenuChildArrayList.add (mainMenuChild);


                                }
                                mainMenu.setMainMenuChildModel(mainMenuChildArrayList);
                                mainMenuArrayList.add (mainMenu);

                            } catch (Exception e) {
                            }
                        }


                        getMenusOutputModel.setMainMenuModel(mainMenuArrayList);


                       JSONArray jsonUserMenu = jsonMain.optJSONArray ("usermenu");
                        userMenuArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonUserMenu.length (); i++) {
                                userMenu = new GetMenusOutputModel().new UserMenu();
                                UserTitle = jsonUserMenu.optJSONObject (i).optString ("title").toString ().trim ();
                                userMenu.setTitle (UserTitle);
                                UserPermalink = jsonUserMenu.optJSONObject (i).optString ("permalink").toString ().trim ();
                                userMenu.setPermalink (UserPermalink);

                                UserParentId = jsonUserMenu.optJSONObject (i).optString ("parent_id").toString ().trim ();
                                userMenu.setParent_id (UserParentId);




                                if (UserPermalink.equals (str)) {
                                    try {

                                        JSONArray jsonUserChildNode = jsonUserMenu.getJSONObject (i).getJSONArray ("children");
                                        int lengthJsonUserChildArr = jsonUserChildNode.length ();
                                        for (int j = 0; j < lengthJsonUserChildArr; j++) {

                                            userMenuChild = userMenu.new UserMenuChild();
                                            UserTitleChild = jsonUserChildNode.optJSONObject (j).optString ("title").toString ().trim ();
                                            userMenuChild.setTitle (UserTitleChild);

                                            UserPermalinkChild = jsonUserChildNode.optJSONObject (j).optString ("permalink").toString ().trim ();
                                            userMenuChild.setPermalink (UserPermalinkChild);

                                            UserIDChild = jsonUserChildNode.optJSONObject (j).optString ("id").toString ().trim ();
                                            userMenuChild.setId (UserIDChild);
                                            Log.d ("ANU", "Response===" + UserIDChild);
                                            UserParentIdChild = jsonUserChildNode.optJSONObject (j).optString ("parent_id").toString ().trim ();
                                            userMenuChild.setParent_id (UserParentIdChild);
                                            Log.d ("ANU", "Response===" + UserParentIdChild);

                                            Log.d ("ANU", "Response===" + UserClasChild);
                                            userMenuChildArrayList.add (userMenuChild);


                                        }

                                        userMenu.setUserMenuChildModel(userMenuChildArrayList);
                                    }



                                    catch (Exception e) {
                                    }
                                }

                                userMenuArrayList.add (userMenu);
                            }


                            getMenusOutputModel.setUserMenuModel(userMenuArrayList);
                        } catch (Exception e) {
                        }


                        footerMenuArrayList = new ArrayList<>();
                        JSONArray jsonFooterMenu = jsonMain.optJSONArray ("footer_menu");

                        try {
                            for (int i = 0; i < jsonFooterMenu.length (); i++) {
                                footerMenu = new GetMenusOutputModel().new FooterMenu();
                                fdomain = jsonFooterMenu.getJSONObject (i).optString ("domain").toString ().trim ();
                                footerMenu.setDomain (fdomain);
                                flink_type = jsonFooterMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
                                footerMenu.setLink_type (Permalink);
                                fid = jsonFooterMenu.getJSONObject (i).optString ("id").toString ().trim ();
                                footerMenu.setId (fid);
                                fdisplay_name = jsonFooterMenu.getJSONObject (i).optString ("display_name").toString ().trim ();
                                footerMenu.setDisplay_name (fdisplay_name);
                                fpermalink = jsonFooterMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
                                footerMenu.setPermalink (fpermalink);
                                furl = jsonFooterMenu.getJSONObject (i).optString ("url").toString ().trim ();
                                footerMenu.setUrl (furl);
                                footerMenuArrayList.add (footerMenu);

                            }


                            getMenusOutputModel.setFooterMenuModel(footerMenuArrayList);
                        } catch (Exception e) {
                        }

                    } catch (Exception e) {
                    }


                } else {
                    responseStr = "0";
                    status = 0;
                    // totalItems = 0;
                    message = "";
                }
            }
        } catch (Exception e) {
            status = 0;
            // totalItems = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetMenusPreExecuteStarted();
        status = 0;
        if(!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetMenusPostExecuteCompleted(getMenusOutputModel, status, message);
            return;
        }
        if(HeaderConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetMenusPostExecuteCompleted(getMenusOutputModel, status,message);
        }

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onGetMenusPostExecuteCompleted(getMenusOutputModel, status,message);

    }

}

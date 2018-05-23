package com.home.vod.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.GetAppMenuAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiModel.GetMenusInputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.MenusOutputModel;
import com.home.vod.R;
import com.home.vod.adapter.ExpandableListAdapter;
import com.home.vod.fragment.AboutUsFragment;
import com.home.vod.fragment.ContactUsFragment;
import com.home.vod.fragment.HomeFragment;
import com.home.vod.fragment.MyLibraryFragment;
import com.home.vod.fragment.PurchaseHistoryFragment;
import com.home.vod.fragment.VideosListFragment;
import com.home.vod.fragment.WatchHistoryFragment;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.CONTACT_US;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONTACT_US;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_HOME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_WATCH_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.HOME;
import static com.home.vod.preferences.LanguagePreference.LOGIN;
import static com.home.vod.preferences.LanguagePreference.LOGOUT;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.MY_DOWNLOAD;
import static com.home.vod.preferences.LanguagePreference.MY_FAVOURITE;
import static com.home.vod.preferences.LanguagePreference.MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.PROFILE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.home.vod.preferences.LanguagePreference.WATCH_HISTORY;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.authTokenStr;


/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements GetAppMenuAsync.GetMenusListener, LogoutAsynctask.LogoutListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private NavigationDrawerCallbacks mCallbacks;
    public static ArrayList<NavDrawerItem> menuList;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private Button Login, Register;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    FeatureHandler featureHandler;

    public HashMap<String, ArrayList<String>> expandableListDetail;
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> idArray = new ArrayList<>();
    ExpandableListAdapter adapter;
    SharedPreferences pref;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;
    GetAppMenuAsync asynLoadMenuItems = null;
    ProgressBarHandler progressDialog;

    boolean my_libary_added = false;
    boolean watch_history_added = false;
    boolean my_favourite_added = false;
    boolean my_download_added = false;
    boolean purchase_history_added = false;
    MenusOutputModel menusOutputModelLocal, menusOutputModelFromAPI = new MenusOutputModel();
    int status;
    String message;
    String loggedInStr = null;
    Fragment fragment = null;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10, statusCode;
    TextView text;
    String str = "#", abs;
    String Title, Permalink, ID, TitleChild, PermalinkChild, IDChild, ClasChild, UserTitleChild,
            UserIDChild, UserParentIdChild, UserPermalinkChild, UserClasChild, fdomain, flink_type, fid, fdisplay_name,
            fpermalink, furl, ParentIdChild, LinkTypeChild, ParentId, Clas, LinkType, UserTitle, UserPermalink, UserID,
            UserParentId, UserClas, Value, Id_seq, Language_id, Language_parent_id, ValueChild, Id_seq_Child, Language_id_Child, Language_parent_id_Child;


    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    Button Send;
    private ProgressBarHandler pDialog = null;


    public static boolean homeSelected = true;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        loggedInStr = preferenceManager.getUseridFromPref();

        if (NetworkStatus.getInstance().isConnected(getActivity())) {
            GetMenusInputModel menuListInput = new GetMenusInputModel();
            menuListInput.setAuthToken(authTokenStr);
            menuListInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            asynLoadMenuItems = new GetAppMenuAsync(menuListInput, NavigationDrawerFragment.this, getActivity());
            asynLoadMenuItems.executeOnExecutor(threadPoolExecutor);
        }
    }

    @Override
    public void onGetMenusPreExecuteStarted() {
        progressDialog = new ProgressBarHandler(getActivity());
        progressDialog.show();
    }

    @Override
    public void onGetMenusPostExecuteCompleted(MenusOutputModel menusOutputModel, int status, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog = null;
        }
        this.menusOutputModelLocal = menusOutputModel;
        this.menusOutputModelFromAPI = menusOutputModel;
        this.status = status;
        this.message = message;
        if (status == 200) {
            setMenuItemsInDrawer(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ExpandableListView) rootView.findViewById(R.id.list_slidermenu);
        Login = (Button) rootView.findViewById(R.id.navigation_login);
        Register = (Button) rootView.findViewById(R.id.navigation_register);

        handleClickFunction();
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectItem(position);

            }
        });


//       expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
//        mDrawerListView.setAdapter(new ExpandableListAdapter(getActivity(),mainMenuModelArrayList, mainMenuChildModelArrayList));

        //for expand the child content in navigation
        mDrawerListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                Util.drawer_collapse_expand_imageview.remove(groupPosition);
                Util.drawer_collapse_expand_imageview.add(groupPosition, groupPosition + "," + 1);
                Log.v("SUBHA1", "setOnGroupExpandListener===" + groupPosition);


                for (int i = 0; i < Util.drawer_collapse_expand_imageview.size(); i++) {
                    String expand_collapse_image_info[] = Util.drawer_collapse_expand_imageview.get(i).split(",");
                    Log.v("SUBHA1", "setOnGroupExpandListener===Data==========" + expand_collapse_image_info[0] + "," + expand_collapse_image_info[1]);
                }


            }
        });
        //for expan less the child content in navigation
        mDrawerListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Util.drawer_collapse_expand_imageview.remove(groupPosition);
                Util.drawer_collapse_expand_imageview.add(groupPosition, groupPosition + "," + 0);
                Log.v("SUBHA1", "setOnGroupCollapseListener====" + groupPosition);

                for (int i = 0; i < Util.drawer_collapse_expand_imageview.size(); i++) {
                    String expand_collapse_image_info[] = Util.drawer_collapse_expand_imageview.get(i).split(",");
                    Log.v("SUBHA1", "setOnGroupCollapseListener===Data==========" + expand_collapse_image_info[0] + "," + expand_collapse_image_info[1]);
                }


            }
        });


        mDrawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int listPosition, long id) {
                //Toast.makeText(getContext(), ""+listPosition, Toast.LENGTH_SHORT).show();

                if (((TextView) v.findViewById(R.id.listTitle)).getText().toString().equalsIgnoreCase(languagePreference.getTextofLanguage(HOME, DEFAULT_HOME))) {
                    FontUtls.loadFont(getContext(), getResources().getString(R.string.regular_fonts),  ((TextView) v.findViewById(R.id.listTitle)));
                    //((TextView) v.findViewById(R.id.listTitle)).setTypeface((null), Typeface.BOLD);
                    homeSelected = true;
                } else
                    homeSelected = false;
                // Kushal
                for (int i = 0; i < parent.getAdapter().getCount(); i++) {
                    //  Log.e("Error",parent.getChildAt(i).findViewById(R.id.listTitle).toString());
                    if (i == listPosition) {
                        //parent.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.red_border));
                        try {
                            if(((TextView) parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.listTitle)).getText().toString().equalsIgnoreCase(" ")){
                                parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.selector).setVisibility(View.INVISIBLE);
                            }else {
                                parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.selector).setVisibility(View.VISIBLE);
                                FontUtls.loadFont(getActivity(), getResources().getString(R.string.regular_fonts), ((TextView) parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.listTitle)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.selector).setVisibility(View.INVISIBLE);
                            FontUtls.loadFont(getActivity(), getResources().getString(R.string.light_fonts), ((TextView) parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.listTitle)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                boolean retVal = true;
                boolean mylibrary_title_added = false;


                if (menusOutputModelLocal.getMainMenuModel().size() > listPosition) {

                    for (int l = 0; l < menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().size(); l++) {
                        if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getId().equals
                                (menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().get(l).getParent_id())) {
                            retVal = false;
                        }

                    }
                }


                for (int i = 0; i < menusOutputModelLocal.getFooterMenuModel().size(); i++) {
                    Log.v("SUBHA", "title" + menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink());
                    Log.v("SUBHA", "titleArray.get(listPosition)" + titleArray.get(listPosition));

                    if (menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name().trim().equals(titleArray.get(listPosition))) {
                        if (menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink().equals("contactus")) {                         //   isNavigated = 1;

                            fragment = new ContactUsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("title", menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name());
                            Log.v("SUBHA", "CONTACT USfooterMenuModelArrayList.get(i).getPermalink()" + menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name());

                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                            mDrawerLayout.closeDrawers();
                        } else if (menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink().trim().equalsIgnoreCase("purchasehistory")) {
                            /*Intent purchaseintent = new Intent(getActivity(), PurchaseHistoryActivity.class);
                            startActivity(purchaseintent);*/
                            fragment = new PurchaseHistoryFragment();
                            /*Bundle bundle = new Bundle();
                            bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                            fragment.setArguments(bundle);*/
                            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                            mDrawerLayout.closeDrawers();
                        }else if(menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink().trim().equalsIgnoreCase("blank")){
                            parent.getChildAt(i - parent.getFirstVisiblePosition()).findViewById(R.id.selector).setVisibility(View.INVISIBLE);

                        }


                        else {

                            Intent browserIntent;

                            if (menusOutputModelLocal.getFooterMenuModel().get(i).getLink_type().trim().equalsIgnoreCase("2")) {
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getFooterMenuModel().get(listPosition).getPermalink().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);

                                //isNavigated = 1;
                                Log.v("SUBHA", "hello" + menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name());
                                LogUtil.showLog("MUVI", "url of link type 2===" + menusOutputModelLocal.getFooterMenuModel().get(listPosition).getPermalink());
                                mDrawerLayout.openDrawer(Gravity.LEFT);
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);

                                String externalLink = menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink();
                                browserIntent = new Intent(Intent.ACTION_VIEW);
                                browserIntent.setData(Uri.parse(externalLink));
                                getActivity().startActivity(browserIntent);

                            } else {
                                // isNavigated = 1;

                                fragment = new AboutUsFragment();
                                Bundle bundle = new Bundle();
                                Log.v("SUBHA", "footerMenuModelArrayList.get(i).getPermalink()" + menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink());
                                bundle.putString("item", menusOutputModelLocal.getFooterMenuModel().get(i).getPermalink());
                                bundle.putString("title", menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name());
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                mDrawerLayout.closeDrawers();


                            }

                        }

                    }
                }

                try {

                    if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equalsIgnoreCase(languagePreference.getTextofLanguage(HOME, DEFAULT_HOME))) {
                        // isNavigated = 1;

                        Fragment fragment = new HomeFragment();
                        Bundle bundle = new Bundle();
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                        mDrawerLayout.closeDrawers();
                    }

                    // this is for if child is not there then another fragment open
                    else {
                        if (retVal) {

                            if (menusOutputModelLocal.getMainMenuModel().size() > listPosition) {
                                if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY))) {
                                    // isNavigated = 1;

                                    fragment = new MyLibraryFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.closeDrawers();
                                } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(WATCH_HISTORY, DEFAULT_WATCH_HISTORY))) {
                                    fragment = new WatchHistoryFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.closeDrawers();
                                } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD))) {

                                    Intent mydownload = new Intent(getActivity(), MyDownloads.class);
                                    startActivity(mydownload);
                                    mDrawerLayout.closeDrawers();
                                } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {

                                /*Intent favoriteIntent = new Intent(getActivity(), FavoriteActivity.class);
                                favoriteIntent.putExtra("sectionName", languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(favoriteIntent);
                                mDrawerLayout.closeDrawers();*/
                                    Fragment fragment = new com.home.vod.fragment.MyFavouriteFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("sectionName", languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.closeDrawers();
                                } else {

                                    if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getLink_type().equalsIgnoreCase("2")) {
                                        //  isNavigated = 1;

                                        // getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                        mDrawerLayout.openDrawer(Gravity.LEFT);
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getMainMenuModel().get(listPosition).getPermalink().trim()));
                                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(browserIntent);
                                        return retVal;


                                    } else {
                                        // isNavigated = 1;

                                        fragment = new VideosListFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                        bundle.putString("item", menusOutputModelLocal.getMainMenuModel().get(listPosition).getPermalink());
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                        mDrawerLayout.closeDrawers();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (retVal) {

                        if (menusOutputModelLocal.getMainMenuModel().size() > listPosition) {
                            if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY))) {
                                // isNavigated = 1;

                                fragment = new MyLibraryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                mDrawerLayout.closeDrawers();
                            } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(WATCH_HISTORY, DEFAULT_WATCH_HISTORY))) {
                                fragment = new WatchHistoryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                mDrawerLayout.closeDrawers();
                            } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD))) {

                                Intent mydownload = new Intent(getActivity(), MyDownloads.class);
                                startActivity(mydownload);
                                mDrawerLayout.closeDrawers();
                            } else if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {

                                /*Intent favoriteIntent = new Intent(getActivity(), FavoriteActivity.class);
                                favoriteIntent.putExtra("sectionName", languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(favoriteIntent);
                                mDrawerLayout.closeDrawers();*/
                                Fragment fragment = new com.home.vod.fragment.MyFavouriteFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("sectionName", languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                mDrawerLayout.closeDrawers();
                            } else {

                                if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getLink_type().equalsIgnoreCase("2")) {
                                    //  isNavigated = 1;

                                    // getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.openDrawer(Gravity.LEFT);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getMainMenuModel().get(listPosition).getPermalink().trim()));
                                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(browserIntent);
                                    return retVal;


                                } else {
                                    // isNavigated = 1;

                                    fragment = new VideosListFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getTitle());
                                    bundle.putString("item", menusOutputModelLocal.getMainMenuModel().get(listPosition).getPermalink());
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.closeDrawers();
                                }
                            }
                        }
                    }
                }

                return retVal;
            }
        });


        mDrawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int listPosition, int childPosition, long id) {

                String ParentId = menusOutputModelLocal.getMainMenuModel().get(listPosition).getId();
                ArrayList<Integer> arrayList = new ArrayList<Integer>();

                for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().size(); i++) {
                    LogUtil.showLog("BKS", "size of whole===" + menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().size());
                    if (menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().get(i).getParent_id().equals(ParentId)) {
                        arrayList.add(i);
                    }
                }

                LogUtil.showLog("BKS", "size add child menu===" + arrayList.size());
                // isNavigated = 1;

                Fragment fragment = new VideosListFragment();
                Bundle args = new Bundle();
                args.putString("title", menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().get(arrayList.get(childPosition)).getTitle());
                args.putString("item", menusOutputModelLocal.getMainMenuModel().get(listPosition).getMainMenuChildModel().get(arrayList.get(childPosition)).getPermalink());
                fragment.setArguments(args);

                //Inflate the fragment

                //=========================================================//

                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                mDrawerLayout.closeDrawers();

                return true;
            }
        });

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        mDrawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                return true;
            }
        });



/*
        View header = inflater.inflate(R.layout.drawer_header, null);
        mDrawerListView.addHeaderView(header);

        View footer = inflater.inflate(R.layout.drawer_footer, null);
        mDrawerListView.addFooterView(footer);
        return mDrawerListView;*/

        return rootView;
    }

    private void setTextToButton() {

        String loggedInStr = preferenceManager.getLoginStatusFromPref();
        int isLogin = preferenceManager.getLoginFeatureFromPref();

        FontUtls.loadFont(getActivity(), getResources().getString(R.string.regular_fonts), Login);
        FontUtls.loadFont(getActivity(), getResources().getString(R.string.regular_fonts), Register);


        if (loggedInStr != null) {
            Login.setText(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            Register.setText(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
        } else if (loggedInStr == null) {

            if (isLogin == 1) {
                Login.setText(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN));
                Register.setText(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            } else {
                Login.setText(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
                Register.setText(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            }

        }


    }

    private void handleClickFunction() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.getText().toString().equalsIgnoreCase(languagePreference.getTextofLanguage(LOGIN, DEFAULT_LOGIN))) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    Util.check_for_subscription = 0;
                    mDrawerLayout.closeDrawers();
                    startActivity(loginIntent);
                } else if (Login.getText().toString().equalsIgnoreCase(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT))) {
                    mDrawerLayout.closeDrawers();
                    logoutPopup();
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Register.getText().toString().equalsIgnoreCase(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER))) {
                    Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                    Util.check_for_subscription = 0;
                    mDrawerLayout.closeDrawers();
                    startActivity(registerIntent);
                } else if (Register.getText().toString().equalsIgnoreCase(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE))) {
                    String id = preferenceManager.getUseridFromPref();
                    String email = preferenceManager.getEmailIdFromPref();
                    Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                    profileIntent.putExtra("EMAIL", email);
                    profileIntent.putExtra("LOGID", id);
                    mDrawerLayout.closeDrawers();

                    startActivity(profileIntent);
                }

            }
        });
    }

    private void logoutPopup() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.logout_layout, null);

        final TextView logoutText = (TextView) alertLayout.findViewById(R.id.logout_text);
        final Button yes = (Button) alertLayout.findViewById(R.id.logout_yes);
        final Button no = (Button) alertLayout.findViewById(R.id.logout_no);

        logoutText.setText(languagePreference.getTextofLanguage(SIGN_OUT_WARNING, DEFAULT_SIGN_OUT_WARNING));
        yes.setText(languagePreference.getTextofLanguage(YES, DEFAULT_YES));
        no.setText(languagePreference.getTextofLanguage(NO, DEFAULT_NO));

        dlgAlert.setView(alertLayout);
        dlgAlert.setCancelable(false);
        final AlertDialog dialog = dlgAlert.create();
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkStatus.getInstance().isConnected(getActivity())) {
                    LogoutInput logoutInput = new LogoutInput();
                    logoutInput.setAuthToken(authTokenStr);
                    LogUtil.showLog("Abhi", authTokenStr);
                    String loginHistoryIdStr = preferenceManager.getLoginHistIdFromPref();
                    logoutInput.setLogin_history_id(loginHistoryIdStr);
                    logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    LogUtil.showLog("Abhi", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, NavigationDrawerFragment.this, getActivity());
                    asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //  dlgAlert.setMessage(languagePreference.getTextofLanguage(SIGN_OUT_WARNING, DEFAULT_SIGN_OUT_WARNING));
        //   dlgAlert.setTitle("");

       /* dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(YES, DEFAULT_YES), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                // dialog.cancel();
                if(NetworkStatus.getInstance().isConnected(getActivity())) {
                    LogoutInput logoutInput = new LogoutInput();
                    logoutInput.setAuthToken(authTokenStr);
                    LogUtil.showLog("Abhi", authTokenStr);
                    String loginHistoryIdStr = preferenceManager.getLoginHistIdFromPref();
                    logoutInput.setLogin_history_id(loginHistoryIdStr);
                    logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    LogUtil.showLog("Abhi", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                    LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, NavigationDrawerFragment.this, getActivity());
                    asynLogoutDetails.executeOnExecutor(threadPoolExecutor);



                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            }
        });

        dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(NO, DEFAULT_NO), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
        dlgAlert.setCancelable(false);
           *//* dlgAlert.setNegativeButton(getResources().getString(R.string.no_str),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no_str),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });*//*
        dlgAlert.create().show();*/
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        try {
            ActionBar actionBar = getActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        } catch (Exception e) {
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getActionBar ().setIcon (R.drawable.ic_drawer);

                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setMenuItemsInDrawer(false);

             /*   Boolean isMyLibraryAdded=false;
                int isLogin=preferenceManager.getLoginFeatureFromPref();


                for(int i=0;i<menusOutputModelLocal.getMainMenuModel().size();i++) {

                    if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY))) {
                        isMyLibraryAdded=true;
                    }
                }
                if (languagePreference.getTextofLanguage(IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null){
                    if(isMyLibraryAdded){

                    }else{
                        setMenuItemsInDrawer(false);
                    }
                }else {
                    if(isMyLibraryAdded)
                        setMenuItemsInDrawer(false);
                    else{}

                }
                    */

            }
        };


        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

  /*  @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putInt (STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.menu_main, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;

            }
            if (code != 200) {
                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
            if (code == 0) {
                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
            if (code > 0) {
                if (code == 200) {
                    preferenceManager.clearLoginPref();

                    if ((featureHandler.getFeatureStatus(FeatureHandler.SIGNUP_STEP, FeatureHandler.DEFAULT_SIGNUP_STEP))) {
                        final Intent startIntent = new Intent(getActivity(), SplashScreen.class);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();

                                getActivity().finish();

                            }
                        });
                    } else {
                        final Intent startIntent = new Intent(getActivity(), MainActivity.class);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Toast.makeText(getActivity(), languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();

                                getActivity().finish();

                            }
                        });
                    }


                } else {
                    Toast.makeText(getActivity(), languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }


    public void setMenuItemsInDrawer(boolean loadHomeFragment) {

        featureHandler = FeatureHandler.getFeaturePreference(getActivity());


//        if(!loadHomeFragment){
        try {
            /* boolean my_libary_added1 = checkMyLibAdded(menusOutputModelLocal);

             *//* for(int i=0;i<menusOutputModelLocal.getMainMenuModel().size();i++) {

                    if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY))) {
                        my_libary_added1 = true;
                    }
                }*//*

                if ((featureHandler.getFeatureStatus(FeatureHandler.IS_MYLIBRARY, FeatureHandler.DEFAULT_IS_MYLIBRARY) && loggedInStr != null)) {
                    if(my_libary_added1)
                    {
                        return;
                    }
                }
                else{
                    if(!my_libary_added1)
                    {
                        return;
                    }
                }*/

            Util.main_menu_list_size = menusOutputModelLocal.getMainMenuModel().size();


        } catch (Exception e) {
            Util.main_menu_list_size = -2;
        }
//        }

        loggedInStr = preferenceManager.getUseridFromPref();
        expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
        menusOutputModelLocal = new MenusOutputModel();

        setTextToButton();
        // Kushal - Handel crashing of app when no menu item is fetched


        /* Adding My Library*/

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY))) {
                my_libary_added = true;
            }
        }


        if (featureHandler.getFeatureStatus(FeatureHandler.IS_MYLIBRARY, FeatureHandler.DEFAULT_IS_MYLIBRARY) && loggedInStr != null) {
            if (!my_libary_added) {
                MenusOutputModel.MainMenu mainMenuLibrary = new MenusOutputModel().new MainMenu();
                mainMenuLibrary.setTitle(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY));
                menusOutputModelLocal.getMainMenuModel().add(0, mainMenuLibrary);

            }
        } else {
            if (my_libary_added) {
                if (checkMyLibAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(0);
                }
            }
        }

        /* Adding My Favourite */


        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {
                my_favourite_added = true;
            }
        }


        if (featureHandler.getFeatureStatus(FeatureHandler.HAS_FAVOURITE, FeatureHandler.DEFAULT_HAS_FAVOURITE) && loggedInStr != null) {
            if (!my_favourite_added) {
                MenusOutputModel.MainMenu mainMenuMyfavourite = new MenusOutputModel().new MainMenu();
                mainMenuMyfavourite.setTitle(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                if (checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(1, mainMenuMyfavourite);
                } else {
                    menusOutputModelLocal.getMainMenuModel().add(0, mainMenuMyfavourite);
                }
            }
        } else {
            if (my_favourite_added) {
                if (checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(1);
                } else {
                    menusOutputModelLocal.getMainMenuModel().remove(0);
                }
            }
        }

        /* Adding MyDownload*/

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD))) {
                my_download_added = true;
            }
        }


        if (featureHandler.getFeatureStatus(FeatureHandler.IS_OFFLINE, FeatureHandler.DEFAULT_IS_OFFLINE) && loggedInStr != null) {
            if (!my_download_added) {
                MenusOutputModel.MainMenu mainMenuMydownload = new MenusOutputModel().new MainMenu();
                mainMenuMydownload.setTitle(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD));
                if (checkMyLibAdded(menusOutputModelLocal) & checkMyfavouritrAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(2, mainMenuMydownload);
                } else if (!checkMyLibAdded(menusOutputModelLocal) & !checkMyfavouritrAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(0, mainMenuMydownload);
                } else {
                    menusOutputModelLocal.getMainMenuModel().add(1, mainMenuMydownload);
                }
            }
        } else {
            if (my_download_added) {
                if (checkMyLibAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal))
                    menusOutputModelLocal.getMainMenuModel().remove(2);
                else if (!checkMyLibAdded(menusOutputModelLocal) && !checkMyfavouritrAdded(menusOutputModelLocal))
                    menusOutputModelLocal.getMainMenuModel().remove(0);
                else
                    menusOutputModelLocal.getMainMenuModel().remove(1);
            }
        }


        /* Adding Watch History */


        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(WATCH_HISTORY, DEFAULT_WATCH_HISTORY))) {
                watch_history_added = true;
            }
        }


        if (featureHandler.getFeatureStatus(FeatureHandler.WATCH_HISTORY, FeatureHandler.DEFAULT_IS_WATCH_HISTORY) && loggedInStr != null) {
            if (!watch_history_added) {
                MenusOutputModel.MainMenu mainMenuWatchHistory = new MenusOutputModel().new MainMenu();
                mainMenuWatchHistory.setTitle(languagePreference.getTextofLanguage(WATCH_HISTORY, DEFAULT_WATCH_HISTORY));

                if (checkMyDownloadAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal) && checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(3, mainMenuWatchHistory);

                } else if (!checkMyDownloadAdded(menusOutputModelLocal) && !checkMyfavouritrAdded(menusOutputModelLocal) && !checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(0, mainMenuWatchHistory);
                } else if (checkMyLibAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(2, mainMenuWatchHistory);

                } else if (checkMyDownloadAdded(menusOutputModelLocal) && checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(2, mainMenuWatchHistory);

                } else if (checkMyfavouritrAdded(menusOutputModelLocal) && checkMyDownloadAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().add(2, mainMenuWatchHistory);

                } else {
                    menusOutputModelLocal.getMainMenuModel().add(1, mainMenuWatchHistory);
                }

            }
        } else {
            if (watch_history_added) {

                if (checkMyDownloadAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal) && checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(3);
                } else if (!checkMyDownloadAdded(menusOutputModelLocal) && !checkMyfavouritrAdded(menusOutputModelLocal) && !checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(0);
                } else if (checkMyLibAdded(menusOutputModelLocal) && checkMyfavouritrAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(2);
                } else if (checkMyDownloadAdded(menusOutputModelLocal) && checkMyLibAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(2);
                } else if (checkMyfavouritrAdded(menusOutputModelLocal) && checkMyDownloadAdded(menusOutputModelLocal)) {
                    menusOutputModelLocal.getMainMenuModel().remove(2);
                } else {
                    menusOutputModelLocal.getMainMenuModel().remove(1);
                }

            }
        }

        MenusOutputModel.MainMenu mainMenuHome = new MenusOutputModel().new MainMenu();
        mainMenuHome.setTitle(languagePreference.getTextofLanguage(HOME, DEFAULT_HOME));
        menusOutputModelLocal.getMainMenuModel().add(mainMenuHome);


        try {
            menusOutputModelLocal.getMainMenuModel().addAll(menusOutputModelFromAPI.getMainMenuModel());
        /*    // Kushal add blank
            MenusOutputModel.MainMenu blank = new MenusOutputModel().new MainMenu();
            mainMenuHome.setTitle("");
            menusOutputModelLocal.getMainMenuModel().add(blank);*/
            menusOutputModelLocal.getFooterMenuModel().addAll(menusOutputModelFromAPI.getFooterMenuModel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        titleArray.clear();
        idArray.clear();
        /* Adding Home Menu*/
        MenusOutputModel.FooterMenu footerMenu = new MenusOutputModel().new FooterMenu();
        MenusOutputModel.FooterMenu footerMenu1 = new MenusOutputModel().new FooterMenu();
        MenusOutputModel.FooterMenu footerMenu2 = new MenusOutputModel().new FooterMenu();
        /*footerMenu.setDisplay_name(languagePreference.getTextofLanguage(CONTACT_US, DEFAULT_CONTACT_US));
        footerMenu.setPermalink("contactus");
        */
        /*Kushal Check Pruchase history added*/


        if (featureHandler.getFeatureStatus(FeatureHandler.IS_PURCHASEHISTORY, FeatureHandler.DEFAULT_IS_PURCHASEHISTORY) && loggedInStr != null) {
            if (!checkForMenuPresence(menusOutputModelLocal.getFooterMenuModel(), languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY))) {
                footerMenu.setDisplay_name(" ");
                footerMenu.setPermalink("blank");
                menusOutputModelLocal.getFooterMenuModel().add(0, footerMenu);

                footerMenu1.setDisplay_name(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
                footerMenu1.setPermalink("purchasehistory");
                menusOutputModelLocal.getFooterMenuModel().add(1, footerMenu1);

                footerMenu2.setDisplay_name(languagePreference.getTextofLanguage(CONTACT_US, DEFAULT_CONTACT_US));
                footerMenu2.setPermalink("contactus");
                menusOutputModelLocal.getFooterMenuModel().add(2, footerMenu2);


            }

        } else if (loggedInStr == null) {
            if (checkForMenuPresence(menusOutputModelLocal.getFooterMenuModel(), languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY))) {
                menusOutputModelLocal.getFooterMenuModel().remove(1);
                footerMenu.setDisplay_name(" ");
                footerMenu.setPermalink("blank");
                menusOutputModelLocal.getFooterMenuModel().add(0, footerMenu);
                footerMenu1.setDisplay_name(languagePreference.getTextofLanguage(CONTACT_US, DEFAULT_CONTACT_US));
                footerMenu1.setPermalink("contactus");
                menusOutputModelLocal.getFooterMenuModel().add(1, footerMenu1);
            } else {
                footerMenu.setDisplay_name(" ");
                footerMenu.setPermalink("blank");
                menusOutputModelLocal.getFooterMenuModel().add(0, footerMenu);
                footerMenu1.setDisplay_name(languagePreference.getTextofLanguage(CONTACT_US, DEFAULT_CONTACT_US));
                footerMenu1.setPermalink("contactus");
                menusOutputModelLocal.getFooterMenuModel().add(1, footerMenu1);
            }
        }



        //////////////////////////////////////////////////////////////////////////////////////////////////////

        /* *//* Adding Favourite*//*



        for(int i=0;i<menusOutputModelLocal.getMainMenuModel().size();i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {
                my_favourite_added = true;
            }
        }



        if (featureHandler.getFeatureStatus(FeatureHandler.HAS_FAVOURITE, FeatureHandler.DEFAULT_HAS_FAVOURITE) && loggedInStr != null) {
            if(!my_favourite_added)
            {
                MenusOutputModel.MainMenu mainMenuLibrary = new MenusOutputModel().new MainMenu();
                mainMenuLibrary.setTitle (languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE));
                menusOutputModelLocal.getMainMenuModel().add(mainMenuLibrary);
            }
        }
        else{
            if(my_favourite_added)
            {
                menusOutputModelLocal.getMainMenuModel().remove(menusOutputModelLocal.getMainMenuModel().size()-1);
            }
        }*/

////////////////


        if (menusOutputModelLocal.getMainMenuModel() != null && menusOutputModelLocal.getMainMenuModel().size() > 0) {

            for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {
                titleArray.add(menusOutputModelLocal.getMainMenuModel().get(i).getTitle());
                idArray.add(menusOutputModelLocal.getMainMenuModel().get(i).getId());
                ArrayList<String> childArray = new ArrayList<>();

                for (int j = 0; j < menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().size(); j++) {
                    if (menusOutputModelLocal.getMainMenuModel().get(i).getId().equals(menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().get(j).getParent_id())) {
                        childArray.add(menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().get(j).getTitle());
                    }
                }
                expandableListDetail.put(menusOutputModelLocal.getMainMenuModel().get(i).getId(), childArray);
            }
        }


        for (int k = 0; k < menusOutputModelLocal.getFooterMenuModel().size(); k++) {
            titleArray.add(menusOutputModelLocal.getFooterMenuModel().get(k).getDisplay_name());
            idArray.add(menusOutputModelLocal.getFooterMenuModel().get(k).getId());
            ArrayList<String> childArray = new ArrayList<>();

            expandableListDetail.put(menusOutputModelLocal.getFooterMenuModel().get(k).getId(), childArray);

        }

        // Commented by kushal - as app was crashing when clicked on the expand button.
        // Util.drawer_collapse_expand_imageview.clear();


        if (Util.main_menu_list_size == menusOutputModelLocal.getMainMenuModel().size()) {

        } else {


            Util.main_menu_list_size = menusOutputModelLocal.getMainMenuModel().size();
            adapter = new ExpandableListAdapter(getActivity(), idArray, titleArray, expandableListDetail, menusOutputModelLocal.getMainMenuModel(), menusOutputModelLocal.getFooterMenuModel());
            mDrawerListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


        if (loadHomeFragment) {
            Fragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }

    }

    private boolean checkForMenuPresence(ArrayList<MenusOutputModel.FooterMenu> arrayModel, String textofLanguage) {
        for (int i = 0; i < arrayModel.size(); i++) {
            if (arrayModel.get(i).getDisplay_name().trim().equalsIgnoreCase(textofLanguage)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMyLibAdded(MenusOutputModel menusOutputModelLocal) {
        boolean status = false;

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY))) {
                status = true;
            }
        }

        return status;
    }

    private boolean checkWatchHistoryAdded(MenusOutputModel menusOutputModelLocal) {
        boolean status = false;

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(WATCH_HISTORY, DEFAULT_WATCH_HISTORY))) {
                status = true;
            }
        }

        return status;
    }

    private boolean checkMyfavouritrAdded(MenusOutputModel menusOutputModelLocal) {
        boolean status = false;

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_FAVOURITE, DEFAULT_MY_FAVOURITE))) {
                status = true;
            }
        }

        return status;
    }

    private boolean checkMyDownloadAdded(MenusOutputModel menusOutputModelLocal) {
        boolean status = false;

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_DOWNLOAD, DEFAULT_MY_DOWNLOAD))) {
                status = true;
            }
        }

        return status;
    }
}

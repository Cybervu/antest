package com.home.vod.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.home.apisdk.apiController.FcmRegistrationDetailsAsynTask;
import com.home.apisdk.apiController.GetAppMenuAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiModel.FcmRegistrationDetailsInputModel;
import com.home.apisdk.apiModel.FcmRegistrationDetailsOutputModel;
import com.home.apisdk.apiModel.GetMenusInputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.MenusOutputModel;
import com.home.vod.R;
import com.home.vod.activity.Login;
import com.home.vod.activity.MainActivity;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.adapter.ExpandableListAdapter;
import com.home.vod.fragment.AboutUsFragment;
import com.home.vod.fragment.ContactUsFragment;
import com.home.vod.fragment.HomeFragment;
import com.home.vod.fragment.MyLibraryFragment;
import com.home.vod.fragment.VideosListFragment;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
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

import static com.home.vod.preferences.LanguagePreference.DEFAULT_EXIT_APP_WARNING;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_HOME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_YES;
import static com.home.vod.preferences.LanguagePreference.EXIT_APP_WARNING;
import static com.home.vod.preferences.LanguagePreference.HOME;
import static com.home.vod.preferences.LanguagePreference.IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.YES;
import static com.home.vod.util.Constant.authTokenStr;


/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements GetAppMenuAsync.GetMenusListener, FcmRegistrationDetailsAsynTask.FcmRegistrationDetailsListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private NavigationDrawerCallbacks mCallbacks;
    public static ArrayList<NavDrawerItem> menuList;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;

    public HashMap<String, ArrayList<String>> expandableListDetail;
    ArrayList<String> titleArray = new ArrayList<>();
    ExpandableListAdapter adapter;
    SharedPreferences pref;
    PreferenceManager preferenceManager;
    LanguagePreference languagePreference;
    GetAppMenuAsync asynLoadMenuItems = null;
    ProgressBarHandler progressDialog;

    RelativeLayout header_layout;

    boolean my_libary_added = false;
    MenusOutputModel menusOutputModelLocal, menusOutputModelFromAPI = new MenusOutputModel();
    int status;
    String message;
    String loggedInStr = null;
    Fragment fragment = null;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10, statusCode;
    LinearLayout exitApp;
    String str = "#", abs;
    String Title, Permalink, ID, TitleChild, PermalinkChild, IDChild, ClasChild, UserTitleChild,
            UserIDChild, UserParentIdChild, UserPermalinkChild, UserClasChild, fdomain, flink_type, fid, fdisplay_name,
            fpermalink, furl, ParentIdChild, LinkTypeChild, ParentId, Clas, LinkType, UserTitle, UserPermalink, UserID,
            UserParentId, UserClas, Value, Id_seq, Language_id, Language_parent_id, ValueChild, Id_seq_Child, Language_id_Child, Language_parent_id_Child;


    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    Button Send;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = PreferenceManager.getPreferenceManager(getActivity());
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        loggedInStr = preferenceManager.getUseridFromPref();
        GetMenusInputModel menuListInput = new GetMenusInputModel();
        menuListInput.setAuthToken(preferenceManager.getAuthToken().trim());
        menuListInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        asynLoadMenuItems = new GetAppMenuAsync(menuListInput, NavigationDrawerFragment.this, getActivity());
        asynLoadMenuItems.executeOnExecutor(threadPoolExecutor);
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


        FcmRegistrationDetailsInputModel fcmRegistrationDetailsInputModel = new FcmRegistrationDetailsInputModel();
        fcmRegistrationDetailsInputModel.setAuthToken(preferenceManager.getAuthToken().trim());
        fcmRegistrationDetailsInputModel.setDevice_id(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        fcmRegistrationDetailsInputModel.setDevice_type(1);
        fcmRegistrationDetailsInputModel.setFcm_token(preferenceManager.getSharedPref());
        FcmRegistrationDetailsAsynTask fcmRegistrationDetailsAsynTask = new FcmRegistrationDetailsAsynTask(fcmRegistrationDetailsInputModel, NavigationDrawerFragment.this, getActivity());
        fcmRegistrationDetailsAsynTask.executeOnExecutor(threadPoolExecutor);


        this.menusOutputModelLocal = menusOutputModel;
        this.menusOutputModelFromAPI = menusOutputModel;
        this.status = status;
        this.message = message;
        if (status == 200) {
            setMenuItemsInDrawer(true);
        }


    }


    @Override
    public void onFcmRegistrationDetailsPreExecuteStarted() {
        progressDialog = new ProgressBarHandler(getActivity());
        progressDialog.show();
    }

    @Override
    public void onFcmRegistrationDetailsPostExecuteCompleted(FcmRegistrationDetailsOutputModel fcmRegistrationDetailsOutputModel, String message) {


        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog = null;

        }

//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_drawer, container, false);
//        mDrawerListView = (ExpandableListView) inflater.inflate (R.layout.drawer_drawer, container, false);
        mDrawerListView = (ExpandableListView) v.findViewById(R.id.list_slidermenu);
        exitApp = (LinearLayout) v.findViewById(R.id.exit_app);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectItem(position);


            }
        });




      /*  expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
        mDrawerListView.setAdapter(new ExpandableListAdapter(getActivity(),mainMenuModelArrayList, mainMenuChildModelArrayList));
     */
        //for expand the child content in navigation

        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(EXIT_APP_WARNING, DEFAULT_EXIT_APP_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(YES, DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        final Intent startIntent = new Intent(getActivity(), Login.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(startIntent);
                        getActivity().finish();
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

                dlgAlert.create().show();

            }
        });

        mDrawerListView.setOnGroupExpandListener(new OnGroupExpandListener() {

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
                        } else {


                            if (menusOutputModelLocal.getFooterMenuModel().get(i).getLink_type().trim().equalsIgnoreCase("2")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getFooterMenuModel().get(listPosition).getPermalink().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);

                                //isNavigated = 1;
                                Log.v("SUBHA", "hello" + menusOutputModelLocal.getFooterMenuModel().get(i).getDisplay_name());

                                Log.v("ANU1", "clicked===");
                                Log.v("ANU1", "clicked url====" + menusOutputModelLocal.getFooterMenuModel().get(listPosition).getPermalink().trim());

                                mDrawerLayout.openDrawer(Gravity.LEFT);
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menusOutputModelLocal.getFooterMenuModel().get(listPosition).getPermalink().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);

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


                if (listPosition == 0) {
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

        /*View header = inflater.inflate (R.layout.drawer_header, null);
        mDrawerListView.addHeaderView (header);*/


        return v;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

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
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }


    public void setMenuItemsInDrawer(boolean loadHomeFragment) {
        expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
        menusOutputModelLocal = new MenusOutputModel();
        menusOutputModelLocal.getMainMenuModel().addAll(menusOutputModelFromAPI.getMainMenuModel());
        menusOutputModelLocal.getFooterMenuModel().addAll(menusOutputModelFromAPI.getFooterMenuModel());

        titleArray.clear();
       /* Adding Home Menu*/
        MenusOutputModel.MainMenu mainMenuHome = new MenusOutputModel().new MainMenu();
        mainMenuHome.setTitle(languagePreference.getTextofLanguage(HOME, DEFAULT_HOME));
        menusOutputModelLocal.getMainMenuModel().add(0, mainMenuHome);

       /* Adding Library*/
        /*if (languagePreference.getTextofLanguage(IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null) {
            MenusOutputModel.MainMenu mainMenuLibrary = new MenusOutputModel().new MainMenu();
            mainMenuLibrary.setTitle (languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY));
            menusOutputModelLocal.getMainMenuModel().add(mainMenuLibrary);

        }*/

        for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {

            if (menusOutputModelLocal.getMainMenuModel().get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY))) {
                my_libary_added = true;
            }
        }


        if (languagePreference.getTextofLanguage(IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null) {
            if (!my_libary_added) {
                MenusOutputModel.MainMenu mainMenuLibrary = new MenusOutputModel().new MainMenu();
                mainMenuLibrary.setTitle(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY));
                menusOutputModelLocal.getMainMenuModel().add(mainMenuLibrary);
            }
        } else {
            if (my_libary_added) {
                menusOutputModelLocal.getMainMenuModel().remove(menusOutputModelLocal.getMainMenuModel().size() - 1);
            }
        }


        if (menusOutputModelLocal.getMainMenuModel() != null && menusOutputModelLocal.getMainMenuModel().size() > 0) {

            for (int i = 0; i < menusOutputModelLocal.getMainMenuModel().size(); i++) {
                titleArray.add(menusOutputModelLocal.getMainMenuModel().get(i).getTitle());
                ArrayList<String> childArray = new ArrayList<>();

                for (int j = 0; j < menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().size(); j++) {
                    if (menusOutputModelLocal.getMainMenuModel().get(i).getId().equals(menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().get(j).getParent_id())) {
                        childArray.add(menusOutputModelLocal.getMainMenuModel().get(i).getMainMenuChildModel().get(j).getTitle());
                    }
                }
                expandableListDetail.put(menusOutputModelLocal.getMainMenuModel().get(i).getTitle(), childArray);
            }
        }


        if (menusOutputModelLocal.getFooterMenuModel() != null && menusOutputModelLocal.getFooterMenuModel().size() > 0) {
            for (int j = 0; j < menusOutputModelLocal.getFooterMenuModel().size(); j++) {
                if (menusOutputModelLocal.getFooterMenuModel().get(j).getPermalink().equalsIgnoreCase("terms-privacy-policy")) {
                    preferenceManager.setPrivacy_policy_url(menusOutputModelLocal.getFooterMenuModel().get(j).getUrl().trim());
                    LogUtil.showLog("BIBHU11", "menuListOutputList ::" + menusOutputModelLocal.getFooterMenuModel().get(j).getUrl());
                    LogUtil.showLog("BIBHU11", "menuListOutputList ::" + menusOutputModelLocal.getFooterMenuModel().get(j).getPermalink());
                }
            }
        }


        for (int k = 0; k < menusOutputModelLocal.getFooterMenuModel().size(); k++) {
            titleArray.add(menusOutputModelLocal.getFooterMenuModel().get(k).getDisplay_name());
            ArrayList<String> childArray = new ArrayList<>();
            expandableListDetail.put(menusOutputModelLocal.getFooterMenuModel().get(k).getDisplay_name(), childArray);

        }

        Util.drawer_collapse_expand_imageview.clear();

        // Kushal -- to handle the crash if no menu option is fetched from server.
        try {
            if (titleArray.size() > 0 && expandableListDetail.size() > 0) {
                adapter = new ExpandableListAdapter(getActivity(),
                        titleArray, expandableListDetail,
                        menusOutputModelLocal.getMainMenuModel(),
                        menusOutputModelLocal.getFooterMenuModel());
                mDrawerListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }/* else {
                titleArray.clear();
                expandableListDetail.clear();
                titleArray.add("Home");
                expandableListDetail.put("blank", titleArray);
                menusOutputModelLocal.getMainMenuModel();
                ArrayList<MenusOutputModel.MainMenu> menu = new ArrayList<>();
                MenusOutputModel.MainMenu me = new MenusOutputModel().new MainMenu();
                me.setId_seq(null);
                me.setTitle("Home");
                me.setPermalink(null);
                me.setId(null);
                me.setParent_id(null);
                me.setLink_type(null);
                me.setValue(null);
                me.setEnable(false);
                me.setIsSubcategoryPresent(null);
                me.setLanguage_id(null);
                me.setLanguage_parent_id(null);
                menu.add(me);
                adapter = new ExpandableListAdapter(getActivity(), titleArray, expandableListDetail,
                        menu, menusOutputModelLocal.getFooterMenuModel());
                mDrawerListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
//  End

        if (loadHomeFragment) {
            Fragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }

    }
}

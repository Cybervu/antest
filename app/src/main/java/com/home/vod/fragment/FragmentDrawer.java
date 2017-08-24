package com.home.vod.fragment;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.home.vod.R;
import com.home.vod.adapter.NavigationDrawerAdapter;
import com.home.vod.model.NavDrawerItem;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.LogUtil;
import com.home.vod.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.home.vod.preferences.LanguagePreference.IS_MYLIBRARY;
import static com.home.vod.preferences.LanguagePreference.MY_LIBRARY;


public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    String loggedInStr=null;
    private static ArrayList<NavDrawerItem> titles = null;
    private static TypedArray navMenuIcons;
    private FragmentDrawerListener drawerListener;
    private LanguagePreference languagePreference;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.size(); i++) {
            LogUtil.showLog("alok getData ::",titles.get(i).getTitle());
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles.get(i).getTitle());
            navItem.setIsEnabled(titles.get(i).getIsEnabled());

//            int id = navMenuIcons.getResourceId(i,0);
          //  navItem.setImageId(id);
            data.add(navItem);
        }
        return data;
    }

    public void  setData(ArrayList<NavDrawerItem> titles){

        LogUtil.showLog("alok setData ::",titles.get(titles.size()-1).getTitle());
        this.titles=titles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        languagePreference = LanguagePreference.getLanguagePreference(getActivity());
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               /* if (MainActivity.menuList != null && MainActivity.menuList.size() > 0) {
                    titles = MainActivity.menuList;*/
                    // navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

                    boolean mylibrary_title_added = false;
                    Util.my_library_visibility = false;

                    for (int i = 0; i < titles.size(); i++) {
                        LogUtil.showLog("alok setUp ::",titles.get(i).getTitle());
                        if (titles.get(i).getTitle().trim().equals(languagePreference.getTextofLanguage(MY_LIBRARY,DEFAULT_MY_LIBRARY))) {
                            LogUtil.showLog("MUVI", "IS_MYLIBRARY =" + languagePreference.getTextofLanguage( IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY));
                            LogUtil.showLog("MUVI", "loggedInStr value =" + loggedInStr);
                            mylibrary_title_added = true;

                            if (languagePreference.getTextofLanguage( IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null) {

                            } else {
                                titles.remove(i);
                                LogUtil.showLog("MUVI", "My lib removed");
                            }
                        }
                    }

                    if(!mylibrary_title_added)
                    {
                        for (int i = 0; i < titles.size(); i++) {
                            if(titles.get(i).getIsEnabled()==false)
                            {
                                if(!mylibrary_title_added) {
                                    if (languagePreference.getTextofLanguage( IS_MYLIBRARY, DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null) {
                                        titles.add(i,new NavDrawerItem(languagePreference.getTextofLanguage( MY_LIBRARY, DEFAULT_MY_LIBRARY), "102", true, "102"));
                                        mylibrary_title_added = true;
                                        LogUtil.showLog("MUVI", "My lib added");
                                    }
                                }
                            }
                        }

                    }
                    // titles.add(new NavDrawerItem(languagePreference.getTextofLanguage(Util.ABOUT_US,Util.DEFAULT_ABOUT_US),"103",true,"103"));


               /* } else {
                    titles = null;
                }*/

                adapter = new NavigationDrawerAdapter(getActivity(), getData());
                recyclerView.setAdapter(adapter);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


}

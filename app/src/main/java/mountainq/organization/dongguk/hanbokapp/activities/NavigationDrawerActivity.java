package mountainq.organization.dongguk.hanbokapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mountainq.organization.dongguk.hanbokapp.R;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;
import mountainq.organization.dongguk.hanbokapp.interfaces.NavigationDrawer;

/**
 * Created by dnay2 on 2016-11-17.
 */

public abstract class NavigationDrawerActivity extends SActivity implements NavigationDrawer {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    View mDrawerLeft;
    ListView mMenuList;
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();

    private boolean isDrawerOpen = false;

    long pressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState, layout);
        initDrawer();
    }

    private void initDrawer(){
        mDrawerLeft = findViewById(R.id.drawerLeft);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(mData.getWidth()*2/5, mData.getHeight());
        mDrawerLeft.setLayoutParams(llp);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        if(toolbar != null){
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    mDrawerLayout,
                    toolbar,
                    R.string.open_drawer,
                    R.string.close_drawer
            ) {

                @Override
                public void onDrawerOpened(View view) {
                    super.onDrawerOpened(view);
                    isDrawerOpen = true;
                    searchEditTextActivated(isDrawerOpen);
                }

                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    isDrawerOpen = false;
                    searchEditTextActivated(isDrawerOpen);
                }
            };
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        } else Log.e("test", "툴바없음");
    }

    public void setToolbarText(String text){
        ((TextView) findViewById(R.id.toolbarText)).setText(text);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public void closeDrawer() {
        if(mDrawerLayout != null && mDrawerLeft != null && isDrawerOpen) mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void openDrawer() {
        if(mDrawerLayout != null && mDrawerLeft != null) mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpen){
            closeDrawer();
        } else if(pressedTime+ 2000 < System.currentTimeMillis()) {
            pressedTime = System.currentTimeMillis();
            Toast.makeText(NavigationDrawerActivity.this, "종료키를 한번 더 눌러주세요", Toast.LENGTH_SHORT).show();
        } else super.onBackPressed();
    }


    protected void searchEditTextActivated(Boolean activated){

    }



}

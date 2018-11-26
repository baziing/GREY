package com.example.materialdesign;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.Menu;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar_light_top);
        setSupportActionBar(toolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView=(NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();

        BottomBar bottomBar_light = (BottomBar) findViewById(R.id.bottomBar_light);
//        初次选中bottombar的tab
        bottomBar_light.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.tab_light_1:
//                        square需要的界面
                        Toast.makeText(MainActivity.this,"tab1",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_light_2:
//                        home需要的界面
                        Toast.makeText(MainActivity.this,"tab2",Toast.LENGTH_SHORT).show();
                        break;
                        default:
                }
            }
        });
//        重复选择bottombar的tab，仅仅提示作用
        bottomBar_light.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_light_1:
                        Toast.makeText(MainActivity.this,"have selected tab1",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_light_2:
                        Toast.makeText(MainActivity.this,"have selected tab2",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        }
        navView.setCheckedItem(R.id.nav_information);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_information:
                        Toast.makeText(MainActivity.this,"information",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_follower:
                        Toast.makeText(MainActivity.this,"follower",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_fans:
                        Toast.makeText(MainActivity.this,"fans",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_remind:
                        Toast.makeText(MainActivity.this,"remind",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this,"setting",Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
                break;
            case R.id.backup:
                Toast.makeText(this,"Backup",Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
    }
    
}

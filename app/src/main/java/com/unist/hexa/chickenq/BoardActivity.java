package com.unist.hexa.chickenq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015-08-06.
 */
public class BoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //탭 변경 문제 해결 (추후에 23 으로 업그레이드 되면 자동 해결)
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != viewPager.getCurrentItem()) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() != viewPager.getCurrentItem()) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_info:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_board:
                Intent intent1 = new Intent(this, BoardActivity.class);
                startActivity(intent1);
                break;
            default:
                Log.d("index is ", Integer.toString(id));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BoardFragment(), "전체");
        adapter.addFragment(new BoardFragment(), "치킨");
        adapter.addFragment(new BoardFragment(), "피자");
        adapter.addFragment(new BoardFragment(), "짜장면");
        adapter.addFragment(new BoardFragment(), "탕수육");
        adapter.addFragment(new BoardFragment(), "패스트푸드");
        viewPager.setAdapter(adapter);
    }

    //ViewPager 을 위한 class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        //Fragment를 List로 받는다.
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

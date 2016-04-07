package com.wms.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wms.indicator.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private String[] titles = {"短信1", "短信2", "短信3", "短信4", "短信5", "短信6", "短信7", "短信8"};
    private List<ContentFragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ViewPagerIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);

        initDatas();

        mViewPager.setAdapter(mAdapter);
        mIndicator.setTitles(titles);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initDatas() {
        for (String title : titles) {
            fragments.add(ContentFragment.newInstance(title));
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
    }
}

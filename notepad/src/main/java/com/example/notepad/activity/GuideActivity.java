package com.example.notepad.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.notepad.R;
import com.example.notepad.fragment.GuideFragmentOne;
import com.example.notepad.fragment.GuideFragmentThree;
import com.example.notepad.fragment.GuideFragmentTwo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alan on 2017/6/26.
 */

public class GuideActivity extends FragmentActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private Fragment fg_one;
    private Fragment fg_two;
    private Fragment fg_three;
    private List<Fragment> listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_guide);

        /**
         * 须EXTEND fragmentActivity
         */

        listFragment = new ArrayList<>();

        fg_one = new GuideFragmentOne();
        fg_two = new GuideFragmentTwo();
        fg_three = new GuideFragmentThree();

        listFragment.add(fg_one);
        listFragment.add(fg_two);
        listFragment.add(fg_three);

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        };
        viewPager.setAdapter(adapter);
    }
}

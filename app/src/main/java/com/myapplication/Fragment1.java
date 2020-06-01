package com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

//import util.MyDatabaseHelper;

public class Fragment1 extends Fragment {
    private View mView;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private TabLayout.Tab trend;
    private TabLayout.Tab category;
//    private Button trend;
//    private Button category;
    private Fragment1_1 fragment1_1;
    private Fragment1_2 fragment1_2;
    private List<Fragment> list;
    private String[] fragmentTitles = new String[]{"趋势","分类"};


    //private MyDatabaseHelper dbHelper;
    private Button ceshi;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment1,container,false);
        }


        //dbHelper=new MyDatabaseHelper(getActivity(),"JiZM",null,1);


//        ceshi=(Button) mView.findViewById(R.id.ceshi);
//        ceshi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.getWritableDatabase();
//            }
//        });

        fragment1_1 = new Fragment1_1();
        fragment1_2 = new Fragment1_2();
        list = new ArrayList<>();
        list.add(fragment1_1);
        list.add(fragment1_2);

        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(viewPager);
        trend = mTabLayout.getTabAt(0);
        category = mTabLayout.getTabAt(1);


//        trend=(Button) mView.findViewById(R.id.trend);
//        trend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(0);
//            }
//        });
//        category=(Button) mView.findViewById(R.id.category);
//        category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(1);
//            }
//        });

        //dbHelper.getWritableDatabase();



        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ceshi=(Button) getView().findViewById(R.id.ceshi);





    }



    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

//        //返回每个position对应的Fragment对象
//        @Override
//        public Fragment getItem(int position) {
//            return list.get(position);
//        }
//
//        //返回list的长度，也就是Fragment对象的个数
//        @Override
//        public int getCount() {
//            return list.size();
//        }
        @Override
        public Fragment getItem(int position) {
          return list.get(position);
        }

        @Override
        public int getCount() {
           return fragmentTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles[position];
        }
    }

}

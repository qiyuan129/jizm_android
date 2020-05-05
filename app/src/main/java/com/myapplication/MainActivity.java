package com.myapplication;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pojo.*;
import dao.*;

import util.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private List<Fragment> list;

    public static MyDatabaseHelper dbHelper;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_graph:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_account:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_cycleaccount:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_myself:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        initView();
        dbHelper=new MyDatabaseHelper(this,"JiZM",null,1);

//        Date date=new Date();
//        Date date1= new Date(2020-1900, 1, 1);
//        Date date2= new Date(2020-1900, 11, 1);
//        Account account=null;
//        Bill bill=null;
//        Category category=null;
//        Periodic periodic=null;
//        AccountDAO accountDAO=new AccountDAOImpl();
//        BillDAO billDAO=new BillDAOImpl();
//        CategoryDAO categoryDAO=new CategoryDAOImpl();
//        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
//        account=new Account(1,1,"微信",500,1,date);
//        accountDAO.insertAccount(account);
//        account=new Account(2,1,"支付宝",800,1,date);
//        accountDAO.insertAccount(account);
//        bill=new Bill(1,1,1,1,0,"早餐",date,5,1,date);
//        billDAO.insertBill(bill);
//        bill=new Bill(2,1,1,1,0,"午饭",date,10,1,date);
//        billDAO.insertBill(bill);
//        bill=new Bill(3,2,2,1,0,"滴滴",date,22,1,date);
//        billDAO.insertBill(bill);
//        bill=new Bill(4,2,3,1,1,"工资",date,6000,1,date);
//        billDAO.insertBill(bill);
//        bill=new Bill(5,1,3,1,1,"分红",date,3000,1,date);
//        billDAO.insertBill(bill);
//        category=new Category(1,1,"饮食",0,1,date);
//        categoryDAO.insertCategory(category);
//        category=new Category(2,1,"出行",0,1,date);
//        categoryDAO.insertCategory(category);
//        category=new Category(3,1,"收入",1,1,date);
//        categoryDAO.insertCategory(category);
//        periodic=new Periodic(1,1,1,1,0,"吃饭",1,date1,date2,6.2,1,date);
//        periodicDAO.addPeriodic(periodic);
//        periodic=new Periodic(2,1,2,1,0,"打车",2,date1,date2,6.2,1,date);
//        periodicDAO.addPeriodic(periodic);



    }

    public void initView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        list = new ArrayList<>();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //返回每个position对应的Fragment对象
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        //返回list的长度，也就是Fragment对象的个数
        @Override
        public int getCount() {
            return list.size();
        }
    }




}

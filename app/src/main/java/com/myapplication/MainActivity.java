package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import util.MyDatabaseHelper;
import util.UserUtil;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private Fragment0 fragment0;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private List<Fragment> list;

    private Toolbar toolbar;

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
                case R.id.navigation_empty:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_cycleaccount:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_myself:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        initView();
        dbHelper=new MyDatabaseHelper(this,"JiZM",null,1);
        Intent intent1 = new Intent(MainActivity.this, LongRunningService.class);
        startService(intent1);

        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));



        boolean rememberMe=UserUtil.getRemember();
        if (!rememberMe) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        float limit=UserUtil.getLimit();
        String warning=UserUtil.earlyWarning();
        if (warning!=null){
            Toast.makeText(this,warning,Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this,"limit为"+limit,Toast.LENGTH_SHORT).show();

        }



 /*
        初始化数据
         */

//        Date date0=new Date(0);
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
//        account=new Account(1,1,"微信",500,0,date0);
//        accountDAO.insertAccount(account);
//        account=new Account(2,1,"支付宝",800,0,date0);
//        accountDAO.insertAccount(account);
//        account=new Account(3,1,"银行卡",1500,0,date0);
//        accountDAO.insertAccount(account);
//        account=new Account(4,1,"现金",900,0,date0);
//        accountDAO.insertAccount(account);
//
//        category=new Category(1,1,"餐饮美食",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(2,1,"服装美容",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(3,1,"生活日用",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(4,1,"充值缴费",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(5,1,"交通出行",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(6,1,"通讯物流",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(7,1,"休闲生活",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(8,1,"医疗保健",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(9,1,"住房物业",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(10,1,"图书教育",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(11,1,"酒店旅行",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(12,1,"爱车养车",0,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(13,1,"工资",1,0,date0);
//        categoryDAO.insertCategory(category);
//        category=new Category(14,1,"分红",1,0,date0);
//        categoryDAO.insertCategory(category);
//
//        bill=new Bill(1,1,1,1,0,"早餐",new Date(2020-1900,1-1,1),5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(2,1,1,1,0,"午饭",new Date(2020-1900,1-1,2),12.8,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(3,4,1,1,0,"晚饭",new Date(2020-1900,1-1,2),15.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(4,2,5,1,0,"滴滴出行",new Date(2020-1900,1-1,3),22,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(5,2,3,1,0,"NIKE运动鞋",new Date(2020-1900,1-1,3),468,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(6,1,3,1,0,"NIKE运动服装",new Date(2020-1900,1-1,3),230,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(7,1,1,1,0,"早餐",new Date(2020-1900,1-1,5),8.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(8,1,1,1,0,"聚餐",new Date(2020-1900,1-1,10),450,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(9,4,1,1,0,"夜宵",new Date(2020-1900,1-1,15),79,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(10,1,1,1,0,"同学聚餐",new Date(2020-1900,1-1,20),98,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(11,1,1,1,0,"约会吃饭",new Date(2020-1900,1-1,21),198,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(12,2,1,1,0,"夜宵",new Date(2020-1900,1-1,22),75,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(13,2,1,1,0,"晚饭",new Date(2020-1900,1-1,25),36,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(14,1,9,1,0,"住房物业费",new Date(2020-1900,1-1,25),300,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(15,1,3,1,0,"洗面奶",new Date(2020-1900,1-1,26),42,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(16,1,8,1,0,"感冒药",new Date(2020-1900,1-1,27),6,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(17,3,12,1,0,"汽车加油",new Date(2020-1900,1-1,28),150,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(18,1,13,1,1,"发工资",new Date(2020-1900,1-1,30),10000,0,date0);
//        billDAO.insertBill(bill);
//
//        bill=new Bill(19,1,1,1,0,"早餐",new Date(2020-1900,2-1,1),5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(20,1,1,1,0,"午饭",new Date(2020-1900,2-1,2),12.8,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(21,1,1,1,0,"晚饭",new Date(2020-1900,2-1,2),15.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(22,2,5,1,0,"滴滴出行",new Date(2020-1900,2-1,3),22,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(23,2,3,1,0,"NIKE运动鞋",new Date(2020-1900,2-1,3),468,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(24,3,3,1,0,"NIKE运动服装",new Date(2020-1900,2-1,3),230,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(25,1,1,1,0,"早餐",new Date(2020-1900,2-1,5),8.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(26,3,4,1,0,"游戏充值",new Date(2020-1900,2-1,8),128,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(27,1,1,1,0,"聚餐",new Date(2020-1900,2-1,10),555,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(28,1,1,1,0,"夜宵",new Date(2020-1900,2-1,15),79,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(29,1,1,1,0,"同学聚餐",new Date(2020-1900,2-1,20),111,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(30,1,1,1,0,"约会吃饭",new Date(2020-1900,2-1,21),222,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(31,2,1,1,0,"夜宵",new Date(2020-1900,2-1,22),75,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(32,4,9,1,0,"住房物业费",new Date(2020-1900,2-1,25),300,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(33,1,3,1,0,"洗面奶",new Date(2020-1900,2-1,26),42,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(34,3,12,1,0,"汽车加油",new Date(2020-1900,2-1,28),150,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(35,1,13,1,1,"发工资",new Date(2020-1900,2-1,28),10000,0,date0);
//        billDAO.insertBill(bill);
//
//        bill=new Bill(36,1,1,1,0,"早餐",new Date(2020-1900,3-1,1),5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(37,1,1,1,0,"午饭",new Date(2020-1900,3-1,2),12.8,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(38,1,1,1,0,"晚饭",new Date(2020-1900,3-1,2),15.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(39,2,5,1,0,"滴滴出行",new Date(2020-1900,3-1,3),22,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(40,1,4,1,0,"游戏充值",new Date(2020-1900,3-1,8),128,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(41,3,1,1,0,"聚餐",new Date(2020-1900,3-1,10),310,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(42,1,1,1,0,"夜宵",new Date(2020-1900,3-1,15),44,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(43,1,1,1,0,"同学聚餐",new Date(2020-1900,3-1,20),55,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(44,1,1,1,0,"约会吃饭",new Date(2020-1900,3-1,21),134,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(45,4,9,1,0,"住房物业费",new Date(2020-1900,3-1,25),300,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(46,1,3,1,0,"洗面奶",new Date(2020-1900,3-1,26),42,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(47,1,8,1,0,"感冒药",new Date(2020-1900,3-1,27),6,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(48,1,12,1,0,"汽车加油",new Date(2020-1900,3-1,28),150,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(49,1,13,1,1,"发工资",new Date(2020-1900,3-1,29),10000,0,date0);
//        billDAO.insertBill(bill);
//
//        bill=new Bill(50,1,1,1,0,"早餐",new Date(2020-1900,4-1,1),5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(51,1,1,1,0,"午饭",new Date(2020-1900,4-1,2),12.8,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(52,1,1,1,0,"晚饭",new Date(2020-1900,4-1,2),15.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(53,3,4,1,0,"游戏充值",new Date(2020-1900,4-1,8),128,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(54,3,1,1,0,"聚餐",new Date(2020-1900,4-1,10),362,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(55,1,1,1,0,"夜宵",new Date(2020-1900,4-1,15),23,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(56,3,1,1,0,"同学聚餐",new Date(2020-1900,4-1,20),62,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(57,1,1,1,0,"约会吃饭",new Date(2020-1900,4-1,21),155,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(58,2,1,1,0,"夜宵",new Date(2020-1900,4-1,22),75,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(59,2,1,1,0,"晚饭",new Date(2020-1900,4-1,25),36,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(60,1,9,1,0,"住房物业费",new Date(2020-1900,4-1,25),300,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(61,4,3,1,0,"洗面奶",new Date(2020-1900,4-1,26),42,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(62,1,13,1,1,"发工资",new Date(2020-1900,4-1,30),10000,0,date0);
//        billDAO.insertBill(bill);
//
//        bill=new Bill(63,1,1,1,0,"早餐",new Date(2020-1900,5-1,1),5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(64,1,1,1,0,"午饭",new Date(2020-1900,5-1,2),12.8,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(65,3,1,1,0,"晚饭",new Date(2020-1900,5-1,2),10.5,0,date0);
//        billDAO.insertBill(bill);
//        bill=new Bill(66,1,4,1,0,"游戏充值",new Date(2020-1900,5-1,8),128,0,date0);
//        billDAO.insertBill(bill);
//
//        periodic=new Periodic(1,1,9,1,2,"物业费",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),300,0,date0);
//        periodicDAO.addPeriodic(periodic);
//        periodic=new Periodic(2,2,2,1,2,"工资",2,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),10000,0,date0);
//        periodicDAO.addPeriodic(periodic);
//        periodic=new Periodic(3,3,9,1,2,"按揭",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),1200,0,date0);
//        periodicDAO.addPeriodic(periodic);
//        periodic=new Periodic(4,4,5,1,1,"动车票",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),60,0,date0);
//        periodicDAO.addPeriodic(periodic);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_category:
//                Toast.makeText(MainActivity.this,"分类",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CategoryEditActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_edit_account:
                Intent intent2 = new Intent(this,AccountEditActivity.class);
                startActivity(intent2);
//                Toast.makeText(MainActivity.this,"账户",Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }

        return true;
    }

    public void initView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        fragment0 = new Fragment0();
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        list = new ArrayList<>();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment0);
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

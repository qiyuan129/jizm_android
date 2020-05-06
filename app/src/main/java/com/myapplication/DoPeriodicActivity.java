package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import pojo.Account;
import pojo.Periodic;

public class DoPeriodicActivity extends AppCompatActivity {
    ArrayList<Periodic> periodics;
    PeriodicDAO periodicDAO;
    AccountDAO accountDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        Date time = new Date();
        Log.i("执行周期事件 "," 开始"+time.toString());
        for(Periodic item:periodics){
           check(item);
        }

        Log.i("执行周期事件 "," 结束");

    }

    public void init(){
        periodicDAO = new PeriodicDAOImpl();
        accountDAO = new AccountDAOImpl();
        periodics=(ArrayList<Periodic>) periodicDAO.listPeriodic();

    }


    public void check(Periodic obj){
        Date now = new Date();
        Date start = obj.getStart();
        Date end = obj.getEnd();
        Calendar calendar = Calendar.getInstance();


        calendar.setTime(start);
        int startDay = calendar.get(Calendar.DATE);//获取日
        int startWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.setTime(now);
        int nowDay = calendar.get(Calendar.DATE);//获取日
        int nowWeek = calendar.get(Calendar.DAY_OF_WEEK);//星期



        //当前时间大于开始而小于结束
        if(now.compareTo(start)>=0 && now.compareTo(end)<0){
            //判断天，周，月

            switch (obj.getCycle()){
                case 0://每天
                    doPeriodic(obj);
                    break;

                case 1://每周

                    if(nowWeek==startWeek){//则执行
                        doPeriodic(obj);
                    }


                        break;

                case 2://月
                    if(nowDay==startDay){//时间差不到一小时,则执行
                        doPeriodic(obj);
                    }

                    break;

                default:break;
            }

        }

        //当前时间大于结束时间，则从数据库删除这个Periodic
        else if(now.compareTo(end)>0){
            delPeriodic(obj);
        }

    }

    public void doPeriodic(Periodic obj){
        double money = obj.getPeriodic_money();
        int accountId = obj.getAccount_id();
        Account account = accountDAO.getAccountById(accountId);
        double accountMoney=account.getMoney();


        if(accountMoney>=money){//账户余额充足
            account.setMoney(accountMoney-money);
            accountDAO.updateAccount(account);
        }

        else{//账户余额不足

        }




    }



    public void delPeriodic(Periodic obj){
        //标记删除
        obj.setState(-1);
        periodicDAO.deletePeriodic(obj.getPeriodic_id());

    }



}

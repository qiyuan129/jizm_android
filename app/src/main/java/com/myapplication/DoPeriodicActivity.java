package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
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

        for(Periodic item:periodics){
           check(item);
        }


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

        //当前时间大于开始而小于结束
        if(now.compareTo(start)>=0 && now.compareTo(end)<0){
            doPeriodic(obj);
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

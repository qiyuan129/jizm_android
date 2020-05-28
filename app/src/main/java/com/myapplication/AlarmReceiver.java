package com.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.BillDAO;
import dao.BillDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import pojo.Account;
import pojo.Bill;
import pojo.Periodic;

public class AlarmReceiver extends BroadcastReceiver {

    ArrayList<Periodic> periodics;
    PeriodicDAO periodicDAO;
    AccountDAO accountDAO;
    @Override
    public void onReceive(Context context, Intent intent) {
        //执行事物逻辑

        init();

        //Date time = new Date();
        if(periodics==null || periodics.size()==0){//无周期事件可执行，直接返回
            return;
        }
        else {
            for(Periodic item:periodics){
                check(item);
            }

        }


        //Log.i("执行周期事件 "," 开始"+time.toString());

        //Log.i("执行周期事件 "," 结束");

        Log.d("LongRunningService", "成功执行今天的周期事件检查和执行 " + new Date().toString());





        //处理完周期事件后再次启动LongRunningService进入下一轮计算时间
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);

        // throw new UnsupportedOperationException("Not yet implemented");
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
        int nowMonth = calendar.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int nowYear =  calendar.get(Calendar.YEAR);//获取当前年


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
                    if(judgeDay(startDay,nowYear,nowMonth,nowDay)){//判断日期是否符合，符合则执行
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



    /*
    判断这个日期的周期时间是否执行
     */
    boolean judgeDay(int startDay,int nowYear,int nowMonth,int nowDay){
        if(nowMonth==2){
            if(bissextile(nowYear)&&nowDay==29){
                return true;//闰年的29号，周期事件都要做
            }
            else if(!bissextile(nowYear) && nowDay==28){//平年的28号，周期事件都要做
                return true;
            }
            else {
                if(startDay==nowDay){
                    return true;
                }
            }


        }

        else if(nowMonth==4||nowMonth==6||nowMonth==9||nowMonth==11){
            if(nowDay==30){
                return true;
            }
            else{
                if(startDay==nowDay){
                    return true;
                }
            }

        }

        else{//1,3,5,7,8,10,12 这几个月有31天，不会错过任何一天，所以无需在31号的时候强行执行这个周期事件
            if(startDay==nowDay){
                return true;
            }

        }


        return false;//其他情况，返回false
    }


    boolean bissextile(int year){  //判断闰年
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){  //平闰年判断算法
            return true;
        }
        else{
            return false;
        }
    }



    public void doPeriodic(Periodic obj){
        double money = obj.getPeriodic_money();
        int accountId = obj.getAccount_id();
        Account account = accountDAO.getAccountById(accountId);
        if(account==null){
            return;
        }
        else {
            double accountMoney=account.getMoney();

            if(accountMoney>=money){//账户余额充足，可以执行
                account.setMoney(accountMoney-money);
                accountDAO.updateAccount(account);

                //新建一个账单写入数据库
                BillDAO billDAO = new BillDAOImpl();
                Bill bill = new Bill(0,obj.getAccount_id(),obj.getCategory_id(),obj.getUser_id(),
                        obj.getType(),"周期事件："+obj.getPeriodic_name(),
                        new Date(),obj.getPeriodic_money(),
                        0,new Date());
                billDAO.insertBill(bill);


            }

            else{//账户余额不足,放弃执行
                Log.d("LongRunningService:", "账户余额不足，周期事件放弃执行" + new Date().toString());
                return;
            }
        }

    }



    public void delPeriodic(Periodic obj){
        //标记删除
        obj.setState(-1);
        periodicDAO.deletePeriodic(obj.getPeriodic_id());

    }









}

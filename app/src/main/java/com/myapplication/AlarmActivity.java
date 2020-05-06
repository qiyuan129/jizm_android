package com.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        // 获取AlarmManager对象
        AlarmManager aManager=(AlarmManager)getSystemService(Service.ALARM_SERVICE);
        Intent intent=new Intent();
        // 启动一个名为DialogActivity的Activity
        intent.setClass(this, DoPeriodicActivity.class);


        // 获取PendingIntent对象
        // requestCode 参数用来区分不同的PendingIntent对象
        // flag 参数常用的有4个值：
        //      FLAG_CANCEL_CURRENT 当需要获取的PendingIntent对象已经存在时，先取消当前的对象，再获取新的；
        // 	FLAG_ONE_SHOT 获取的PendingIntent对象只能使用一次，再次使用需要重新获取
        // 	FLAG_NO_CREATE 如果获取的PendingIntent对象不存在，则返回null
        //	FLAG_UPDATE_CURRENT 如果希望获取的PendingIntent对象与已经存在的PendingIntent对象相比，如果只是Intent附加的数据不同，那么当前存在的PendingIntent对象不会被取消，而是重新加载新的Intent附加的数据
        PendingIntent pi= PendingIntent.getActivity(this, 1210, intent, PendingIntent.FLAG_CANCEL_CURRENT);//如果存在，先取消再重新获取

        //获取当天24点的时间戳
        long timeDistance = getDistance();

        //实际使用，晚上12点开始，每天12点运行一次
        //aManager.setRepeating(AlarmManager.RTC_WAKEUP,timeDistance,24*60*60*1000,pi);


        //实验用，后面删除   当前开始，每隔三分钟运行一次
        aManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,24*60*60*1000,pi);


    }


    /*
   获取当前时间到今晚24点的毫秒数
    */
    public long getDistance(){
        long distance=0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        Date now = new Date();
        distance=time.getTime()-now.getTime();
        return distance;
    }


}

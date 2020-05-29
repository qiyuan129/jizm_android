package util;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import dao.BillDAO;
import dao.BillDAOImpl;

public class UserUtil {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;


    public static void setUserId(int id) {
        editor.putInt("user_id",id);
        editor.apply();
    }

    public static int getUserId() {
        int userId=preferences.getInt("user_id",0);
        return userId;
    }

    public static void setUserName(String name) {
        editor.putString("user_name",name);
        editor.apply();
    }

    public static String getUserName() {
        String name=preferences.getString("user_name","user");
        return name;
    }

    public static void setPhone(String phone) {
        editor.putString("phone",phone);
        editor.apply();
    }

    public static String getPhone() {
        String phone=preferences.getString("phone","");
        return phone;
    }

    public static void setEmail(String email) {
        editor.putString("email",email);
        editor.apply();
    }

    public static String getEmail() {
        String email=preferences.getString("email","");
        return email;
    }

    public static void setPassword(String password) {
        editor.putString("password",password);
        editor.apply();
    }

    public static String getPassword() {
        String password=preferences.getString("password","");
        return password;
    }

    public static void setRemember(boolean remember) {
        editor.putBoolean("remember",remember);
        editor.apply();
    }

    public static boolean getRemember() {
        boolean remember=preferences.getBoolean("remember",false);
        return remember;
    }

    public static void setLimit(float limit) {
        editor.putFloat("limit",limit);
        editor.apply();
    }

    public static float getLimit() {
        float limit=preferences.getFloat("limit",-1f);
        return limit;
    }

    public static void setToken(String token){
        editor.putString("token",token);
        editor.apply();
    }
    public static String getToken(){
        String token=preferences.getString("token","");
        return token;
    }

    public static String earlyWarning(){
        float limit=getLimit();
        String warning=null;
        if (limit>0){

            float oneThird=limit/3;
            float twoThird=2*limit/3;
            Date date=new Date();

            Date date1=new Date(date.getYear(),date.getMonth(),1);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.MONTH,1);
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            Date date2=calendar.getTime();

            BillDAO billDAO=new BillDAOImpl();
            double sum=billDAO.getAllmoney(date1,date2,0);

            if (sum>oneThird && sum<=twoThird){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已超出本月预算的1/3";
            }
            else if (sum>twoThird && sum<=limit){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已超出本月预算的2/3";
            }
            else if (sum>limit){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已超出本月预算";
            }
        }

        return warning;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setPreferences(SharedPreferences p) {
        preferences = p;
        editor = p.edit();
    }
}

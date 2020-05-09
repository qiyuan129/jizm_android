package util;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import dao.BillDAO;
import dao.BillDAOImpl;

public class User {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public User(SharedPreferences preferences) {

        this.preferences = preferences;
        this.editor = preferences.edit();
    }

    public void setUserId(int id) {
        editor.putInt("user_id",id);
        editor.apply();
    }

    public int getUserId() {
        int userId=preferences.getInt("user_id",0);
        return userId;
    }

    public void setUserName(String name) {
        editor.putString("user_name",name);
        editor.apply();
    }

    public String getUserName() {
        String name=preferences.getString("user_name","user");
        return name;
    }

    public void setPhone(String phone) {
        editor.putString("phone",phone);
        editor.apply();
    }

    public String getPhone() {
        String phone=preferences.getString("phone","");
        return phone;
    }

    public void setEmail(String email) {
        editor.putString("email",email);
        editor.apply();
    }

    public String getEmail() {
        String email=preferences.getString("email","");
        return email;
    }

    public void setPassword(String password) {
        editor.putString("password",password);
        editor.apply();
    }

    public String getPassword() {
        String password=preferences.getString("password","");
        return password;
    }

    public void setRemember(boolean remember) {
        editor.putBoolean("remember",remember);
        editor.apply();
    }

    public boolean getRemember() {
        boolean remember=preferences.getBoolean("remember",false);
        return remember;
    }

    public void setLimit(float limit) {
        editor.putFloat("limit",limit);
        editor.apply();
    }

    public float getLimit() {
        float limit=preferences.getFloat("limit",-1f);
        return limit;
    }

    public String earlyWarning(){
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

            if (sum>=oneThird && sum<twoThird){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已达到本月预算的1/3";
            }
            else if (sum>=twoThird && sum<limit){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已达到本月预算的2/3";
            }
            else if (sum>=limit){
                warning="本月消费"+String.format("%.2f",sum)+"￥，已达到本月预算";
            }
        }

        return warning;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }
}

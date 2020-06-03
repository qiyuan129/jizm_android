package com.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import util.UserUtil;

public class EditSettingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView goBack;
    TextView moneyText;
    TextView payWarnText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setting);

        init();
        setActionListener();

        setData();


    }



    public void init(){
        goBack = (ImageView)findViewById(R.id.setting_go_back);
        moneyText = (TextView)findViewById(R.id.pay_warn_money_text);
        payWarnText = (TextView)findViewById(R.id.pay_warn_edit);


    }


    public void setData(){
        float limitMoney = UserUtil.getLimit();
        moneyText.setText(String.valueOf(limitMoney));


    }


    public void setActionListener(){
        goBack.setOnClickListener(this);
        moneyText.setOnClickListener(this);
        payWarnText.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_go_back:
                this.finish();
                break;

            case R.id.pay_warn_edit:
                showDialog(getWindow().getDecorView());
                break;

            case R.id.pay_warn_money_text:
                showDialog(getWindow().getDecorView());
                break;

                default:
                    break;
        }
    }






    public void showDialog(View view){
        final EditText et = new EditText(EditSettingActivity.this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(EditSettingActivity.this).setTitle("请输入金额")

                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                      Log.i("确定  ","输入数字");
                        if(TextUtils.isEmpty(et.getText())){//输入为空
                            Toast.makeText(EditSettingActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                            return;//输入为空，不做处理
                        }
                        else {
                            String moneyStr = et.getText().toString();
                            float money = Float.valueOf(moneyStr);

                            //存入本地
                            UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
                            UserUtil.setLimit(money);

                            //更新文本框
                            moneyText.setText(moneyStr);

                        }



                    }
                })
                .setNegativeButton("取消",null).show();
    }










}

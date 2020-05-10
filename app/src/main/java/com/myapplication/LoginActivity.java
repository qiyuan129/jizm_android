package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private EditText et_phonenum;
    private Button btn_sure;
    private Button btn_regiser;
    private EditText et_password;
    private String phone ="";
    private String password ="";
    private Button btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_password = (EditText) findViewById(R.id.et_key);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_regiser =(Button) findViewById(R.id.btn_register) ;
        btn_find =(Button)findViewById(R.id.btn_find);
        Intent intent = new Intent();
        phone=intent.getStringExtra("phone");
        et_phonenum.setText(phone);


        //跳转到寻找密码页面
        btn_find.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this,FindActivity.class);
                startActivity(intent1);
            }
        });

        //跳转到注册页面
        btn_regiser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 =new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });


        //登录按钮对账号密码确认然后跳转到相应的页面
        btn_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户输入的验证码
                String pn = et_phonenum.getText().toString().trim().replaceAll("/s","");
                String pw = et_password.getText().toString().replaceAll("/s","");
                if (TextUtils.isEmpty(pn)) {//判断手机号是否为空
                    toast("请输入手机号");
                }
                else if (TextUtils.isEmpty(pw)) {//判断密码是否为空
                    toast("请输入密码");
                }
                //写登录的账号密码判断语句 和跳转



            }
        });
    }
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


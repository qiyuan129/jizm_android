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

import util.User;


public class LoginActivity extends AppCompatActivity {

    private EditText et_phonenum;
    private Button btn_sure;
    private Button btn_regiser;
    private EditText et_password;
    private String phone ="";
    //private String password ="";
    private Button btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                String pw = et_password.getText().toString().trim().replaceAll("/s","");
                /**
                 * 用set和get注册和登录
                 */
                User user = new User(getSharedPreferences("user",MODE_PRIVATE));
                String phoneNum=user.getPhone();
                String password=user.getPassword();
                String userName=user.getEmail();

                if (TextUtils.isEmpty(pn)) {//判断手机号是否为空
                    toast("请输入账号");
                }
                else if (TextUtils.isEmpty(pw)) {//判断密码是否为空
                    toast("请输入密码");
                }
                //写登录的账号密码判断语句 和跳转
                else if ((phoneNum.equals(pn)||userName.equals(pn))&&password.equals(pw)) {
                    user.setRemember(true);
                    Intent intent3 =new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent3);
                }
                else {
                    toast("账号或密码错误");
                }

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


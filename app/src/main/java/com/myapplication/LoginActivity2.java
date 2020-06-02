package com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

public class LoginActivity2 extends AppCompatActivity {

    private MaterialEditText etPhoneNumber;
    private PasswordEditText etVerifyCode;
    private SuperButton login;
    private XUIAlphaTextView register;

    private CountDownButtonHelper mCountDownHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etPhoneNumber=findViewById(R.id.et_phone_number);
        etVerifyCode=findViewById(R.id.input_password);
        login=findViewById(R.id.btn_login);
        register=findViewById(R.id.tv_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhoneNumber.validate()) {
                    Toast.makeText(LoginActivity2.this,"登陆成功",Toast.LENGTH_SHORT).show();

                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });


    }




}

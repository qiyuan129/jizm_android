package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

public class FindActivity2 extends AppCompatActivity {

    private AppCompatImageView appCompatImageView;
    private MaterialEditText phone;
    private MaterialEditText VerifyCode;
    private PasswordEditText password;

    private RoundButton btnGetVerifyCode;
    private SuperButton modify;

    private CountDownButtonHelper mCountDownHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find2);

        appCompatImageView=findViewById(R.id.back);
        phone=findViewById(R.id.et_phone_number);
        VerifyCode=findViewById(R.id.et_verify_code);
        btnGetVerifyCode=findViewById(R.id.btn_get_verify_code);
        password=findViewById(R.id.input_password);
        modify=findViewById(R.id.btn_modify);

        appCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
        btnGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownHelper.start();
            }
        });


        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.validate()){
                    if(VerifyCode.validate()){
                        Toast.makeText(FindActivity2.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}

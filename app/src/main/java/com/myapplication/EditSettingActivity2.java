package com.myapplication;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import util.UserUtil;

public class EditSettingActivity2 extends AppCompatActivity {


    private Toolbar toolbar;
    private SuperTextView password;
    private SuperTextView limit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setting2);

        toolbar=findViewById(R.id.toolbar);
        password=findViewById(R.id.password);
        limit=findViewById(R.id.limit);


        //设置值
        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
        float limitMoney = UserUtil.getLimit();
        limit.setRightString(String.valueOf(limitMoney));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                name.setRightString("张三");
            }
        });

        limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLimitDialog();
            }
        });


    }


    //点击超出预警
    public void showLimitDialog() {
        new MaterialDialog.Builder(EditSettingActivity2.this)
                .title("本月预算")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("本月预算", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.i("确定  ","输入新数字");

                        if(TextUtils.isEmpty(input)){//输入为空
                            Toast.makeText(EditSettingActivity2.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                            return;//输入为空，不做处理
                        }
                        else {
                            String moneyStr = input.toString();
                            float money = Float.valueOf(moneyStr);

                            //存入本地
                            UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
                            UserUtil.setLimit(money);

                            limit.setRightString(String.valueOf(money));

                        }

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        Toast.makeText(PersonalDataEditActivity2.this, "点击确定", Toast.LENGTH_SHORT).show();
                        limit.setRightString(dialog.getInputEditText().getText().toString());


                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        Toast.makeText(PersonalDataEditActivity2.this, "点击取消", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        dialog = null;
                    }
                })
                .show();
    }







}

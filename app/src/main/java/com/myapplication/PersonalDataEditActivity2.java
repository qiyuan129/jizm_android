package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class PersonalDataEditActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SuperTextView name;
    private SuperTextView phone;
    private SuperTextView email;
    private SuperTextView consume;

    String nameDefault="";
    String phoneDefault="";
    String emailDefault="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_edit2);

        toolbar=findViewById(R.id.toolbar);
        name=findViewById(R.id.user_name);
        phone=findViewById(R.id.user_phone);
        email=findViewById(R.id.user_email);
        consume=findViewById(R.id.user_consume);





//        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                name.setRightString("张三");
                showNameDialog();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                phone.setRightString("张三");
                showPhoneDialog();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                email.setRightString("张三");
                showEmailDialog();
            }
        });




    }



    //点击昵称
    public void showNameDialog() {
        new MaterialDialog.Builder(PersonalDataEditActivity2.this)
                .title("用户昵称")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("用户昵称", nameDefault, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        name.setRightString(dialog.getInputEditText().getText().toString());
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

    //点击昵称
    public void showPhoneDialog() {
        new MaterialDialog.Builder(PersonalDataEditActivity2.this)
                .title("手机号")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("手机号", phoneDefault, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        phone.setRightString(dialog.getInputEditText().getText().toString());
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

    //点击昵称
    public void showEmailDialog() {
        new MaterialDialog.Builder(PersonalDataEditActivity2.this)
                .title("邮箱")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("邮箱", emailDefault, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        email.setRightString(dialog.getInputEditText().getText().toString());
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

package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

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

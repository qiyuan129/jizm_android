package com.myapplication;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.UserUtil;

public class PersonalDataEditActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SuperTextView name;
    private SuperTextView phone;
    private SuperTextView email;
    private SuperTextView consume;
    private Button updatePersonalData;

    String nameDefault="";
    String phoneDefault="";
    String emailDefault="";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_edit2);

        toolbar=findViewById(R.id.toolbar);
        name=findViewById(R.id.user_name);
        phone=findViewById(R.id.user_phone);
        email=findViewById(R.id.user_email);
        consume=findViewById(R.id.user_consume);
        updatePersonalData=findViewById(R.id.update_personal_data_button);


        //设置电话号码，email，name数据
        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
        nameDefault = UserUtil.getUserName();
        phoneDefault = UserUtil.getPhone();
        emailDefault = UserUtil.getEmail();
        name.setRightString(nameDefault);
        phone.setRightString(phoneDefault);
        email.setRightString(emailDefault);




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
                showNameDialog();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setRightString("123*********");
                showPhoneDialog();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setRightString("20466**");
                showEmailDialog();
            }
        });

        updatePersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgeData(phoneDefault,emailDefault,nameDefault)){
                    updatePersonalData(phoneDefault,emailDefault,nameDefault);

                }

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
                        if(TextUtils.isEmpty(input)){//输入为空
                            Toast.makeText(PersonalDataEditActivity2.this,"输入不能为空",Toast.LENGTH_SHORT).show();

                            return;//输入为空，不做处理
                        }

                        else {
                            //设置名字

                            nameDefault = input.toString();
                        }


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
                        if(TextUtils.isEmpty(input)){//输入为空
                            Toast.makeText(PersonalDataEditActivity2.this,"输入不能为空",Toast.LENGTH_SHORT).show();

                            return;//输入为空，不做处理
                        }

                        else {
                            //设置名字

                            phoneDefault = input.toString();
                        }

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
                        if(TextUtils.isEmpty(input)){//输入为空
                            Toast.makeText(PersonalDataEditActivity2.this,"输入不能为空",Toast.LENGTH_SHORT).show();

                            return;//输入为空，不做处理
                        }

                        else {
                            //设置名字

                            emailDefault = input.toString();
                        }

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



    public boolean judgeData(String phone,String email,String nickName){
        AlertDialog.Builder builder  = new AlertDialog.Builder(PersonalDataEditActivity2.this);
        builder.setTitle("确认" ) ;
        builder.setPositiveButton("是" ,  null );

        //判断非空
        if(phone.equals("")||email.equals("")||nickName.equals("")){
            builder.setMessage("输入信息不能为空" ) ;
            builder.show();
            return false;
        }

        //判断手机号是否合法
        Pattern phoneP = Pattern.compile("^1(3|5|7|8|4)\\d{9}");
        Matcher m = phoneP.matcher(phone);
        if(!m.matches()){
            builder.setMessage("电话号码错误" ) ;
            builder.show();
            return false;
        }

        //判断邮箱
        Pattern emailP = Pattern.compile("[a-zA-Z0-9_\\-\\.]+@[a-zA-Z0-9]+(\\.)+[a-zA-z]+$");// + 表示至少出现一次 $表示以前面这个字符结尾
        Matcher em = emailP.matcher(email);
        if(!em.matches()){
            builder.setMessage("邮箱地址格式不正确") ;
            builder.show();
            return false;
        }

        //昵称不空就合法
        return true;
    }



    /**
     * 向服务器发起修改用户信息请求，成功回到设置界面，失败显示提示并留在原页面
     * @param phoneNumber
     * @param email
     * @param userName
     */
    private void updatePersonalData(String phoneNumber,String email,String userName){
        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
        RequestBody formBody = new FormBody.Builder()
                .add("userName",userName)
                .add("phone",phoneNumber)
                .add("email",email)
                .build();

        Request request = new Request.Builder()
                .url("http://39.100.48.69:8080/user/information")    //这里的主机地址要填电脑的ip地址
                .post(formBody)
                .addHeader("token",UserUtil.getToken())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    toast("更改信息失败:网络链接超时");
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    toast("更改信息失败:无法连接到服务器");
                    e.printStackTrace();
                }
                else{
                    toast("更改信息失败：其他未知错误，详情请查看控制台");
                    e.printStackTrace();
                }

            }


            @Override
            public void onResponse(Call call, Response response)  {
                try (ResponseBody responseBody = response.body()) {
                    String responseString=responseBody.string();
                    JSONObject resultJson=JSONObject.parseObject(responseString);
                    int statusCode=resultJson.getInteger("code");
                    String message=resultJson.getString("msg");

                    //如果响应结果状态码为成功的
                    if(statusCode>=200 && statusCode<400) {
                        toast("保存成功！");
                        //服务器改完后本地更改
                        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
                        UserUtil.setPhone(phoneDefault);
                        UserUtil.setEmail(emailDefault);
                        UserUtil.setUserName(nameDefault);

                        finish();
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="状态码："+statusCode+",错误信息："+message+'\n';
                        toast("保存失败\n"+errorMessage);
                    }
                } catch (IOException e) {
                    toast("保存失败，未知错误，错误信息请查看控制台");
                    e.printStackTrace();
                }
            }
        });
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PersonalDataEditActivity2.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }














}

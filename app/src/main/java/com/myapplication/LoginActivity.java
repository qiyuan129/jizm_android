package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.SyncUtil;


public class LoginActivity extends AppCompatActivity {

    private EditText et_phonenum;
    private Button btn_sure;
    private Button btn_regiser;
    private EditText et_password;
    private String phone ="";
    private String password ="";
    private Button btn_find;

    private final OkHttpClient client = new OkHttpClient();

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

    /**
     * 发起登录请求
     * @param phoneNumber
     * @param password
     */
    private void postLoginRequest(String phoneNumber,String password){
        JSONObject syncRecordsJsonObject= SyncUtil.getAllSyncRecords();
        RequestBody formBody = new FormBody.Builder()
                .add("type",Integer.toString(0))
                .add("account",phoneNumber)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://39.100.48.69:8080/user/login")    //这里的主机地址要填电脑的ip地址
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    toast("登录失败:网络链接超时");
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    toast("登录失败:无法连接到服务器");
                    e.printStackTrace();
                }
                else{
                    toast("登录失败：其他未知错误，详情请查看控制台");
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
                        //取出返回的数据；更新本地记录状态
                        JSONObject dataJson = resultJson.getJSONObject("data");

                        toast("登陆成功！");
                        //@TODO 用户登录时获得的token，需要保存
                        String token=dataJson.toString();
                        //@TODO 在这里添加用户信息的载入跟页面跳转等等
                        return;
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="状态码："+statusCode+",错误信息："+message+'\n';
                        toast("登录失败\n"+errorMessage);
                        return;
                    }
                } catch (IOException e) {
                    toast("登录失败，未知错误，错误信息请查看控制台");
                    e.printStackTrace();
                    return;
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


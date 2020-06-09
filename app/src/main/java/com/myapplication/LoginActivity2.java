package com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

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
import util.UserUtil;

import static com.xuexiang.xutil.tip.ToastUtils.toast;
import static util.SyncUtil.HOST_IP;

public class LoginActivity2 extends AppCompatActivity {

    private MaterialEditText etPhoneNumber;
    private PasswordEditText etVerifyCode;
    private SuperButton login;
    private XUIAlphaTextView register;

    private CountDownButtonHelper mCountDownHelper;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etVerifyCode = findViewById(R.id.input_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.tv_register);

        //登录按钮对账号密码确认然后跳转到相应的页面
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户输入的验证码
                String pn = etPhoneNumber.getText().toString().trim().replaceAll("/s", "");
                String pw = etVerifyCode.getText().toString().replaceAll("/s", "");
                if (etPhoneNumber.validate()) ;
                else if (TextUtils.isEmpty(pw)) {//判断密码是否为空
                    toast("请输入密码");
                }
                //写登录的账号密码判断语句 和跳转
                postLoginRequest(pn, pw);
            }
        });


        //跳转到注册页面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 发送登录请求
     * @param phoneNumber
     * @param password
     */
    private void postLoginRequest(String phoneNumber,String password){
        RequestBody formBody = new FormBody.Builder()
                .add("type",Integer.toString(0))
                .add("account",phoneNumber)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/user/login")    //这里的主机地址要填电脑的ip地址
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

                        toast("登录成功！");
                        storeUserInfomation(dataJson);     //将用户信息及token存储进文件

                        Intent intent=new Intent(LoginActivity2.this,MainActivity.class);
                        startActivity(intent);              //跳转到主界面
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="状态码："+statusCode+",错误信息："+message+'\n';
                        toast("登录失败\n"+errorMessage);
                    }
                } catch (IOException e) {
                    toast("登录失败，未知错误，错误信息请查看控制台");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将json中的用户数据存储智文件
     * @param dataJson
     */
    private void storeUserInfomation(JSONObject dataJson){
        String token=dataJson.getString("token");
        int userId=dataJson.getInteger("userId");
        String userName=dataJson.getString("userName");
        String phone=dataJson.getString("phone");
        String email=dataJson.getString("email");

        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
        UserUtil.setRemember(true);
        UserUtil.setToken(token);
        UserUtil.setUserId(userId);
        UserUtil.setUserName(userName);
        UserUtil.setPhone(phone);
        UserUtil.setEmail(email);
    }
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity2.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.myapplication;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.SyncUtil;
import util.UserUtil;

import static com.xuexiang.xutil.tip.ToastUtils.toast;
import static util.SyncUtil.HOST_IP;

public class LoginActivity2 extends AppCompatActivity {

    private MaterialEditText etPhoneNumber;
    private PasswordEditText etVerifyCode;
    private SuperButton login;
    private XUIAlphaTextView register;
    private XUIAlphaTextView forget;

    private CountDownButtonHelper mCountDownHelper;
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etVerifyCode = findViewById(R.id.input_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.tv_register);
        forget = findViewById(R.id.tv_forget_password);

        //登录按钮对账号密码确认然后跳转到相应的页面
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户输入的验证码
                if (etPhoneNumber.validate()){
                    String pn = etPhoneNumber.getText().toString().trim().replaceAll("/s", "");
                    String pw = etVerifyCode.getText().toString().replaceAll("/s", "");
                    if (etPhoneNumber.validate()) ;
                    else if (TextUtils.isEmpty(pw)) {//判断密码是否为空
                        toast("请输入密码");
                    }
                    //写登录的账号密码判断语句 和跳转
                    postLoginRequest(pn, pw);
                }

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

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, FindActivity2.class);
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

//                        toast("登录成功！");
                        storeUserInfomation(dataJson);     //将用户信息及token存储进文件
                        postRecoverRequest();

                        Intent intent=new Intent(LoginActivity2.this,MainActivity.class);
                        startActivity(intent);              //跳转到主界面
                        finish();
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

    private void postRecoverRequest(){
        JSONObject dateObject=new JSONObject();
        dateObject.put("Account",new Date(0));
        dateObject.put("Bill",new Date(0));
        dateObject.put("Category",new Date(0));
        dateObject.put("Periodic",new Date(0));

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                dateObject.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/app/download")
                .post(requestBody)
                .addHeader("token", UserUtil.getToken())
                .build();

        Log.d("发送恢复数据请求","尝试从服务器恢复当前用户的所有数据（该请求由软件登录后自动执行）");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    toast("下载时出错：网络连接超时");
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    toast("下载时出错：连接到服务器失败");
                    e.printStackTrace();
                }
                else{
                    toast("下载时出错：其他错误");
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

                        Log.d("处理恢复数据请求","下载数据成功，开始重置数据库并进行恢复数据");
                        SyncUtil.deleteAllRecords();
                        SyncUtil.processDownloadResult(dataJson);
                        toast("从用户数据成功");
                        //syncTextView.setText("更新数据（上次同步时间："+new Date()+")");

                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        toast("下载服务器记录失败\n"+errorMessage);
                    }
                } catch (IOException e) {
                    toast("转换响应结果为字符串时出错");
                    e.printStackTrace();
                }
                catch (Exception e){
                    toast("其他错误，详细信息见控制台");
                    e.printStackTrace();
                }
            }
        });
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

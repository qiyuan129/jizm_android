package com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xuexiang.xui.utils.CountDownButtonHelper;
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

import static util.SyncUtil.HOST_IP;

public class EditPasswordActivity extends AppCompatActivity {

    private AppCompatImageView appCompatImageView;
    private MaterialEditText oldPassword;
    private PasswordEditText newPassword;

    private SuperButton modify;

    private CountDownButtonHelper mCountDownHelper;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        appCompatImageView=findViewById(R.id.back);
        oldPassword=findViewById(R.id.old_password);
//        btnGetVerifyCode=findViewById(R.id.btn_get_verify_code);
        newPassword=findViewById(R.id.new_password);
        modify=findViewById(R.id.btn_modify);

        appCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!oldPassword.getText().toString().equals("")) || (oldPassword.getText().toString()==null)){
                    if((!newPassword.getText().toString().equals("")) || (newPassword.getText().toString()==null)){
                        updatePassword(oldPassword.getText().toString(),newPassword.getText().toString());
                    }
                    else {
                        Toast.makeText(EditPasswordActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EditPasswordActivity.this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * 向服务器发送修改密码请求
     * @param oldPassword
     * @param newPassword
     */
    private void updatePassword(String oldPassword,String newPassword){
        UserUtil.setPreferences(getSharedPreferences("user",MODE_PRIVATE));
        RequestBody formBody = new FormBody.Builder()
                .add("oldPassword",oldPassword)
                .add("newPassword",newPassword)
                .build();

        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/user/password/byOldPassword")    //这里的主机地址要填电脑的ip地址
                .post(formBody)
                .addHeader("token",UserUtil.getToken())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    toast("更改密码失败:网络链接超时");
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    toast("更改密码失败:无法连接到服务器");
                    e.printStackTrace();
                }
                else{
                    toast("更改密码失败：其他未知错误，详情请查看控制台");
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
                        toast("修改密码成功！");

                        startActivity(new Intent(EditPasswordActivity.this,EditSettingActivity2.class));
                        finish();
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="状态码："+statusCode+",错误信息："+message+'\n';
                        toast("修改密码失败\n"+errorMessage);
                    }
                } catch (IOException e) {
                    toast("修改密码失败，未知错误，错误信息请查看控制台");
                    e.printStackTrace();
                }
            }
        });
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EditPasswordActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

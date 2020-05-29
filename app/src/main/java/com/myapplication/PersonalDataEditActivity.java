package com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import util.UserUtil;

public class PersonalDataEditActivity extends AppCompatActivity implements View.OnClickListener{

    Button cancelEdit;
    Button updatePersonalData;
    EditText phoneEdit;
    EditText emailEdit;
    EditText nickNameEdit;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_edit);

        init();
        setListener();


    }


    public void setListener(){
        cancelEdit.setOnClickListener(this);
        updatePersonalData.setOnClickListener(this);
    }

    public void init(){
        cancelEdit = (Button)findViewById(R.id.cancel_edit);
        updatePersonalData =(Button)findViewById(R.id.update_personal_data);
        phoneEdit=(EditText)findViewById(R.id.phone_edit);
        emailEdit=(EditText)findViewById(R.id.email_edit);
        nickNameEdit=(EditText)findViewById(R.id.nickname_edit);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.cancel_edit:
                this.finish();//退出这个页面
                break;

            case R.id.update_personal_data: {
                Toast.makeText(this, "保存用户信息还未实现", Toast.LENGTH_SHORT).show();
                //@TODO 对输入的各条信息判断非空后，调用写好的 updatePersonalData(xxxx)函数
                // Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
                break;
            }

                default:
                    break;

        }

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

                        Intent intent=new Intent(PersonalDataEditActivity.this,Fragment4.class);
                        startActivity(intent);              //跳转回“我的”界面
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
                Toast.makeText(PersonalDataEditActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }


}

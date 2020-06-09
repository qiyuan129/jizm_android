package com.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.mob.MobSDK;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.xuexiang.xutil.tip.ToastUtils.toast;
import static util.SyncUtil.HOST_IP;

public class RegisterActivity2 extends AppCompatActivity {

    private TimerTask tt;
    private Timer tm;
    private AppCompatImageView back;
    private MaterialEditText phone;
    private MaterialEditText user;
    private MaterialEditText VerifyCode;
    private PasswordEditText password;
    private RoundButton btnGetVerifyCode;
    private SuperButton register;
    private String Password;
    private String userName;
    private String phoneNum;
    private String Phone;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private CountDownButtonHelper mCountDownHelper;
    private static final int CODE_REPEAT = 1; //重新发送

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        MobSDK.init(this, "24793dde94dc6", "6e636da9b16e5bf8d5fae19ca30ea6ac");
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）

        back =findViewById(R.id.back);
        phone=findViewById(R.id.et_phone_number);
        user=findViewById(R.id.et_user);
        VerifyCode=findViewById(R.id.et_verify_code);
        btnGetVerifyCode=findViewById(R.id.btn_get_verify_code);
        password=findViewById(R.id.input_password);
        register=findViewById(R.id.btn_login);

        btnGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = phone.getText().toString().trim().replaceAll("/s", "");
                if (!TextUtils.isEmpty(Phone)) {
                    //定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE = "[1][358]\\d{9}";
                    //把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    //把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(Phone);
                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {//匹配手机号是否存在
                        alterWarning();

                    } else {
                        toast("手机号格式错误");
                    }
                } else {
                    toast("请先输入手机号");
                }
            }
    });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户输入的验证码
                String name = user.getText().toString().replaceAll("/s","");
                String code = VerifyCode.getText().toString().replaceAll("/s","");
                String pn = phone.getText().toString().trim().replaceAll("/s","");
                String pw = password.getText().toString().replaceAll("/s","");

                if (TextUtils.isEmpty(pn)) {//判断手机号是否为空
                    toast("请输入手机号");
                }
                //else if (!TextUtils.isEmpty(pn)) {//手机号非空的情况下判断唯一性
                /**
                 *
                 *
                 *
                 * 判断填写的手机号（这里的变量是pn）是否是唯一的
                 *
                 *
                 */
                // }
                else if (TextUtils.isEmpty(name)) {//判断用户名是否为空
                    toast("请输入用户名");
                }
                //else if (!TextUtils.isEmpty(name)) {//用户名非空的情况下判断唯一性
                /**
                 *
                 *
                 * 判断填写的用户名(这里的变量是name)是否是唯一的
                 *
                 *
                 */
                //}
                else if (TextUtils.isEmpty(pw)) {//判断密码是否为空
                    toast("请输入密码");
                }
                else if (!TextUtils.isEmpty(code)) {//判断验证码是否为空
                    //验证
                    SMSSDK.submitVerificationCode( country, Phone, code);
                }else{//如果用户输入的内容为空，提醒用户
                    toast("请输入验证码后再提交");
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity2.this,LoginActivity2.class));
                finish();
            }
        });
}
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                btnGetVerifyCode.setEnabled(true);
                register.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                btnGetVerifyCode.setText("重新发送验证码");
            }else {
                btnGetVerifyCode.setText(TIME + "重新发送验证码");
            }
        }
    };
    //回调
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {     //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {       //提交验证码成功
                    phoneNum = phone.getText().toString();
                    Password = password.getText().toString();
                    userName = user.getText().toString();

                    postRegisterRequest(userName,phoneNum,Password);

                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    toast("获取验证码成功");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            }else{//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                toast("验证码不匹配，请重新输入验证码");
            }
        }
    };
    //吐司的一个小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity2.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //弹窗确认下发
    private void alterWarning() {
        //构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("我们将要发送到" + Phone + "验证"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                //通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                SMSSDK.getVerificationCode(country, Phone);
                //做倒计时操作
                Toast.makeText(RegisterActivity2.this, "已发送" + which, Toast.LENGTH_SHORT).show();
                btnGetVerifyCode.setEnabled(false);
                register.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt,0,1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(RegisterActivity2.this, "已取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);

    }

    /**
     * 向服务器发送注册请求（成功则跳转到登录界面）
     * @param userName
     * @param phoneNumber
     * @param password
     */
    private void postRegisterRequest(String userName,String phoneNumber,String password){
        RequestBody formBody = new FormBody.Builder()
                .add("userName",userName)
                .add("phoneNumber",phoneNumber)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/user/registry")    //这里的主机地址要填电脑的ip地址
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    toast("注册失败:网络链接超时");
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    toast("注册失败:无法连接到服务器");
                    e.printStackTrace();
                }
                else{
                    toast("注册失败：其他未知错误，详情请查看控制台");
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

                        toast("注册成功！");

                        Intent intent=new Intent(RegisterActivity2.this,LoginActivity2.class);
                        startActivity(intent);              //跳转到登录界面
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="状态码："+statusCode+",错误信息："+message+'\n';
                        toast("注册失败\n"+errorMessage);
                    }
                } catch (IOException e) {
                    toast("注册失败，未知错误，错误信息请查看控制台");
                    e.printStackTrace();
                }
            }
        });
    }

}


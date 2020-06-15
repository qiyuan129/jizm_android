package com.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.BillDAO;
import dao.BillDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.SyncUtil;
import util.UserUtil;

import static android.content.Context.MODE_PRIVATE;
import static util.SyncUtil.HOST_IP;

public class Fragment5  extends Fragment {

    private View mView;

    SuperTextView user;
    SuperTextView userAccount;
    SuperTextView userRecover;
    SuperTextView userUpdate;
    //SuperTextView userExport;
    SuperTextView userSetting;
    SuperTextView userAbout;
    SuperButton logout;

    TextView userName;
    TextView comsume;


    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment5,container,false);
        }

        user=(SuperTextView)mView.findViewById(R.id.user_head) ;
        userAccount=(SuperTextView)mView.findViewById(R.id.user_account) ;
        userRecover=(SuperTextView)mView.findViewById(R.id.user_recover) ;
        userUpdate=(SuperTextView)mView.findViewById(R.id.user_update) ;
     //   userExport=(SuperTextView)mView.findViewById(R.id.user_export) ;
        userSetting=(SuperTextView)mView.findViewById(R.id.user_setting) ;
        userAbout=(SuperTextView)mView.findViewById(R.id.user_about) ;
        logout=(SuperButton)mView.findViewById(R.id.btn_logout) ;

        comsume = (TextView)mView.findViewById(R.id.consume) ;
        userName = (TextView)mView.findViewById(R.id.user_name_f5);



        //设置值
        UserUtil.setPreferences(requireContext().getSharedPreferences("user",MODE_PRIVATE));

        comsume.setText("本月消费："+String.valueOf(monthMoney())+"￥");
        userName.setText(UserUtil.getUserName());








        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalDataEditActivity2.class);
                startActivity(intent);
            }
        });

        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"我的账户",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(),AccountEditActivity.class);
                startActivity(intent2);

            }
        });

        userRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                    .setMessage("将使用服务器数据覆盖本地数据，是否继续?")//设置显示的内容
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            Toast.makeText(getActivity(),"开始恢复...",Toast.LENGTH_SHORT).show();
                            postRecoverRequest();

                        }
                    }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件

//                            Toast.makeText(getActivity(),"点击取消",Toast.LENGTH_SHORT).show();

                        }
                    }).show();//在按键响应事件中显示此对话框
//                Toast.makeText(getActivity(),"开始恢复...",Toast.LENGTH_SHORT).show();
//                postRecoverRequest();
            }
        });

        userUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"同步中...请稍候",Toast.LENGTH_SHORT).show();
                postDownloadRequest();

            }
        });

//        userExport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"导出账单",Toast.LENGTH_SHORT).show();
//
//            }
//        });

        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSettingActivity2.class);
                startActivity(intent);

            }
        });

        userAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"关于我们",Toast.LENGTH_SHORT).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题
                        .setMessage("退出将清空本地全部数据，有未同步内容请先同步")//设置显示的内容
                        .setPositiveButton("确定退出",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                UserUtil.setRemember(false);
                                UserUtil.setEmail(null);
                                UserUtil.setPhone(null);
                                UserUtil.setToken(null);
                                UserUtil.setUserId(0);
                                UserUtil.setUserName(null);
                                UserUtil.setPassword(null);

                                SyncUtil.deleteAllRecords();      //清空所有数据

                                Toast.makeText(getActivity(),"退出登录",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginActivity2.class));

                                getActivity().finish();

                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

//                            Toast.makeText(getActivity(),"点击取消",Toast.LENGTH_SHORT).show();

                    }
                }).show();//在按键响应事件中显示此对话框


            }
        });


        return mView;
    }





    public double monthMoney(){
        Date date=new Date();
        Date date1=new Date(date.getYear(),date.getMonth(),1);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        Date date2=calendar.getTime();
        BillDAO billDAO=new BillDAOImpl();
        double sum=billDAO.getAllmoney(date1,date2,0);

        return sum;
    }




    /**
     * 执行上传任务
     */
    private void postUploadRequest(){
        JSONObject syncRecordsJsonObject= SyncUtil.getAllSyncRecords();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                syncRecordsJsonObject.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/app/synchronization")
                .post(requestBody)
                .addHeader("token", UserUtil.getToken())
                .build();

        Log.d("发送上传请求","发送了上传请求");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(getActivity(),"上传时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(getActivity(),"上传时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(getActivity(),"上传时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
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
                    Log.d("处理上传相应","接收到了上传响应结果，状态码为"+statusCode);

                    //如果响应结果状态码为成功的
                    if(statusCode>=200 && statusCode<400) {
                        //取出返回的数据；更新本地记录状态
                        JSONObject dataJson = resultJson.getJSONObject("data");
                        Log.d("处理上传响应","响应结果为成功（200），开始更新本地记录同步状态");
                        SyncUtil.processUploadResult(dataJson);


                        Looper.prepare();
                        Toast.makeText(getActivity(),"同步成功！",Toast.LENGTH_LONG).show();
                        Looper.loop();

                        Log.d("处理上传响应","!!!更新本地记录同步状态完成，同步成功！");
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(getActivity(),"上传本地记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getActivity(),"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 向服务器发起请求，获取待下载数据并对本地数据库执行相应更新
     */
    private void postDownloadRequest(){
        JSONObject lastUpdateDates= SyncUtil.getTableLastUpdateTime();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                lastUpdateDates.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/app/download")
                .post(requestBody)
                .addHeader("token", UserUtil.getToken())
                .build();

        Log.d("发送下载请求","开始尝试下载");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
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

                        SyncUtil.processDownloadResult(dataJson);

                        Log.d("处理下载请求","下载成功，开始执行上传");
                        postUploadRequest();

                        //syncTextView.setText("更新数据（上次同步时间："+new Date()+")");

                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(getActivity(),"下载服务器待同步记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getActivity(),"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
                catch (Exception e){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"其他错误，详细信息见控制台",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 向服务器发送恢复数据请求
     */
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

        Log.d("发送恢复数据请求","开始尝试获取当前用户在服务器端的所有数据");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(getActivity(),"下载时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
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
                        //syncTextView.setText("更新数据（上次同步时间："+new Date()+")");

                        Looper.prepare();
                        Toast.makeText(getActivity(),"恢复数据成功",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(getActivity(),"下载服务器记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(getActivity(),"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
                catch (Exception e){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"其他错误，详细信息见控制台",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });

    }

}

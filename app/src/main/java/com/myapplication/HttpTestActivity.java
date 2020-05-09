package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pojo.Account;
import pojo.Bill;
import pojo.Category;
import pojo.Periodic;
import util.MyDatabaseHelper;
import util.SyncUtil;
import static com.myapplication.MainActivity.dbHelper;

public class HttpTestActivity extends AppCompatActivity {
    private Button submitButton;
    private EditText editText;
    private EditText resultText;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

//    public static MyDatabaseHelper dbHelper;

    public static String jsonStr = "{\n" +
            "    \"Category\": {\n" +
            "        \"tableName\": \"Category\",\n" +
            "        \"needSync\": true,\n" +
            "        \"recordList\": [\n" +
            "            {\n" +
            "                \n" +
            "                \"localId\": 1,\n" +
            "                \"userId\": 1,\n" +
            "                \"name\": \"手机1\",\n" +
            "                \"type\": 1,\n" +
            "                \"modified\": null,\n" +
            "                \"state\": 1\n" +
            "               \n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"Bill\": {\n" +
            "        \"tableName\": \"Bill\",\n" +
            "        \"needSync\": true,\n" +
            "        \"recordList\": [\n" +
            "            {\n" +
            "               \n" +
            "                \"localId\": 1,\n" +
            "                \"account\": {\n" +
            "                    \"id\": null,\n" +
            "                    \"localId\": 1,\n" +
            "                    \"userId\": null,\n" +
            "                    \"name\": null,\n" +
            "                    \"money\": null,\n" +
            "                    \"modified\": null,\n" +
            "                    \"state\": 0,\n" +
            "                    \"anchor\": null\n" +
            "                },\n" +
            "                \"category\": {\n" +
            "                    \"id\": null,\n" +
            "                    \"localId\": 1,\n" +
            "                    \"userId\": null,\n" +
            "                    \"name\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"modified\": null,\n" +
            "                    \"state\": 0,\n" +
            "                    \"anchor\": null\n" +
            "                },\n" +
            "                \"userId\": 1,\n" +
            "                \"type\": 1,\n" +
            "                \"name\": \"买一台iphone se\",\n" +
            "                \"date\": null,\n" +
            "                \"money\": 2899.0,\n" +
            "                \"modified\": null,\n" +
            "                \"state\": 1,\n" +
            "                \"anchor\": null\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": null,\n" +
            "                \"localId\": 2,\n" +
            "                \"account\": {\n" +
            "                    \"id\": null,\n" +
            "                    \"localId\": 1,\n" +
            "                    \"userId\": null,\n" +
            "                    \"name\": null,\n" +
            "                    \"money\": null,\n" +
            "                    \"modified\": null,\n" +
            "                    \"state\": 0,\n" +
            "                    \"anchor\": null\n" +
            "                },\n" +
            "                \"category\": {\n" +
            "                    \"id\": null,\n" +
            "                    \"localId\": 2,\n" +
            "                    \"userId\": null,\n" +
            "                    \"name\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"modified\": null,\n" +
            "                    \"state\": 0,\n" +
            "                    \"anchor\": null\n" +
            "                },\n" +
            "                \"userId\": 1,\n" +
            "                \"type\": 1,\n" +
            "                \"name\": \"买c++primerplus\",\n" +
            "                \"date\": null,\n" +
            "                \"money\": 102.0,\n" +
            "                \"modified\": null,\n" +
            "                \"state\": 1,\n" +
            "                \"anchor\": null\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);

        init();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RequestBody formBody = new FormBody.Builder()
//                        .add("search", "Jurassic Park")
//                        .build();
                JSONObject syncRecordsJsonObject=SyncUtil.getAllSyncRecords();
                RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                        syncRecordsJsonObject.toJSONString());

                Request request = new Request.Builder()
                        .url("http://192.168.0.100:8080/app/synchronization")    //这里的主机地址要填电脑的ip地址
                        .post(requestBody)
                        .addHeader("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIiwiZXhwIjoxNTg4OTMzMjMwfQ.329QAOqCnB6GcqDxwdH9h_Ui7gAwAi4T0MguwnTZ_p8")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

//                            Headers responseHeaders = response.headers();
//                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                            }
                            //resultText.setText(responseBody.string());
                            //System.out.println(responseBody.string());
                            JSONObject resultJson=JSONObject.parseObject(responseBody.string());

                            JSONObject dataJson=resultJson.getJSONObject("data");
                            SyncUtil.processUploadResult(dataJson);
                        }
                    }
                });

//                Request request = new Request.Builder()
//                        .url("https://publicobject.com/helloworld.txt")
//                        .build();
//
//                try (Response response = client.newCall(request).execute()) {
//                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0; i < responseHeaders.size(); i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//
//                    System.out.println(response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }




    private void init() {
        submitButton = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        resultText = (EditText) findViewById(R.id.resultText);
//
        dbHelper=new MyDatabaseHelper(this,"JiZM",null,1);

        Date date0=new Date(0);
        Date date=new Date();
        Date date1= new Date(2020-1900, 1, 1);
        Date date2= new Date(2020-1900, 11, 1);
        Account account=null;
        Bill bill=null;
        Category category=null;
        Periodic periodic=null;
        AccountDAO accountDAO=new AccountDAOImpl();
        BillDAO billDAO=new BillDAOImpl();
        CategoryDAO categoryDAO=new CategoryDAOImpl();
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
        account=new Account(1,1,"微信",500,0,date0);
        accountDAO.insertAccount(account);
        account=new Account(2,1,"支付宝",800,0,date0);
        accountDAO.insertAccount(account);
        account=new Account(3,1,"银行卡",1500,0,date0);
        accountDAO.insertAccount(account);
        account=new Account(4,1,"现金",900,0,date0);
        accountDAO.insertAccount(account);

        category=new Category(1,1,"餐饮美食",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(2,1,"服装美容",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(3,1,"生活日用",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(4,1,"充值缴费",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(5,1,"交通出行",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(6,1,"通讯物流",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(7,1,"休闲生活",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(8,1,"医疗保健",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(9,1,"住房物业",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(10,1,"图书教育",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(11,1,"酒店旅行",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(12,1,"爱车养车",0,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(13,1,"工资",1,0,date0);
        categoryDAO.insertCategory(category);
        category=new Category(14,1,"分红",1,0,date0);
        categoryDAO.insertCategory(category);

        bill=new Bill(1,1,1,1,0,"早餐",new Date(2020-1900,1-1,1),5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(2,1,1,1,0,"午饭",new Date(2020-1900,1-1,2),12.8,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(3,4,1,1,0,"晚饭",new Date(2020-1900,1-1,2),15.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(4,2,5,1,0,"滴滴出行",new Date(2020-1900,1-1,3),22,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(5,2,3,1,0,"NIKE运动鞋",new Date(2020-1900,1-1,3),468,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(6,1,3,1,0,"NIKE运动服装",new Date(2020-1900,1-1,3),230,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(7,1,1,1,0,"早餐",new Date(2020-1900,1-1,5),8.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(8,1,1,1,0,"聚餐",new Date(2020-1900,1-1,10),450,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(9,4,1,1,0,"夜宵",new Date(2020-1900,1-1,15),79,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(10,1,1,1,0,"同学聚餐",new Date(2020-1900,1-1,20),98,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(11,1,1,1,0,"约会吃饭",new Date(2020-1900,1-1,21),198,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(12,2,1,1,0,"夜宵",new Date(2020-1900,1-1,22),75,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(13,2,1,1,0,"晚饭",new Date(2020-1900,1-1,25),36,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(14,1,9,1,0,"住房物业费",new Date(2020-1900,1-1,25),300,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(15,1,3,1,0,"洗面奶",new Date(2020-1900,1-1,26),42,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(16,1,8,1,0,"感冒药",new Date(2020-1900,1-1,27),6,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(17,3,12,1,0,"汽车加油",new Date(2020-1900,1-1,28),150,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(18,1,13,1,1,"发工资",new Date(2020-1900,1-1,30),10000,0,date0);
        billDAO.insertBill(bill);

        bill=new Bill(19,1,1,1,0,"早餐",new Date(2020-1900,2-1,1),5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(20,1,1,1,0,"午饭",new Date(2020-1900,2-1,2),12.8,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(21,1,1,1,0,"晚饭",new Date(2020-1900,2-1,2),15.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(22,2,5,1,0,"滴滴出行",new Date(2020-1900,2-1,3),22,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(23,2,3,1,0,"NIKE运动鞋",new Date(2020-1900,2-1,3),468,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(24,3,3,1,0,"NIKE运动服装",new Date(2020-1900,2-1,3),230,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(25,1,1,1,0,"早餐",new Date(2020-1900,2-1,5),8.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(26,3,4,1,0,"游戏充值",new Date(2020-1900,2-1,8),128,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(27,1,1,1,0,"聚餐",new Date(2020-1900,2-1,10),555,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(28,1,1,1,0,"夜宵",new Date(2020-1900,2-1,15),79,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(29,1,1,1,0,"同学聚餐",new Date(2020-1900,2-1,20),111,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(30,1,1,1,0,"约会吃饭",new Date(2020-1900,2-1,21),222,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(31,2,1,1,0,"夜宵",new Date(2020-1900,2-1,22),75,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(32,4,9,1,0,"住房物业费",new Date(2020-1900,2-1,25),300,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(33,1,3,1,0,"洗面奶",new Date(2020-1900,2-1,26),42,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(34,3,12,1,0,"汽车加油",new Date(2020-1900,2-1,28),150,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(35,1,13,1,1,"发工资",new Date(2020-1900,2-1,28),10000,0,date0);
        billDAO.insertBill(bill);

        bill=new Bill(36,1,1,1,0,"早餐",new Date(2020-1900,3-1,1),5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(37,1,1,1,0,"午饭",new Date(2020-1900,3-1,2),12.8,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(38,1,1,1,0,"晚饭",new Date(2020-1900,3-1,2),15.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(39,2,5,1,0,"滴滴出行",new Date(2020-1900,3-1,3),22,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(40,1,4,1,0,"游戏充值",new Date(2020-1900,3-1,8),128,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(41,3,1,1,0,"聚餐",new Date(2020-1900,3-1,10),310,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(42,1,1,1,0,"夜宵",new Date(2020-1900,3-1,15),44,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(43,1,1,1,0,"同学聚餐",new Date(2020-1900,3-1,20),55,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(44,1,1,1,0,"约会吃饭",new Date(2020-1900,3-1,21),134,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(45,4,9,1,0,"住房物业费",new Date(2020-1900,3-1,25),300,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(46,1,3,1,0,"洗面奶",new Date(2020-1900,3-1,26),42,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(47,1,8,1,0,"感冒药",new Date(2020-1900,3-1,27),6,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(48,1,12,1,0,"汽车加油",new Date(2020-1900,3-1,28),150,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(49,1,13,1,1,"发工资",new Date(2020-1900,3-1,29),10000,0,date0);
        billDAO.insertBill(bill);

        bill=new Bill(50,1,1,1,0,"早餐",new Date(2020-1900,4-1,1),5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(51,1,1,1,0,"午饭",new Date(2020-1900,4-1,2),12.8,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(52,1,1,1,0,"晚饭",new Date(2020-1900,4-1,2),15.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(53,3,4,1,0,"游戏充值",new Date(2020-1900,4-1,8),128,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(54,3,1,1,0,"聚餐",new Date(2020-1900,4-1,10),362,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(55,1,1,1,0,"夜宵",new Date(2020-1900,4-1,15),23,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(56,3,1,1,0,"同学聚餐",new Date(2020-1900,4-1,20),62,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(57,1,1,1,0,"约会吃饭",new Date(2020-1900,4-1,21),155,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(58,2,1,1,0,"夜宵",new Date(2020-1900,4-1,22),75,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(59,2,1,1,0,"晚饭",new Date(2020-1900,4-1,25),36,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(60,1,9,1,0,"住房物业费",new Date(2020-1900,4-1,25),300,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(61,4,3,1,0,"洗面奶",new Date(2020-1900,4-1,26),42,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(62,1,13,1,1,"发工资",new Date(2020-1900,4-1,30),10000,0,date0);
        billDAO.insertBill(bill);

        bill=new Bill(63,1,1,1,0,"早餐",new Date(2020-1900,5-1,1),5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(64,1,1,1,0,"午饭",new Date(2020-1900,5-1,2),12.8,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(65,3,1,1,0,"晚饭",new Date(2020-1900,5-1,2),10.5,0,date0);
        billDAO.insertBill(bill);
        bill=new Bill(66,1,4,1,0,"游戏充值",new Date(2020-1900,5-1,8),128,0,date0);
        billDAO.insertBill(bill);

        periodic=new Periodic(1,1,9,1,2,"物业费",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),300,0,date0);
        periodicDAO.addPeriodic(periodic);
        periodic=new Periodic(2,2,2,1,2,"工资",2,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),10000,0,date0);
        periodicDAO.addPeriodic(periodic);
        periodic=new Periodic(3,3,9,1,2,"按揭",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),1200,0,date0);
        periodicDAO.addPeriodic(periodic);
        periodic=new Periodic(4,4,5,1,1,"动车票",1,new Date(2020-1900,1-1,1),new Date(2020-1900,5-1,1),60,0,date0);
        periodicDAO.addPeriodic(periodic);

    }


    public void run() throws Exception {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }
}

package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
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
import util.SyncUtil;

public class HttpTestActivity extends AppCompatActivity {
    private Button submitButton;
    private EditText editText;
    private EditText resultText;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

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
                        .addHeader("token", "emptyToken")
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
                            System.out.println(responseBody.string());
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

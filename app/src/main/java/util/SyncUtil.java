package util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pojo.Account;
import pojo.Bill;
import pojo.Category;
import pojo.Periodic;

import static android.support.constraint.Constraints.TAG;


public class SyncUtil {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

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

    public static void uploadRecords(){

    }
    public static JSONObject getAccountSyncRecordsJson(boolean needSync, List<Account> accountList){
        JSONObject accountSyncRecords=new JSONObject();

        //构建符合格式的json记录数组
        JSONArray accountArray=new JSONArray();
        for(Account account:accountList){
            //创建对应每个Account记录的json对象
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("localId",account.getAccount_id());
            jsonObject.put("userId",account.getUser_id());
            jsonObject.put("name",account.getAccount_name());
            jsonObject.put("money",account.getMoney());
            jsonObject.put("state",account.getState());
            jsonObject.put("anchor",account.getAnchor());

            //添加到jsonArray中
            accountArray.add(jsonObject);
        }

        accountSyncRecords.put("needSync",needSync);
        accountSyncRecords.put("tableName","Account");
        accountSyncRecords.put("recordList",accountArray);

        return accountSyncRecords;
    }


    public static JSONObject getBillSyncRecordsJson(boolean needSync, List<Bill> billList){
        JSONObject billSyncRecords=new JSONObject();

        //构建符合格式的json记录数组
        JSONArray billArray=new JSONArray();
        for(Bill bill:billList){
            //创建对应每个Bill记录的json对象
            JSONObject billObject=new JSONObject();
            JSONObject accountObject=new JSONObject();
            JSONObject categoryObject=new JSONObject();

            accountObject.put("localId",bill.getAccount_id());
            categoryObject.put("localId",bill.getCategory_id());
            billObject.put("localId",bill.getBill_id());
            billObject.put("account",accountObject);
            billObject.put("category",categoryObject);
            billObject.put("userId",bill.getUser_id());
            billObject.put("type",bill.getType());
            billObject.put("name",bill.getBill_name());
            billObject.put("date",bill.getBill_date());
            billObject.put("money",bill.getBill_money());
            billObject.put("state",bill.getState());
            billObject.put("anchor",bill.getAnchor());
            //添加到jsonArray中
            billArray.add(billObject);
        }

        billSyncRecords.put("needSync",needSync);
        billSyncRecords.put("tableName","Bill");
        billSyncRecords.put("recordList",billArray);

        return billSyncRecords;
    }

    public static JSONObject getCategorySyncRecordsJson(boolean needSync,
                                                        List<Category> categoryList){
       JSONObject categorySyncRecords=new JSONObject();

        JSONArray categoryArray=new JSONArray();
        for(Category category:categoryList){
            JSONObject categoryObject=new JSONObject();

            categoryObject.put("localId",category.getCategory_id());
            categoryObject.put("userId",category.getUser_id());
            categoryObject.put("name",category.getCategory_name());
            categoryObject.put("type",category.getType());
            categoryObject.put("state",category.getState());
            categoryObject.put("anchor",category.getAnchor());

            categoryArray.add(categoryObject);
        }

        categorySyncRecords.put("needSync",needSync);
        categorySyncRecords.put("tableName","Category");
        categorySyncRecords.put("recordList",categoryArray);

        return categorySyncRecords;
    }

    public static JSONObject getPeriodicSyncRecordsJson(boolean needSync,
                                                        List<Periodic> periodicList){
        JSONObject  periodicSyncRecords=new JSONObject();

        JSONArray periodicArray=new JSONArray();
        for(Periodic periodic:periodicList){
            JSONObject periodicObject=new JSONObject();

            periodicObject.put("localId",periodic.getPeriodic_id());
            periodicObject.put("accountId",periodic.getAccount_id());
            periodicObject.put("categoryId",periodic.getCategory_id());
            periodicObject.put("userId",periodic.getUser_id());
            periodicObject.put("type",periodic.getType());
            periodicObject.put("name",periodic.getPeriodic_name());
            periodicObject.put("cycle",periodic.getCycle());
            periodicObject.put("start",periodic.getStart());
            periodicObject.put("end",periodic.getEnd());
            periodicObject.put("money",periodic.getPeriodic_money());

            periodicArray.add(periodicObject);

        }

        periodicSyncRecords.put("needSync",needSync);
        periodicSyncRecords.put("tableName","Periodic");
        periodicSyncRecords.put("recordList",periodicArray);

        return periodicSyncRecords;
    }

}

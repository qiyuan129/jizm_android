package util;


import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
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

import static android.support.constraint.Constraints.TAG;


public class SyncUtil {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final static OkHttpClient client = new OkHttpClient();

    public static void uploadRecords(){
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
                    JSONObject resultJson=JSONObject.parseObject(responseBody.string());
                    SyncUtil.processUploadResult(resultJson);
                }
            }
        });
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
    public static JSONObject getAllSyncRecords(){
        JSONObject accountRecordsJson;
        JSONObject billRecordsJson;
        JSONObject categoryRecordsJson;
        JSONObject periodicRecordsJson;
        JSONObject finalResult=new JSONObject();

        AccountDAO accountDAO = new AccountDAOImpl();
        BillDAO billDAO = new BillDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        PeriodicDAO periodicDAO = new PeriodicDAOImpl();

        List<Account> accountList = accountDAO.getSyncAccount();
        List<Bill> billList = billDAO.getSyncBill();
        List<Category> categoryList=categoryDAO.getSyncCategory();
        List<Periodic> periodicList=periodicDAO.getSyncPeriodic();

        if(accountList.size()>0) {
            accountRecordsJson = SyncUtil.getAccountSyncRecordsJson(true, accountList);
        }
        else{
            accountRecordsJson=SyncUtil.getAccountSyncRecordsJson(false,null);
        }

        if(billList.size()>0){
            billRecordsJson=SyncUtil.getBillSyncRecordsJson(true,billList);
        }
        else{
            billRecordsJson=SyncUtil.getBillSyncRecordsJson(false,null);
        }

        if(categoryList.size()>0){
            categoryRecordsJson=SyncUtil.getCategorySyncRecordsJson(true,categoryList);
        }
        else{
            categoryRecordsJson=SyncUtil.getCategorySyncRecordsJson(false,null);
        }

        if(periodicList.size()>0){
            periodicRecordsJson=SyncUtil.getPeriodicSyncRecordsJson(true,periodicList);
        }
        else{
            periodicRecordsJson=SyncUtil.getPeriodicSyncRecordsJson(false,null);
        }

        finalResult.put("Account",accountRecordsJson);
        finalResult.put("Bill",billRecordsJson);
        finalResult.put("Category",categoryRecordsJson);
        finalResult.put("Periodic",periodicRecordsJson);

        return finalResult;
    }

    public static void processUploadResult(JSONObject dataJson){
        JSONObject accountSyncRecords=dataJson.getJSONObject("Account");
        JSONObject billSyncRecords=dataJson.getJSONObject("Bill");
        JSONObject categorySyncRecords=dataJson.getJSONObject("Category");
        JSONObject periodicSyncRecords=dataJson.getJSONObject("Periodic");

        AccountDAO accountDAO = new AccountDAOImpl();
        BillDAO billDAO = new BillDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        PeriodicDAO periodicDAO = new PeriodicDAOImpl();

        if(accountSyncRecords.getBoolean("needSync")==true){
            JSONArray accounts=accountSyncRecords.getJSONArray("recordList");
            for(int i=0;i<accounts.size();i++){
                JSONObject account=accounts.getJSONObject(i);
                //更新记录的同步状态和对应的服务器记录更新时间
                accountDAO.setStateAndAnchor(account.getInteger("localId"),
                        9,account.getDate("modified"));
            }
        }
        if(billSyncRecords.getBoolean("needSync")==true){
            JSONArray bills=billSyncRecords.getJSONArray("recordList");
            for(int i=0;i<bills.size();i++){
                JSONObject bill=bills.getJSONObject(i);
                billDAO.setStateAndAnchor(bill.getInteger("localId"),
                        9,bill.getDate("modified"));
            }
        }
        if(categorySyncRecords.getBoolean("needSync")==true){
            JSONArray categories=categorySyncRecords.getJSONArray("recordList");
            for(int i=0;i<categories.size();i++){
                JSONObject category=categories.getJSONObject(i);
                categoryDAO.setStateAndAnchor(category.getInteger("localId"),
                        9,category.getDate("modified"));
            }
        }
        if(periodicSyncRecords.getBoolean("needSync")==true){
            JSONArray periodics=periodicSyncRecords.getJSONArray("recordList");
            for(int i=0;i<periodics.size();i++){
                JSONObject periodic=periodics.getJSONObject(i);
                periodicDAO.setStateAndAnchor(periodic.getInteger("localId"),
                        9,periodic.getDate("modified"));
            }
        }
        System.out.println("处理上传结果响应成功");
    }
}

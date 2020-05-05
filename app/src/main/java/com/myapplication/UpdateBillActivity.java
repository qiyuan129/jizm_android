package com.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.BillDAO;
import dao.BillDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import pojo.Account;
import pojo.Bill;
import pojo.Category;

/*
实现修改账单
 */
public class UpdateBillActivity extends AppCompatActivity implements View.OnClickListener{

    Bill bill;
    ArrayList<Category> categories;
    ArrayList<Account> accounts;


    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic  5.3  0:06
    public ArrayList<String> listData = new  ArrayList<String>();
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;



    public ArrayList<String> listAccount=new  ArrayList<String>();
    private TextView accountView ;
    private Spinner accountSpinner;
    private ArrayAdapter<String> accountAdapter;


    Button incomeButton;
    Button outComeButton;
    Button saveBillButton;

    //名称和金额
    EditText BillNameEdit;
    EditText BillMoneyEdit;


    //时间
    private TextView billUpdateTime;
    protected String days;
    public int mYear;
    public int mMonth;
    public int mDays;


    //辅助变量
    public int categoryId;
    public int accountId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);


        init();
        setData();




        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listData);
        accountAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listAccount);



        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        accountSpinner.setAdapter(accountAdapter);


        //spinner数据加载完，设置一下默认选中的值
        setDefaultPsinnerItem();
        setDefaultAccountItem();


        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListenerBup());
        accountSpinner.setOnItemSelectedListener(new BillAccountSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        accountSpinner.setVisibility(View.VISIBLE);


    }



    public void init(){
        view = (TextView) findViewById(R.id.bill_update_spinnerText);
        accountView=(TextView)findViewById(R.id.bill_account_spinnerText);
        spinner = (Spinner) findViewById(R.id.bill_update_spinner);
        accountSpinner=(Spinner)findViewById(R.id.bill_account_spinner);

        //输入支出按钮
        incomeButton = (Button)findViewById(R.id.bill_update_income);
        outComeButton = (Button)findViewById((R.id.bill_update_outcome));
        saveBillButton = (Button)findViewById(R.id.save_bill_update) ;


        //选择时间
        billUpdateTime=(TextView)findViewById(R.id.bill_update_time);


        //账单名称和金额
        BillNameEdit = (EditText)findViewById(R.id.bill_name_update) ;
        BillMoneyEdit = (EditText)findViewById(R.id.bill_update_money) ;







        //设置事件监听者
        billUpdateTime.setOnClickListener(this);
        incomeButton.setOnClickListener(this);
        outComeButton.setOnClickListener(this);
        saveBillButton.setOnClickListener(this);


    }




    /*
    设置下拉列表默认选中的值
     */
    public void setDefaultPsinnerItem(){
        //dao的代码还没好，先注释掉
        //拿着periodic的category_id取得Category，然后用category名字和listData做对比得到选中的值
       /* int catId = bill.getCategory_id();
        CategoryDAO catDAO = new CategoryDAOImpl();
        Category category = catDAO.getCategoryById(catId);
        String categoryName = category.getCategory_name();
        for(int i=0;i<listData.size();i++){
            if(categoryName.equals(listData.get(i))){
                spinner.setSelection(i);
                break;
            }
        }*/


        spinner.setSelection(1);


    }



    public void setDefaultAccountItem(){
       /* //dao的代码还没好，先注释掉
        int actId = periodic.getAccount_id();
        //CategoryDAO catDAO = new CategoryDAOImpl();
        AccountDAO actDAO = new AccountDAOImpl();
        //Category category = catDAO.getById(catId);
        Account account = actDAO.getAccountById(actId);
       // String categoryName = category.getCategory_name();
        String accountName = account.getAccount_name();

      //  for(int i=0;i<listData.size();i++){
          //  if(categoryName.equals(listData.get(i))){
           //     spinner.setSelection(i);
           //     break;
          //  }
        //}


        for(int i=0;i<listAccount.size();i++){
            if(accountName.equals(listAccount.get(i))){
                accountSpinner.setSelection(i);
                break;
            }
        }
*/


        //后面删除
        accountSpinner.setSelection(1);
    }





    public void setData(){

        String tmpId=getIntent().getStringExtra("billId");
        int id = Integer.parseInt(tmpId);

        BillDAO billDAO = new BillDAOImpl();
        bill= billDAO.getBillById(id);


       //实验用，后面删除
       // bill = new Bill(1,3,1,5,1,"早餐",new Date(12457244),32.2,2, null);


        //设置开始结束时间

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        days = formatter.format(bill.getBill_date());

        billUpdateTime.setText(days);




        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        //开始时间
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);






        //设置按钮选中
        if(bill.getType()==0){//0是支出
            outComeButton.setSelected(true);
            incomeButton.setSelected(false);
            // outComeButton.setBackgroundColor();
        }
        else {//1是收入
            incomeButton.setSelected(true);
            outComeButton.setSelected(false);
            // incomeButton.setBackgroundColor();
        }



        //设置账目名称和金额输入框内容
        BillNameEdit.setText(bill.getBill_name());
        BillMoneyEdit.setText(String.valueOf(bill.getBill_money()));



       /* //设置类别值

        CategoryDAO categoryDAO=new CategoryDAOImpl();
        categories = (ArrayList<Category>) categoryDAO.listCategory();
        for(Category cat:categories){
            listData.add(cat.getCategory_name());
        }
*/

        listData.add("学习用品");
        listData.add("生活用品");
        listData.add("娱乐消费");
        listData.add("买菜");
        listData.add("旅游");
        listData.add("其他");



          /*//设置账户值

          AccountDAO accountDAO=new AccountDAOImpl();
          accounts = (ArrayList<Account>) accountDAO.listAccount();
          for(Account act:accounts){
              listData.add(act.getAccount_name());
          }*/

        listAccount.add("支付宝账户");
        listAccount.add("微信账户");
        listAccount.add("银行账户");





    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bill_update_time:
                showStartDateSelector();
                break;

            case R.id.bill_update_income:
                setBillType(0);//0代表支出
                break;
            case R.id.bill_update_outcome:
                setBillType(1);//1代表收入
                break;

            case R.id.save_bill_update:

                if(saveBill()){
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    //把值periodicId传回上一个界面
                    Intent intent = new Intent();
                    intent.putExtra("id_return_bill",String.valueOf(bill.getBill_id()));
                    setResult(RESULT_OK,intent);
                    this.finish();
                }

                break;


                default:
                    break;

        }

    }


    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListenerBup implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData.get(arg2));
            //设置类别
            //adapter和listData顺序应该是一样的吧

            //设置Category_id 记得去掉注释
            //categoryId = categories.get(arg2).getCategory_id();

            //测试用，后面删除
            categoryId=0;


        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }




    //内部类，下拉列表监听者，使用数组形式操作
    class BillAccountSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            accountView.setText("你的选择是："+ listAccount.get(arg2));



            //设置Account_id 记得去掉注释
            //  accountId = accounts.get(arg2).getAccount_id();


            //测试用，后面删除
            accountId=0;


        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }








    //显示开始日期选择器
    public void showStartDateSelector() {
        new DatePickerDialog(this,(DatePicker datePicker, int i, int i1, int i2) -> {
            mYear = i;
            mMonth = i1;
            mDays = i2;
            if (mMonth + 1 < 10) {
                if (mDays < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0")
                            .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0")
                            .append(mMonth + 1).append("-").append(mDays).toString();
                }
            } else{
                if (mDays < 10) {
                    days = new StringBuffer().append(mYear).append("-")
                            .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-")
                            .append(mMonth + 1).append("-").append(mDays).toString();
                }
            }
            billUpdateTime.setText(days);
        },mYear,mMonth,mDays).show();
    }





    public void setBillType(int type){
        if(type==0){//支出
            outComeButton.setSelected(true);
            incomeButton.setSelected(false);
            bill.setType(0);
        }
        else {//1代表收入
            incomeButton.setSelected(true);
            outComeButton.setSelected(false);
            bill.setType(1);
        }
    }




    public void setBillCategory(){
        //设置categoryId
        bill.setCategory_id(categoryId);
    }

    public void setBillAccount(){
        bill.setAccount_id(accountId);
    }



    public boolean review(){
       /*
       检查数据是否达到存入要求
        */
        AlertDialog.Builder builder  = new AlertDialog.Builder(UpdateBillActivity.this);
        builder.setTitle("确认" ) ;
        builder.setMessage("账单信息有误，请检查" ) ;
        builder.setPositiveButton("是" ,  null );

        //输入框为空
        if(TextUtils.isEmpty(BillMoneyEdit.getText())||
               TextUtils.isEmpty(BillMoneyEdit.getText())){
            builder.show();
            return false;
       }

        //检查时间是否超过现在
        Log.i("days: ",days);
        try {
            Date date= new SimpleDateFormat("yyyy-MM-dd").parse(days);
            Date now = new Date();
            if(date.compareTo(now)==1){//输入事件比当前时间大，错误
                builder.show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }





        String moneyStr=BillMoneyEdit.getText().toString();
        if(moneyStr.startsWith("-")){
            builder.show();
            return false;
        }


        return true;

    }


    public boolean saveBill(){

        if(!review()){ //检查是否有为空的数据，不合法的数据等,不合法则直接返回
            return false;
        }

       //设置名称和金额
        String moneyStr=BillMoneyEdit.getText().toString();

        bill.setBill_name(BillNameEdit.getText().toString());
        bill.setBill_money(Double.valueOf(moneyStr));


        //设置种类
        setBillCategory();
        //设置账户
        setBillAccount();


        //设置时间
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Date date=new Date(days);


        bill.setBill_date(date);


        Log.i("bill_name: ",bill.getBill_name());
        Log.i("bill_money: ",String.valueOf(bill.getBill_money()));
        Log.i("bill_type: ",String.valueOf(bill.getType()));
        Log.i("bill_category: ",String.valueOf(bill.getCategory_id()));
        Log.i("bill_recycle: ",String.valueOf(bill));


        //存入数据库 暂时注解掉
        BillDAO billDAO = new BillDAOImpl();
        billDAO.updateBill(bill);



        return true;
    }



}

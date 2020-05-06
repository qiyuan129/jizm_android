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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import pojo.Account;
import pojo.Category;
import pojo.Periodic;

public class AddPeriodicActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Category> categories;
    ArrayList<Account> accounts;


    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic
    public ArrayList<String> listData = new ArrayList<String>();
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;



    public ArrayList<String> listAccount = new ArrayList<String>();
    private TextView accountView ;
    private Spinner accountSpinner;
    private ArrayAdapter<String> accountAdapter;





    //收入支出单选button组
    private RadioGroup RBGroup;
    private RadioButton incomeRB, outcomeRB;

    //周期类型单选button组
    private RadioGroup RecycleRBGroup;
    private RadioButton perDay,perWeek,perMonth;



    //日期选择
    private TextView startDate;
    private TextView endDate;


    //选择开始时间
    protected String myStartDay;
    protected int startYear;
    protected int startMonth;
    protected int startDay;

    //选择开始时间
    protected String myEndDay;
    protected int endYear;
    protected int endMonth;
    protected int endDay;


    //确认添加按钮
    Button storePeriodic;
    private EditText nameEditText;
    private EditText moneyEditText;




    //辅助变量
    String periodicName;
    double money;
    Date start;
    Date end;
    Date anchor;
    int recycleId;
    int typeId;
    int categoryId;
    int accountId;
    int state=0;//本地新增






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_periodic);


        init();
        setData();


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listData);
        accountAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listAccount);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        accountSpinner.setAdapter(accountAdapter);


        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        accountSpinner.setOnItemSelectedListener(new AccountSpinnerSelectedListener());


        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        accountSpinner.setVisibility(View.VISIBLE);


        //设置默认选中的下拉列表项，需要在spinner填充数据之后
        spinner.setSelection(0,true);
        accountSpinner.setSelection(0,true);


        //注意是给RadioGroup绑定监视器
        RBGroup.setOnCheckedChangeListener(new MyRadioButtonListener());
        RecycleRBGroup.setOnCheckedChangeListener(new MyRadioButtonListener());

    }



    public void init(){
        view = (TextView) findViewById(R.id.spinnerText);
        accountView= (TextView) findViewById(R.id.account_spinnerText);
        spinner = (Spinner) findViewById(R.id.Spinner01);
        accountSpinner=(Spinner)findViewById(R.id.account_Spinner01);


        //收入支出单选
        RBGroup = (RadioGroup) findViewById(R.id.rg_type);
        incomeRB = (RadioButton) findViewById(R.id.income_RB);
        outcomeRB = (RadioButton) findViewById(R.id.outcome_RB);

        //周期单选按钮组
        RecycleRBGroup = (RadioGroup) findViewById(R.id.rg_cycle);
        perDay = (RadioButton) findViewById(R.id.per_day_RB);
        perWeek = (RadioButton) findViewById(R.id.per_week_RB);
        perMonth = (RadioButton) findViewById(R.id.per_month_RB);


        //名称金额
        nameEditText = (EditText)findViewById(R.id.add_periodic_name_edit);
        moneyEditText = (EditText)findViewById(R.id.add_periodic_money_edit);






        //日期选择
        startDate = (TextView)findViewById(R.id.date_start);
        endDate = (TextView)findViewById(R.id.date_end);


        //保存按钮
        storePeriodic=(Button)findViewById(R.id.store_periodic);


        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        //开始时间
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        //结束时间
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);




        //设置当前时间
        myStartDay = new StringBuffer().append(startYear).append("-").append( startMonth + 1).append("-").append(startDay).toString();
        startDate.setText(myStartDay);
        startDate.setOnClickListener(this);

        myEndDay = new StringBuffer().append(endYear).append("-").append( endMonth + 1).append("-").append(endDay).toString();
        endDate.setText(myEndDay);
        endDate.setOnClickListener(this);


        //保存按钮事件监听
        storePeriodic.setOnClickListener(this);



    }





    public void setData(){

        //后面把注释消除
        //设置种类
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        categories = (ArrayList<Category>) categoryDAO.listCategory();
        for(Category cat:categories){
            listData.add(cat.getCategory_name());
        }


        //设置账户
        AccountDAO accountDAO = new AccountDAOImpl();
        accounts = (ArrayList<Account>) accountDAO.listAccount();
        for(Account act:accounts){
            listAccount.add(act.getAccount_name());
        }




        //测试用，后面删除
        if(categories==null){
            categories = new ArrayList<Category>();
        }
        if(listData==null){
            listData=new ArrayList<String>();
            listData.add("学习用品");
            listData.add("生活用品");
        }


        if(accounts==null){
            accounts=new ArrayList<Account>();
        }

        if(listAccount==null){
            listAccount=new ArrayList<String>();
            listAccount.add("支付宝账户");
            listAccount.add("微信账户");
            listAccount.add("银行账户");

        }





        //设置默认选中的值
        RBGroup.check(outcomeRB.getId());
        RecycleRBGroup.check(perDay.getId());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_start:
                showStartDateSelector();
                break;

            case R.id.date_end:
                showEndDateSelector();
                break;


            case R.id.store_periodic:
                //Toast.makeText(this,"保存还没有实现",Toast.LENGTH_SHORT).show();
                savePeriodic();

                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                //告诉上一个界面添加成功
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                this.finish();
                break;


            default:
                break;
        }

    }


    public boolean review(){
        //检查数据是否达到存入要求

        AlertDialog.Builder builder  = new AlertDialog.Builder(AddPeriodicActivity.this);
        builder.setTitle("确认" ) ;
        builder.setMessage("事件信息有误，请检查" ) ;
        builder.setPositiveButton("是" ,  null );

        //输入框为空
        if(TextUtils.isEmpty(nameEditText.getText())||
                TextUtils.isEmpty(moneyEditText.getText())){
            builder.show();
            return false;
        }



        try {
            Date startDate= new SimpleDateFormat("yyyy-MM-dd").parse(myStartDay);
            Date endDate= new SimpleDateFormat("yyyy-MM-dd").parse(myEndDay);

           // Date now = new Date();

            if(startDate.compareTo(endDate)==1){
                builder.setMessage("开始时间应小于结束时间" ) ;
                builder.show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }


    public boolean savePeriodic(){
        if(!review()){
            return false;
        }

        periodicName=nameEditText.getText().toString();
        money=Double.valueOf(moneyEditText.getText().toString());

       //recycleId和typeId已近处理了


        //categoryId在  SpinnerSelectedListener里处理了



        try {
            start= new SimpleDateFormat("yyyy-MM-dd").parse(myStartDay);
            end= new SimpleDateFormat("yyyy-MM-dd").parse(myEndDay);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        //存入数据库，暂时不做，因为缺少一些信息，比如Periodic.id需要沟通一下
        //id是随便填的
        Periodic periodic=new Periodic(0,accountId,categoryId,1,
                typeId,periodicName,recycleId,start,end,money,state,new Date());


        PeriodicDAO periodicDAO = new PeriodicDAOImpl();
        periodicDAO.addPeriodic(periodic);


        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
        return true;
    }

    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData.get(arg2)+":"+String.valueOf(arg2));

            //设置Category_id 记得去掉注释
            categoryId = categories.get(arg2).getCategory_id();

            //测试用，后面删除
           // categoryId=0;


        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    //内部类，下拉列表监听者，使用数组形式操作
    class AccountSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData.get(arg2)+":"+String.valueOf(arg2));

            //设置Account_id 记得去掉注释
            accountId=accounts.get(arg2).getAccount_id();

            //测试用，后面删除
            //accountId=0;


        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }



    //显示开始日期选择器
    public void showStartDateSelector() {
        new DatePickerDialog(this,(DatePicker datePicker, int i, int i1, int i2) -> {
            startYear = i;
            startMonth = i1;
            startDay = i2;
            if (startMonth + 1 < 10) {
                if (startDay < 10) {
                    myStartDay = new StringBuffer().append(startYear).append("-").append("0")
                            .append(startMonth + 1).append("-").append("0").append(startDay).toString();
                } else {
                    myStartDay = new StringBuffer().append(startYear).append("-").append("0")
                            .append(startMonth + 1).append("-").append(startDay).toString();
                }
            } else{
                if (startDay < 10) {
                    myStartDay = new StringBuffer().append(startYear).append("-")
                            .append(startMonth + 1).append("-").append("0").append(startDay).toString();
                } else {
                    myStartDay = new StringBuffer().append(startYear).append("-")
                            .append(startMonth + 1).append("-").append(startDay).toString();
                }
            }
            startDate.setText(myStartDay);

            //打印看一下
            Toast.makeText(this,
                    String.valueOf(startYear)+String.valueOf(startMonth + 1)+String.valueOf(startDay),
                    Toast.LENGTH_SHORT).show();
            Log.i("month", myStartDay);


        }, startYear, startMonth, startDay).show();
    }




    //显示结束日期选择器
    public void showEndDateSelector() {
        new DatePickerDialog(this,(DatePicker datePicker, int i, int i1, int i2) -> {
            endYear = i;
            endMonth = i1;
            endDay = i2;
            if (endMonth + 1 < 10) {
                if (endDay < 10) {
                    myEndDay = new StringBuffer().append(endYear).append("-").append("0")
                            .append(endMonth + 1).append("-").append("0").append(endDay).toString();
                } else {
                    myEndDay = new StringBuffer().append(endYear).append("-").append("0")
                            .append(endMonth + 1).append("-").append(endDay).toString();
                }
            } else{
                if (endDay < 10) {
                    myEndDay = new StringBuffer().append(endYear).append("-")
                            .append(endMonth + 1).append("-").append("0").append(endDay).toString();
                } else {
                    myEndDay = new StringBuffer().append(endYear).append("-")
                            .append(endMonth + 1).append("-").append(endDay).toString();
                }
            }
            endDate.setText(myEndDay);

            //打印看一下
            Toast.makeText(this,
                    String.valueOf(endYear)+String.valueOf(endMonth + 1)+String.valueOf(endDay),
                    Toast.LENGTH_SHORT).show();
            Log.i("month", myEndDay);


        }, endYear, endMonth, endDay).show();
    }








    //按钮组的监听者
    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.outcome_RB:
                    // 当用户选择收入时
                    Log.i("outcome_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    setType(1);
                    break;
                case R.id.income_RB:
                    // 当用户选择支出时
                    setType(0);
                    Log.i("income_RB", "当前用户选择"+ incomeRB.getText().toString());
                    break;

                case R.id.per_day_RB:
                    setRecycleId(0);
                    break;

                case R.id.per_week_RB:
                    setRecycleId(1);
                    break;

                case R.id.per_month_RB:
                    setRecycleId(2);
                    break;



                    default:break;
            }



        }


        /*
        设置周期事件周期 day week month season year
         */
        public void setRecycleId(int id){
          /*
          将周期设为多少天就可以了
           */

         Log.i("周期id将被设置为： ",String.valueOf(id));
         recycleId = id;

        }


        public void setType(int id){
            typeId=id;
        }



    }








}

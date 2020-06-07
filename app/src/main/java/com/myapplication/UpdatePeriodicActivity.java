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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import pojo.Account;
import pojo.Category;
import pojo.Periodic;

public class UpdatePeriodicActivity extends AppCompatActivity implements View.OnClickListener{

    public Periodic periodic;

    ArrayList<Category> categories;
    ArrayList<Account> accounts;

    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic  5.3  0:06
//    public ArrayList<String> listData=new  ArrayList<String>();
    public ArrayList<String> outcomeListData = new ArrayList<String>();
    public ArrayList<String> incomeListData = new ArrayList<String>();
    public ArrayList<Integer> outcomeListId = new ArrayList<Integer>();
    public ArrayList<Integer> incomeListId = new ArrayList<Integer>();

    //private TextView view ;
    private MaterialSpinner spinner;
    private ArrayAdapter<String> adapter;

    public ArrayList<String> listAccount=new  ArrayList<String>();
    //private TextView accountView ;
    private MaterialSpinner accountSpinner;
    private ArrayAdapter<String> accountAdapter;

    //收入支出单选button组
    private RadioGroup RBGroup;
    private RadioButton incomeRB, outcomeRB;
    private int isIncome = 0;         //记录类别（收入/支出）

    //周期类型单选button组
    private RadioGroup RecycleRBGroup;
    private RadioButton perDay,perWeek,perMonth;

    //日期选择
    private RoundButton startDate;
    private RoundButton endDate;

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

    //确认修改按钮
    SuperButton storePeriodic;
    ImageView cancelUpdate;

    //需要设置默认值组件
    EditText periodicName;
    EditText periodicMoney;

    //辅助变量
    public String name;
    public double money;
    public Date start;
    public Date end;
    //Date anchor;
    public int recycleId;
    public int typeId;
    public int categoryId;
    public int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_periodic);

        init();
        setData();

        //将可选内容与ArrayAdapter连接起来
        if(isIncome == 0) {
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, outcomeListData);
        } else {
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, incomeListData);
        }

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
        spinner.setOnItemSelectedListener(new SpinnerSelectedListenerUp());
        accountSpinner.setOnItemSelectedListener(new AccountSelectedListenerUp());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        accountSpinner.setVisibility(View.VISIBLE);

        //注意是给RadioGroup绑定监视器
        RBGroup.setOnCheckedChangeListener(new MyRadioButtonListenerUp());
        RecycleRBGroup.setOnCheckedChangeListener(new MyRadioButtonListenerUp());

    }


    /*
    设置下拉列表默认选中的值
     */
    public void setDefaultPsinnerItem(){

        //拿着periodic的category_id取得Category，然后用category名字和listData做对比得到选中的值
        int catId = periodic.getCategory_id();
        CategoryDAO catDAO = new CategoryDAOImpl();
        Category category = catDAO.getCategoryById(catId);
        String categoryName = category.getCategory_name();

        if (isIncome == 0) {
            for(int i=0;i<outcomeListData.size();i++){
                if(categoryName.equals(outcomeListData.get(i))){
                    spinner.setSelectedIndex(i);
                    categoryId = outcomeListId.get(i);
                    break;
                }
            }
        } else {
            for(int i=0;i<incomeListData.size();i++){
                if(categoryName.equals(incomeListData.get(i))){
                    spinner.setSelectedIndex(i);
                    categoryId = incomeListId.get(i);
                    break;
                }
            }
        }
    }

    public void setDefaultAccountItem(){
        //dao的代码还没好，先注释掉
        int actId = periodic.getAccount_id();
        AccountDAO actDAO = new AccountDAOImpl();
        Account account = actDAO.getAccountById(actId);
        String accountName = account.getAccount_name();


        for(int i=0;i<listAccount.size();i++){
            if(accountName.equals(listAccount.get(i))){
                accountSpinner.setSelectedIndex(i);
                accountId = periodic.getAccount_id();
                break;
            }
        }
    }

    public void setData(){
        String tmpId=getIntent().getStringExtra("periodicId");
        int id = Integer.parseInt(tmpId);

        PeriodicDAO perDao = new PeriodicDAOImpl();
        //id是对的，但是没有取到这个periodic

        periodic= perDao.getPeriodicById(id);

        //设置收入支出类别
        if(periodic.getType()==1){//收入
            RBGroup.check(incomeRB.getId());
            typeId = 1;
            isIncome = 1;
        }
        else {//支出
            RBGroup.check(outcomeRB.getId());
            typeId = 0;
            isIncome = 0;
        }

        //设置事件名称和金额
        periodicName.setText(periodic.getPeriodic_name());
        periodicMoney.setText(Double.toString(periodic.getPeriodic_money()));


        //设置类别值

        CategoryDAO categoryDAO=new CategoryDAOImpl();
        //categories=(ArrayList<Category>) categoryDAO.listCategory();

        //筛选未删除的类别
        List<Category> tmpList = categoryDAO.listCategory();
        List<Category> newData = new ArrayList<Category>();
        for(Category item:tmpList){
            if(item.getState()!=-1){
                newData.add(item);
            }
        }
        categories = (ArrayList<Category>)newData;




//        for(Category cat:categories){
//            listData.add(cat.getCategory_name());
//        }

        //设置收入支出切换时类别切换
        for(int i = 0; i < categories.size(); i++){
            Category category = (Category)categories.get(i);
            if (category.getType() == 0) {
                outcomeListData.add(category.getCategory_name());
                outcomeListId.add(category.getCategory_id());
            } else {
                incomeListData.add(category.getCategory_name());
                incomeListId.add(category.getCategory_id());
            }
        }

        //设置账户值
        AccountDAO accountDAO=new AccountDAOImpl();
        //accounts = (ArrayList<Account>) accountDAO.listAccount();

        //筛选未被删除的账户
        List<Account> tmpAccounts = accountDAO.listAccount();
        List<Account> newAccounts = new ArrayList<Account>();
        for(Account item:tmpAccounts){
            if(item.getState()!=-1){
                newAccounts.add(item);
            }
        }

        accounts = (ArrayList<Account>)newAccounts;

        for(Account act:accounts){
            listAccount.add(act.getAccount_name());
        }



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

       /* //设置当前时间
        myStartDay = new StringBuffer().append(startYear).append("-").append( startMonth + 1).append("-").append(startDay).toString();
        startDate.setText(myStartDay);

        myEndDay = new StringBuffer().append(endYear).append("-").append( endMonth + 1).append("-").append(endDay).toString();
        endDate.setText(myEndDay);*/


        //设置开始结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String satrtDateString = formatter.format(periodic.getStart());
        String endDateString = formatter.format( periodic.getEnd());

        myStartDay=satrtDateString;
        myEndDay=endDateString;

        startDate.setText(satrtDateString);
        endDate.setText(endDateString);

        //设置周期
        switch (periodic.getCycle()){
            case 0:
                RecycleRBGroup.check(perDay.getId());
                recycleId=0;//每天
                break;
            case 1:
                RecycleRBGroup.check(perWeek.getId());
                recycleId=1;//每周
                break;
            case 2:
                RecycleRBGroup.check(perMonth.getId());
                recycleId=2;//每月
                break;

            default:
                break;
        }
    }

    public void init(){
        //view = (TextView) findViewById(R.id.spinnerText_update);
       // accountView=(TextView)findViewById(R.id.account_spinnerText_update);
        spinner = (MaterialSpinner) findViewById(R.id.spinner_category_new);
        accountSpinner=(MaterialSpinner)findViewById(R.id.spinner_account_new);

        //收入支出单选
        RBGroup = (RadioGroup) findViewById(R.id.rg_type_update);
        incomeRB = (RadioButton) findViewById(R.id.income_RB_update);
        outcomeRB = (RadioButton) findViewById(R.id.outcome_RB_update);

        //周期单选按钮组
        RecycleRBGroup = (RadioGroup) findViewById(R.id.rg_cycle_update);
        perDay = (RadioButton) findViewById(R.id.per_day_RB_update);
        perWeek = (RadioButton) findViewById(R.id.per_week_RB_update);
        perMonth = (RadioButton) findViewById(R.id.per_month_RB_update);

        //日期选择
        startDate = (RoundButton)findViewById(R.id.periodic_start_time_new);
        endDate = (RoundButton)findViewById(R.id.periodic_end_time_new);

        //保存按钮
        storePeriodic=(SuperButton)findViewById(R.id.btn_save_new);
        cancelUpdate = (ImageView)findViewById((R.id.cancel_update_periodic));

        //需要设置默认值的组件
        periodicName = (EditText)findViewById(R.id.periodic_name_new);
        periodicMoney = (EditText)findViewById(R.id.periodic_money_new);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        //保存按钮事件监听
        storePeriodic.setOnClickListener(this);
        cancelUpdate.setOnClickListener(this);

    }


    public boolean review(){
        //检查数据是否达到存入要求

        AlertDialog.Builder builder  = new AlertDialog.Builder(UpdatePeriodicActivity.this);
        builder.setTitle("确认" ) ;
        builder.setMessage("事件信息有误，请检查" ) ;
        builder.setPositiveButton("是" ,  null );

        //输入框为空
        if(TextUtils.isEmpty(periodicName.getText())||
                TextUtils.isEmpty(periodicMoney.getText())){
            builder.show();
            return false;
        }

        //检查开始结束时间
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

        name=periodicName.getText().toString();
        money=Double.valueOf(periodicMoney.getText().toString());

        //categoryId在SpinnerSelectedListenerUp里面设置了
        //accountId在AccountSpinnerSelectedListenerUp里面设置了
        //recycleId;  setRecycle()里面
        //typeId; MyRadioButtonListenerUp   按钮事件监听里面

        try {
            start= new SimpleDateFormat("yyyy-MM-dd").parse(myStartDay);
            end= new SimpleDateFormat("yyyy-MM-dd").parse(myEndDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //更新事件的值
        periodic.setPeriodic_name(name);
        periodic.setPeriodic_money(money);
        periodic.setStart(start);
        periodic.setEnd(end);
        //periodic.setAnchor(anchor);
        periodic.setCycle(recycleId);
        periodic.setType(typeId);
        periodic.setCategory_id(categoryId);
        periodic.setAccount_id(accountId);
        periodic.setState(1);//本地更新

        //存入数据库
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
        periodicDAO.updatePeriodic(periodic);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.periodic_start_time_new:
                showStartDateSelector();
                break;

            case R.id.periodic_end_time_new:
                showEndDateSelector();
                break;

            case R.id.btn_save_new:
                if(savePeriodic()){
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    //把值periodicId传回上一个界面
                    Intent intent = new Intent();
                    intent.putExtra("id_return_per",String.valueOf(periodic.getPeriodic_id()));
                    setResult(RESULT_OK,intent);
                    this.finish();
                }

                break;

            case R.id.cancel_update_periodic:
                this.finish();
                break;

            default:
                break;
        }

    }


    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListenerUp implements MaterialSpinner.OnItemSelectedListener {

       /* public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isIncome == 0) {
                view.setText("你选择的类别："+ outcomeListData.get(arg2));
                categoryId = outcomeListId.get(arg2);
            } else {
                view.setText("你选择的类别："+ incomeListData.get(arg2));
                categoryId = incomeListId.get(arg2);
            }
        }*/

        public void onNothingSelected(AdapterView<?> arg0) {
        }

        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            if (isIncome == 0) {
                view.setText(outcomeListData.get(position));
                categoryId = outcomeListId.get(position);
            } else {
                view.setText(incomeListData.get(position));
                categoryId = incomeListId.get(position);
            }
        }
    }

    //内部类，下拉列表监听者，使用数组形式操作
    class AccountSelectedListenerUp implements MaterialSpinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            //accountView.setText("你选择的账户："+ listAccount.get(arg2));

            accountId = accounts.get(arg2).getAccount_id();

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }

        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            //accountView.setText(listAccount.get(position));
            view.setText(listAccount.get(position));

            //设置Account_id 记得去掉注释
            accountId = accounts.get(position).getAccount_id();
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
    class MyRadioButtonListenerUp implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.income_RB_update:
                    // 当用户选择收入时
                    typeId=1;
                    isIncome = 1;
                    adapter = new ArrayAdapter<String>(UpdatePeriodicActivity.this,android.R.layout.simple_spinner_item, incomeListData);
                    adapter.notifyDataSetChanged();
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    setDefaultPsinnerItem();
                    spinner.setOnItemSelectedListener(new SpinnerSelectedListenerUp());
                    spinner.setVisibility(View.VISIBLE);
                    Log.i("income_RB", "当前用户选择"+ incomeRB.getText().toString());
                    break;
                case R.id.outcome_RB_update:
                    // 当用户选择支出时
                    typeId=0;
                    isIncome = 0;
                    adapter = new ArrayAdapter<String>(UpdatePeriodicActivity.this,android.R.layout.simple_spinner_item, outcomeListData);
                    adapter.notifyDataSetChanged();
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    setDefaultPsinnerItem();
                    spinner.setOnItemSelectedListener(new SpinnerSelectedListenerUp());
                    spinner.setVisibility(View.VISIBLE);
                    Log.i("income_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_day_RB_update:
                    Log.i("per_day_RB", "当前用户选择"+ perDay.getText().toString());
                    setRecycle(0);
                    Log.i("per_day_RB", "当前用户选择"+ perDay.getText().toString());
                    break;

                case R.id.per_week_RB_update:
                    Log.i("per_day_RB", "当前用户选择"+ perWeek.getText().toString());
                    setRecycle(1);
                    break;

                case R.id.per_month_RB_update:
                    Log.i("per_day_RB", "当前用户选择"+ perMonth.getText().toString());
                    setRecycle(2);
                    break;

                default:
                    break;
            }

        }

        /*
        设置周期事件周期 day week month
         */
        public void setRecycle(int id){
          /*
          将周期设为多少天就可以了
           */
            recycleId = id;
            Log.i("周期将被设置为： ",String.valueOf(id));

        }
    }
}
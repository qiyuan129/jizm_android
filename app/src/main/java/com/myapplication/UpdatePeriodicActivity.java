package com.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import pojo.Category;
import pojo.Periodic;

public class UpdatePeriodicActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Category> categories;
    public Periodic periodic;
    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic  5.3  0:06
    public ArrayList<String> listData=new  ArrayList<String>();
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

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


    //确认修改按钮
    Button storePeriodic;


    //需要设置默认值组件
    EditText periodicName;
    EditText periodicMoney;



    //辅助变量
    String name;
    double money;
    long start;
    long end;
    long anchor;
    int recycleId;
    int typeId;
    int categoryId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_periodic);

        init();
        setData();



        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listData);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //spinner数据加载完，设置一下默认选中的值
        setDefaultPsinnerItem();


        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListenerUp());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);


        //注意是给RadioGroup绑定监视器
        RBGroup.setOnCheckedChangeListener(new MyRadioButtonListenerUp());
        RecycleRBGroup.setOnCheckedChangeListener(new MyRadioButtonListenerUp());


    }


    /*
    设置下拉列表默认选中的值
     */
    public void setDefaultPsinnerItem(){
        //dao的代码还没好，先注释掉
       /* //拿着periodic的category_id取得Category，然后用category名字和listData做对比得到选中的值
        int catId = periodic.getCategory_id();
        CategoryDAO catDAO = new CategoryDAOImpl();
        Category category = catDAO.getById(catId);

        String categoryName = category.getCategory_name();
        for(int i=0;i<listData.size();i++){
            if(categoryName.equals(listData.get(i))){
                spinner.setSelection(i);
                break;
            }
        }
        */
        spinner.setSelection(1);


    }





    public void setData(){
        String tmpId=getIntent().getStringExtra("periodicId");
        int id = Integer.parseInt(tmpId);

        /*
         PeriodicDAO perDao = new PeriodicDAOImpl();
         Periodic periodic= perDao.getById(id);
         */


//        periodic = new Periodic(1,2,1,3,1,"shopping"+String.valueOf(2),
//                1,3838737,25785872,50,3,21288);


        //设置收入支出类别
        if(periodic.getType()==1){//收入
            RBGroup.check(incomeRB.getId());
        }
        else {//支出
            RBGroup.check(outcomeRB.getId());
        }


        //设置事件名称和金额
        periodicName.setText(periodic.getPeriodic_name());
        periodicMoney.setText(Double.toString(periodic.getPeriodic_money()));




       /* //设置类别值

          CategoryDAO categoryDAO=new CategoryDAOImpl();
          categories=categoryDAO.getList();
          for(Category cat:categories){
              listData.add(cat.getCategory_name());
          }*/

        listData.add("学习用品");
        listData.add("生活用品");
        listData.add("娱乐消费");
        listData.add("买菜");
        listData.add("旅游");
        listData.add("其他");






        /*//获取当前年，月，日
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


        myEndDay = new StringBuffer().append(endYear).append("-").append( endMonth + 1).append("-").append(endDay).toString();
        endDate.setText(myEndDay);*/


        //设置开始结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String satrtDateString = formatter.format(periodic.getStart());
        String endDateString = formatter.format(periodic.getEnd());

        startDate.setText(satrtDateString);
        endDate.setText(endDateString);



        //设置周期
        switch (periodic.getCycle()){
          case 0:RecycleRBGroup.check(perDay.getId());
              break;
          case 1:RecycleRBGroup.check(perWeek.getId());
              break;
          case 2:RecycleRBGroup.check(perMonth.getId());
              break;

              default:
                  break;
      }













    }

    public void init(){
        view = (TextView) findViewById(R.id.spinnerText_update);
        spinner = (Spinner) findViewById(R.id.Spinner_update);

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
        startDate = (TextView)findViewById(R.id.date_start_update);
        endDate = (TextView)findViewById(R.id.date_end_update_);


        //保存按钮
        storePeriodic=(Button)findViewById(R.id.store_periodic_update);


        //需要设置默认值的组件
        periodicName = (EditText)findViewById(R.id.periodic_name_update);
        periodicMoney = (EditText)findViewById(R.id.periodic_money_update);








        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        //保存按钮事件监听
        storePeriodic.setOnClickListener(this);


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
        //recycleId;  setRecycle()里面
        //typeId; MyRadioButtonListenerUp   按钮事件监听里面




        try {
            Date startDate= new SimpleDateFormat("yyyy-MM-dd").parse(myStartDay);
            Date endDate= new SimpleDateFormat("yyyy-MM-dd").parse(myEndDay);
            Date now = new Date();

            start=startDate.getTime();
            end=endDate.getTime();
            anchor=now.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }



        //更新事件的值
        periodic.setPeriodic_name(name);
        periodic.setPeriodic_money(money);
        periodic.setStart(new Date(start));
        periodic.setEnd(new Date(end));
        periodic.setAnchor(new Date(anchor));
        periodic.setCycle(recycleId);
        periodic.setType(typeId);
        periodic.setCategory_id(categoryId);

        /*//存入数据库
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
        periodicDAO.updatePeriodic(periodic);*/



        return true;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_start_update:
                showStartDateSelector();
                break;

            case R.id.date_end_update_:
                showEndDateSelector();
                break;


            case R.id.store_periodic:
                Toast.makeText(this,"保存还没有实现",Toast.LENGTH_SHORT).show();
                break;


            default:
                break;
        }

    }




    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListenerUp implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData.get(arg2)+":"+String.valueOf(arg2));



            //设置Category_id 记得去掉注释
            //categoryId = categories.get(arg2).getCategory_id();

            //测试用，后面删除
            categoryId=0;


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
    class MyRadioButtonListenerUp implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.income_RB_update:
                    // 当用户选择收入时
                    typeId=1;
                    Log.i("income_RB", "当前用户选择"+ incomeRB.getText().toString());
                    break;
                case R.id.outcome_RB_update:
                    // 当用户选择支出时
                    typeId=0;
                    Log.i("income_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_day_RB:
                    setRecycle(0);
                    break;

                case R.id.per_week_RB:
                    setRecycle(1);
                    break;

                case R.id.per_month_RB:
                    setRecycle(2);
                    break;



                default:break;
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

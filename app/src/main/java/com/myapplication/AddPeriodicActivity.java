package com.myapplication;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddPeriodicActivity extends AppCompatActivity implements View.OnClickListener{
    //这个数组用Periodic数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic  5.3  0:06
    private static final String[] listData ={"学习用品","生活用品","买菜","娱乐","其他"};
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    //收入支出单选button组
    private RadioGroup RBGroup;
    private RadioButton incomeRB, outcomeRB;

    //周期类型单选button组
    private RadioGroup RecycleRBGroup;
    private RadioButton perDay,perWeek,perMonth,perSeason,perYear;



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








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_periodic);


        init();


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listData);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);


        //注意是给RadioGroup绑定监视器
        RBGroup.setOnCheckedChangeListener(new MyRadioButtonListener());
        RecycleRBGroup.setOnCheckedChangeListener(new MyRadioButtonListener());

    }



    public void init(){
        view = (TextView) findViewById(R.id.spinnerText);
        spinner = (Spinner) findViewById(R.id.Spinner01);

        //收入支出单选
        RBGroup = (RadioGroup) findViewById(R.id.rg_type);
        incomeRB = (RadioButton) findViewById(R.id.income_RB);
        outcomeRB = (RadioButton) findViewById(R.id.outcome_RB);

        //周期单选按钮组
        RecycleRBGroup = (RadioGroup) findViewById(R.id.rg_cycle);
        perDay = (RadioButton) findViewById(R.id.per_day_RB);
        perWeek = (RadioButton) findViewById(R.id.per_week_RB);
        perMonth = (RadioButton) findViewById(R.id.per_month_RB);
        perSeason = (RadioButton) findViewById(R.id.per_season_RB);
        perYear = (RadioButton) findViewById(R.id.per_year_RB);




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
                Toast.makeText(this,"保存还没有实现",Toast.LENGTH_SHORT).show();
                break;


            default:
                break;
        }

    }


    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData[arg2]+":"+String.valueOf(arg2));

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
                    break;
                case R.id.income_RB:
                    // 当用户选择支出时
                    Log.i("income_RB", "当前用户选择"+ incomeRB.getText().toString());
                    break;

                case R.id.per_day_RB:
                    Log.i("per_day_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_week_RB:
                    Log.i("per_week_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_month_RB:
                    Log.i("per_month_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_season_RB:
                    Log.i("per_season_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;

                case R.id.per_year_RB:
                    Log.i("per_year_RB", "当前用户选择"+ outcomeRB.getText().toString());
                    break;


                    default:break;
            }



        }
    }








}

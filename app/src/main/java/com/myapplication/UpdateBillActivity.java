package com.myapplication;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import dao.BillDAO;
import dao.BillDAOImpl;
import pojo.Bill;

/*
实现修改账单
 */
public class UpdateBillActivity extends AppCompatActivity implements View.OnClickListener{

    Bill bill;
    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic  5.3  0:06
    public String[] listData ={"学习用品","生活用品","买菜","娱乐","其他"};
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;


    //名称和金额
    EditText BillNameEdit;
    EditText BillMoneyEdit;


    //时间
    private TextView billUpdateTime;
    protected String days;
    public int mYear;
    public int mMonth;
    public int mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);
        init();
        setData();



        // BillNameEdit = (EditText)findViewById(R.id.)
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listData);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListenerBup());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);


    }



    public void init(){
        view = (TextView) findViewById(R.id.bill_update_spinnerText);
        spinner = (Spinner) findViewById(R.id.bill_update_spinner);

        //选择时间
        billUpdateTime=(TextView)findViewById(R.id.bill_update_time);



        billUpdateTime.setOnClickListener(this);


    }



    public void setData(){

       /* String tmpId=getIntent().getStringExtra("periodicId");
        int id = Integer.parseInt(tmpId);

        BillDAO billDAO = new BillDAOImpl();
        bill= billDAO.getById(id);*/

       //实验用，后面删除
        bill = new Bill(2,3,2,5,2,"早餐",1245785,32.2,2, 5545278);


        //设置开始结束时间

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        days = formatter.format(bill.getBill_date());

        billUpdateTime.setText(days);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bill_update_time:
                showStartDateSelector();
                break;

                default:
                    break;

        }

    }


    //内部类，下拉列表监听者，使用数组形式操作
    class SpinnerSelectedListenerBup implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            view.setText("你的选择是："+ listData[arg2]+":"+String.valueOf(arg2));

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




}

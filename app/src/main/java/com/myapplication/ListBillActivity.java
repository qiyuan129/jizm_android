package com.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;
import pojo.Bill;
/*
这个类是没用的了
 */

public class ListBillActivity extends AppCompatActivity {

    private List<Bill> billList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        //初始化数据数组
        initBills("学习用品");

        //创建listView的数据适配器
        BillListItemAdapter adapter = new BillListItemAdapter(ListBillActivity.this,R.layout.bill_list_item,billList);

        //设置适配器
        SlideDeleteListView listView=(SlideDeleteListView)findViewById(R.id.list_bill_view);
        listView.setAdapter(adapter);


        //viewItem点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListBillActivity.this, "点击的是第" + position + "项", Toast.LENGTH_SHORT).show();

                //查看账单,后续添加
            }
        });


        //左滑删除
        listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int position) {
                Toast.makeText(ListBillActivity.this, "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();

                Log.i("list:",String.valueOf(billList.get(position).getBill_id()));

                //删除这个账单
                deleteBill(position);
            }
        });


    }


    private void initBills(String category){
       /*
       实际代码
        BillDAO billDao = new BillDAOImpl();
        billList=billDao.listByCategory(category);
        */

        //实验用，后面删除
        billList = new ArrayList<>();
        for(int i=0;i<=20;i++){
            Bill bill = new Bill(i,23,2,25,
                    1,"学习用品:"+i,new Date(2452777),36.5,2,null);
            billList.add(bill);
        }


    }


    /*
    删除账单
    id: 账单id
     */
    private boolean deleteBill(int id){


        return true;
    }


}











class BillListItemAdapter extends ArrayAdapter<Bill>{
    private int resourceId;

    public BillListItemAdapter(Context context, int textViewResourceId,List<Bill> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }



    /*
    在每个子项滚到屏幕内时被调用
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Bill  billItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        //TextView textView = (TextView)view.findViewById(R.id.bill_item_text);


        /*
        设置bill项目的值，信息
         */
        //textView.setText(billItem.getBill_name()+"  "+billItem.getBill_money());
        return view;
    }






}
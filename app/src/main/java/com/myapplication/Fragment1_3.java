package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;
import pojo.Bill;

public class Fragment1_3 extends Fragment {


    private View mView;
    private List<Bill> billList;
    BillListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment1_3,container,false);
        }

        //初始化数据数组
        initBills("学习用品");
        //创建listView的数据适配器
        adapter = new BillListAdapter(getActivity(),R.layout.bill_list_item,billList);

        //设置视图
        SlideDeleteListView listView=(SlideDeleteListView)mView.findViewById(R.id.list_bill_view_frag);
        listView.setAdapter(adapter);


        //viewItem点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击的是第" + position + "项", Toast.LENGTH_SHORT).show();

                //查看账单,后续添加
                Bill tempBill = billList.get(position);
                Intent intent1 = new Intent(getActivity(),UpdateBillActivity.class);
                intent1.putExtra("billId",String.valueOf(tempBill.getBill_id()));
                startActivity(intent1);
            }
        });






        //左滑删除
        listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int position) {
               Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
               Log.i("list:",String.valueOf(billList.get(position).getBill_id()));
                //删除这个账单
                deleteBill(position);
            }
        });




        return mView;
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
                    1,"学习用品:"+i,546524,36.5,2,254625);
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


class BillListAdapter extends ArrayAdapter<Bill> {
    private int resourceId;

    public BillListAdapter(Context context, int textViewResourceId, List<Bill> objects){
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
        TextView textView = (TextView)view.findViewById(R.id.bill_item_text);


        /*
        设置bill项目的值，信息
         */
        textView.setText(billItem.getBill_name()+"  "+billItem.getBill_money());
        return view;
    }






}
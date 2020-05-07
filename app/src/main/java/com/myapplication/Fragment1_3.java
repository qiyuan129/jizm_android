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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.BillDAO;
import dao.BillDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;
import pojo.Account;
import pojo.Bill;
import pojo.Category;
import pojo.Periodic;

import static android.app.Activity.RESULT_OK;

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



       /* //初始化数据。。。。。。。。。。。。。。。。。。。。。。。。
        Date date=new Date();
        Date date1= new Date(2020-1900, 3, 1);
        Date date2= new Date(2020-1900, 11, 1);
        Account account=null;
        Bill bill=null;
        Category category=null;
        Periodic periodic=null;
        AccountDAO accountDAO=new AccountDAOImpl();
        BillDAO billDAO=new BillDAOImpl();
        CategoryDAO categoryDAO=new CategoryDAOImpl();
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();

        account=new Account(1,1,"微信",500,1,date);
        accountDAO.insertAccount(account);
        account=new Account(2,1,"支付宝",800,1,date);
        accountDAO.insertAccount(account);

        bill=new Bill(1,1,1,1,0,"早餐",date,5,1,date);
        billDAO.insertBill(bill);
        bill=new Bill(2,1,1,1,0,"午饭",date,10,1,date);
        billDAO.insertBill(bill);
        bill=new Bill(3,2,2,1,0,"滴滴",date,22,1,date);
        billDAO.insertBill(bill);
        bill=new Bill(4,2,3,1,1,"工资",date,6000,1,date);
        billDAO.insertBill(bill);
        bill=new Bill(5,1,3,1,1,"分红",date,3000,1,date);
        billDAO.insertBill(bill);

        category=new Category(1,1,"饮食",0,1,date);
        categoryDAO.insertCategory(category);
        category=new Category(2,1,"出行",0,1,date);
        categoryDAO.insertCategory(category);
        category=new Category(3,1,"收入",1,1,date);
        categoryDAO.insertCategory(category);

        periodic=new Periodic(1,1,1,1,0,"吃饭",1,date1,date2,6.2,1,date);
        periodicDAO.addPeriodic(periodic);
        periodic=new Periodic(2,1,2,1,0,"打车",2,date1,date2,6.2,1,date);
        periodicDAO.addPeriodic(periodic);

        /////////////////
*/


        //初始化数据数组
        initBills();
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

                startActivityForResult(intent1,1110);//设置请求码为1010

                //startActivity(intent1);
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



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent Tdata){
        switch (requestCode){
            case 1110 :
                if(resultCode==RESULT_OK){

                    Log.i("更新修改的bill数据  ","开始");
                    String billId = Tdata.getStringExtra("id_return_bill");
                    updateItem(Integer.valueOf(billId));
                    Log.i("更新修改的bill数据  : ","结束");

                }

                break;


            default:
                break;
        }
    }





    public void updateItem(int billId){
        Log.i("更新bill id 为  : ",String.valueOf(billId));


        BillDAO billDAO=new BillDAOImpl();
        Bill tmp = billDAO.getBillById(billId);

        for(Bill item:billList){
            if(item.getBill_id()==tmp.getBill_id()){
                item.setAccount_id(tmp.getAccount_id());
                item.setCategory_id(tmp.getCategory_id());
                item.setUser_id(tmp.getUser_id());
                item.setType(tmp.getType());
                item.setBill_name(tmp.getBill_name());
                item.setBill_date(tmp.getBill_date());
                item.setBill_money(tmp.getBill_money());
                item.setState(tmp.getState());
                item.setAnchor(tmp.getAnchor());

                break;
            }
        }




        //通知适配器数据改变
        adapter.notifyDataSetChanged();




    }










    private void initBills(){

       //实际代码
        BillDAO billDao = new BillDAOImpl();
        billList=billDao.listBill();


      /*  //实验用，后面删除
        billList = new ArrayList<>();
        for(int i=0;i<=20;i++){
            Bill bill = new Bill(i,23,2,25,
                    1,"学习用品:"+i,new Date(546524),36.5,2,null);
            billList.add(bill);
        }*/




    }


    /*
    删除账单
    id: 账单id
     */
    private boolean deleteBill(int id){

        Bill delBill = billList.get(id);
        //标记为删除
        delBill.setState(-1);
        //从数据库中删除
        BillDAO billDAO = new BillDAOImpl();
        billDAO.deleteBill(delBill.getBill_id());

        //从适配器里删除
        adapter.remove(delBill);
        adapter.notifyDataSetChanged();
        //本地list与adapter绑定的，因此本地的list不用处理







        Toast.makeText(getActivity(), "账单删除成功", Toast.LENGTH_SHORT).show();
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

        TextView nametext =(TextView)view.findViewById(R.id.bill_item_name);
        TextView moneyText = (TextView)view.findViewById(R.id.bill_item_money);
        TextView typeText = (TextView)view.findViewById(R.id.bill_item_type);
        TextView dateText = (TextView)view.findViewById(R.id.bill_item_date);


        String billType = null;
        if(billItem.getType()==0){
            billType = "支出";
        }
        else {
            billType = "收入";
        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(billItem.getBill_date());
        /*
        设置bill项目的值，信息
         */

        nametext.setText(billItem.getBill_name());
        moneyText.setText(String.valueOf(billItem.getBill_money()));
        typeText.setText(billType);
        dateText.setText(dateStr);


        return view;
    }






}
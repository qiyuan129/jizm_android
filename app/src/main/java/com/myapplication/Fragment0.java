package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.BillDAO;
import dao.BillDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;
import pojo.Bill;
import pojo.Category;

import static android.app.Activity.RESULT_OK;

public class Fragment0 extends Fragment {
    private View mView;

    SlideDeleteListView listView;

    private List<Bill> billList;//只有初始化的时候用过一次，之后里面的数据都是过期的

    BillRecomendAdapterF0 newAdapter;


    //这个数组用Category数组的名称来初始化，然后通过选择的下标来判定选了那个Periodic
    ArrayList<Category> categories;
    public ArrayList<String> CategoryListData;
    private Spinner spinner;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment0,container,false);
        }


        //初始化数据数组
        initBills();
        setCategoryData();

        //创建listView的数据适配器
        //adapter = new BillListAdapter(getActivity(),R.layout.bill_list_item,billList);
        newAdapter = new BillRecomendAdapterF0(getActivity(),billList);




        //设置视图
        spinner = (Spinner) mView.findViewById(R.id.category_spinner_F0);
        listView=mView.findViewById(R.id.list_bill_view_frag_f0);
        //listView.setAdapter(adapter);
        listView.setAdapter(newAdapter);





        //        下拉列表
        //将可选内容与ArrayAdapter连接起来
        categoryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, CategoryListData);
        //设置下拉列表的风格
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new Fragment0.CategorySelectedListener());
        spinner.setVisibility(View.VISIBLE);

        //设置默认选中的下拉列表项(0项)，需要在spinner填充数据之后
        spinner.setSelection(0,true);




        //设置左滑删除产生器




        //viewItem点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击的是第" + position + "项", Toast.LENGTH_SHORT).show();

                //查看账单,后续添加
                //Bill tempBill = billList.get(position);
                Bill tempBill = (Bill)newAdapter.getItem(position);

                Intent intent1 = new Intent(getActivity(),UpdateBillActivity.class);
                intent1.putExtra("billId",String.valueOf(tempBill.getBill_id()));

                startActivityForResult(intent1,1110);//设置请求码为1010

            }
        });







        //左滑删除
        listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int position) {
                Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
                //Log.i("list:",String.valueOf(billList.get(position).getBill_id()));
                //删除这个账单
                deleteBill(position);
            }
        });


        return mView;
    }




    public void setCategoryData(){

        //填充类别列表
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        categories = (ArrayList<Category>) categoryDAO.listCategory();
        CategoryListData = new ArrayList<String>();

        CategoryListData.add("全部类别");
        for(Category cat:categories){
            CategoryListData.add(cat.getCategory_name());
        }


    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent Tdata){
        //返回数据给上一层
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("TTPPPPP", "onHiddenChanged  ！hidden刷新数据");
        if(hidden){
            //TODO now visible to user
            Log.i("TTP", "onHiddenChanged  hidden刷新数据");
        } else {
            //TODO now invisible to user
            Log.i("TTP", "onHiddenChanged  ！hidden刷新数据");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //Log.i("Fragment0", "---------setUserVisibleHint(" + isVisibleToUser + ")");
        Log.i("Fragment0", "账单列表刷新数据");

        //到显示状态为true，不可见为false
        if (isVisibleToUser) {
            RefreshData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }



    public void RefreshData(){
        if(billList!=null && newAdapter!=null && categoryAdapter!=null && spinner!=null && listView!=null){
            billList.clear();
            BillDAO billDao = new BillDAOImpl();
            //按时间顺序
            //billList = billDao.listBill();
            billList = billDao.listBillByDate();

            newAdapter.clearAll();
            newAdapter.setData(billList);

            //刷新类别
            setCategoryData();
            categoryAdapter.clear();
            categoryAdapter.addAll(CategoryListData);


            //更新适配器
            categoryAdapter.notifyDataSetChanged();
            spinner.setAdapter(categoryAdapter);
            newAdapter.notifyDataSetChanged();
            listView.setAdapter(newAdapter);
        }


    }



    @Override
    public void onResume() {
        super.onResume();
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
        //adapter.notifyDataSetChanged();
        newAdapter.notifyDataSetChanged();




    }










    private void initBills(){
        //实际代码
        BillDAO billDao = new BillDAOImpl();
        //billList=billDao.listBill();
        //按时间顺序排列
        billList = billDao.listBillByDate();

    }


    /*
    删除账单
    id:此id为 adapter里面data数组的下标
     */
    private boolean deleteBill(int index){

        //Bill delBill = billList.get(id);
        Bill delBill = (Bill)newAdapter.getItem(index);
        //标记为删除
        delBill.setState(-1);
        //从数据库中删除
        BillDAO billDAO = new BillDAOImpl();
        billDAO.deleteBill(delBill.getBill_id());

        //从适配器里删除
        //adapter.remove(delBill);
        //adapter.notifyDataSetChanged();
        newAdapter.removeItem(index);
        newAdapter.notifyDataSetChanged();


        //本地list与adapter绑定的，因此本地的list不用处理

        //Toast.makeText(getActivity(), "账单删除成功", Toast.LENGTH_SHORT).show();
        return true;
    }






    //内部类，下拉列表监听者，使用数组形式操作
    class CategorySelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            //view.setText("你选择的类别："+ listData.get(arg2));

            //设置Category_id 记得去掉注释
            //categoryId = categories.get(arg2).getCategory_id();

            //测试用，后面删除
            // categoryId=0;

            //Log.i("选中: ",CategoryListData.get(arg2));
            Log.i("选中: ",String.valueOf(arg2));

            if(arg2==0){
                //listView.setFilterText("");
                listView.clearTextFilter();
                newAdapter.getFilter().filter("");

            }
            else{
                int categoryId = categories.get(arg2-1).getCategory_id();//因为第一项是“全部” 所以需要有一个位置的偏移
                //使用用户输入的内容对ListView的列表项进行过滤
                //listView.setFilterText(String.valueOf(categoryId));

                newAdapter.getFilter().filter(String.valueOf(categoryId));


            }


        }

        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }








    /**
     数据适配器
     *
     */
    class BillRecomendAdapterF0 extends BaseAdapter implements Filterable {
        Context context;
        List<Bill> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据
        List<Bill> backData;//用来备份原始数据,处理删除时两个数组中的元素都要删除
        MyFilter mFilter ;

        public BillRecomendAdapterF0(Context context, List<Bill> data) {
            this.context = context;
            this.data = data;
            backData = data;
        }

        public void clearAll(){
            data.clear();
            backData.clear();
        }

        public void setData(List<Bill> data){
            this.data = data;
            backData = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
            //return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public  void addItem(Bill obj){
            data.add(obj);
        }

        public void removeItem(int index){
            Bill tep = data.get(index);
            backData.remove(tep);
            data.remove(tep);
        }





        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (view ==null){
                view = LayoutInflater.from(context).inflate(R.layout.bill_list_item,null);
            }


            //从数组中得到数据并然后给页面组件设置值
            //Periodic periodicItem=data.get(position);
            Bill  billItem = data.get(position);



            //获取组件
            TextView nametext =(TextView)view.findViewById(R.id.bill_item_name);
            TextView moneyText = (TextView)view.findViewById(R.id.bill_item_money);
            TextView categoryText = (TextView)view.findViewById(R.id.bill_item_category);
            TextView dateText = (TextView)view.findViewById(R.id.bill_item_date);



            String tmpMoney=null;
            String billCategory=null;
            if(billItem.getType()==0){//支出
                tmpMoney = "-" + String.valueOf(billItem.getBill_money());
                moneyText.setTextColor(Color.parseColor("#ff0000"));


            }
            else {//收入
                tmpMoney = "+" + String.valueOf(billItem.getBill_money());
                moneyText.setTextColor(Color.parseColor("#00ff00"));

            }


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = formatter.format(billItem.getBill_date());


            for(Category cat:categories){
                if(cat.getCategory_id()==billItem.getCategory_id()){
                    billCategory = cat.getCategory_name();
                    break;
                }
            }
        /*
        设置bill项目的值，信息
         */

            nametext.setText(billItem.getBill_name());
            moneyText.setText(tmpMoney);
            categoryText.setText(billCategory);
            dateText.setText(dateStr);




            return view;
        }

        //当ListView调用setTextFilter()方法的时候，便会调用该方法
        @Override
        public Filter getFilter() {
            if (mFilter ==null){
                mFilter = new MyFilter();
            }
            return mFilter;
        }




        //我们需要定义一个过滤器的类来定义过滤规则
        class MyFilter extends Filter{
            //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults result = new FilterResults();
                List<Bill> list ;



                //

                Log.i("到达过滤器:  ",charSequence.toString());
                //

                if (TextUtils.isEmpty(charSequence)){//当过滤的关键字为空的时候，我们则显示所有的数据
                    list  = backData;
                }
                else {//否则把符合条件的数据对象添加到集合中
                    String tmp = charSequence.toString();
                    int categoryId=Integer.valueOf(tmp);

                    list = new ArrayList<>();
                    for (Bill recomend:backData){
                        //过滤规则

                        //先用名字过滤一下
                        if (recomend.getCategory_id()==categoryId){
                            //if (recomend.getPeriodic_name().contains(charSequence)||recomend.getDesc().contains(charSequence))


                            list.add(recomend);
                        }

                    }
                }
                result.values = list; //将得到的集合保存到FilterResults的value变量中
                result.count = list.size();//将集合的大小保存到FilterResults的count变量中

                return result;
            }
            //在publishResults方法中告诉适配器更新界面
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = (List<Bill>)filterResults.values;
                Log.d("++publishResults:",String.valueOf(filterResults.count));

                if (filterResults.count>0){
                    notifyDataSetChanged();//通知数据发生了改变
                    Log.d("publishResults:","notifyDataSetChanged");
                }else {
                    notifyDataSetInvalidated();//通知数据失效
                    Log.d("++publishResults:","notifyDataSetInvalidated");
                }
            }
        }
    }






}








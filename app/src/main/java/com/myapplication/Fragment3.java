package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.PeriodicDAO;
import dao.PeriodicDAOImpl;
import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;
import pojo.Bill;
import pojo.Periodic;

public class Fragment3 extends Fragment implements View.OnClickListener{

    private View mView;
    private Button addPeriodic;
    private SearchView searchPeriodic;
    private List<Periodic> periodics;
    private  SlideDeleteListView listView;
    private RecomendAdapter tmpadapter;

    //private PeriodicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment3,container,false);
        }

        //初始化数据数组
        initPeriodics();

        //创建listView的数据适配器
        //adapter = new PeriodicListAdapter(getActivity(),R.layout.periodic_list_item,periodics);
        tmpadapter = new RecomendAdapter(getActivity(),periodics);

        //设置视图
        listView=(SlideDeleteListView)mView.findViewById(R.id.list_periodic_view);
        addPeriodic =(Button) mView.findViewById(R.id.add_periodic);
        searchPeriodic = (SearchView) mView.findViewById(R.id.search_periodic);



        //设置适配器
        listView.setAdapter(tmpadapter);

        //为ListView启动过滤
        listView.setTextFilterEnabled(true);
        //设置SearchView自动缩小为图标
        searchPeriodic.setIconifiedByDefault(false);//设为true则搜索栏 缩小成俄日一个图标点击展开
        //设置该SearchView显示搜索按钮
        searchPeriodic.setSubmitButtonEnabled(true);







        //添加事件监听
        addPeriodic.setOnClickListener(this);
        searchPeriodic.setOnClickListener(this);

        //viewItem点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点击到的Periodic
                Periodic tmp=(Periodic)tmpadapter.getItem(position);


                Toast.makeText(getActivity(), "点击的是第" + position + "项:"+tmp.getPeriodic_name(), Toast.LENGTH_SHORT).show();


                //查看和修改账单,后续添加
                Intent intent1 = new Intent(getContext(),UpdatePeriodicActivity.class);
                tmp.setPeriodic_id(45);//设置一下id
                intent1.putExtra("periodicId",Integer.toString(tmp.getPeriodic_id()));
                startActivity(intent1);

                //应该要从数据库重新查询更新列表，因为数据可能已经 修改了



            }
        });


        //左滑删除
        listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int position) {
                Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
                Log.i("list:",String.valueOf(periodics.get(position).getPeriodic_id()));
                //删除这个周期事件
                deletePeriodic(position);
                Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });



        //搜索框事件监听
        searchPeriodic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交搜索后的处理逻辑
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容
                Toast.makeText(getActivity(), "你输入的选择是：" + query,
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            // 当搜索内容改变时触发该方法，时刻监听输入搜索框的值
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText长度不为0
                if (TextUtils.isEmpty(newText)){
                    //清除ListView的过滤
                    listView.clearTextFilter();
                }else{
                    //使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);

                }
                return true;

            }
        });




        return mView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_periodic:
                Intent intent1 = new Intent(getActivity(), AddPeriodicActivity.class);
                startActivity(intent1);

               // Log.d("add_periodic","添加事件");
                //Toast.makeText(getActivity(), "功能尚未编写", Toast.LENGTH_SHORT).show();

                break;
            case R.id.search_periodic:
                Log.d("search_periodic","查询事件");
                Toast.makeText(getActivity(), "搜索功能尚未编写", Toast.LENGTH_SHORT).show();
                break;


                default:break;
        }


    }








    private void initPeriodics(){
       /*
       实际代码
       调用DAO代码获得周期事件数组
        */

        //实验用，后面删除
        periodics = new ArrayList<>();
        for(int i=0;i<=20;i++){
           Periodic periodic = new Periodic(i,i,5,3,6,"eat"+String.valueOf(i),
                   6,new Date(45236+i),new Date(99954+i),50,3,null);
           periodics.add(periodic);
        }


    }



    /*
    删除周期事件
    id: 周期事件id
     */
    private boolean deletePeriodic(int id){
        Periodic delPeriodic = periodics.get(id);

        //从数据库里删除
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
        periodicDAO.deletePeriodic(delPeriodic.getPeriodic_id());

        //periodics与tmpadapter里的数据是绑定的，因此periodics不用重复删除
        tmpadapter.removeItem(id);
        tmpadapter.notifyDataSetChanged();

        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        return true;
    }





}









class PeriodicListAdapter extends ArrayAdapter<Periodic> {
    private int resourceId;

    public PeriodicListAdapter(Context context, int textViewResourceId, List<Periodic> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }



    /*
    在每个子项滚到屏幕内时被调用
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Periodic  periodicItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        //TextView textView = (TextView)view.findViewById(R.id.periodic_item_text);


        TextView nameView = (TextView)view.findViewById(R.id.periodic_item_name);
        TextView moneyView = (TextView)view.findViewById(R.id.periodic_item_money);
        TextView startView = (TextView)view.findViewById(R.id.periodic_item_start);
        TextView endView = (TextView)view.findViewById(R.id.periodic_item_end);




        /*
        设置periodic项目的值，信息
         */
        //时间格式化
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        nameView.setText(periodicItem.getPeriodic_name());
        moneyView.setText(String.valueOf(periodicItem.getPeriodic_money()));
        startView.setText(format.format(periodicItem.getStart()));
        endView.setText(format.format(periodicItem.getEnd()));



        return view;
    }






}











/**
 数据适配器
 *
 */
class RecomendAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<Periodic> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据
    List<Periodic> backData;//用来备份原始数据
    MyFilter mFilter ;

    public RecomendAdapter(Context context, List<Periodic> data) {
        this.context = context;
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


    public void removeItem(int index){
        data.remove(index);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.periodic_list_item,null);
        }


       //从数组中得到数据并然后给页面组件设置值
        Periodic periodicItem=data.get(position);
        TextView nameView = (TextView)view.findViewById(R.id.periodic_item_name);
        TextView moneyView = (TextView)view.findViewById(R.id.periodic_item_money);
        TextView startView = (TextView)view.findViewById(R.id.periodic_item_start);
        TextView endView = (TextView)view.findViewById(R.id.periodic_item_end);




        /*
        设置periodic项目的值，信息
         */
        //时间格式化
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        nameView.setText(periodicItem.getPeriodic_name());
        moneyView.setText(String.valueOf(periodicItem.getPeriodic_money()));
        startView.setText(format.format(periodicItem.getStart()));
        endView.setText(format.format(periodicItem.getEnd()));

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
            List<Periodic> list ;
            if (TextUtils.isEmpty(charSequence)){//当过滤的关键字为空的时候，我们则显示所有的数据
                list  = backData;
            }else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Periodic recomend:backData){
                    if (recomend.getPeriodic_name().contains(charSequence)){
                        //if (recomend.getPeriodic_name().contains(charSequence)||recomend.getDesc().contains(charSequence))
                        Log.d("++performFiltering:",recomend.getPeriodic_name());

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
            data = (List<Periodic>)filterResults.values;
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
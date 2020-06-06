package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Switch;
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
import pojo.Category;
import pojo.Periodic;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment implements View.OnClickListener{

    private View mView;
    private Button addPeriodic;
    private SearchView searchPeriodic;
    private List<Periodic> periodics = new ArrayList<Periodic>();
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
                //Toast.makeText(getActivity(), "点击的是第" + position + "项:"+tmp.getPeriodic_name(), Toast.LENGTH_SHORT).show();

                //这里构造的periodic没有id,先写一个
                //tmp.setPeriodic_id(4);//设置一下id

                //查看和修改账单
                Intent intent1 = new Intent(getContext(),UpdatePeriodicActivity.class);
                intent1.putExtra("periodicId",Integer.toString(tmp.getPeriodic_id()));

                startActivityForResult(intent1,1010);//设置请求码为1010

               // startActivity(intent1);



                //应该要从数据库重新查询更新列表，因为数据可能已经 修改了



            }
        });


        //左滑删除
        listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int position) {
                //Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
                Log.i("list:",String.valueOf(periodics.get(position).getPeriodic_id()));
                //删除这个周期事件
                deletePeriodic(position);
                //Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });



        //搜索框事件监听
        searchPeriodic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交搜索后的处理逻辑
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容
                Toast.makeText(getActivity(), "你输入的选择是：" + query, Toast.LENGTH_SHORT).show();

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
    public void onActivityResult(int requestCode,int resultCode,Intent Tdata){
        switch (requestCode){
            case 1010 :
                if(resultCode==RESULT_OK){

                    Log.i("更新修改的数据  ","开始");
                    String reId = Tdata.getStringExtra("id_return_per");
                    updateItem(Integer.valueOf(reId));
                    Log.i("更新修改的数据  : ","结束");

                }

                break;

            case 1210:
                if(resultCode==RESULT_OK){
                    Log.i("添加了数据，需要更新数据  ","开始");
                    updatePeriodicList();
                    Log.i("更新数据  : ","结束");
                }


            default:
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("Fragment3 周期事件列表", "onHiddenChanged  ！hidden刷新数据");
        if(hidden){
            //TODO now visible to user
            Log.i("Fragment3 周期事件列表", "onHiddenChanged  hidden刷新数据");
        } else {
            //TODO now invisible to user
            Log.i("Fragment3 周期事件列表", "onHiddenChanged  ！hidden刷新数据");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i("Fragment3", "刷新周期事件列表");

        //到显示状态为true，不可见为false
        if (isVisibleToUser) {
            RefreshData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }




    public void RefreshData(){
        if(periodics!=null && tmpadapter!=null && listView!=null){
            periodics.clear();
            initPeriodics();

            /*PeriodicDAO periodicDAO = new PeriodicDAOImpl();
            periodics = periodicDAO.listPeriodic();

            //筛选状态不为删除的
            for(int i=0;i<periodics.size();i++){
                if(periodics.get(i).getState() == -1){
                    periodics.remove(i);
                }
            }*/




            tmpadapter.clearAll();
            tmpadapter.setData(periodics);
            tmpadapter.notifyDataSetChanged();
            listView.setAdapter(tmpadapter);
        }





        Log.i("Fragment3", "RefreshData");



    }



    @Override
    public void onResume() {
        super.onResume();
    }














    public void updatePeriodicList(){
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();
        List<Periodic> tmpList = periodicDAO.listPeriodic();

       tmpadapter.clearAll();
       for(Periodic item:tmpList){
           if(item.getState() != -1){
               tmpadapter.addItem(item);
           }

       }

       tmpadapter.notifyDataSetChanged();

    }



    public void updateItem(int periodicId){
        Log.i("更新periodic id 为  : ",String.valueOf(periodicId));


        PeriodicDAO periodicDAO = new PeriodicDAOImpl();
        Periodic temp = periodicDAO.getPeriodicById(periodicId);

        for(Periodic per:periodics){
            if(per.getPeriodic_id()==temp.getPeriodic_id()){
                per.setAccount_id(temp.getAccount_id());
                per.setCategory_id(temp.getCategory_id());
                per.setUser_id(temp.getUser_id());
                per.setType(temp.getType());
                per.setPeriodic_name(temp.getPeriodic_name());
                per.setCycle(temp.getCycle());
                per.setStart(temp.getStart());
                per.setEnd(temp.getEnd());
                per.setPeriodic_money(temp.getPeriodic_money());
                per.setState(temp.getState());
                per.setAnchor(temp.getAnchor());

                break;
            }
        }


        //通知适配器数据改变
        tmpadapter.notifyDataSetChanged();



    }







    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_periodic:
                Intent intent1 = new Intent(getActivity(), AddPeriodicActivity.class);
                startActivityForResult(intent1,1210);//设置请求码为1210

                //startActivity(intent1);

               // Log.d("add_periodic","添加事件");
                //Toast.makeText(getActivity(), "功能尚未编写", Toast.LENGTH_SHORT).show();

                break;
            case R.id.search_periodic:
                Log.d("search_periodic","查询事件");
                Toast.makeText(getActivity(), "搜索功能", Toast.LENGTH_SHORT).show();
                break;


                default:break;
        }


    }








    private void initPeriodics(){
       //实际代码
       //调用DAO代码获得周期事件数组

       PeriodicDAO periodicDAO = new PeriodicDAOImpl();
       List<Periodic> tmp = periodicDAO.listPeriodic();
       List<Periodic> newData = new ArrayList<Periodic>();
        //筛选状态不为删除的
        for(Periodic item:tmp){
            if(item.getState()!=-1){
                newData.add(item);
            }
        }

        periodics = newData;



    }



    /*
    删除周期事件
    id: 周期事件id
     */
    private boolean deletePeriodic(int index){
        //Periodic delPeriodic = periodics.get(id);
        Periodic delPeriodic = (Periodic) tmpadapter.getItem(index);

        //标记为删除
        delPeriodic.setState(-1);

        //从数据库里删除
        PeriodicDAO periodicDAO=new PeriodicDAOImpl();

        //periodicDAO.deletePeriodic(delPeriodic.getPeriodic_id());
        //逻辑删除
        periodicDAO.setState(delPeriodic.getPeriodic_id(),-1);


        //periodics与tmpadapter里的数据是绑定的，因此periodics不用重复删除
        tmpadapter.removeItem(index);
        tmpadapter.notifyDataSetChanged();

        //Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
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

    public  void addItem(Periodic obj){
        data.add(obj);
    }

    public void removeItem(int index){
        Periodic tep = data.get(index);
        backData.remove(tep);
        data.remove(tep);


    }


    public void clearAll(){
       data.clear();
       backData.clear();
    }

    public void setData(List<Periodic> data){
        this.data = data;
        backData = data;
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

        String tmpMoney=null;
        if(periodicItem.getType()==0){//支出
            tmpMoney = "-" + String.valueOf(periodicItem.getPeriodic_money());
            moneyView.setTextColor(Color.parseColor("#ff0000"));


        }
        else {//收入
            tmpMoney = "+" + String.valueOf(periodicItem.getPeriodic_money());
            moneyView.setTextColor(Color.parseColor("#00ff00"));

        }



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
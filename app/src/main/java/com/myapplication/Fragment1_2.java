package com.myapplication;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1_2 extends Fragment
{
    boolean OutcomeFlag = false;
    boolean IncomeFlag = true;
    private View mView;
    private ImageButton refresh;
    private PieChart mPieChart;
    private Switch mChangeType;
    private ListView lview;
    private String strbeginDate;
    private String strendDate;
    private TextView tvBeginDate;
    private TextView tvEndDate;
    private CategoryListAdapter adapter;
    private List<CategoryListItem> categoryList = new ArrayList<>();
    private List<CategoryChartItem> categoryChart = new ArrayList<>();
    private TimePickerView mStartDatePickerView1;
    private TimePickerView mStartDatePickerView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        strendDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        strbeginDate =  new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_2, container, false);
        }

        lview = mView.findViewById(R.id.CategoryList);
        mPieChart = mView.findViewById(R.id.PieChart);
        mChangeType = mView.findViewById(R.id.Switch_In_Out);
        refresh = mView.findViewById(R.id.refresh_category);
        tvBeginDate = mView.findViewById(R.id.beginDate);
        tvEndDate = mView.findViewById(R.id.endDate);

        //设置texeview初始时间，为本月1号——今日
        tvBeginDate.setText(strbeginDate);
        tvEndDate.setText(strendDate);
        tvBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartDatePickerView1.show();
            }
        });
        initStartTimePicker1();
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartDatePickerView2.show();
            }
        });
        initStartTimePicker2();


        //设置Listview,默认数据为当月支出前10
        categoryList = loadcategoryList(strbeginDate,strendDate,OutcomeFlag);
        adapter = new CategoryListAdapter(getActivity(), R.layout.categorylist_item, categoryList);
        lview.setAdapter(adapter);

        //设置PieChart,默认数据为当月支出
        categoryChart = loadcategoryChart(strbeginDate,strendDate,OutcomeFlag);
        ShowPieChart(mPieChart, setPieChartData(categoryChart), OutcomeFlag);

        //收支切换时 refresh数据
        mChangeType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    RefreshData(strbeginDate,strendDate,IncomeFlag);
                    Toast.makeText(getActivity(),"收入时间:"+strbeginDate+"——"+strendDate,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RefreshData(strbeginDate,strendDate,OutcomeFlag);
                    Toast.makeText(getActivity(),"支出时间:"+strbeginDate+"——"+strendDate,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //起止日期切换时 refresh数据
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(compareDate(tvBeginDate.getText().toString(),tvEndDate.getText().toString()))
                {
                    strbeginDate = tvBeginDate.getText().toString();
                    strendDate = tvEndDate.getText().toString();
                    if(mChangeType.isChecked())
                    {
                        RefreshData(strbeginDate,strendDate,IncomeFlag);
                        Toast.makeText(getActivity(),"收入时间:"+strbeginDate+"——"+strendDate,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RefreshData(strbeginDate,strendDate,OutcomeFlag);
                        Toast.makeText(getActivity(),"支出时间:"+strbeginDate+"——"+strendDate,Toast.LENGTH_SHORT).show();

                    }

                }
                else {
                    tvBeginDate.setText(strbeginDate);
                    tvEndDate.setText(strendDate);
                    Toast.makeText(getActivity(), "起始时间大于终止时间，请重新选择", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mView;
    }

    //比较两个yyyy-mm-dd格式的时间，d1<=d2返回true
    public boolean compareDate(String d1,String d2)
    {
        boolean flag=false;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            Date dt1 = df.parse(d1);
            Date dt2 = df.parse(d2);
            //dt1在dt2后
            if (dt1.getTime() <= dt2.getTime()) {
                flag=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    //加载数据用于饼图展示  后续可能加入 dadte事件 使用接口获取数据
    public List<CategoryChartItem> loadcategoryChart(String begindate,String enddate,boolean typeflag)
    {
        int i;
        List<CategoryChartItem> mcategoryChart = new ArrayList<>();
        if (typeflag == OutcomeFlag) {
            for (i = 0; i < 5; i++) {
                CategoryChartItem c1 = new CategoryChartItem("火锅",15F);
                mcategoryChart.add(c1);
                CategoryChartItem c2 = new CategoryChartItem("购物",5F);
                mcategoryChart.add(c2);
            }
        }

        else {
            for (i = 0; i < 5; i++) {
                CategoryChartItem c1 = new CategoryChartItem("工资",10F);
                mcategoryChart.add(c1);
                CategoryChartItem c2 = new CategoryChartItem("奖金",10F);
                mcategoryChart.add(c2);
            }
        }
        return mcategoryChart;
    }

    private List<PieEntry> setPieChartData(List<CategoryChartItem> chartdata) {
        //       List<String> dataList = new ArrayList<>();
        List<PieEntry> mPie = new ArrayList<>();
        PieEntry pieEntry;
        for (CategoryChartItem c : chartdata) {
            mPie.add(new PieEntry(c.getPrecent(), c.getCategoryname()));
        }
        return mPie;
    }

    private void ShowPieChart(PieChart pieChart, List<PieEntry> piedata, boolean typeflag) {
        PieDataSet dataSet = new PieDataSet(piedata, "");
        ArrayList<Integer> mColorList = new ArrayList<Integer>();
        int[] color = {Color.rgb(239, 199, 194),
                Color.rgb(215, 144, 123),
                Color.rgb(232, 221, 181),
                Color.rgb(178, 201, 171),
                Color.rgb(104, 166, 145),
                Color.rgb(108, 75, 94),
                Color.rgb(179, 103, 155),
                Color.rgb(244, 165, 174),
                Color.rgb(125, 130, 184),
                Color.rgb(250, 156, 56)
        };
        for (int c : color) {
            mColorList.add(c);
        }
        dataSet.setColors(mColorList);
        PieData pieData = new PieData(dataSet);
        // 设置描述，我设置了不显示，因为不好看，你也可以试试让它显示，真的不好看
        //Description description = new Description();
        //description.setEnabled(false);
        // pieChart.setDescription(description);
        //设置半透明圆环的半径, 0为透明
        pieChart.setTransparentCircleRadius(0f);
        //设置初始旋转角度
        pieChart.setRotationAngle(-15);
        //数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1OffsetPercentage(80f);
        //设置连接线的颜色
        dataSet.setValueLineColor(Color.LTGRAY);
        // 连接线在饼状图外面
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        // 设置饼块之间的间隔
        dataSet.setSliceSpace(1f);
        dataSet.setHighlightEnabled(true);
        // 显示图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        // 和四周相隔一段距离,显示数据
        pieChart.setExtraOffsets(26, 5, 26, 5);
        // 设置pieChart图表是否可以手动旋转
        pieChart.setRotationEnabled(true);
        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);
        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //设置pieChart是否只显示饼图上百分比不显示文字
        pieChart.setDrawEntryLabels(false);
        //是否绘制PieChart内部中心文本
        pieChart.setDrawCenterText(true);
        if (typeflag == OutcomeFlag)
            pieChart.setCenterText("支出比例");
        else
            pieChart.setCenterText("收入比例");
        pieChart.setCenterTextColor(Color.DKGRAY);//中间的文字颜色
        pieChart.setCenterTextSize(12);//中间的文字字体大小
        // 绘制内容value，设置字体颜色大小
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setData(pieData);
        // 更新 piechart 视图
        pieChart.postInvalidate();
    }
    //加载获取数据源 用于列表展示  后续可能加入 dadte事件 使用接口获取数据
    public List<CategoryListItem> loadcategoryList(String begindate,String enddate,boolean typeflag) {
        int i;
        List<CategoryListItem> mcategoryList = new ArrayList<>();
        if (typeflag == OutcomeFlag) {
            for (i = 0; i < 5; i++) {
                CategoryListItem c1 = new CategoryListItem(i * 2 + 1, "支出", 1234.00, "2020-2-7");
                mcategoryList.add(c1);
                CategoryListItem c2 = new CategoryListItem(i * 2 + 2, "支出", 123451.00, "2020-2-7");
                mcategoryList.add(c2);
            }
            CategoryListItem c2 = new CategoryListItem(i * 2 + 2, "支出", 123451.00, "2020-2-7");
            mcategoryList.add(c2);
        } else {
            for (i = 0; i < 5; i++) {
                CategoryListItem c1 = new CategoryListItem(i * 2 + 1, "收入", 12, "2020-2-7");
                mcategoryList.add(c1);
                CategoryListItem c2 = new CategoryListItem(i * 2 + 2, "收入", 12, "2020-2-7");
                mcategoryList.add(c2);
            }
            CategoryListItem c2 = new CategoryListItem(i * 2 + 2, "收入", 12, "2020-2-7");
            mcategoryList.add(c2);
        }
        return mcategoryList;
    }

    public void RefreshData(String begindate,String enddate,boolean flag)
    {
        categoryList.clear();
        categoryList.addAll(loadcategoryList(begindate,enddate,flag));
        adapter.notifyDataSetChanged();
        lview.setAdapter(adapter);
        categoryChart.clear();
        categoryChart.addAll(loadcategoryChart(begindate,enddate,flag));
        ShowPieChart(mPieChart, setPieChartData(categoryChart), flag);
    }

    private void initStartTimePicker1() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        //设置最小日期和最大日期
        Calendar startDate = Calendar.getInstance();
        try {
            startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endDate = Calendar.getInstance();//最大日期是今天

        //时间选择器
        mStartDatePickerView1 = new TimePickerBuilder(mView.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                tvBeginDate.setText(DateTimeHelper.formatToString(date,"yyyy-MM-dd"));
            }
        })
                .setDecorView((ConstraintLayout)mView.findViewById(R.id.container))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTitleText("开始日期")//标题文字
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
                .setCancelText("取消")//取消按钮文字
                .setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
                .setRangDate(startDate, endDate)//设置最小和最大日期
                .setDate(selectedDate)//设置选中的日期
                .build();
    }
    private void initStartTimePicker2() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        //设置最小日期和最大日期
        Calendar startDate = Calendar.getInstance();
        try {
            startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endDate = Calendar.getInstance();//最大日期是今天

        //时间选择器
        mStartDatePickerView2 = new TimePickerBuilder(mView.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                tvEndDate.setText(DateTimeHelper.formatToString(date,"yyyy-MM-dd"));
            }
        })
                .setDecorView((ConstraintLayout)mView.findViewById(R.id.container))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTitleText("开始日期")//标题文字
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
                .setCancelText("取消")//取消按钮文字
                .setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
                .setRangDate(startDate, endDate)//设置最小和最大日期
                .setDate(selectedDate)//设置选中的日期
                .build();


    }
}
class CategoryListItem
{
    private int order;
    private String categoryname;
    private double money;
    private Float precent;
    private String date;

    public CategoryListItem(int order,String categoryname,double money,String date)
    {
        this.order=order;
        this.categoryname=categoryname;
        this.money=money;
        this.date=date;
    }

    public int getOrder() {
        return order;
    }
    public String getCategoryname() {
        return categoryname;
    }

    public Float getPrecent() {
        return precent;
    }

    public double getMoney() {
        return money;
    }
    public String getDate() {
        return date;
    }
}

class CategoryListAdapter extends ArrayAdapter<CategoryListItem>
{
     private int id;
     public CategoryListAdapter(Context context, int textid, List<CategoryListItem> objects)
     {
         super(context, textid, objects);
         id = textid;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent)
     {
         CategoryListItem categoryListItem = getItem(position);
         View view = LayoutInflater.from(getContext()).inflate(id, parent, false);
         TextView month = (TextView) view.findViewById(R.id.order);
         TextView income = (TextView) view.findViewById(R.id.categoryname);
         TextView outcome = (TextView) view.findViewById(R.id.money);
         TextView balance = (TextView) view.findViewById(R.id.date);
         month.setText(String.valueOf(categoryListItem.getOrder()));
         income.setText(categoryListItem.getCategoryname());
         outcome.setText(String.valueOf(categoryListItem.getMoney()));
         balance.setText(String.valueOf(categoryListItem.getDate()));
         return view;
     }
 }

class CategoryChartItem
{
     private String categoryname;
     private Float precent;
     private double money;
     public CategoryChartItem(String categoryname,Float precent)
     {
         this.categoryname=categoryname;
         this.precent=precent;
     }
     public CategoryChartItem(String categoryname,Float precent,Float money)
     {
         this.categoryname=categoryname;
         this.precent=precent;
         this.money=money;
     }
     public String getCategoryname() {
         return categoryname;
     }

     public Float getPrecent() {
         return precent;
     }

     public double getMoney() {
         return money;
     }
 }
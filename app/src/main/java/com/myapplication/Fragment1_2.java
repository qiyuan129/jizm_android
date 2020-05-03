package com.myapplication;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1_2 extends Fragment
{
    boolean OutcomeFlag = false;
    boolean IncomeFlag = true;
    private View mView;
    private PieChart mPieChart;
    private Switch mChangeType;
    private ListView lview;
    private Date mdate;
    private CategoryListAdapter adapter;
    private List<CategoryListItem> categoryList = new ArrayList<>();
    private List<CategoryChartItem> categoryChart = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
            //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_2, container, false);
        }

        lview = mView.findViewById(R.id.CategoryList);
        categoryList = loadcategoryList(OutcomeFlag);
        adapter = new CategoryListAdapter(getActivity(), R.layout.categorylist_item, categoryList);
        lview.setAdapter(adapter);

        mPieChart = mView.findViewById(R.id.PieChart);
        categoryChart = loadcategoryChart(OutcomeFlag);
        ShowPieChart(mPieChart, setPieChartData(categoryChart), OutcomeFlag);
        mChangeType = mView.findViewById(R.id.Switch_In_Out);
        mChangeType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    RefreshData(IncomeFlag);
                }
                else
                {
                    RefreshData(OutcomeFlag);
                }
            }
        });
        return mView;
    }

    //加载数据用于饼图展示  后续可能加入 dadte事件 使用接口获取数据
    public List<CategoryChartItem> loadcategoryChart(boolean typeflag)
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
    public List<CategoryListItem> loadcategoryList(boolean typeflag) {
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


    public void RefreshData(boolean flag)
    {
        categoryList.clear();
        categoryList.addAll(loadcategoryList(flag));
        adapter.notifyDataSetChanged();
        lview.setAdapter(adapter);
        categoryChart.clear();
        categoryChart.addAll(loadcategoryChart(flag));
        ShowPieChart(mPieChart, setPieChartData(categoryChart), flag);
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
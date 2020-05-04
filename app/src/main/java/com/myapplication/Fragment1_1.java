package com.myapplication;
import dao.BillDAO;
import dao.BillDAOImpl;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Long.valueOf;

public class Fragment1_1 extends Fragment {

    private TimePickerView mStartDatePickerView;
    private View mView;
    private ListView lview;
    private TextView choiceYear;
    private ImageButton refresh;
    private int year=(Calendar.getInstance()).get(Calendar.YEAR);
    private LineChart lineChart;
    private TrendListAdapter adapter;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    private List<TrendListItem> trendList = new ArrayList<>();
    private List<IncomeLineItem> incomeLine = new ArrayList<>();
    private List<OutcomeLineItem> outcomeLine = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_1, container, false);
        }
        lview = (ListView) mView.findViewById(R.id.TrendList);
        lineChart = mView.findViewById(R.id.LineChart);
        initChart(lineChart);

        incomeLine = loadincomeChart(year);
        showLineChart(incomeLine, "收入", Color.rgb(104,134,197));
        outcomeLine = loadoutcomeChart(year);
        addLine(outcomeLine, "支出", Color.rgb(187,59,14));

        lview = (ListView) mView.findViewById(R.id.TrendList);
        trendList = loadtrendList(year);
        adapter = new TrendListAdapter(getActivity(), R.layout.trendlist_item, trendList);
        lview.setAdapter(adapter);

        choiceYear = (TextView) mView.findViewById(R.id.ButtonYear);
        choiceYear.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        choiceYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartDatePickerView.show();
            }
        });
        initStartTimePicker();

        //refresh_year = getYear(choiceYear.getText().toString());
        refresh=(ImageButton)mView.findViewById(R.id.refresh_trend);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = getYear(choiceYear.getText().toString());
                RefreshData(year);
                //Toast.makeText(mView.getContext(),String.valueOf(refresh_year),Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }


    public List<IncomeLineItem> loadincomeChart(int myear)
    {
        BillDAOImpl billDAO = new BillDAOImpl();
        int i=0;
        List<IncomeLineItem> incomeChart = new ArrayList<>();
        List<Double> dbData = billDAO.monthlyIncome(myear);
        for(double c:dbData) {
            incomeChart.add(new IncomeLineItem(c,i+1));
            i++;
        }
        if(i>0)
            incomeChart.add(new IncomeLineItem(dbData.get(i-1),i-1));
        incomeChart.add(new IncomeLineItem(110,i));
        return incomeChart;
    }

    public List<OutcomeLineItem> loadoutcomeChart(int myear)
    {
        BillDAOImpl billDAO = new BillDAOImpl();
        int i=0;
        List<OutcomeLineItem> outcomeChart = new ArrayList<>();
        List<Double> dbData = billDAO.monthlyOutcome(myear);
        for(double c:dbData) {
            outcomeChart.add(new OutcomeLineItem(c,i+1));
            i++;
        }
        if(i>0)
            outcomeChart.add(new OutcomeLineItem(dbData.get(i-1),i-1));
        outcomeChart.add(new OutcomeLineItem(220,i));
        //outcomeChart.add(new OutcomeLineItem(90,i));
        /*
        for(i=0;i<dbData.size();i++) {
            outcomeChart.add(new OutcomeLineItem(dbData.get(i),i+1));
        }
        i=i-1;
        outcomeChart.add(new OutcomeLineItem(dbData.get(i),i++));
        outcomeChart.add(new OutcomeLineItem(220,i++));*/
        /*if (year == 2020)
        {
            for (i = 0; i < 13; i++)
            {
                outcomeChart.add(new OutcomeLineItem(15*i,i));
            }

        }
        else
        {
            for (i = 0; i < 5; i++)
            {
                OutcomeLineItem c1 = new OutcomeLineItem(82,i*2+1);
                outcomeChart.add(c1);
                OutcomeLineItem c2 = new OutcomeLineItem(64,i*2+2);
                outcomeChart.add(c2);
            }
            OutcomeLineItem c2 = new OutcomeLineItem(102,i*2+2);
            outcomeChart.add(c2);
        }*/
        return outcomeChart;
    }

    public List<TrendListItem> loadtrendList(int myear)
    {
        BillDAOImpl billDAO = new BillDAOImpl();
        int i=0;
        double in,out;
        TrendListItem t1;
        List<TrendListItem> trendList = new ArrayList<>();
        List<Double> dbDataIncome = billDAO.monthlyIncome(myear);
        List<Double> dbDataOutcome = billDAO.monthlyOutcome(myear);
        for (i=0;i<dbDataIncome.size();i++)
        {
            in = dbDataIncome.get(i);
            out = dbDataOutcome.get(i);
            t1 = new TrendListItem(String.valueOf(myear)+"/"+String.format("%02d",i+1),String.valueOf(in), String.valueOf(out), String.valueOf(in-out));
            trendList.add(t1);
        }
        TrendListItem tt = new TrendListItem("测试"+String.format("%02d",i+1),"1111", "1111", "1111");
        trendList.add(tt);
        if(i>0)
        {
            t1 = new TrendListItem(String.valueOf(myear)+"/"+String.format("%02d",i), String.valueOf(dbDataIncome.get(i-1)),
                String.valueOf(dbDataOutcome.get(i-1)), String.valueOf(dbDataIncome.get(i-1)-dbDataOutcome.get(i-1)));
            trendList.add(t1);
        }
        return trendList;
    }
    public void RefreshData(int year)
    {
        incomeLine.clear();
        outcomeLine.clear();
        incomeLine.addAll(loadincomeChart(year));
        outcomeLine.addAll(loadoutcomeChart(year));
        showLineChart(incomeLine, "收入", Color.rgb(104,134,197));
        addLine(outcomeLine, "支出", Color.rgb(187,59,14));

        trendList.clear();
        trendList.addAll(loadtrendList(year));
        adapter.notifyDataSetChanged();
        lview.setAdapter(adapter);
    }





    public int getYear(String stryear) {
        int year = 0;
        try {

            year = valueOf(stryear).intValue();

        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return year;
    }

     private void initChart(LineChart lineChart)
     {
           /***图表设置***/
           //是否展示网格线
          lineChart.setDrawGridBackground(false);
            //是否显示边界
          lineChart.setDrawBorders(false);
          //是否可以拖动
          lineChart.setDragEnabled(false);
          //是否有触摸事件
          lineChart.setTouchEnabled(true);
          //设置XY轴动画效果
          lineChart.animateY(2500);
          lineChart.animateX(1500);


          /***XY轴的设置***/
          xAxis = lineChart.getXAxis();
          leftYAxis = lineChart.getAxisLeft();
          rightYaxis = lineChart.getAxisRight();
          xAxis.setDrawGridLines(false);
          rightYaxis.setDrawGridLines(false);
          leftYAxis.setDrawGridLines(true);

          //X轴设置显示位置在底部
          xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
          xAxis.setAxisMinimum(1f);
          xAxis.setGranularity(1f);
          //X轴分成10份
          xAxis.setLabelCount(10,false);

          //保证Y轴从0开始，不然会上移一点
          leftYAxis.setAxisMinimum(0f);
          //rightYaxis.setAxisMinimum(0f);
          rightYaxis.setEnabled(false);
          leftYAxis.enableGridDashedLine(10f, 10f, 0f);

          /***折线图例 标签 设置***/

          legend = legend = lineChart.getLegend();
                //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
          legend.setForm(Legend.LegendForm.LINE);
          legend.setTextSize(12f);
          //显示位置 左下方
          legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
          legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
          legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
          //是否绘制在图表里面
          legend.setDrawInside(false);
      }
      private void initLineDataSet(LineDataSet lineDataSet,int color, LineDataSet.Mode mode)
      {
          lineDataSet.setColor(color);
          lineDataSet.setCircleColor(color);
          lineDataSet.setLineWidth(1f);
          lineDataSet.setCircleRadius(3f);
          lineDataSet.setDrawValues(false);
          //设置曲线值的圆点是实心还是空心
          lineDataSet.setDrawCircles(false);
          //lineDataSet.setDrawCircleHole(false);
          //lineDataSet.setValueTextSize(10f);
          //设置折线图填充
          lineDataSet.setDrawFilled(true);
          lineDataSet.setFormLineWidth(1f);
          lineDataSet.setFormSize(15.f);
          if (mode == null)
          {
              //设置曲线展示为圆滑曲线（如果不设置则默认折线）
              lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
          }
          else
           {
               lineDataSet.setMode(mode);
           }
      }

      private void addLine(List<OutcomeLineItem> dataList, String name, int color)
      {
          List<Entry> entries = new ArrayList<>();
          for (int i = 0; i < dataList.size(); i++) {
              OutcomeLineItem data = dataList.get(i);
              Entry entry = new Entry(i, (float) data.getValue());
              entries.add(entry);
          }
          // 每一个LineDataSet代表一条线
          LineDataSet lineDataSet = new LineDataSet(entries, name);
          initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
          lineChart.getLineData().addDataSet(lineDataSet);
          lineChart.invalidate();
      }

      public void setMarkerView()
      {
          LineChartMarkView mv = new LineChartMarkView(getActivity(), xAxis.getValueFormatter());
          mv.setChartView(lineChart);
          lineChart.setMarker(mv);
          lineChart.invalidate();
      }
      public void setChartFillDrawable(Drawable drawable)
      {
          if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0)
          {
              LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
              //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
              lineDataSet.setDrawFilled(true);
              lineDataSet.setFillDrawable(drawable);
              lineChart.invalidate();
          }
      }
      public void showLineChart(List<IncomeLineItem> dataList, String name, int color)
      {
          List<Entry> entries = new ArrayList<>();
          for (int i = 0; i < dataList.size(); i++)
          {
              IncomeLineItem data = dataList.get(i);
              Entry entry = new Entry(i, (float) data.getValue());
              entries.add(entry);
          }
          // 每一个LineDataSet代表一条线
          LineDataSet lineDataSet = new LineDataSet(entries, name);
          initLineDataSet(lineDataSet,color, LineDataSet.Mode.LINEAR);
          LineData lineData = new LineData(lineDataSet);
          lineChart.setData(lineData);
          Drawable drawable = getResources().getDrawable(R.drawable.income_fade);
          setChartFillDrawable(drawable);
          setMarkerView();
      }

    public void initStartTimePicker() {
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
        mStartDatePickerView = new TimePickerBuilder(mView.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                choiceYear.setText(DateTimeHelper.formatToString(date,"yyyy"));
            }
        })
                .setDecorView((ConstraintLayout)mView.findViewById(R.id.container))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, false, false, false, false, false})
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


class TrendListItem
{
    private String month;
    private String income;
    private String outcome;
    private String balance;
    public TrendListItem(String month,String income,String outcome,String balance)
    {
        this.month=month;
        this.income=income;
        this.outcome=outcome;
        this.balance=balance;
    }
    public String getMonth() { return month; }
    public String getIncome() { return income; }
    public String getOutcome() { return outcome; }
    public String getBalance() { return balance; }
}
class TrendListAdapter extends ArrayAdapter<TrendListItem>
{
    private int id;
    public TrendListAdapter(Context context, int textid, List<TrendListItem>objects)
    {
        super(context,textid,objects);
        id=textid;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TrendListItem trendListItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(id,parent,false);
        TextView month = (TextView) view.findViewById(R.id.trendmonth);
        TextView income = (TextView) view.findViewById(R.id.trendincome);
        TextView outcome = (TextView) view.findViewById(R.id.trendoutcome);
        TextView balance = (TextView) view.findViewById(R.id.trendbalance);
        month.setText(trendListItem.getMonth());
        income.setText(trendListItem.getIncome());
        outcome.setText(trendListItem.getOutcome());
        balance.setText(trendListItem.getBalance());
        return view;
    }
}

class IncomeLineItem
{
    private double value;
    private int month;

    public IncomeLineItem(double value,int month)
    {
        this.value=value;
        this.month=month;
    }
    public double getValue(){return value; }
    public int getMonth() { return month; }
}

class OutcomeLineItem
{
    private double value;
    private int month;
    public OutcomeLineItem(double value,int month)
    {
        this.value=value;
        this.month=month;
    }
    public double getValue() { return value; }
    public int getMonth() { return month; }
}
class LineChartMarkView extends MarkerView
 {
     private TextView tvDate;
     private TextView tvValue1;
     private TextView tvValue2;
     private IAxisValueFormatter xAxisValueFormatter;
     DecimalFormat df = new DecimalFormat("0.00");
     public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter)
     {
         super(context, R.layout.markview_layout);
         this.xAxisValueFormatter = xAxisValueFormatter;
         tvDate = findViewById(R.id.tv_date);
         tvValue1 = findViewById(R.id.tv_value1);
         tvValue2 = findViewById(R.id.tv_value2);
     }

     @SuppressLint("SetTextI18n")
     @Override
     public void refreshContent(Entry e, Highlight highlight)
     {
         Chart chart = getChartView();
         if (chart instanceof LineChart) {
             LineData lineData = ((LineChart) chart).getLineData();
             //获取到图表中的所有曲线
             List<ILineDataSet> dataSetList = lineData.getDataSets();
             for (int i = 0; i < dataSetList.size(); i++)
             {
                 LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                 //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                 float y = dataSet.getValues().get((int) e.getX()).getY();
                 if (i == 0)
                 {
                     tvValue1.setText(dataSet.getLabel() + "：" + df.format(y));
                 }
                 if (i == 1)
                 {
                     tvValue2.setText(dataSet.getLabel() + "：" + df.format(y));
                 }
             }
             tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
         }
         super.refreshContent(e, highlight);
     }

     @Override
     public MPPointF getOffset()
     {
         MPPointF mpPointF = new MPPointF(-(getWidth() / 2), -getHeight());
         return mpPointF;
     }
 }

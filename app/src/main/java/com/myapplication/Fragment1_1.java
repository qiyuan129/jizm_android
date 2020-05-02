package com.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fragment1_1 extends Fragment {

    private View mView;
    private ListView lview;
    private Button mButtonYear;
    private LineChart lineChart;
    private TrendListAdapter adapter;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    private int year = (Calendar.getInstance()).get(Calendar.YEAR);
    private List<TrendListItem> trendList = new ArrayList<>();
    private List<IncomeLineItem> incomeLine = new ArrayList<>();
    private List<OutcomeLineItem> outcomeLine = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_1, container, false);
        }
        //initTrentList();
        lineChart = mView.findViewById(R.id.LineChart);
        initChart(lineChart);

        incomeLine = loadincomeChart(2020);
        showLineChart(incomeLine, "收入", Color.rgb(104,134,197));
        outcomeLine = loadoutcomeChart(2020);
        addLine(outcomeLine, "支出", Color.rgb(187,59,14));

        lview = (ListView) mView.findViewById(R.id.TrendList);
        trendList = loadtrendList(year);
        adapter = new TrendListAdapter(getActivity(), R.layout.trendlist_item, trendList);
        lview.setAdapter(adapter);



        mButtonYear = (Button)mView.findViewById(R.id.ButtonYear);
        mButtonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshLineChart(2019);
                RefreshTrendList(2019);
            }
        });
        return mView;
    }


    public List<TrendListItem> loadtrendList(int myear) {
        int i;
        List<TrendListItem> trendList = new ArrayList<>();
        if (myear == 2020 ) {
            for (i = 0; i < 6; i++) {
                TrendListItem c1 = new TrendListItem(String.valueOf(myear)+String.valueOf(i * 2 + 1), "1435.00", "126434.00", "774.00");
                trendList.add(c1);
                TrendListItem c2 = new TrendListItem(String.valueOf(myear)+String.valueOf(i * 2 + 2), "123464.00", "151.00", "227.00");
                trendList.add(c2);
            }

        } else {
            for (i = 0; i < 6; i++) {
                TrendListItem c1 = new TrendListItem(String.valueOf(myear)+String.valueOf(i * 2 + 1), "1111.00", "14.00", "842.00");
                trendList.add(c1);
                TrendListItem c2 = new TrendListItem(String.valueOf(myear)+String.valueOf(i * 2 + 2),"2222.00", "121334.00", "1234.00");
                trendList.add(c2);
            }

        }
        return trendList;
    }
    public List<IncomeLineItem> loadincomeChart(int year) {
        int i;
        List<IncomeLineItem> incomeChart = new ArrayList<>();
            if (year == 2020)
            {
                for (i = 0; i < 6; i++) {
                incomeChart.add(new IncomeLineItem(5*i,i));
            }

        } else {
            for (i = 0; i < 5; i++) {
                IncomeLineItem c1 = new IncomeLineItem(83,i*2+1);
                incomeChart.add(c1);
                IncomeLineItem c2 = new IncomeLineItem(24,i*2+2);
                incomeChart.add(c2);
            }
            IncomeLineItem c2 = new IncomeLineItem(182,i*2+2);
            incomeChart.add(c2);
        }
        return incomeChart;
    }
    public List<OutcomeLineItem> loadoutcomeChart(int year) {
        int i;
        List<OutcomeLineItem> outcomeChart = new ArrayList<>();
        if (year == 2020) { for (i = 0; i < 6; i++) {
            outcomeChart.add(new OutcomeLineItem(10*i,i));
        }

        } else {
            for (i = 0; i < 5; i++) {
                OutcomeLineItem c1 = new OutcomeLineItem(82,i*2+1);
                outcomeChart.add(c1);
                OutcomeLineItem c2 = new OutcomeLineItem(64,i*2+2);
                outcomeChart.add(c2);
            }
            OutcomeLineItem c2 = new OutcomeLineItem(102,i*2+2);
            outcomeChart.add(c2);
        }
        return outcomeChart;
    }

    private void initChart(LineChart lineChart) {
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
    private void initLineDataSet(LineDataSet lineDataSet,int color, LineDataSet.Mode mode) {
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
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }
    private void addLine(List<OutcomeLineItem> dataList, String name, int color) {
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
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
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
        for (int i = 0; i < dataList.size(); i++) {
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

    public void RefreshLineChart(int year)
    {
        incomeLine.clear();
        outcomeLine.clear();
        incomeLine.addAll(loadincomeChart(year));
        outcomeLine.addAll(loadoutcomeChart(year));
        showLineChart(incomeLine, "收入", Color.rgb(104,134,197));
        addLine(outcomeLine, "支出", Color.rgb(187,59,14));
    }

    public void RefreshTrendList(int year)
    {
        trendList.clear();
        trendList.addAll(loadtrendList(year));
        adapter.notifyDataSetChanged();
        lview.setAdapter(adapter);
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

        public String getMonth() {
            return month;
        }

        public String getIncome() {
            return income;
        }

        public String getOutcome() {
            return outcome;
        }

        public String getBalance() {
            return balance;
        }
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
        public View getView (int position, View convertView, ViewGroup parent)
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
    private float value;
    private int month;

    public IncomeLineItem(float value,int month){this.value=value;this.month=month;}
    public double getValue()
    {
        return value;
    }
    public int getMonth()
    {
        return month;
    }
}

class OutcomeLineItem
{
    private float value;
    private int month;

    public OutcomeLineItem(float value,int month){this.value=value;this.month=month;}
    public double getValue()
    {
        return value;
    }
    public int getMonth()
    {
        return month;
    }
}

class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue1;
    private TextView tvValue2;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat("0.00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.markview_layout);
        this.xAxisValueFormatter = xAxisValueFormatter;

        tvDate = findViewById(R.id.tv_date);
        tvValue1 = findViewById(R.id.tv_value1);
        tvValue2 = findViewById(R.id.tv_value2);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                if (i == 0) {
                    tvValue1.setText(dataSet.getLabel() + "：" + df.format(y));
                }
                if (i == 1) {
                    tvValue2.setText(dataSet.getLabel() + "：" + df.format(y));
                }
            }
            tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        MPPointF mpPointF = new MPPointF(-(getWidth() / 2), -getHeight());
        return mpPointF;
    }
}
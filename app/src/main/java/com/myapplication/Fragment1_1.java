package com.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment1_1 extends Fragment {

    private View mView;
    private List<TrendListItem> trendList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_1, container, false);
        }
        initTrentList();
        ListView lview = (ListView) mView.findViewById(R.id.TrendList);
        /*lview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                ListView lview = (ListView) mView.findViewById(R.id.TrendList);
                // TODO Auto-generated method stub
                lview.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        TrendListAdapter adapter = new TrendListAdapter(getActivity(), R.layout.trendlist_item, trendList);
        lview.setAdapter(adapter);
        return mView;
    }

    private void initTrentList() {
        for (int i = 0; i < 2; i++) {
            TrendListItem t1 = new TrendListItem("1月", "12345.99", "1234.00", "1234.00");
            trendList.add(t1);
            TrendListItem t2 = new TrendListItem("2月", "123451.00", "123451.00", "123451.00");
            trendList.add(t2);
            TrendListItem t3 = new TrendListItem("3月", "123451.00", "123451.00", "123451.00");
            trendList.add(t3);
            TrendListItem t4 = new TrendListItem("4月", "123451.00", "123451.00", "123451.00");
            trendList.add(t4);
        }
        TrendListItem t4 = new TrendListItem("4月", "123451.00", "123451.00", "123451.00");
        trendList.add(t4);
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
        public TrendListAdapter(Context context, int textid, List<TrendListItem>objects){
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

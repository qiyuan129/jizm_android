package com.myapplication;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
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

public class Fragment1_2 extends Fragment
{

    private View mView;
    private List<CategoryListItem> categoryList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment1_2, container, false);
        }
        initCategoryList();
        ListView lview = (ListView) mView.findViewById(R.id.CategoryList);
        /*lview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                ListView lview = (ListView) mView.findViewById(R.id.CategoryList);
                // TODO Auto-generated method stub
                lview.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), R.layout.categorylist_item, categoryList);
        lview.setAdapter(adapter);
        return mView;
    }

    private void initCategoryList() {
        int i;
        for (i = 0; i < 5; i++) {
            CategoryListItem c1 = new CategoryListItem(String.valueOf(i*2+1), "购物", "1234.00", "2020-2-7");
            categoryList.add(c1);
            CategoryListItem c2 = new CategoryListItem(String.valueOf(i*2+2), "饮食", "123451.00", "2020-2-7");
            categoryList.add(c2);
        }
        CategoryListItem c2 = new CategoryListItem(String.valueOf(i*2+2), "饮食", "123451.00", "2020-2-7");
        categoryList.add(c2);
    }
}


class CategoryListItem
{
    private String order;
    private String categoryname;
    private String money;
    private String date;

    public CategoryListItem(String order,String categoryname,String money,String date)
    {
        this.order=order;
        this.categoryname=categoryname;
        this.money=money;
        this.date=date;
    }

    public String getOrder() {
        return order;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public String getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }
}

class CategoryListAdapter extends ArrayAdapter<CategoryListItem>
{
    private int id;
    public CategoryListAdapter(Context context, int textid, List<CategoryListItem>objects){
        super(context,textid,objects);
        id=textid;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        CategoryListItem categoryListItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(id,parent,false);
        TextView month = (TextView) view.findViewById(R.id.order);
        TextView income = (TextView) view.findViewById(R.id.categoryname);
        TextView outcome = (TextView) view.findViewById(R.id.money);
        TextView balance = (TextView) view.findViewById(R.id.date);
        month.setText(categoryListItem.getOrder());
        income.setText(categoryListItem.getCategoryname());
        outcome.setText(categoryListItem.getMoney());
        balance.setText(categoryListItem.getDate());
        return view;
    }
}
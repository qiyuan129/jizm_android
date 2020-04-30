package com.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment3 extends Fragment implements View.OnClickListener{

    private View mView;
    private Button addPeriodic;
    private Button searchPeriodic;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment3,container,false);
        }

        addPeriodic =mView.findViewById(R.id.add_periodic);
        searchPeriodic = mView.findViewById(R.id.search_periodic);

        //按钮添加事件监听
        addPeriodic.setOnClickListener(this);
        searchPeriodic.setOnClickListener(this);


        return mView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_periodic:
                Log.d("add_periodic","添加事件");

                break;
            case R.id.search_periodic:
                Log.d("search_periodic","查询事件");
                break;


                default:break;
        }


    }










}

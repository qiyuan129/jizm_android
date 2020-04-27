package com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import util.MyDatabaseHelper;

public class Fragment1 extends Fragment {
    private View mView;


    private MyDatabaseHelper dbHelper;
    private Button ceshi;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment1,container,false);
        }


        dbHelper=new MyDatabaseHelper(getActivity(),"JiZM",null,1);


//        ceshi=(Button) mView.findViewById(R.id.ceshi);
//        ceshi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.getWritableDatabase();
//            }
//        });
        dbHelper.getWritableDatabase();



        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ceshi=(Button) getView().findViewById(R.id.ceshi);





    }
}

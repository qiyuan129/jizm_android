package com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment2 extends Fragment {

    private View mView;
    private Button incomeTv;    //收入按钮
    private Button outcomeTv;   //支出按钮

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment2,container,false);
        }

        incomeTv=(Button) mView.findViewById(R.id.income_tv);
        incomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //支出按钮监听器
                Log.d("Fragment2","支出");
            }
        });

        outcomeTv = (Button) mView.findViewById(R.id.outcome_tv);
        outcomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收入按钮监听器
                Log.d("Fragment2","收入");
            }
        });

        return mView;
    }

}

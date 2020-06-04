package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class Fragment5  extends Fragment {

    private View mView;

    SuperTextView user;
    SuperTextView userAccount;
    SuperTextView userRecover;
    SuperTextView userUpdate;
    SuperTextView userExport;
    SuperTextView userSetting;
    SuperTextView userAbout;
    SuperButton logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment5,container,false);
        }

        user=(SuperTextView)mView.findViewById(R.id.user_head) ;
        userAccount=(SuperTextView)mView.findViewById(R.id.user_account) ;
        userRecover=(SuperTextView)mView.findViewById(R.id.user_recover) ;
        userUpdate=(SuperTextView)mView.findViewById(R.id.user_update) ;
        userExport=(SuperTextView)mView.findViewById(R.id.user_export) ;
        userSetting=(SuperTextView)mView.findViewById(R.id.user_setting) ;
        userAbout=(SuperTextView)mView.findViewById(R.id.user_about) ;
        logout=(SuperButton)mView.findViewById(R.id.btn_logout) ;

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalDataEditActivity2.class);
                startActivity(intent);
            }
        });

        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountEditActivity.class);
                startActivity(intent);
            }
        });

        userRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"恢复数据",Toast.LENGTH_SHORT).show();

            }
        });

        userUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"更新数据",Toast.LENGTH_SHORT).show();

            }
        });

        userExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"导出账单",Toast.LENGTH_SHORT).show();

            }
        });

        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSettingActivity2.class);
                startActivity(intent);
            }
        });

        userAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"关于我们",Toast.LENGTH_SHORT).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"退出登录",Toast.LENGTH_SHORT).show();

            }
        });











        return mView;
    }







}

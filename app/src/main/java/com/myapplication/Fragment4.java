package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class Fragment4 extends Fragment implements View.OnClickListener{

    private View mView;

    ImageView imageViewb;
    ImageView imageViewh;
    ImageView personalDateEnter;
    ImageView userAccount;
    ImageView settingEnter;
    TextView userName;
    TextView userTel;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment4,container,false);
        }

        //初始化页面组件数据
        initInfomation();

        //设置事件监听
        setActionListener();



        return mView;
    }





    private void initInfomation(){
        imageViewb=(ImageView)mView.findViewById(R.id.h_back) ;
        imageViewh=(ImageView)mView.findViewById(R.id.h_head) ;
        personalDateEnter =(ImageView)mView.findViewById(R.id.personal_enter);
        userAccount =(ImageView)mView.findViewById(R.id.user_account);
        settingEnter =(ImageView) mView.findViewById(R.id.setting_enter);



        userName=(TextView)mView.findViewById(R.id.user_name);
        userTel=(TextView)mView.findViewById(R.id.user_val);



        Glide.with(getActivity()).
                load(R.drawable.user_back).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).
                into(imageViewb);

        Glide.with(getActivity()).load(R.drawable.user_head)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(imageViewh);

        userName.setText("乔大");
        userTel.setText("132546785");




    }


    public void setActionListener(){
        imageViewh.setOnClickListener(this);
        personalDateEnter.setOnClickListener(this);
        userAccount.setOnClickListener(this);
        settingEnter.setOnClickListener(this);
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.h_head:

                break;


            case R.id.personal_enter:
                Intent intent1 = new Intent(getActivity(),PersonalDataEditActivity.class);
                startActivity(intent1);
                break;

            case R.id.user_account:
                Intent intent2 = new Intent(getActivity(),AccountEditActivity.class);
                startActivity(intent2);
                break;

            case R.id.setting_enter:
                Intent intent3 = new Intent(getActivity(),EditSettingActivity.class);
                startActivity(intent3);
                break;


                default:
                    break;

        }
    }










}

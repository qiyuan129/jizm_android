package com.myapplication;

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


public class Fragment4 extends Fragment {

    private View mView;

    ImageView imageViewb;
    ImageView imageViewh;
    TextView userName;
    TextView userTel;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment4,container,false);
        }

        initInfomation();
        return mView;
    }

    private void initInfomation(){
        imageViewb=(ImageView)mView.findViewById(R.id.h_back) ;
        imageViewh=(ImageView)mView.findViewById(R.id.h_head) ;
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




}

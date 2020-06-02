package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditSettingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView goBack;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setting);

        init();


        setActionListener();

    }



    public void init(){
        goBack = (ImageView)findViewById(R.id.setting_go_back);


    }



    public void setActionListener(){
        goBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_go_back:
                this.finish();
                break;

                default:
                    break;
        }
    }
}

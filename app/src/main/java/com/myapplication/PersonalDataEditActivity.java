package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PersonalDataEditActivity extends AppCompatActivity implements View.OnClickListener{

    Button cancelEdit;
    Button updatePersonalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_edit);

        init();
        setListener();


    }


    public void setListener(){
        cancelEdit.setOnClickListener(this);
        updatePersonalData.setOnClickListener(this);
    }

    public void init(){
        cancelEdit = (Button)findViewById(R.id.cancel_edit);
        updatePersonalData =(Button)findViewById(R.id.update_personal_data);




    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.cancel_edit:
                this.finish();//退出这个页面
                break;

            case R.id.update_personal_data:
                Toast.makeText(this,"保存用户信息还未实现",Toast.LENGTH_SHORT).show();
               // Toast.makeText(getActivity(), "点击删除的是第" + position + "项", Toast.LENGTH_SHORT).show();
                break;


                default:
                    break;

        }

    }
}

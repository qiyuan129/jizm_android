package com.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import pojo.Category;

/**
 * 账单分类编辑Activity
 */

public class TypeEditActivity extends AppCompatActivity implements View.OnClickListener {
    private View mView;
    private Context mContext;

    private List<Category> categoryList = new ArrayList<>();

    private RecyclerView mRecycleView;
    private TextView incomeTv;   //收入按钮
    private TextView outcomeTv;  //支出按钮
    private ImageView backIv;      //返回键
    private ImageView addIv;      //添加按钮

    public boolean isOutcome = true;

    private String[] category_outcome = {"餐饮美食", "服饰美容", "生活日用", "充值缴费",
            "交通出行", "通讯物流", "休闲娱乐", "医疗保健", "住房物业", "文体教育",
            "酒店旅行", "爱车养车", "其他"};

    private String[] category_income = {"投资理财", "经营所得", "奖金红包", "工资", "生活费"};

    private int category_id = 1;
    private int user_id = 1;
    private int type = 1;
    private int state = 1;
    private long anchor = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_type);

        incomeTv = (TextView) findViewById(R.id.tb_note_income);
        incomeTv.setOnClickListener(this);

        outcomeTv = (TextView) findViewById(R.id.tb_note_outcome);
        outcomeTv.setOnClickListener(this);

        backIv = (ImageView) findViewById(R.id.back_btn);
        backIv.setOnClickListener(this);

        addIv = (ImageView) findViewById(R.id.add_btn);
        addIv.setOnClickListener(this);

        initCategory();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.add_btn:
                showContentDialog();
                break;
            case R.id.tb_note_income:
                isOutcome = false;
                initCategory();
                break;
            case R.id.tb_note_outcome:
                isOutcome = true;
                initCategory();
                break;
        }
    }

    //显示新增分类输入框
    public void showContentDialog() {
        new MaterialDialog.Builder(this)
                .title("添加分类")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("分类名称", null, (dialog, input) -> {
                    if (input.equals("")) {
                        Toast.makeText(mContext, "内容不能为空！" + input,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //添加新分类
//                        BSort sort = new BSort(null, input.toString(), "sort_tianjiade.png",mDatas.size(),0, !isOutcome);
//                        mPresenter.addBSort(sort);
//                        mDatas.add(sort);
                        Log.d("TypeEditActivity","添加新分类");
                    }
                })
                .positiveText("确定")
                .show();
    }

    private void initCategory() {
        if (categoryList != null) {
            categoryList.clear();
            if (isOutcome) {
                for(int i = 0; i < category_outcome.length; i++){
                    Category category = new Category(category_id,user_id, category_outcome[i],type,state,anchor);
                    categoryList.add(category);
                }
            } else {
                for(int i = 0; i < category_income.length; i++){
                    Category category = new Category(category_id,user_id, category_income[i],type,state,anchor);
                    categoryList.add(category);
                }
            }
            mRecycleView = (RecyclerView) findViewById(R.id.edit_category_recycleview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecycleView.setLayoutManager(layoutManager);
            CategoryAdapter adapter = new CategoryAdapter(categoryList);
            adapter.notifyDataSetChanged();
            mRecycleView.setAdapter(adapter);
        } else {
            if (isOutcome) {
                for(int i = 0; i < category_outcome.length; i++){
                    Category category = new Category(category_id,user_id, category_outcome[i],type,state,anchor);
                    categoryList.add(category);
                }
            } else {
                for(int i = 0; i < category_income.length; i++){
                    Category category = new Category(category_id,user_id, category_income[i],type,state,anchor);
                    categoryList.add(category);
                }
            }
            mRecycleView = (RecyclerView) findViewById(R.id.edit_category_recycleview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecycleView.setLayoutManager(layoutManager);
            CategoryAdapter adapter = new CategoryAdapter(categoryList);
            adapter.notifyDataSetChanged();
            mRecycleView.setAdapter(adapter);
        }
    }
}

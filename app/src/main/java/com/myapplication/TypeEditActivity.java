package com.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 账单分类编辑Activity
 */

public class TypeEditActivity extends AppCompatActivity implements View.OnClickListener {
    private View mView;
    private Context mContext;

//    private RecyclerView mRecycleView;
    private TextView incomeTv;   //收入按钮
    private TextView outcomeTv;  //支出按钮
    private ImageView backIv;      //返回键
    private ImageView addIv;      //添加按钮

    public boolean isOutcome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_type);

//        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecycleView.setOnClickListener(this);

        incomeTv = (TextView) findViewById(R.id.tb_note_income);
        incomeTv.setOnClickListener(this);

        outcomeTv = (TextView) findViewById(R.id.tb_note_outcome);
        outcomeTv.setOnClickListener(this);

        backIv = (ImageView) findViewById(R.id.back_btn);
        backIv.setOnClickListener(this);

        addIv = (ImageView) findViewById(R.id.add_btn);
        addIv.setOnClickListener(this);
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
                break;
            case R.id.tb_note_outcome:
                isOutcome = true;
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
}

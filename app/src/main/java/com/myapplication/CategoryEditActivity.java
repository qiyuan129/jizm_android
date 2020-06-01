package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import pojo.Category;

/**
 * 账单分类编辑Activity
 */

public class CategoryEditActivity extends AppCompatActivity implements View.OnClickListener {

    public CategoryChooseAdapter categoryChooseAdapter;
    private List<Category> categoryList = new ArrayList<>();

    public RecyclerView recycleView;
    private TextView incomeTv;   //收入按钮
    private TextView outcomeTv;  //支出按钮
    private ImageView backIv;      //返回键
    private ImageView addIv;      //添加按钮

    private String edit_category_name = "";

    private int category_id = 1;
    private int user_id = 1;
    private int isIncome = 0;
    private int state = 0;
    private Date anchor = new Date(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_type);

        incomeTv = (TextView) findViewById(R.id.tb_note_income);
        incomeTv.setOnClickListener(this);

        outcomeTv = (TextView) findViewById(R.id.tb_note_outcome);
        outcomeTv.setOnClickListener(this);

        outcomeTv.setSelected(true);

        backIv = (ImageView) findViewById(R.id.back_btn);
        backIv.setOnClickListener(this);

        addIv = (ImageView) findViewById(R.id.add_btn);
        addIv.setOnClickListener(this);

        recycleView = (RecyclerView) findViewById(R.id.edit_category_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

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
                isIncome = 1;
                initCategory();
                incomeTv.setSelected(true);
                outcomeTv.setSelected(false);
                break;
            case R.id.tb_note_outcome:
                isIncome = 0;
                initCategory();
                incomeTv.setSelected(false);
                outcomeTv.setSelected(true);
                break;
        }
    }

    //显示新增分类输入框
    public void showContentDialog() {
        new MaterialDialog.Builder(this)
                .title("添加分类")
                .canceledOnTouchOutside(false)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("分类名称", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (TextUtils.isEmpty(dialog.getInputEditText().getText().toString())) {
                            Toast.makeText(CategoryEditActivity.this, "您没有输入分类名称", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else {
                            Category category = new Category(1, 1, dialog.getInputEditText().getText().toString(), isIncome, state, anchor);
                            CategoryDAO categoryDAO = new CategoryDAOImpl();
                            categoryDAO.insertCategory(category);
                            initCategory();
                            Toast.makeText(CategoryEditActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialog = null;
                    }
                })
                .show();
    }

    public void setCategoryChooseAdapter () {
        categoryChooseAdapter.setOnItemClickListener(new CategoryChooseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                CategoryDAO categoryDAO = new CategoryDAOImpl();
                categoryList = categoryDAO.listCategory();
                List<String> outcome_category_name1 = new ArrayList<>();
                List<String> income_category_name1 = new ArrayList<>();
                List<Integer> outcome_category_id1 = new ArrayList<>();
                List<Integer> income_category_id1 = new ArrayList<>();
                String category_name1;

                for (int j = 0; j < categoryList.size(); j++) {
                    Category category = (Category)categoryList.get(j);
                    if (category.getType() == 0) {
                        outcome_category_name1.add(category.getCategory_name());
                        outcome_category_id1.add(category.getCategory_id());
                    } else {
                        income_category_name1.add(category.getCategory_name());
                        income_category_id1.add(category.getCategory_id());
                    }
                }

                if (isIncome == 0) {
                    category_id = outcome_category_id1.get(position);
                    category_name1 = outcome_category_name1.get(position);
                } else {
                    category_id = income_category_id1.get(position);
                    category_name1 = income_category_name1.get(position);
                }
                new MaterialDialog.Builder(CategoryEditActivity.this)
                        .title("修改分类")
                        .canceledOnTouchOutside(false)
                        .input(category_name1, "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            }
                        })
                        .positiveText("确认修改")
                        .negativeText("取消")
                        .neutralText("删除该分类")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (TextUtils.isEmpty(dialog.getInputEditText().getText().toString())) {
                                    Toast.makeText(CategoryEditActivity.this, "您没有输入分类名称", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    dialog = null;
                                    return;
                                } else {
                                    //修改分类名称
                                    edit_category_name = dialog.getInputEditText().getText().toString();
                                    Category category = new Category(category_id, user_id, edit_category_name, isIncome, state, anchor);
                                    CategoryDAO categoryDAO = new CategoryDAOImpl();
                                    categoryDAO.updateCategory(category);
                                    edit_category_name = "";
                                    initCategory();
                                    Toast.makeText(CategoryEditActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //删除该分类
                                CategoryDAO categoryDAO = new CategoryDAOImpl();
                                categoryDAO.deleteCategory(category_id);
                                initCategory();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //取消
                                dialog.dismiss();
                                dialog = null;
                            }
                        })
                        .show();
            }
        });
    }


    private void initCategory() {
        //取出分类名称、id
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        categoryList = categoryDAO.listCategory();
        List<String> outcome_category = new ArrayList<>();
        List<String> income_category = new ArrayList<>();
        List<Integer> outcome_category_id = new ArrayList<>();
        List<Integer> income_category_id = new ArrayList<>();

        if (outcome_category_id != null) {
            outcome_category_id.clear();
        }
        if (income_category_id != null) {
            income_category_id.clear();
        }

        for (int j = 0; j < categoryList.size(); j++) {
            Category category = (Category)categoryList.get(j);
            if (category.getType() == 0) {
                outcome_category.add(category.getCategory_name());
                outcome_category_id.add(category.getCategory_id());
            } else {
                income_category.add(category.getCategory_name());
                income_category_id.add(category.getCategory_id());
            }
        }

        if (categoryList != null) {
            categoryList.clear();
            if (isIncome == 0) {
                for(int i = 0; i < outcome_category.size(); i++){
                    category_id = outcome_category_id.get(i);
                    Category category = new Category(category_id,user_id, outcome_category.get(i),isIncome,state,anchor);
                    categoryList.add(category);
                }
            } else {
                for(int i = 0; i < income_category.size(); i++){
                    category_id = income_category_id.get(i);
                    Category category = new Category(category_id,user_id, income_category.get(i),isIncome,state,anchor);
                    categoryList.add(category);
                }
            }
        } else {
            if (isIncome == 0) {
                for(int i = 0; i < outcome_category.size(); i++){
                    category_id = outcome_category_id.get(i);
                    Category category = new Category(category_id,user_id, outcome_category.get(i),isIncome,state,anchor);
                    categoryList.add(category);
                }
            } else {
                for(int i = 0; i < income_category.size(); i++){
                    category_id = income_category_id.get(i);
                    Category category = new Category(category_id,user_id, income_category.get(i),isIncome,state,anchor);
                    categoryList.add(category);
                }
            }
        }
        categoryChooseAdapter = new CategoryChooseAdapter(categoryList);
        categoryChooseAdapter.notifyItemRangeChanged(0, categoryList.size());
        recycleView.setAdapter(categoryChooseAdapter);

        setCategoryChooseAdapter();
    }
}
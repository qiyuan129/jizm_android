package com.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import pojo.Account;
import pojo.Category;

/**
 * 账户编辑Activity
 */

public class AccountEditActivity extends AppCompatActivity implements View.OnClickListener {

    public AccountAdapter accountAdapter;
    private List<Account> accountList = new ArrayList<>();

    public RecyclerView recycleView;

    private ImageView backIv;      //返回键
    private ImageView addIv;      //添加按钮

    private String edit_account_name = "";

    private int account_id = 1;
    private int user_id = 1;
    private Date anchor = new Date(0);
    //状态
    private int addState = 0;  //本地新增
    private int deleteState = -1;  //标记删除
    private int updateState = 1;  //本地更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        backIv = (ImageView) findViewById(R.id.back_btn1);
        backIv.setOnClickListener(this);

        addIv = (ImageView) findViewById(R.id.add_btn1);
        addIv.setOnClickListener(this);

        recycleView = (RecyclerView) findViewById(R.id.edit_account_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initAccount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        setResult(RESULT_OK, new Intent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn1:
                finish();
                setResult(RESULT_OK, new Intent());
                break;
            case R.id.add_btn1:
                showContentDialog();
                break;
        }
    }

    //显示新增账户输入框
    public void showContentDialog() {
        LayoutInflater factory = LayoutInflater.from(AccountEditActivity.this);
        final View textEntryView = factory.inflate(R.layout.account_edit_dialog, null);
        final EditText editAccountName = (EditText) textEntryView.findViewById(R.id.editAccountName);
        final EditText editAccountMoney = (EditText)textEntryView.findViewById(R.id.editAccountMoney);

        new AlertDialog.Builder(this)
                .setTitle("添加账户")
                .setCancelable(false)
                .setView(textEntryView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(editAccountName.getText().toString())) {
                            Toast.makeText(AccountEditActivity.this, "您没有输入账户名称", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else if (TextUtils.isEmpty(editAccountMoney.getText().toString())) {
                            Toast.makeText(AccountEditActivity.this, "您没有输入账户金额", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else {
                            //新增账户
                            Account account = new Account(account_id, user_id, editAccountName.getText().toString(), Double.valueOf(editAccountMoney.getText().toString()), addState, anchor);
                            AccountDAO accountDAO = new AccountDAOImpl();
                            accountDAO.insertAccount(account);
                            initAccount();
                            Toast.makeText(AccountEditActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                        dialog = null;
                    }
                })
                .show();
    }

    public void setAccountAdapter () {
        accountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                AccountDAO accountDAO = new AccountDAOImpl();
                accountList = accountDAO.listAccount();
                List<String> account_name_list = new ArrayList<>();
                List<Integer> account_id_list = new ArrayList<>();
                List<Double> account_money_list = new ArrayList<>();
                String account_name1;
                String account_money1;

                for (int j = 0; j < accountList.size(); j++) {
                    Account account = (Account) accountList.get(j);
                    account_name_list.add(account.getAccount_name());
                    account_id_list.add(account.getAccount_id());
                    account_money_list.add(account.getMoney());
                }
                account_id = account_id_list.get(position);
                account_name1 = account_name_list.get(position);
                account_money1 =String.valueOf(account_money_list.get(position));

                LayoutInflater factory = LayoutInflater.from(AccountEditActivity.this);
                final View textEntryView = factory.inflate(R.layout.account_edit_dialog, null);
                final EditText editAccountName = (EditText) textEntryView.findViewById(R.id.editAccountName);
                final EditText editAccountMoney = (EditText)textEntryView.findViewById(R.id.editAccountMoney);
                editAccountName.setText(account_name1);
                editAccountMoney.setText(account_money1);

                new AlertDialog.Builder(AccountEditActivity.this)
                        .setTitle("修改账户")
                        .setView(textEntryView)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(editAccountName.getText().toString())) {
                                    Toast.makeText(AccountEditActivity.this, "您没有输入账户名称", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    dialog = null;
                                    return;
                                } else if (TextUtils.isEmpty(editAccountMoney.getText().toString())) {
                                    Toast.makeText(AccountEditActivity.this, "您没有输入账户金额", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    dialog = null;
                                    return;
                                } else {
                                    //修改账户
                                    Account account = new Account(account_id, user_id, editAccountName.getText().toString(), Double.valueOf(editAccountMoney.getText().toString()), updateState, anchor);
                                    AccountDAO accountDAO = new AccountDAOImpl();
                                    accountDAO.updateAccount(account);
                                    initAccount();
                                    Toast.makeText(AccountEditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消
                                dialog.dismiss();
                                dialog = null;
                            }
                        })
                        .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除该分类
                                AccountDAO accountDAO = new AccountDAOImpl();
                                accountDAO.deleteAccount(account_id);
                                Toast.makeText(AccountEditActivity.this, "该分类已删除", Toast.LENGTH_SHORT).show();
                                initAccount();
                            }
                        })
                        .show();
            }
        });
    }


    private void initAccount() {
        //取出账户名称、id、钱数
        AccountDAO accountDAO = new AccountDAOImpl();
        accountList = accountDAO.listAccount();
        List<String> account_name_list1 = new ArrayList<>();
        List<Integer> account_id_list1 = new ArrayList<>();
        List<Double> account_money_list1 = new ArrayList<>();

        if (account_id_list1 != null) {
            account_id_list1.clear();
        }

        for (int j = 0; j < accountList.size(); j++) {
            Account account = (Account) accountList.get(j);
            account_name_list1.add(account.getAccount_name());
            account_id_list1.add(account.getAccount_id());
            account_money_list1.add(account.getMoney());
        }

        if (accountList != null) {
            accountList.clear();
            for(int i = 0; i < account_name_list1.size(); i++){
                account_id = account_id_list1.get(i);
                Account account = new Account(account_id,user_id, account_name_list1.get(i),account_money_list1.get(i),updateState,anchor);
                accountList.add(account);
            }

        } else {
            for(int i = 0; i < account_name_list1.size(); i++){
                account_id = account_id_list1.get(i);
                Account account = new Account(account_id,user_id, account_name_list1.get(i),account_money_list1.get(i),updateState,anchor);
                accountList.add(account);
            }

        }
        accountAdapter = new AccountAdapter(accountList);
        accountAdapter.notifyItemRangeChanged(0, accountList.size());
        recycleView.setAdapter(accountAdapter);

        setAccountAdapter();
    }
}

package com.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.BillDAO;
import dao.BillDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import pojo.Account;
import pojo.Bill;
import pojo.Category;

public class Fragment2 extends Fragment implements View.OnClickListener{

    private View mView;
    private CategoryChooseAdapter categoryChooseAdapter;

    private List<Category> categoryList = new ArrayList<>();
    private List<Account> accountList = new ArrayList<>();

    //属性
    private int category_id = 1;
    private int user_id = 1;
    private int bill_id = 1;
    private int account_id = 1;
    private Date bill_date = new Date();
    private Date anchor= new Date(0);
    public int isIncome = 0;         //记录类别（收入/支出）
    //状态
    private int addState = 0;  //本地新增
    private int deleteState = -1;  //标记删除
    private int updateState = 1;  //本地更新

    private RecyclerView recyclerView;
    private Button incomeTv;        //收入按钮
    private Button outcomeTv;       //支出按钮
    private TextView edittypeTv;    //编辑类别
    private TextView sortTv;        //显示选择的分类
    private TextView moneyTv;       //金额
    private TextView dateTv;        //日期选择
    private TextView cashTv;        //支出账户
    private TextView remarkTv;      //账单名称
    private ImageView clear;        //清空金额
    private ImageView remarkIv;     //账单名称
    private RelativeLayout delect;  //数字键盘回格键

    //数字键盘
    private TextView num1;
    private TextView num2;
    private TextView num3;
    private TextView num4;
    private TextView num5;
    private TextView num6;
    private TextView num7;
    private TextView num8;
    private TextView num9;
    private TextView num0;
    private TextView dot;        //小数点
    private TextView done;       //确认

    //时间选择器
    protected String days;
    protected int mYear;
    protected int mMonth;
    protected int mDays;

    //备注
    protected String remarkInput = "";

    //数字键盘
    protected boolean isDot;
    protected String num = "0";               //整数部分
    protected String dotNum = ".00";         //小数部分
    protected final int MAX_NUM = 9999999;   //最大整数
    protected final int DOT_NUM = 2;         //小数点后最大位数
    protected int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment2,container,false);
        }

        incomeTv=(Button) mView.findViewById(R.id.income_tv);
        incomeTv.setOnClickListener(this);

        outcomeTv = (Button) mView.findViewById(R.id.outcome_tv);
        outcomeTv.setOnClickListener(this);

        outcomeTv.setSelected(true);
        incomeTv.setSelected(false);

        edittypeTv = (TextView) mView.findViewById(R.id.type_edit);
        edittypeTv.setOnClickListener(this);

        sortTv = (TextView) mView.findViewById(R.id.item_tb_type_tv);
        sortTv.setText("");

        recyclerView = (RecyclerView) mView.findViewById(R.id.category_recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(),4);
        recyclerView.setLayoutManager(layoutManager);

        initsortTv();
        initCategory();

        moneyTv = (TextView) mView.findViewById(R.id.tb_note_money);
        moneyTv.setText(num+dotNum);

        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);

        dateTv = (TextView) mView.findViewById(R.id.tb_note_date);
        days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDays).toString();
        dateTv.setText(days);
        dateTv.setOnClickListener(this);

        cashTv = (TextView) mView.findViewById(R.id.tb_note_cash);
        cashTv.setOnClickListener(this);
        cashTv.setText("");
        initcashTv();

        remarkTv = (TextView) mView.findViewById(R.id.tb_note_text);
        remarkTv.setOnClickListener(this);

        remarkIv = (ImageView) mView.findViewById(R.id.tb_note_remark);
        remarkIv.setOnClickListener(this);

        num1 = (TextView) mView.findViewById(R.id.tb_calc_num_1);
        num1.setOnClickListener(this);
        num2 = (TextView) mView.findViewById(R.id.tb_calc_num_2);
        num2.setOnClickListener(this);
        num3 = (TextView) mView.findViewById(R.id.tb_calc_num_3);
        num3.setOnClickListener(this);
        num4 = (TextView) mView.findViewById(R.id.tb_calc_num_4);
        num4.setOnClickListener(this);
        num5 = (TextView) mView.findViewById(R.id.tb_calc_num_5);
        num5.setOnClickListener(this);
        num6 = (TextView) mView.findViewById(R.id.tb_calc_num_6);
        num6.setOnClickListener(this);
        num7 = (TextView) mView.findViewById(R.id.tb_calc_num_7);
        num7.setOnClickListener(this);
        num8 = (TextView) mView.findViewById(R.id.tb_calc_num_8);
        num8.setOnClickListener(this);
        num9 = (TextView) mView.findViewById(R.id.tb_calc_num_9);
        num9.setOnClickListener(this);
        num0 = (TextView) mView.findViewById(R.id.tb_calc_num_0);
        num0.setOnClickListener(this);
        dot = (TextView) mView.findViewById(R.id.tb_calc_num_dot);
        dot.setOnClickListener(this);
        done = (TextView) mView.findViewById(R.id.tb_calc_num_done);
        done.setOnClickListener(this);

        clear = (ImageView) mView.findViewById(R.id.tb_note_clear);
        clear.setOnClickListener(this);

        delect = (RelativeLayout) mView.findViewById(R.id.tb_calc_num_del);
        delect.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            initCategory();
            initsortTv();
            initcashTv();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.income_tv:  //收入按钮
                isIncome = 1;
                initsortTv();
                initCategory();
                incomeTv.setSelected(true);
                outcomeTv.setSelected(false);
                break;
            case R.id.outcome_tv:  //支出按钮
                isIncome = 0;
                initsortTv();
                initCategory();
                outcomeTv.setSelected(true);
                incomeTv.setSelected(false);
                break;
            case R.id.type_edit:  //编辑分类
                Intent intent = new Intent(getActivity(), CategoryEditActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tb_note_cash:  //选择账户按钮
                showPayAccount();
                break;
            case R.id.tb_note_date:  //选择日期按钮
                showDateSelector();
                break;
            case R.id.tb_note_remark:  //设置账单名称
                showContentDialog();
                break;
            case R.id.tb_note_text:  //设置账单名称
                showContentDialog();
                break;
            case R.id.tb_calc_num_done:  //确定按钮
                doCommit();
                break;
            case R.id.tb_calc_num_1:  //以下数字键盘按钮
                calMoney(1);
                break;
            case R.id.tb_calc_num_2:
                calMoney(2);
                break;
            case R.id.tb_calc_num_3:
                calMoney(3);
                break;
            case R.id.tb_calc_num_4:
                calMoney(4);
                break;
            case R.id.tb_calc_num_5:
                calMoney(5);
                break;
            case R.id.tb_calc_num_6:
                calMoney(6);
                break;
            case R.id.tb_calc_num_7:
                calMoney(7);
                break;
            case R.id.tb_calc_num_8:
                calMoney(8);
                break;
            case R.id.tb_calc_num_9:
                calMoney(9);
                break;
            case R.id.tb_calc_num_0:
                calMoney(0);
                break;
            case R.id.tb_calc_num_dot:
                if (dotNum.equals(".00")) {
                    isDot = true;
                    dotNum =".";
                }
                moneyTv.setText(num + dotNum);
                Log.d("Fragemnt",".");
                break;
            case R.id.tb_note_clear:  //清空按钮
                doClear();
                break;
            case R.id.tb_calc_num_del:  //数字键盘回格按钮
                doDelect();
                break;
        }
    }

    //显示支付账户选择器
    public void showPayAccount() {
        //获取账户名称
        AccountDAO accountDAO = new AccountDAOImpl();
        accountList=accountDAO.listAccount();
        List<String> accoutNameList = new ArrayList<>();
        List<Integer> accountIdList = new ArrayList<>();
        if (accountList.size() != 0) {
            for (int j = 0; j < accountList.size(); j++) {
                Account account = (Account) accountList.get(j);
                if (account.getState() != -1) {
                    accoutNameList.add(account.getAccount_name());
                    accountIdList.add(account.getAccount_id());
                }
            }
        }

        new MaterialDialog.Builder(getActivity())
                .title("请选择支付账户")
                .titleGravity(GravityEnum.CENTER)
                .items(accoutNameList)
                .positiveText("确定")
                .negativeText("取消")
                .canceledOnTouchOutside(false)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        cashTv.setText(text);
                        if (accountIdList.size() != 0) {
                            account_id = accountIdList.get(which);
                        }
                        return true;
                    }
                })
                .show();
    }

    //显示日期选择器
    public void showDateSelector() {
        new DatePickerDialog(getActivity(),(DatePicker datePicker, int i, int i1, int i2) -> {
            mYear = i;
            mMonth = i1;
            mDays = i2;
            if (mMonth + 1 < 10) {
                if (mDays < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0")
                            .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0")
                            .append(mMonth + 1).append("-").append(mDays).toString();
                }
            } else{
                if (mDays < 10) {
                    days = new StringBuffer().append(mYear).append("-")
                            .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-")
                            .append(mMonth + 1).append("-").append(mDays).toString();
                }
            }
            dateTv.setText(days);
        },mYear,mMonth,mDays).show();
    }

    //显示账单名称输入框
    public void showContentDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("账单名称")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("账单名称", remarkInput, new MaterialDialog.InputCallback() {
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
                            Toast.makeText(getActivity(), "您没有输入账单名称", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else {
                            remarkInput = dialog.getInputEditText().getText().toString();
                            dialog.dismiss();
                            dialog = null;
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

    //计算金额
    protected void calMoney(int money) {
        if (num.equals("0") && money == 0)
            return;
        if (isDot) {
            if (count < DOT_NUM) {
                count++;
                dotNum += money;
                moneyTv.setText(num + dotNum);
            }
        } else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += money;
            moneyTv.setText(num + dotNum);
        }
    }

    //清空金额
    public void doClear() {
        num = "0";
        count = 0;
        dotNum = ".00";
        isDot = false;
        moneyTv.setText("0.00");
    }

    //数字键盘回格
    public void doDelect() {
        if (isDot) {
            if (count > 0) {
                dotNum = dotNum.substring(0, dotNum.length()-1);
                count--;
            }
            if (count == 0) {
                isDot = false;
                dotNum = ".00";
            }
            moneyTv.setText(num + dotNum);
        } else {
            if (num.length() > 0)
                num = num.substring(0, num.length()-1);
            if (num.length() == 0)
                num = "0";
            moneyTv.setText(num + dotNum);
        }
    }

    //确认按钮
    public void doCommit() {
        if ((num + dotNum).equals("0.00")) {
            Toast.makeText(getActivity(), "请输入账单金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sortTv.getText().toString() == "") {
            Toast.makeText(getActivity(), "请选择账单类别", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cashTv.getText().toString() == "") {
            Toast.makeText(getActivity(), "请选择账户", Toast.LENGTH_SHORT).show();
            return;
        }
        if (remarkInput == "") {
            remarkInput = String.valueOf(sortTv.getText());
        }

        String categorySelect = String.valueOf(sortTv.getText());
        String accountText = String.valueOf(cashTv.getText());

        String bill_name = remarkInput;
        Double bill_money = Double.valueOf(num + dotNum);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = String.valueOf(dateTv.getText());
            bill_date = sdf.parse(date);
        } catch (Exception e) {

        }

        Bill bill = new Bill(bill_id, account_id, category_id, user_id, isIncome, bill_name, bill_date, bill_money, addState, anchor);
        BillDAO billDAO = new BillDAOImpl();
        billDAO.insertBill(bill);

        Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();

        //页面数据清空
        num = "0";
        dotNum = ".00";
        moneyTv.setText("0.00");
        remarkInput = "";
        initsortTv();
        initCategory();
        initcashTv();
    }

    //初始化账单类别
    private void initCategory() {
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        categoryList = categoryDAO.listCategory();
        List<String> outcome_category = new ArrayList<>();
        List<String> income_category = new ArrayList<>();
        List<Integer> outcome_category_id = new ArrayList<>();
        List<Integer> income_category_id = new ArrayList<>();

        for (int j = 0; j < categoryList.size(); j++) {
            Category category = (Category)categoryList.get(j);
            if (category.getState() != -1) {
                if (category.getType() == 0) {
                    outcome_category.add(category.getCategory_name());
                    outcome_category_id.add(category.getCategory_id());
                } else {
                    income_category.add(category.getCategory_name());
                    income_category_id.add(category.getCategory_id());
                }
            }
        }

        if (categoryList != null) {
            categoryList.clear();
            if (isIncome == 0) {
                for(int i = 0; i < outcome_category.size(); i++){
                    category_id = outcome_category_id.get(i);
                    Category category = new Category(category_id,user_id, outcome_category.get(i),isIncome,updateState,anchor);
                    categoryList.add(category);
                    if (outcome_category_id.size() != 0) {
                        category_id = outcome_category_id.get(0);
                    }
                }
            } else {
                for(int i = 0; i < income_category.size(); i++){
                    category_id = income_category_id.get(i);
                    Category category = new Category(category_id,user_id, income_category.get(i),isIncome,updateState,anchor);
                    categoryList.add(category);
                    if (income_category_id.size() != 0) {
                        category_id = income_category_id.get(0);
                    }
                }
            }
        } else {
            if (isIncome == 0) {
                for(int i = 0; i < outcome_category.size(); i++){
                    category_id = outcome_category_id.get(i);
                    Category category = new Category(category_id,user_id, outcome_category.get(i),isIncome,updateState,anchor);
                    categoryList.add(category);
                    if (outcome_category_id.size() != 0) {
                        category_id = outcome_category_id.get(0);
                    }
                }
            } else {
                for(int i = 0; i < income_category.size(); i++){
                    category_id = income_category_id.get(i);
                    Category category = new Category(category_id,user_id, income_category.get(i),isIncome,updateState,anchor);
                    categoryList.add(category);
                    if (income_category_id.size() != 0) {
                        category_id = income_category_id.get(0);
                    }
                }
            }
        }
        categoryChooseAdapter = new CategoryChooseAdapter(categoryList);
        categoryChooseAdapter.notifyItemRangeChanged(0, categoryList.size());
        recyclerView.setAdapter(categoryChooseAdapter);

        categoryChooseAdapter.setOnItemClickListener(new CategoryChooseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Category category = categoryList.get(position);
                sortTv.setText(category.getCategory_name());
                category_id = category.getCategory_id();
            }
        });

    }

    //分类文本初始化
    private void initsortTv() {
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        categoryList = categoryDAO.listCategory();
        List<String> init_outcome_category_name = new ArrayList<>();
        List<String> init_income_category_name = new ArrayList<>();
        List<Integer> init_outcome_category_id = new ArrayList<>();
        List<Integer> init_income_category_id = new ArrayList<>();

        if (categoryList.size() != 0){
            for (int j = 0; j < categoryList.size(); j++) {
                Category category = (Category)categoryList.get(j);
                if (category.getState() != -1){
                    if (category.getType() == 0) {
                        init_outcome_category_name.add(category.getCategory_name());
                        init_outcome_category_id.add(category.getCategory_id());
                    } else {
                        init_income_category_name.add(category.getCategory_name());
                        init_income_category_id.add(category.getCategory_id());
                    }
                }
            }

            if (isIncome == 0 && init_outcome_category_name.size() != 0) {
                sortTv.setText(init_outcome_category_name.get(0));
                category_id = init_outcome_category_id.get(0);
            } else if (isIncome == 1 && init_income_category_name.size() != 0){
                sortTv.setText(init_income_category_name.get(0));
                category_id = init_income_category_id.get(0);
            } else if (isIncome == 0 && init_outcome_category_name.size() == 0) {
                sortTv.setText("");
            } else if (isIncome == 1 && init_income_category_name.size() == 0) {
                sortTv.setText("");
            }
        }
    }

    //账户文本初始化
    public void initcashTv () {
        AccountDAO accountDAO = new AccountDAOImpl();
        accountList = accountDAO.listAccount();
        List<String> init_account_name = new ArrayList<>();
        List<Integer> init_account_id = new ArrayList<>();

        if (accountList.size() != 0) {
            for (int i = 0; i < accountList.size(); i++) {
                Account account = (Account)accountList.get(i);
                if (account.getState() != -1) {
                    init_account_name.add(account.getAccount_name());
                    init_account_id.add(account.getAccount_id());
                }
                if (init_account_name.size() != 0) {
                    cashTv.setText(init_account_name.get(0));
                    account_id = init_account_id.get(0);
                }
            }
        }
    }
}

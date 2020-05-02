package com.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.Categorytest;

public class Fragment2 extends Fragment implements View.OnClickListener{

    private View mView;

    private List<Categorytest> categorytestList = new ArrayList<>();

    private String[] category_outcome = {"餐饮美食", "服饰美容", "生活日用", "充值缴费",
            "交通出行", "通讯物流", "休闲娱乐", "医疗保健", "住房物业", "文体教育",
            "酒店旅行", "爱车养车", "其他"};

    private String[] category_income = {"投资理财", "经营所得", "奖金红包", "工资", "生活费"};

    public boolean isOutcome = true;

    private Button incomeTv;        //收入按钮
    private Button outcomeTv;       //支出按钮
    private TextView edittypeTv;    //编辑类别
    private TextView sortTv;        //显示选择的分类
    private TextView moneyTv;       //金额
    private TextView dateTv;        //日期选择
    private TextView cashTv;        //支出账户
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

    private ImageView clear;     //清空金额
    private ImageView remarkIv;  //备注
    private RelativeLayout delect;    //数字键盘回格键
    private ViewPager viewPagerItem;
    private LinearLayout layoutIcon;
    private RecyclerView recyclerView;

    //选择器
    protected  String[] account = {"支付宝", "微信", "现金", "信用卡", "银行卡"};
   // protected List<String> cardItems;
    protected int selectedPayinfoIndex = 0;      //选择的支付方式序号

    //选择时间
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

        edittypeTv = (TextView) mView.findViewById(R.id.type_edit);
        edittypeTv.setOnClickListener(this);

        recyclerView = (RecyclerView) mView.findViewById(R.id.category_recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(),4);
        recyclerView.setLayoutManager(layoutManager);
        //分类展示
        initCategory();

        sortTv = (TextView) mView.findViewById(R.id.item_tb_type_tv);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.income_tv:
                isOutcome = true;
                Log.d("Fragment","支出");
                break;
            case R.id.outcome_tv:
                isOutcome = false;
                Log.d("Fragment","收入");
                break;
            case R.id.type_edit:
                Intent intent = new Intent(getActivity(), TypeEditActivity.class);
                startActivity(intent);
                Log.d("Fragment","编辑分类");
                break;
            case R.id.tb_note_cash:
                showPayAccount();
                Log.d("Fragment","选择账户");
                break;
            case R.id.tb_note_date:
                showDateSelector();
                Log.d("Fragment","选择日期");
                break;
            case R.id.tb_note_remark:
                showContentDialog();
                Log.d("Fragment","备注");
                break;
            case R.id.tb_calc_num_done:
                Log.d("Fragment","确定按钮");
                break;
            case R.id.tb_calc_num_1:
                calMoney(1);
                Log.d("Fragment","1");
                break;
            case R.id.tb_calc_num_2:
                calMoney(2);
                Log.d("Fragemnt","2");
                break;
            case R.id.tb_calc_num_3:
                calMoney(3);
                Log.d("Fragemnt","3");
                break;
            case R.id.tb_calc_num_4:
                calMoney(4);
                Log.d("Fragemnt","4");
                break;
            case R.id.tb_calc_num_5:
                calMoney(5);
                Log.d("Fragemnt","5");
                break;
            case R.id.tb_calc_num_6:
                calMoney(6);
                Log.d("Fragemnt","6");
                break;
            case R.id.tb_calc_num_7:
                calMoney(7);
                Log.d("Fragemnt","7");
                break;
            case R.id.tb_calc_num_8:
                calMoney(8);
                Log.d("Fragemnt","8");
                break;
            case R.id.tb_calc_num_9:
                calMoney(9);
                Log.d("Fragemnt","9");
                break;
            case R.id.tb_calc_num_0:
                calMoney(0);
                Log.d("Fragemnt","0");
                break;
            case R.id.tb_calc_num_dot:
                if (dotNum.equals(".00")) {
                    isDot = true;
                    dotNum =".";
                }
                moneyTv.setText(num + dotNum);
                Log.d("Fragemnt",".");
                break;
            case R.id.tb_note_clear:
                doClear();
                Log.d("Fragemnt","清空");
                break;
            case R.id.tb_calc_num_del:
                doDelect();
                Log.d("Fragemnt","删除");
                break;
        }
    }

    //显示支付账户选择器
    public void showPayAccount() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择支付账户")
                .titleGravity(GravityEnum.CENTER)
                .items(account)
                .positiveText("确定")
                .negativeText("取消")
                .canceledOnTouchOutside(false)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        cashTv.setText(text);
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

    //显示备注内容输入框
    public void showContentDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("备注")
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.colorPrimaryDark)
                .input("备注", remarkInput, (dialog, input) -> {
                    if (input.equals("")) {
                        Toast.makeText(getContext(), "内容不能为空！" + input,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        remarkInput = input.toString();
                    }
                })
                .positiveText("确定")
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

    private void initCategory() {
        if (isOutcome) {
            for(int i = 0; i < category_outcome.length; i++){
                Categorytest categorytest = new Categorytest(category_outcome[i]);
                categorytestList.add(categorytest);
            }
        } else {
            for(int i = 0; i < category_income.length; i++){
                Categorytest categorytest = new Categorytest(category_income[i]);
                categorytestList.add(categorytest);
            }
        }
        CategoryAdapter adapter = new CategoryAdapter(categorytestList);
        recyclerView.setAdapter(adapter);
    }

}

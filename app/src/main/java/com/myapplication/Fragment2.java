package com.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pojo.Categorytest;

public class Fragment2 extends Fragment implements View.OnClickListener{

    private View mView;
    private Context mContext;

    private Button incomeTv;    //收入按钮
    private Button outcomeTv;   //支出按钮
    private TextView sortTv;    //显示选择的分类
    private TextView moneyTv;   //金额
    private TextView dateTv;    //日期选择
    private TextView cashTv;    //支出账户
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
    protected List<String> cardItems;
    protected int selectedPayinfoIndex = 0;      //选择的支付方式序号

    //选择时间
    protected String days = "2020-4-30";

    //备注
    protected String remarkInput = "";

    //数字键盘
    protected boolean isDot;
    protected String num = "0";               //整数部分
    protected String dotNum = ".00";         //小数部分
    protected final int MAX_NUM = 9999999;   //最大整数
    protected final int DOT_NUM = 2;         //小数点后最大位数
    protected int count = 0;

    private List<Categorytest> categorytestList = new ArrayList<>();

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

        sortTv = (TextView) mView.findViewById(R.id.item_tb_type_tv);

        moneyTv = (TextView) mView.findViewById(R.id.tb_note_money);
        moneyTv.setText(num+dotNum);

        dateTv = (TextView) mView.findViewById(R.id.tb_note_date);
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

        //分类展示
        initCategory();
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(),5);
        recyclerView.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(categorytestList);
        recyclerView.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.income_tv:
                Log.d("Fragment","支出");
                break;
            case R.id.outcome_tv:
                Log.d("Fragment","收入");
                break;
            case R.id.tb_note_cash:
                showPayAccount();
                Log.d("Fragment","选择账户");
                break;
            case R.id.tb_note_date:
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
                .items(cardItems)
                .positiveText("确定")
                .negativeText("取消")
                .itemsCallbackSingleChoice(selectedPayinfoIndex,(dialog, itemView, which, text)->{
                    selectedPayinfoIndex = which;
                    cashTv.setText(cardItems.get(which));
                    dialog.dismiss();
                    return false;
                }).show();
    }

    //显示备注内容输入框
    public void showContentDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("备注")
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
        for (int i = 0; i < 2; i++) {
            Categorytest apple = new Categorytest("Apple");
            categorytestList.add(apple);
            Categorytest banana = new Categorytest("Banana");
            categorytestList.add(banana);
            Categorytest orange = new Categorytest("orange");
            categorytestList.add(orange);
            Categorytest apple1 = new Categorytest("apple1");
            categorytestList.add(apple1);
            Categorytest apple2 = new Categorytest("apple2");
            categorytestList.add(apple2);
            Categorytest apple3 = new Categorytest("apple3");
            categorytestList.add(apple3);
            Categorytest apple4 = new Categorytest("apple4");
            categorytestList.add(apple4);
            Categorytest apple5 = new Categorytest("apple5");
            categorytestList.add(apple5);
        }
    }

}

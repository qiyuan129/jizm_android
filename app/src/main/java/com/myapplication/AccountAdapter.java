package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pojo.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<Account> mAccountList;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView accountName;
        TextView accountMoney;

        public ViewHolder(View view) {
            super(view);
            accountName = (TextView) view.findViewById(R.id.account_name);
            accountMoney = (TextView) view.findViewById(R.id.account_money);
        }
    }

    public AccountAdapter(List<Account> mAList) {
        mAccountList = mAList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = mAccountList.get(position);
        holder.accountName.setText(account.getAccount_name());
        holder.accountMoney.setText(String.valueOf(account.getMoney()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }

    public void clear() {
        mAccountList.clear();
    }
}

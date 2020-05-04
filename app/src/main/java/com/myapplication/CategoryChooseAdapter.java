package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pojo.Categorytest;

public class CategoryChooseAdapter extends RecyclerView.Adapter<CategoryChooseAdapter.ViewHolder> {
    private List<Categorytest> mCategoryList;

    public interface OnItemClickListener {
        void onClick(int position);
    }
    private OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.category_name);
        }
    }

    public CategoryChooseAdapter(List<Categorytest> mCList) {
        mCategoryList = mCList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorytest categorytest = mCategoryList.get(position);
        holder.categoryName.setText(categorytest.getName());
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
        return mCategoryList.size();
    }

    public void clear() {
        mCategoryList.clear();
    }
}

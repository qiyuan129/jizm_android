package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pojo.Categorytest;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Categorytest> mCategoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.category_name);
        }
    }

    public CategoryAdapter(List<Categorytest> categorytestList) {
        mCategoryList = categorytestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorytest categorytest = mCategoryList.get(position);
        holder.categoryName.setText(categorytest.getName());
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}

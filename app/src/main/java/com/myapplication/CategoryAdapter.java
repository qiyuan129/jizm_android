package com.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pojo.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> mCategoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View categoryView;
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            categoryView = view;
            categoryName = (TextView) view.findViewById(R.id.category_name);
        }
    }

    public CategoryAdapter(List<Category> categoryList) {
        mCategoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Category category = mCategoryList.get(position);
                Toast.makeText(v.getContext(), "you clicked view" + category.getCategory_name(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        holder.categoryName.setText(category.getCategory_name());
        holder.itemView.getTag(position);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void clear() {
        mCategoryList.clear();
    }
}

package com.example.appbangiay.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.ProductDetailsActivity;
import com.example.projectfinal.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter {
    private List<String> sizeProducts;
    private int item_index = -1;
    public SizeAdapter(List<String> sizeProducts) {
        this.sizeProducts = sizeProducts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String sizeValue = sizeProducts.get(position);
        ((ViewHolder)holder).setBtnValue(sizeValue, position);
    }

    @Override
    public int getItemCount() {
        return sizeProducts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button sizeBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeBtn = itemView.findViewById(R.id.size_item_btn);
        }
        private void setBtnValue(String value, int position) {
            sizeBtn.setText(value);
            sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_index = position;
                    ProductDetailsActivity.sizeCheck = item_index;
                    notifyDataSetChanged();
                }
            });
            if (item_index==position) {
                sizeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5f0e7")));
            } else {
                sizeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFFFF")));
            }

        }
    }
}

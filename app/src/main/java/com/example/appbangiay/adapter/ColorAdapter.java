package com.example.appbangiay.adapter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.ProductDetailsActivity;
import com.example.projectfinal.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter{
    private List<String> colorProducts;
    private int item_index = -1;
    public ColorAdapter(List<String> colorProducts) {
        this.colorProducts = colorProducts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String colorValue = colorProducts.get(position);
        ((ViewHolder)holder).setColorBtn(colorValue, position);
    }

    @Override
    public int getItemCount() {
        return colorProducts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button colorBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorBtn = itemView.findViewById(R.id.color_item_btn);
        }
        private void setColorBtn(String value, int position) {
            colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(value)));
            colorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_index = position;
                    ProductDetailsActivity.colorCheck = item_index;
                    Log.i("color check", String.valueOf(item_index));
                    notifyDataSetChanged();
                }
            });
            if (item_index == position) {
                colorBtn.setBackgroundResource(R.drawable.border_button_background);
            } else {
                colorBtn.setBackgroundResource(R.drawable.round_button);
            }

        }
    }
}

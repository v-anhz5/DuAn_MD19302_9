package com.example.appbangiay.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.R;
import com.example.projectfinal.models.AttributeModel;

import java.util.List;
//Giao diện danh sách kích thước
public class AttributeAdapter extends RecyclerView.Adapter {

    private List<AttributeModel> attributeModelList;

    public AttributeAdapter(List<AttributeModel> sizeModelList) {
        this.attributeModelList = sizeModelList;
    }
    @Override
    public int getItemViewType(int position) {
        switch (attributeModelList.get(position).getType()) {
            case 0:
                return AttributeModel.SIZE_TYPE;
            case 1:
                return AttributeModel.COLOR_TYPE;
            default:
                return -1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AttributeModel.SIZE_TYPE:
                View sizeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_item_layout, parent, false);
                return new SizeItemViewHolder(sizeItemView);
            case AttributeModel.COLOR_TYPE:
                View colorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_layout, parent, false);
                return new ColorItemViewHolder(colorItemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (attributeModelList.get(position).getType()) {
            case AttributeModel.SIZE_TYPE:
                String sizeValue = attributeModelList.get(position).getValue();
                ((SizeItemViewHolder)holder).setBtnValue(sizeValue);
                break;
            case AttributeModel.COLOR_TYPE:
                String colorValue = attributeModelList.get(position).getValue();
                ((ColorItemViewHolder)holder).setColorBtn(colorValue);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return attributeModelList.size();
    }
    public class SizeItemViewHolder extends RecyclerView.ViewHolder {
        private Button sizeBtn;
        public SizeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeBtn = itemView.findViewById(R.id.size_item_btn);
        }
        private void setBtnValue(String value) {
            sizeBtn.setText(value);
        }
    }
    public class ColorItemViewHolder extends RecyclerView.ViewHolder {
        private Button colorBtn;
        public ColorItemViewHolder(@NonNull View itemView) {
            super(itemView);
            colorBtn = itemView.findViewById(R.id.color_item_btn);
        }
        private void setColorBtn(String value) {
            colorBtn.setBackgroundColor(Color.parseColor(value));
        }
    }

}
 
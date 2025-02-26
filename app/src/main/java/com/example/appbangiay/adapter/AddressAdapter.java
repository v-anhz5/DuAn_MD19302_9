package com.example.appbangiay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.DeliveryActivity;
import com.example.projectfinal.MyAddressActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.models.AddressModel;
import com.example.projectfinal.utils.DBQueries;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;

    public AddressAdapter(List<AddressModel> addressModelList, int MODE) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
        preSelectedPosition = DBQueries.addressSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fullName = addressModelList.get(position).getFullName();
        String phoneNumber = addressModelList.get(position).getPhoneNumber();
        String fullAddress = addressModelList.get(position).getFullAddressInfo();
        Boolean selected = addressModelList.get(position).getSelected();
        holder.setDate(fullName, phoneNumber, fullAddress, selected, position);
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName;
        private TextView phoneNumber;
        private TextView fullAddress;
        private ImageView checkIcon;
        private LinearLayout optionsContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.tv_address_item_fullname);
            phoneNumber = itemView.findViewById(R.id.tv_address_item_phonenumber);
            fullAddress = itemView.findViewById(R.id.tv_address_item_full_info);
            checkIcon = itemView.findViewById(R.id.icon_view_check);
            optionsContainer = itemView.findViewById(R.id.options_container);
        };
        private void setDate(String fullNameText, String phoneNumberText, String fullAddressText, Boolean selected, int position) {
            fullName.setText(fullNameText);
            phoneNumber.setText(phoneNumberText);
            fullAddress.setText(fullAddressText);
            if (MODE == DeliveryActivity.SELECT_ADDRESS) {
                checkIcon.setImageResource(R.drawable.check_icon_24);
                if (selected) {
                    checkIcon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    checkIcon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preSelectedPosition != position) {
                            addressModelList.get(position).setSelected(true);
                            addressModelList.get(preSelectedPosition).setSelected(false);
                            MyAddressActivity.refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBQueries.addressSelected = position;
                        }

                    }
                });
            }
        }
    }
}

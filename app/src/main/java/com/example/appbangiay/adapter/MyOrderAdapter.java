package com.example.appbangiay.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectfinal.OrderDetailsActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.models.MyOrderItemModel;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderItemModel> myOrderItemModelList;
    private String order_id;
    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String orderID = myOrderItemModelList.get(position).getOrderID();
        order_id = orderID;
        String resource = myOrderItemModelList.get(position).getProductImage();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String size = myOrderItemModelList.get(position).getProductSize();
        String color = myOrderItemModelList.get(position).getProductColor();
        String price = myOrderItemModelList.get(position).getProductPrice();
        String cutPrice = myOrderItemModelList.get(position).getProductCutPrice();
        long quantity = myOrderItemModelList.get(position).getProductQuantity();
        long totalItems = myOrderItemModelList.get(position).getTotalItems();
        String totalAmount = myOrderItemModelList.get(position).getTotalAmount();
        String deliveredDate = myOrderItemModelList.get(position).getDeliveryStatus();
        holder.setOrderData(orderID, resource, title, size, color, price, cutPrice, quantity, totalItems, totalAmount, deliveredDate);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderID;
        private ImageView productImage;
        private TextView productTitle;
        private TextView productSize;
        private TextView productColor;
        private TextView productQuantity;
        private TextView productPrice;
        private TextView productCutPrice;
        private TextView totalItems;
        private TextView totalAmount;
        private ImageView deliveryIcon;
        private TextView deliveryStatus;
        private TextView seeMoreItems;
        private View orderDividerSeeMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.order_id_text_view);
            productImage = itemView.findViewById(R.id.order_product_image);
            productTitle = itemView.findViewById(R.id.order_product_title);
            productSize = itemView.findViewById(R.id.order_product_size);
            productColor = itemView.findViewById(R.id.order_product_color);
            productQuantity = itemView.findViewById(R.id.order_product_qty);
            productPrice = itemView.findViewById(R.id.order_product_price);
            productCutPrice = itemView.findViewById(R.id.order_product_cut_price);

            totalItems = itemView.findViewById(R.id.order_total_items);
            totalAmount = itemView.findViewById(R.id.order_total_price);

            deliveryIcon = itemView.findViewById(R.id.order_delivery_icon);
            deliveryStatus = itemView.findViewById(R.id.order_delivery_status);

            seeMoreItems = itemView.findViewById(R.id.order_see_more);
            orderDividerSeeMore = itemView.findViewById(R.id.order_divider_see_more);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    orderDetailsIntent.putExtra("ORDER_ID", order_id);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });
        };
        private void setOrderData(String orderIDText, String image, String title, String size, String color, String price, String cutPrice, long quantity, long totalItemsText, String totalAmountText, String deliveredDate) {
            orderID.setText("ID: "+orderIDText);
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions()).placeholder(R.drawable.placeholder_photo_512).into(productImage);
            productTitle.setText(title);
            productSize.setText(size);
            productColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            productPrice.setText(price + "đ");
            productCutPrice.setText(cutPrice + "đ");
            productQuantity.setText("x"+String.valueOf(quantity));
            totalItems.setText(String.valueOf(totalItemsText) + " items");
            totalAmount.setText(totalAmountText + "đ");
            if (Integer.parseInt(String.valueOf(totalItemsText)) > 1) {
                seeMoreItems.setVisibility(View.VISIBLE);
                orderDividerSeeMore.setVisibility(View.VISIBLE);
            }
            if (deliveredDate.equals("Cancelled")) {
                deliveryStatus.setTextColor(Color.parseColor("#4CAF50"));
                deliveryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            } else {
                deliveryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }
            deliveryStatus.setText(deliveredDate);
        }
    }
}

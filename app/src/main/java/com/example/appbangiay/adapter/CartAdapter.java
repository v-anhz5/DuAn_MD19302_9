package com.example.appbangiay.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectfinal.ProductDetailsActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.models.CartItemModel;
import com.example.projectfinal.utils.DBQueries;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                String size = cartItemModelList.get(position).getProductSize();
                String color = cartItemModelList.get(position).getProductColor();
                String price = cartItemModelList.get(position).getProductPrice();
                String cutPrice = cartItemModelList.get(position).getCutPrice();
                boolean inStock = cartItemModelList.get(position).isInStock();
                ((CartItemViewHolder) holder).setItemDetails(productID, resource, title, size, color, price, cutPrice, position, inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
                int totalAmount;
                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        totalItems++;
                        totalItemsPrice = totalItemsPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice().replace(",", ""));
                    }
                }
                if (totalItemsPrice > 1000000) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemsPrice;
                } else {
                    deliveryPrice = "50000";
                    totalAmount = totalItemsPrice + 50000;
                }
                ((CartTotalAmountViewHolder) holder).setTotalCartAmount(totalItems, totalItemsPrice, deliveryPrice, totalAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView productSize;
        private Button productColor;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView productQuantity;
        private ImageButton minusBtn;
        private ImageButton plusBtn;
        private ImageButton removeItem;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_cart_title);
            productSize = itemView.findViewById(R.id.product_cart_size);
            productColor = itemView.findViewById(R.id.product_cart_color);
            productPrice = itemView.findViewById(R.id.product_cart_price);
            cuttedPrice = itemView.findViewById(R.id.product_cart_cutted_price);
            productQuantity = itemView.findViewById(R.id.product_cart_quatity);
            minusBtn = itemView.findViewById(R.id.minus_quantity_btn);
            plusBtn = itemView.findViewById(R.id.plus_quantity_btn);
            removeItem = itemView.findViewById(R.id.product_cart_remove_btn);
        }

        ;

        private void setItemDetails(String productID, String resource, String title, String size, String color, String price, String cutPrice, int position, boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder_photo_512)).into(productImage);
            productTitle.setText(title);
            productSize.setText(size);
            productColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            if (inStock) {
                productPrice.setText(price + "đ");
                cuttedPrice.setText(cutPrice + "đ");
            } else {
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getColor(R.color.colorError));
                cuttedPrice.setText("");
                LinearLayout parent = (LinearLayout) productQuantity.getParent();
                parent.setVisibility(View.GONE);
            }

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(productQuantity.getText().toString());
                    if (quantity > 1) {
                        productQuantity.setText(String.valueOf(quantity - 1));
                    }
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(productQuantity.getText().toString());
                    productQuantity.setText(String.valueOf(quantity + 1));
                }
            });
            if (showDeleteBtn) {
                removeItem.setVisibility(View.VISIBLE);
            } else {
                removeItem.setVisibility(View.GONE);
            }
            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query = true;
                        DBQueries.removeProductFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItems;
        private TextView totalItemsPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.tv_total_items);
            totalItemsPrice = itemView.findViewById(R.id.tv_total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
        }

        private void setTotalCartAmount(int totalItemsText, int totalItemsPriceText, String deliveryPriceText, int totalAmountText) {
            totalItems.setText("Price (" + totalItemsText + " items)");
            totalItemsPrice.setText(totalItemsPriceText + "đ");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText("FREE");
            } else {
                deliveryPrice.setText(deliveryPriceText + "đ");
            }
            totalAmount.setText(totalAmountText + "đ");
            cartTotalAmount.setText(totalAmountText + "đ");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemsPriceText == 0) {
                DBQueries.cartItemModelList.remove(DBQueries.cartItemModelList.size() - 1);
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}

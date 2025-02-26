package com.example.appbangiay.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectfinal.ProductDetailsActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.models.WishlistModel;
import com.example.projectfinal.utils.DBQueries;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;
    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = wishlistModelList.get(position).getProductID();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        String ratingText = wishlistModelList.get(position).getRating();
        long totalRatingText = wishlistModelList.get(position).getTotalRatings();
        String productPriceText = wishlistModelList.get(position).getProductPrice();
        String cutProductPriceText = wishlistModelList.get(position).getCutProductPrice();
        holder.setData(productId, resource, title, ratingText, totalRatingText, productPriceText, cutProductPriceText, position);

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView rating;
        private TextView totalRatings;
        private View priceCut;
        private TextView productPrice;
        private TextView cutProductPrice;
        private ImageButton deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_wishlist_image);
            productTitle = itemView.findViewById(R.id.product_wishlist_title);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.total_wishlist_ratings);
            productPrice = itemView.findViewById(R.id.product_wishlist_price);
            priceCut = itemView.findViewById(R.id.cut_price_view);
            cutProductPrice = itemView.findViewById(R.id.product_wishlist_cutted_price);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        };
        private void setData(String productId, String resource, String title, String ratingText, long totalRatingText, String productPriceText, String cutProductPriceText, int index) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder_photo_512)).into(productImage);
            productTitle.setText(title);
            rating.setText(ratingText);
            totalRatings.setText("("+totalRatingText+" ratings)");
            productPrice.setText(productPriceText+"đ");
            cutProductPrice.setText(cutProductPriceText+"đ");
            if (wishlist) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetailsActivity.running_wishlist_query) {
                        ProductDetailsActivity.running_wishlist_query = true;
                        DBQueries.removeProductWishList(index, itemView.getContext());
                        Toast.makeText(itemView.getContext(), "Delete Item successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}

package com.example.appbangiay.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.AddAddressActivity;
import com.example.projectfinal.DeliveryActivity;
import com.example.projectfinal.HomeFragment;
import com.example.projectfinal.MyCartFragment;
import com.example.projectfinal.MyOrdersFragment;
import com.example.projectfinal.ProductDetailsActivity;
import com.example.projectfinal.WishlistFragment;
import com.example.projectfinal.adapter.CategoryAdapter;
import com.example.projectfinal.adapter.HomePageAdapter;
import com.example.projectfinal.models.AddressModel;
import com.example.projectfinal.models.CartItemModel;
import com.example.projectfinal.models.CategoryModel;
import com.example.projectfinal.models.HomePageModel;
import com.example.projectfinal.models.HorizontalProductScrollModel;
import com.example.projectfinal.models.MyOrderItemModel;
import com.example.projectfinal.models.SliderModel;
import com.example.projectfinal.models.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DBQueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
    public static List<HomePageModel> homePageModelList = new ArrayList<HomePageModel>();

    public static List<List<HomePageModel>> listHomePageCategories = new ArrayList<>();
    public static List<String> loadCategoriesName = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();


    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static List<AddressModel> addressModelList = new ArrayList<>();
    public static int addressSelected = -1;

    public static List<String> orderList = new ArrayList<>();
    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            };
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, final String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase(Locale.ROOT))
                .collection("TOP_DEALS")
                .orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                Log.i("view_type", documentSnapshot.get("view_type").toString());
                                if (((long) documentSnapshot.get("view_type")) == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<SliderModel>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long i = 1; i < no_of_banners + 1; i++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + i).toString(), documentSnapshot.get("banner_background_" + i).toString()));
                                    }
                                    listHomePageCategories.get(index).add(new HomePageModel(0, sliderModelList));
                                }
                                else if (((long) documentSnapshot.get("view_type")) == 1) {
                                    List<WishlistModel> viewAllProductList = new ArrayList<WishlistModel>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<HorizontalProductScrollModel>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    Log.i("no_of_products", String.valueOf(no_of_products));
                                    String layout_title = documentSnapshot.get("layout_title").toString();
                                    String backgroundContainer = documentSnapshot.get("layout_background").toString();
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        String id = documentSnapshot.get("product_ID_"+x).toString();
                                        String image = documentSnapshot.get("product_image_"+ x).toString();
                                        String title = documentSnapshot.get("product_title_"+ x).toString();
                                        String price = documentSnapshot.get("product_price_"+ x).toString();
                                        String cutPrice = documentSnapshot.get("cut_price_"+ x).toString();
                                        String rating = documentSnapshot.get("average_rating_"+x).toString();

                                        long totalRatings = (long) documentSnapshot.get("total_ratings_" + x);
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(id, image, title, price));
                                        viewAllProductList.add(new WishlistModel(id, image, title, rating, totalRatings , price, cutPrice));
                                    }

                                    listHomePageCategories.get(index).add(new HomePageModel(1, layout_title, backgroundContainer, horizontalProductScrollModelList, viewAllProductList));
                                }  else if (((long) documentSnapshot.get("view_type")) == 2) {

                                    List<HorizontalProductScrollModel> gridProductList = new ArrayList<HorizontalProductScrollModel>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    Log.i("no_of_products", String.valueOf(no_of_products));
                                    String backgroundContainer = documentSnapshot.get("layout_background").toString();
                                    String layout_title_grid = documentSnapshot.get("layout_title").toString();
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        String id = documentSnapshot.get("product_ID_"+x).toString();
                                        String image = documentSnapshot.get("product_image_"+ x).toString();
                                        String title = documentSnapshot.get("product_title_"+ x).toString();
                                        String price = documentSnapshot.get("product_price_"+ x).toString();
                                        gridProductList.add(new HorizontalProductScrollModel(id, image, title, price));
                                    }
                                    listHomePageCategories.get(index).add(new HomePageModel(2, layout_title_grid, backgroundContainer, gridProductList));
                                }
                                HomePageAdapter homePageAdapter = new HomePageAdapter(listHomePageCategories.get(index));
                                homePageRecyclerView.setAdapter(homePageAdapter);
                                homePageAdapter.notifyDataSetChanged();
                                HomeFragment.swipeRefreshLayout.setRefreshing(false);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void loadWishlist(final Context context, final Dialog dialog, final boolean loadProductData)  {
        // load wishlist in firebase store
        wishList.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            long size = (long) task.getResult().get("list_size");
                            Log.i("size", String.valueOf(size));
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                // add product id to wishlist
                                wishList.add(task.getResult().get("product_ID_"+x).toString());
                                if (DBQueries.wishList.contains(ProductDetailsActivity.productID)) {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                                    if (ProductDetailsActivity.addToWishListBtn != null) {
                                        ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
                                    }
                                } else {
                                    if (ProductDetailsActivity.addToWishListBtn != null) {
                                        ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#d4d4d4")));
                                    }
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                                }
                                final int finalID = (int) x;
                                String productId = task.getResult().get("product_ID_" + x).toString();
                                if (loadProductData) {
                                    wishlistModelList.clear();
                                    firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_ID_" + x).toString())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        String image = task.getResult().get("product_image_1").toString();
                                                        String title = task.getResult().get("product_title").toString();
                                                        String price = task.getResult().get("product_price").toString();
                                                        String cutPrice = task.getResult().get("cut_price").toString();
                                                        String rating = task.getResult().get("average_rating").toString();
                                                        long totalRatings = (long) task.getResult().get("total_ratings");
                                                        wishlistModelList.add(new WishlistModel(productId, image, title, rating, totalRatings , price, cutPrice));
                                                        WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });

    }
    public static void removeProductWishList(int index, Context context) {
        String removedProductId = wishList.get(index);
        wishList.remove(index);

        Map<String, Object> updateWishList = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishList.put("product_ID_"+x, wishList.get(x));
        };
        updateWishList.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (wishlistModelList.size() != 0) {
                                wishlistModelList.remove(index);
                                WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                            };
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context, "Remove successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            if (ProductDetailsActivity.addToWishListBtn != null) {
                                ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
                            }
                            wishList.add(index, removedProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                        if (ProductDetailsActivity.addToWishListBtn != null) {
                            ProductDetailsActivity.addToWishListBtn.setEnabled(true);
                        }
                        ProductDetailsActivity.running_wishlist_query = false;
                    }
                });
    }
    public static void loadRatingList(final Context context) {
        if (!ProductDetailsActivity.isRunning_rating_query) {
            ProductDetailsActivity.isRunning_rating_query = true;
            myRatedIds.clear();
            myRating.clear();

            firebaseFirestore.collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA")
                    .document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                    myRatedIds.add(task.getResult().get("product_ID_"+x).toString());
                                    myRating.add((long) task.getResult().get("rating_"+x));
                                    if (task.getResult().get("product_ID_"+ x).toString().equals(ProductDetailsActivity.productID)) {
                                        ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_"+x))) -1;
                                        if (ProductDetailsActivity.ratingNowContainer != null) {
                                            ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                        }
                                    }
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }
                            ProductDetailsActivity.isRunning_rating_query = false;
                        }

                    });
        }
    }
    public static void loadCartList(Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, TextView cartTotalAmount) {
        cartList.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            long size = (long) task.getResult().get("list_size");
                            Log.i("size", String.valueOf(size));
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                // add product id to wishlist
                                cartList.add(task.getResult().get("product_ID_"+x).toString());
                                String sizeNo = task.getResult().get("size_"+x).toString();
                                String colorNo = task.getResult().get("color_"+x).toString();
                                if (loadProductData) {
                                    cartItemModelList.clear();
                                    String productId = task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_ID_" + x).toString())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        int index = 0;
                                                        Log.i("cart list", String.valueOf(cartList.size()));
                                                        String image = task.getResult().get("product_image_1").toString();
                                                        String title = task.getResult().get("product_title").toString();
                                                        String sizeValue = task.getResult().get("size_" + sizeNo).toString();
                                                        String colorValue = task.getResult().get("color_"+ colorNo).toString();
                                                        String price = task.getResult().get("product_price").toString();
                                                        String cutPrice = task.getResult().get("cut_price").toString();
                                                        boolean inStock = (boolean) task.getResult().get("in_stock");
                                                        if (cartList.size() >= 2) {
                                                            index = cartList.size() - 2;
                                                        }
                                                        cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productId, image, title, sizeValue, colorValue, price, cutPrice, (long) 1, inStock));
                                                        Log.i("cart model", String.valueOf(cartItemModelList.size()));
                                                        if (cartList.size() == cartItemModelList.size()) {
                                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                            parent.setVisibility(View.VISIBLE);
                                                        }
                                                        if (cartList.size() == 0) {
                                                            cartItemModelList.clear();
                                                        }
                                                        Log.i("index", String.valueOf(index));
                                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            }


                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
    }
    public static void removeProductFromCart(int index, Context context, TextView cartTotalAmount) {
        String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();
        updateCartList.put("list_size", (long) cartList.size());
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                int i = 0;
                                long removeIndex = (long) index;
                                if (removeIndex != x) {
                                    String productId = task.getResult().get("product_ID_"+ x).toString();
                                    String color = task.getResult().get("color_"+x).toString();
                                    String size = task.getResult().get("size_"+x).toString();
                                    updateCartList.put("product_ID_"+ i, productId);
                                    updateCartList.put("color_"+i, color);
                                    updateCartList.put("size_"+ i, size);
                                    i++;
                                }
                            }
                                    firebaseFirestore.collection("USERS")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("USER_DATA").document("MY_CART")
                                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (cartItemModelList.size() != 0) {
                                                    cartItemModelList.remove(index);
                                                    MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                };
                                                if (cartList.size() == 0) {
                                                    LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                    parent.setVisibility(View.GONE);
                                                    cartItemModelList.clear();
                                                }
                                                ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                                                Toast.makeText(context, "Remove successfully!", Toast.LENGTH_LONG).show();
                                            } else {
                                                cartList.add(index, removedProductId);
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                            ProductDetailsActivity.running_cart_query = false;
                                        }
                                    });
                        }
                    }
                });
    }
    public static void loadAddresses(Context context, Dialog loadingDialog) {
        addressModelList.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            long listSize = (long) task.getResult().get("list_size");
                            Intent deliveryIntent;
                            if (listSize == 0) {
                                deliveryIntent = new Intent(context, AddAddressActivity.class);
                                deliveryIntent.putExtra("INTENT", "deliveryIntent");
                            } else {
                                for (long x = 1; x < (long) listSize + 1; x++) {
                                    String fullName = task.getResult().get("full_name_"+x).toString();
                                    String phoneNumber = task.getResult().get("phone_number_"+x).toString();
                                    String fullAddress = task.getResult().get("full_address_"+x).toString();
                                    boolean selected = (boolean) task.getResult().get("selected_"+x);
                                    addressModelList.add(new AddressModel(fullName, phoneNumber, fullAddress, selected));
                                    if (selected) {
                                        addressSelected = Integer.parseInt(String.valueOf(x - 1));
                                    }
                                }
                                deliveryIntent = new Intent(context, DeliveryActivity.class);
                            }
                            context.startActivity(deliveryIntent);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }
    public static void loadOrderList(Context context, final Dialog dialog, final boolean loadProductData) {
        orderList.clear();
        firebaseFirestore.collection("ORDERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("MY_ORDER")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orderList.add(document.getId());
                            }
                            Log.d("order list", String.valueOf(orderList.size()));
                            if (loadProductData) {
                                myOrderItemModelList.clear();
                                for (int x = 0; x < orderList.size(); x++) {
                                    String orderID = orderList.get(x);
                                    firebaseFirestore.collection("ORDERS")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .collection("MY_ORDER")
                                            .document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        long listSize = (long) task.getResult().get("list_size");
                                                        String orderID = task.getResult().get("order_id").toString();
                                                        String status = task.getResult().get("status").toString();
                                                        String totalAmount = task.getResult().get("total_amount").toString();
                                                        long totalItems = (long) task.getResult().get("total_items");
                                                        String addressID = task.getResult().get("address_ID").toString();
                                                        firebaseFirestore.collection("ORDER_DETAILS")
                                                                .document(FirebaseAuth.getInstance().getUid())
                                                                .collection("MY_ORDER_DETAILS")
                                                                .document(orderID)
                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            String color = task.getResult().get("color_0").toString();
                                                                            String size = task.getResult().get("size_0").toString();
                                                                            String price = task.getResult().get("price_0").toString();
                                                                            long quantity = (long) task.getResult().get("quantity_0");
                                                                            String title = task.getResult().get("product_title_0").toString();
                                                                            String image = task.getResult().get("product_image_0").toString();
                                                                            String cutPrice = task.getResult().get("cut_price_0").toString();
                                                                            myOrderItemModelList.add(new MyOrderItemModel(orderID, image, title, color, size, price, cutPrice, quantity, totalItems, totalAmount, status));
                                                                            MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                                                                        } else {
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            }

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void clearData() {
        categoryModelList.clear();
        listHomePageCategories.clear();
        loadCategoriesName.clear();
        wishList.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
    }
}

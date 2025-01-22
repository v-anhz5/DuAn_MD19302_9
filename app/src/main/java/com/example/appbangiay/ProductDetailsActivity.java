package com.example.appbangiay;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.projectfinal.adapter.ColorAdapter;
import com.example.projectfinal.adapter.ProductImagesAdapter;
import com.example.projectfinal.adapter.SizeAdapter;
import com.example.projectfinal.models.CartItemModel;
import com.example.projectfinal.models.WishlistModel;
import com.example.projectfinal.utils.DBQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    public static boolean running_wishlist_query = false;
    public static boolean isRunning_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailsActivity;
    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;

    public static MenuItem cartItem;
    private TextView badgeCount;
    // product title
    private TextView productTitle;
    // average rating
    private TextView averageRating;
    // total rating;
    private TextView totalRatings;
    // product price
    private TextView productPrice;
    // product cut price
    private TextView cutPrice;
    private TextView productDescription;
    public static FloatingActionButton addToWishListBtn;
    // rating layout
    public static int initialRating;
    public static LinearLayout ratingNowContainer;
    private LinearLayout ratingsNoContainer;
    private TextView averageRatingFigure;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBar;
    private TextView totalRatingsMiniView;
    // rating layout
    private Button buyNowBtn;
    // size recyclerview
    private RecyclerView sizeRecyclerView;
    // color recyclerview
    private RecyclerView colorRecyclerView;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    public static int sizeCheck = -1;
    public static int colorCheck = -1;

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private LinearLayout addToCartBtn;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    public static String productID;

    private DocumentSnapshot documentSnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        productImagesViewPager = findViewById(R.id.product_image_view_pager);
        viewPagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wishlist_btn);
        productTitle = findViewById(R.id.product_title);
        averageRating = findViewById(R.id.tv_product_rating_miniview);
        totalRatings = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cutPrice = findViewById(R.id.cutted_price);
        productDescription = findViewById(R.id.product_detail_body);
        sizeRecyclerView = findViewById(R.id.size_recycler_view);
        colorRecyclerView = findViewById(R.id.color_recycler_view);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        averageRatingFigure = findViewById(R.id.average_rating);
        totalRatingsFigure = findViewById(R.id.total_ratings);
        totalRatingsMiniView = findViewById(R.id.tv_mini_total_ratings);
        ratingsProgressBar = findViewById(R.id.ratings_process_bar_container);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();

        initialRating = -1;
        // loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        // loading dialog
        // sign in dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_dialog_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_dialog_btn);

        final Intent LoginIntent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                LoginActivity.setSignUpFragment = false;
                startActivity(LoginIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                LoginActivity.setSignUpFragment = true;
                startActivity(LoginIntent);
            }
        });
        // sign in dialog
        // Size product
        LinearLayoutManager sizeLayout = new LinearLayoutManager(this );
        sizeLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        sizeRecyclerView.setLayoutManager(sizeLayout);


        // Color product
        LinearLayoutManager colorLayout = new LinearLayoutManager(this );
        colorLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        colorRecyclerView.setLayoutManager(colorLayout);
        // List product images
        List<String> productImages = new ArrayList<String>();

        // Size product
        List<String> sizeListValue = new ArrayList<String>();

        // color product
        List<String> colorListValue = new ArrayList<String>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshot = task.getResult();
                            // list product image
                            long no_of_product_images = (long) documentSnapshot.get("no_of_product_images");
                            for (long i = 1; i < no_of_product_images + 1; i++) {
                                productImages.add(documentSnapshot.get("product_image_"+i).toString());
                            }
                            // list product size
                            long no_of_sizes = (long) documentSnapshot.get("no_of_sizes");
                            for (long x = 1; x < no_of_sizes + 1; x++) {
                                sizeListValue.add(documentSnapshot.get("size_"+x).toString());
                            }
                            // list product color
                            long no_of_colors = (long) documentSnapshot.get("no_of_colors");
                            for (long j = 1; j < no_of_colors + 1; j++) {
                                colorListValue.add(documentSnapshot.get("color_"+j).toString());
                            };

                            // rating number container
                            for (int k = 1; k < 6; k++) {
                                int ratingNumberPosition = 6 - k;

                                long ratingNumberValue = (long) documentSnapshot.get(ratingNumberPosition+"_star");

                                TextView rating = (TextView) ratingsNoContainer.getChildAt(k -1);
                                rating.setText(String.valueOf(ratingNumberValue));

                                ProgressBar progressBar = (ProgressBar) ratingsProgressBar.getChildAt(k - 1);
                                int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                progressBar.setMax(maxProgress);
                                progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((6-k)+ "_star"))));
                            };


                            // set product image
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            productImagesViewPager.setAdapter(productImagesAdapter);
                            productImagesAdapter.notifyDataSetChanged();

                            // set size product
                            SizeAdapter sizeAdapter = new SizeAdapter(sizeListValue);
                            sizeRecyclerView.setAdapter(sizeAdapter);
                            sizeAdapter.notifyDataSetChanged();

                            // set color product
                            ColorAdapter colorAdapter = new ColorAdapter(colorListValue);
                            colorRecyclerView.setAdapter(colorAdapter);
                            colorAdapter.notifyDataSetChanged();

                            // set data product
                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            averageRating.setText(documentSnapshot.get("average_rating").toString());
                            totalRatings.setText("(" + (long) documentSnapshot.get("total_ratings") + " ratings)");
                            productPrice.setText(documentSnapshot.get("product_price").toString()+"đ");
                            cutPrice.setText(documentSnapshot.get("cut_price").toString()+"đ");
                            productDescription.setText(documentSnapshot.get("product_description").toString());
                            averageRatingFigure.setText(documentSnapshot.get("average_rating").toString());
                            totalRatingsFigure.setText(documentSnapshot.get("total_ratings").toString() + " ratings");
                            totalRatingsMiniView.setText(documentSnapshot.get("total_ratings").toString());
                            if ((boolean) documentSnapshot.get("in_stock")) {
                                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (currentUser == null) {
                                            signInDialog.show();
                                        } else {
                                            if (!running_cart_query) {
                                                running_cart_query = true;
                                                if (ALREADY_ADDED_TO_CART) {
                                                    running_cart_query = false;
                                                    Toast.makeText(ProductDetailsActivity.this, "already added to cart", Toast.LENGTH_LONG).show();
                                                } else {
                                                    // todo add to cart
                                                    Map<String, Object> addProduct = new HashMap<>();
                                                    if (!DBQueries.cartList.contains(productID)) {
                                                        addProduct.put("product_ID_" + String.valueOf(DBQueries.cartList.size()), productID);
                                                        addProduct.put("list_size", (long) DBQueries.cartList.size() + 1);
                                                        String sizeNo = String.valueOf(sizeCheck + 1);
                                                        addProduct.put("size_"+ String.valueOf(DBQueries.cartList.size()), sizeNo);
                                                        String colorNo = String.valueOf(colorCheck + 1);
                                                        addProduct.put("color_"+ String.valueOf(DBQueries.cartList.size()), colorNo);
                                                        firebaseFirestore.collection("USERS")
                                                                .document(currentUser.getUid())
                                                                .collection("USER_DATA")
                                                                .document("MY_CART")
                                                                .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (DBQueries.cartItemModelList.size() != 0) {
                                                                                String image = documentSnapshot.get("product_image_1").toString();
                                                                                String title = documentSnapshot.get("product_title").toString();
                                                                                String sizeValue = documentSnapshot.get("size_" + sizeNo).toString();
                                                                                String colorValue = documentSnapshot.get("color_"+ colorNo).toString();
                                                                                String price = documentSnapshot.get("product_price").toString();
                                                                                String cutPrice = documentSnapshot.get("cut_price").toString();
                                                                                boolean inStock = (boolean) documentSnapshot.get("in_stock");
                                                                                DBQueries.cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM, productID, image, title, sizeValue, colorValue, price, cutPrice, (long) 1, inStock));
                                                                            }
                                                                            DBQueries.cartList.add(productID);
                                                                            Toast.makeText(ProductDetailsActivity.this, "Adđ product to cart successfully!", Toast.LENGTH_LONG).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        String sizeNo = String.valueOf(sizeCheck + 1);
                                                        String colorNo = String.valueOf(colorCheck + 1);
                                                        firebaseFirestore.collection("USERS")
                                                                .document(currentUser.getUid())
                                                                .collection("USER_DATA")
                                                                .document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            long list_size = (long) task.getResult().get("list_size");
                                                                            for (long x = 0; x < list_size; x++) {
                                                                                if (task.getResult().get("product_ID_"+ x).toString().equals(productID)){
                                                                                    String sizeOld = task.getResult().get("size_" + x).toString();
                                                                                    String colorOld = task.getResult().get("color_" +x).toString();
                                                                                    Log.i("size check", String.valueOf(sizeCheck));
                                                                                    Log.i("color check", String.valueOf(colorCheck));
                                                                                    Log.i("size old", sizeOld);
                                                                                    Log.i("color old", colorOld);
                                                                                    Log.i("size no", sizeNo);
                                                                                    Log.i("color no", colorNo);
                                                                                    if (sizeOld.equals(sizeNo) && colorOld.equals(colorNo)) {
                                                                                        ALREADY_ADDED_TO_CART = true;
                                                                                        running_cart_query = false;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (ALREADY_ADDED_TO_CART) {
                                                                                running_cart_query = false;
                                                                                Toast.makeText(ProductDetailsActivity.this, "already added to cart", Toast.LENGTH_LONG).show();
                                                                            } else {
                                                                                addProduct.put("product_ID_" + String.valueOf(DBQueries.cartList.size()), productID);
                                                                                addProduct.put("list_size", (long) DBQueries.cartList.size() + 1);
                                                                                addProduct.put("size_"+ String.valueOf(DBQueries.cartList.size()), sizeNo);
                                                                                addProduct.put("color_"+ String.valueOf(DBQueries.cartList.size()), colorNo);
                                                                                firebaseFirestore.collection("USERS")
                                                                                        .document(currentUser.getUid())
                                                                                        .collection("USER_DATA")
                                                                                        .document("MY_CART")
                                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (DBQueries.cartItemModelList.size() != 0) {
                                                                                                        String image = documentSnapshot.get("product_image_1").toString();
                                                                                                        String title = documentSnapshot.get("product_title").toString();
                                                                                                        String sizeValue = documentSnapshot.get("size_" + sizeNo).toString();
                                                                                                        String colorValue = documentSnapshot.get("color_"+ colorNo).toString();
                                                                                                        String price = documentSnapshot.get("product_price").toString();
                                                                                                        String cutPrice = documentSnapshot.get("cut_price").toString();
                                                                                                        boolean inStock = (boolean) documentSnapshot.get("in_stock");
                                                                                                        DBQueries.cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM, productID, image, title, sizeValue, colorValue, price, cutPrice, (long) 1, inStock));
                                                                                                    }
                                                                                                    DBQueries.cartList.add(productID);
                                                                                                    Toast.makeText(ProductDetailsActivity.this, "Adđ product to cart successfully!", Toast.LENGTH_LONG).show();
                                                                                                    running_cart_query = false;
                                                                                                    invalidateOptionsMenu();
                                                                                                } else {
                                                                                                    running_cart_query = false;
                                                                                                    String error = task.getException().getMessage();
                                                                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }

                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });


                                                    }
                                                }
                                            }

                                        }
                                    }
                                });
                            } else {
                                buyNowBtn.setVisibility(View.GONE);
                                TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                outOfStock.setText("Out of stock");
                                outOfStock.setTextColor(getResources().getColor(R.color.colorError));
                                outOfStock.setCompoundDrawables(null, null, null, null);
                            }

                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });


        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    // todo set enable false
//                    addToWishListBtn.setEnabled(false);
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBQueries.wishList.indexOf(productID);
                            DBQueries.removeProductWishList(index, ProductDetailsActivity.this);
                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#d4d4d4")));
                        } else {
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBQueries.wishList.size()), productID);
                            firebaseFirestore.collection("USERS")
                                    .document(currentUser.getUid())
                                    .collection("USER_DATA")
                                    .document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> updateListSize = new HashMap<>();
                                                updateListSize.put("list_size", (long) DBQueries.wishList.size() + 1);
                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                                        .document("MY_WISHLIST")
                                                        .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    if (DBQueries.wishlistModelList.size() != 0) {
                                                                        String image = documentSnapshot.get("product_image_1").toString();
                                                                        String title = documentSnapshot.get("product_title").toString();
                                                                        String price = documentSnapshot.get("product_price").toString();
                                                                        String cutPrice = documentSnapshot.get("cut_price").toString();
                                                                        String rating = documentSnapshot.get("average_rating").toString();
                                                                        long totalRatings = (long) documentSnapshot.get("total_ratings");
                                                                        DBQueries.wishlistModelList.add(new WishlistModel(productID, image, title, rating, totalRatings, price, cutPrice));
                                                                    }
                                                                    ALREADY_ADDED_TO_WISHLIST = true;
                                                                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
                                                                    DBQueries.wishList.add(productID);
                                                                    Toast.makeText(ProductDetailsActivity.this, "Adđ product to wishlist successfully!", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#d4d4d4")));
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                                }
                                                                // todo set enable true
                                                                addToWishListBtn.setEnabled(true);
                                                                running_wishlist_query = false;
                                                            }

                                                        });
                                            } else {
//                                                addToWishListBtn.setEnabled(true);
                                                running_wishlist_query = false;
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                        }
                    }
                }
            }
        });


        // rating layout

        ratingNowContainer = findViewById(R.id.rating_now_container);
        for (int x = 0; x < ratingNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            ratingNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            if (!isRunning_rating_query) {
                                isRunning_rating_query = true;
                                setRating(starPosition);
                                Log.i("init",  String.valueOf(initialRating));
                                Log.i("final", String.valueOf(starPosition));
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBQueries.myRatedIds.contains(productID)) {
                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long)starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating(starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> rating = new HashMap<>();
                                                    if (DBQueries.myRatedIds.contains(productID)) {
                                                        rating.put("rating_" +DBQueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                    } else {
                                                        rating.put("list_size", (long) DBQueries.myRatedIds.size() +1);
                                                        rating.put("product_ID_"+ DBQueries.myRatedIds.size(), productID);
                                                        rating.put("rating_"+ DBQueries.myRatedIds.size(), (long) starPosition + 1);
                                                    }
                                                    firebaseFirestore.collection("USERS")
                                                            .document(currentUser.getUid())
                                                            .collection("USER_DATA")
                                                            .document("MY_RATINGS")
                                                            .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (DBQueries.myRatedIds.contains(productID)) {
                                                                            DBQueries.myRating.set(DBQueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                                        } else {
                                                                            DBQueries.myRatedIds.add(productID);
                                                                            DBQueries.myRating.add((long) starPosition + 1);
                                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                                            totalRatings.setText("(" + ((long) documentSnapshot.get("total_ratings")+1)+" ratings)");
                                                                            totalRatingsFigure.setText((long) documentSnapshot.get("total_ratings")+1+" ratings");
                                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating", Toast.LENGTH_LONG).show();
                                                                        };

                                                                        // rating number container
                                                                        for (int k = 1; k < 6; k++) {
                                                                            int ratingNumberPosition = 6 - k;
//                                                                            long ratingNumberValue = (long) documentSnapshot.get(ratingNumberPosition+"_star");
                                                                            TextView ratingFigures = (TextView) ratingsNoContainer.getChildAt(k -1);

                                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBar.getChildAt(k - 1);
                                                                            int maxProgress = Integer.parseInt(totalRatingsMiniView.getText().toString());
                                                                            progressBar.setMax(maxProgress);
                                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                                        };
                                                                        initialRating = starPosition;
                                                                        averageRating.setText(calculateAverageRating(0, true));
                                                                        averageRatingFigure.setText(calculateAverageRating(0, true));
                                                                        if (DBQueries.wishList.contains(productID) && DBQueries.wishlistModelList.size() != 0) {
                                                                            int index = DBQueries.wishList.indexOf(productID);
                                                                            DBQueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                                            DBQueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(documentSnapshot.get("total_ratings").toString()));
                                                                        }
                                                                    }else {
                                                                        setRating(initialRating);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                                    }
                                                                    isRunning_rating_query = false;
                                                                }
                                                            });
                                                } else {
                                                    isRunning_rating_query = false;
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }
                        }
                    }
                }
            });
        }
        // rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    if (sizeCheck == -1 && colorCheck == -1) {
                        Toast.makeText(ProductDetailsActivity.this, "Please select size and color", Toast.LENGTH_LONG).show();
                    }

                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    String sizeNo = String.valueOf(sizeCheck + 1);
                    String colorNo = String.valueOf(colorCheck + 1);
                    Log.i("size no", sizeNo);
                    Log.i("color no ", colorNo);
                    String image = documentSnapshot.get("product_image_1").toString();
                    String title = documentSnapshot.get("product_title").toString();
                    String sizeValue = documentSnapshot.get("size_" + sizeNo).toString();
                    String colorValue = documentSnapshot.get("color_"+ colorNo).toString();
                    String price = documentSnapshot.get("product_price").toString();
                    String cutPrice = documentSnapshot.get("cut_price").toString();
                    boolean inStock = (boolean) documentSnapshot.get("in_stock");
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID, image, title, sizeValue, colorValue, price, cutPrice, (long) 1, inStock));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if (DBQueries.addressModelList.size() == 0) {
                        DBQueries.loadAddresses(ProductDetailsActivity.this, loadingDialog);
                    } else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }

                }

            }
        });

    };

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (DBQueries.myRating.size() == 0) {
                DBQueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBQueries.wishList.size() == 0) {
                DBQueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }

            if (DBQueries.wishList.contains(productID)) {
                ALREADY_ADDED_TO_WISHLIST = true;
                addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
            } else {
                ALREADY_ADDED_TO_WISHLIST = false;
                addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#d4d4d4")));
            }

            if (DBQueries.myRatedIds.contains(productID)) {
                int index = DBQueries.myRatedIds.indexOf(productID);
                initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index))) - 1;
                setRating(initialRating);
            }
            invalidateOptionsMenu();
        } else {
            loadingDialog.dismiss();
        }

    }

    public static void setRating(int starPosition) {
            for (int x = 0; x < ratingNowContainer.getChildCount(); x ++ ) {
                ImageView starBtn = (ImageView) ratingNowContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C8C8C8")));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                }
            }

   }
   private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x );
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        };
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars/ Long.parseLong(totalRatingsMiniView.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars/(Long.parseLong(totalRatingsMiniView.getText().toString()) + 1)).substring(0,3);
        }

   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart_icon);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
            if (currentUser != null) {
                if (DBQueries.cartList.size() == 0) {
                    DBQueries.loadCartList(ProductDetailsActivity.this, new Dialog(ProductDetailsActivity.this), false, badgeCount, new TextView(ProductDetailsActivity.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBQueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBQueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo: cart
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                        MainActivity.showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });

        return true;
    };
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            productDetailsActivity = null;
            finish();
            return true;
        }
        else if (id == R.id.main_search_icon) {
            // todo: search
            return true;
        }  else if (id == R.id.main_cart_icon) {
            // todo: cart
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                MainActivity.showCart = true;
                startActivity(cartIntent);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}
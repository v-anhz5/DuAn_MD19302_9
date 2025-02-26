package com.example.appbangiay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.adapter.CartAdapter;
import com.example.projectfinal.models.CartItemModel;
import com.example.projectfinal.utils.DBQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {
    public static boolean running_order_query = false;
    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecycler;
    private Button changeOrAddAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;

    private TextView fullName;
    private TextView phoneNumber;
    private TextView fullAddress;
    private Button continueBtn;
    private Dialog loadingDialog;
    private ConstraintLayout confirmOrderLayout;
    private TextView orderId;
    private ImageButton continueShoppingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // loading dialog

        deliveryRecycler = findViewById(R.id.delivery_recycler_view);
        changeOrAddAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.tv_address_item_fullname);
        phoneNumber = findViewById(R.id.tv_address_item_phonenumber);
        fullAddress = findViewById(R.id.tv_address_item_full_info);
        continueBtn = findViewById(R.id.cart_continue_btn);
        // order confirm
        confirmOrderLayout = findViewById(R.id.order_confirm_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_confirm_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecycler.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecycler.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeOrAddAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressIntent = new Intent(DeliveryActivity.this, MyAddressActivity.class);
                myAddressIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressIntent);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loadingDialog.show();
                if (!running_order_query) {
                    running_order_query = true;
                    String customer_id = FirebaseAuth.getInstance().getUid();
                    String order_id = UUID.randomUUID().toString().substring(0,10);
                    String totalAmountOrder = removeLastChar(totalAmount.getText().toString());
                    String address_ID = String.valueOf(DBQueries.addressSelected);
                    long totalItems = Long.parseLong(String.valueOf(cartItemModelList.size() - 1));
                    String index = String.valueOf(DBQueries.orderList.size() + 1);

                    Map<String, Object> orderMap = new HashMap<>();
                    orderMap.put("list_size", (long) DBQueries.orderList.size() + 1);
                    orderMap.put("total_amount", totalAmountOrder);
                    orderMap.put("status", "ordered");
                    orderMap.put("address_ID", address_ID);
                    orderMap.put("total_items", totalItems);
                    orderMap.put("order_id", order_id);
                    FirebaseFirestore.getInstance().collection("ORDERS")
                            .document(customer_id)
                            .collection("MY_ORDER")
                            .document(order_id)
                            .set(orderMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> itemsMap = new HashMap<>();
                                        itemsMap.put("list_size", totalItems);
                                        // Lưu dữ liệu cho order details
                                        int listSize = (int) cartItemModelList.size() - 1;
                                        for (int i = 0; i < listSize; i++) {
                                            itemsMap.put("product_ID_"+i, cartItemModelList.get(i).getProductID());
                                            itemsMap.put("product_image_"+i, cartItemModelList.get(i).getProductImage());
                                            itemsMap.put("product_title_"+i, cartItemModelList.get(i).getProductTitle());
                                            itemsMap.put("size_"+i, cartItemModelList.get(i).getProductSize());
                                            itemsMap.put("color_"+i, cartItemModelList.get(i).getProductColor());
                                            itemsMap.put("price_"+ i, cartItemModelList.get(i).getProductPrice());
                                            itemsMap.put("cut_price_"+i, cartItemModelList.get(i).getCutPrice());
                                            itemsMap.put("quantity_"+i, cartItemModelList.get(i).getProductQuantity());
                                        };
                                        FirebaseFirestore.getInstance().collection("ORDER_DETAILS")
                                                .document(customer_id)
                                                .collection("MY_ORDER_DETAILS")
                                                .document(order_id)
                                                .set(itemsMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Map<String, Object> cartMap = new HashMap<>();
                                                                    cartMap.put("list_size", (long) 0);
                                                                    FirebaseFirestore.getInstance().collection("USERS")
                                                                            .document(FirebaseAuth.getInstance().getUid())
                                                                            .collection("USER_DATA")
                                                                            .document("MY_CART").update(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        DBQueries.cartList.clear();
                                                                                        DBQueries.cartItemModelList.clear();

                                                                                    } else {
                                                                                        String error = task.getException().getMessage();
                                                                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                    if (MainActivity.mainActivity != null) {
                                                                        MainActivity.mainActivity.finish();
                                                                        MainActivity.mainActivity = null;
                                                                        MainActivity.showCart = false;
                                                                    }
                                                                    if (ProductDetailsActivity.productDetailsActivity != null) {
                                                                        ProductDetailsActivity.productDetailsActivity.finish();
                                                                        ProductDetailsActivity.productDetailsActivity = null;
                                                                    }
                                                                    DBQueries.orderList.add(order_id);
                                                                    orderId.setText("Order ID "+ order_id);
                                                                    confirmOrderLayout.setVisibility(View.VISIBLE);
                                                                    running_order_query = false;
                                                                    continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            finish();
                                                                        }
                                                                    });
                                                                } else {
                                                                    running_order_query = false;
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                    } else {
                                        running_order_query = false;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fullName.setText(DBQueries.addressModelList.get(DBQueries.addressSelected).getFullName());
        phoneNumber.setText(DBQueries.addressModelList.get(DBQueries.addressSelected).getPhoneNumber());
        fullAddress.setText(DBQueries.addressModelList.get(DBQueries.addressSelected).getFullAddressInfo());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
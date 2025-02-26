package com.example.appbangiay;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailsActivity extends AppCompatActivity {
    private Button changeOrAddAddressBtn;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        changeOrAddAddressBtn = findViewById(R.id.change_or_add_address_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeOrAddAddressBtn.setVisibility(View.GONE);

        String orderID = getIntent().getStringExtra("ORDER_ID");
        FirebaseFirestore.getInstance().collection("ORDERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("MY_ORDER")
                .document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        long listSize = (long) task.getResult().get("list_size");
                        String addressID = task.getResult().get("address_ID").toString();
                        String status = task.getResult().get("status").toString();
                        String totalAmount = task.getResult().get("total_amount").toString();
                        long totalItems = (long) task.getResult().get("total_items");

                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("USER_DATA")
                                .document("MY_ADDRESSES")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        String fullAddress = task.getResult().get("full_address_"+addressID+1).toString();
                                        String fullName = task.getResult().get("full_name_"+addressID+1).toString();
                                        String phoneNumber = task.getResult().get("phone_number"+addressID+1).toString();
                                    }
                                });

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
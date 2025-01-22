package com.example.appbangiay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.adapter.AddressAdapter;
import com.example.projectfinal.utils.DBQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity {

    private int previousAddress;
    private LinearLayout addNewAddress;
    private RecyclerView addressRecyclerView;
    private Button deliveryHereBtn;
    private TextView addressesSaved;
    private static AddressAdapter addressAdapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        previousAddress = DBQueries.addressSelected;

        addressRecyclerView = findViewById(R.id.addresses_recycler_view);
        deliveryHereBtn = findViewById(R.id.delivery_here_btn);
        addNewAddress = findViewById(R.id.add_new_address_btn);
        addressesSaved = findViewById(R.id.addresses_saved);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        addressRecyclerView.setLayoutManager(layoutManager);

        // loading dialog
        loadingDialog = new Dialog(MyAddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // loading dialog


        int mode = getIntent().getIntExtra("MODE", -1);
        if (mode == DeliveryActivity.SELECT_ADDRESS) {
            deliveryHereBtn.setVisibility(View.VISIBLE);
        } else {
            deliveryHereBtn.setVisibility(View.GONE);
        }

        deliveryHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DBQueries.addressSelected != previousAddress) {
                    loadingDialog.show();
                    final int previousAddressIndex = previousAddress;

                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_"+ String.valueOf(previousAddress+1), false);
                    updateSelection.put("selected_"+ String.valueOf(DBQueries.addressSelected + 1), true);

                    previousAddress = DBQueries.addressSelected;
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("USER_DATA")
                            .document("MY_ADDRESSES").update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                    } else {
                                        previousAddress = previousAddressIndex;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(MyAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });
                } else {
                    finish();
                }
            }
        });

        addressAdapter = new AddressAdapter(DBQueries.addressModelList, mode);
        addressRecyclerView.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAddressIntent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
                addAddressIntent.putExtra("INTENT", "null");
                startActivity(addAddressIntent);
            }
        });

    };

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(String.valueOf(DBQueries.addressModelList.size()) + "addresses saved");
    }

    public static void refreshItem(int deselect, int select) {
        addressAdapter.notifyItemChanged(deselect);
        addressAdapter.notifyItemChanged(select);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (DBQueries.addressSelected != previousAddress) {
                DBQueries.addressModelList.get(DBQueries.addressSelected).setSelected(false);
                DBQueries.addressModelList.get(previousAddress).setSelected(true);
                DBQueries.addressSelected = previousAddress;
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (DBQueries.addressSelected != previousAddress) {
            DBQueries.addressModelList.get(DBQueries.addressSelected).setSelected(false);
            DBQueries.addressModelList.get(previousAddress).setSelected(true);
            DBQueries.addressSelected = previousAddress;
        }
        super.onBackPressed();
    }
}
package com.example.appbangiay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectfinal.models.AddressModel;
import com.example.projectfinal.utils.DBQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private Dialog loadingDialog;

    private TextView fullName;
    private TextView phoneNumber;
    private TextView city;
    private TextView district;
    private TextView ward;
    private TextView detailAddress;

    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // loading dialog
        loadingDialog = new Dialog(AddAddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // loading dialog

        fullName = findViewById(R.id.edit_text_address_fullname);
        phoneNumber = findViewById(R.id.edit_text_address_phone_number);
        city = findViewById(R.id.edit_text_address_city);
        district = findViewById(R.id.edit_text_address_district);
        ward = findViewById(R.id.edit_text_address_ward);
        detailAddress = findViewById(R.id.edit_text_address_details);

        saveBtn = findViewById(R.id.save_address_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(fullName.getText())) {
                    fullName.setError("Full name is required");
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber.getText())) {
                    phoneNumber.setError("Phone number is required");
                    return;
                }
//                if (!phoneNumber.getText().matches(phoneNumberPattern)) {
//                    phoneNumber.setError("Phone invalid");
//                    return;
//                }
                if (TextUtils.isEmpty(city.getText())) {
                    city.setError("City is required");
                    return;
                }
                if (TextUtils.isEmpty(district.getText())) {
                    district.setError("Phone number is required");
                    return;
                }
                if (TextUtils.isEmpty(ward.getText())) {
                    ward.setError("Phone number is required");
                    return;
                }
                if (TextUtils.isEmpty(detailAddress.getText())) {
                    detailAddress.setError("Phone number is required");
                    return;
                }

                loadingDialog.show();
                Map<String, Object> addAddress = new HashMap<>();
                int index = DBQueries.addressModelList.size() + 1;
                int indexSelected = DBQueries.addressSelected + 1;
                String fullNameText = fullName.getText().toString();
                String phoneNumberText = phoneNumber.getText().toString();
                String fullAddress = detailAddress.getText().toString() + ", " + ward.getText().toString() + ", " + district.getText().toString() + ", " + city.getText().toString();
                addAddress.put("list_size", (long) DBQueries.addressModelList.size() + 1);
                addAddress.put("full_name_"+index, fullName.getText().toString());
                addAddress.put("phone_number_"+index, phoneNumber.getText().toString());
                addAddress.put("full_address_"+index, fullAddress);
                addAddress.put("selected_"+ index, true);
                if (DBQueries.addressModelList.size() > 0) {
                    addAddress.put("selected_"+indexSelected, false);
                }

                FirebaseFirestore.getInstance().collection("USERS")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("USER_DATA")
                        .document("MY_ADDRESSES").update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (DBQueries.addressModelList.size() > 0) {
                                        DBQueries.addressModelList.get(DBQueries.addressSelected).setSelected(false);
                                    }
                                    DBQueries.addressModelList.add(new AddressModel(fullNameText , phoneNumberText, fullAddress, true));

                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                        startActivity(deliveryIntent);
                                    } else {
                                        MyAddressActivity.refreshItem(DBQueries.addressSelected, DBQueries.addressModelList.size() - 1);
                                    }
                                    DBQueries.addressSelected = DBQueries.addressModelList.size() - 1;
                                    finish();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
            }
        });
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
}
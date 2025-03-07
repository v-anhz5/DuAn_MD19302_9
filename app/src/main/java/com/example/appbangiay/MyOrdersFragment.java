package com.example.appbangiay;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.adapter.MyOrderAdapter;
import com.example.projectfinal.utils.DBQueries;
import com.google.firebase.auth.FirebaseUser;
//Giao diện đơn hàng
public class MyOrdersFragment extends Fragment {

    public static MyOrderAdapter myOrderAdapter;
    public MyOrdersFragment() {
        // Required empty public constructor
    }
    private RecyclerView myOrderRecyclerView;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrderRecyclerView = view.findViewById(R.id.my_orders_recycler_view);

        // loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        // loading dialog

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);

        if (DBQueries.myOrderItemModelList.size() == 0) {
            DBQueries.orderList.clear();
            DBQueries.loadOrderList(getContext(), loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }

        myOrderAdapter = new MyOrderAdapter(DBQueries.myOrderItemModelList);
        myOrderRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
}
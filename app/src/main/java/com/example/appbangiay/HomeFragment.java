package com.example.appbangiay;

import static com.example.projectfinal.utils.DBQueries.categoryModelList;
import static com.example.projectfinal.utils.DBQueries.listHomePageCategories;
import static com.example.projectfinal.utils.DBQueries.loadCategories;
import static com.example.projectfinal.utils.DBQueries.loadCategoriesName;
import static com.example.projectfinal.utils.DBQueries.loadFragmentData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.projectfinal.adapter.CategoryAdapter;
import com.example.projectfinal.adapter.HomePageAdapter;
import com.example.projectfinal.models.CategoryModel;
import com.example.projectfinal.models.HomePageModel;
import com.example.projectfinal.models.HorizontalProductScrollModel;
import com.example.projectfinal.models.SliderModel;
import com.example.projectfinal.models.WishlistModel;

import java.util.ArrayList;
import java.util.List;

//Danh mục sản phẩm
public class HomeFragment extends Fragment {

    public static SwipeRefreshLayout swipeRefreshLayout;

    private List<CategoryModel> categoryFakeModelList = new ArrayList<CategoryModel>();
    private List<HomePageModel> homePageModelFakeList = new ArrayList<HomePageModel>();

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private RecyclerView categoryRecyclerView;
    private RecyclerView homePageRecyclerView;
    private CategoryAdapter categoryAdapter;
    private HomePageAdapter homePageAdapter;
    private ImageView noInternetConnection;

    // button retry connection
    private Button retryConnectBtn;
    // Banner Slider

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        retryConnectBtn = view.findViewById(R.id.retry_btn);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        homePageRecyclerView = view.findViewById(R.id.home_page_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager homePageLinearLayout = new LinearLayoutManager(getContext());
        homePageLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(homePageLinearLayout);

        // category model fake list
        categoryFakeModelList.add(new CategoryModel("null", "null"));
        categoryFakeModelList.add(new CategoryModel("", "null"));
        categoryFakeModelList.add(new CategoryModel("", "null"));
        categoryFakeModelList.add(new CategoryModel("", "null"));
        categoryFakeModelList.add(new CategoryModel("", "null"));
        categoryFakeModelList.add(new CategoryModel("", "null"));
        categoryAdapter = new CategoryAdapter(categoryFakeModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        // category model fake list

        // home page fake list
        homePageAdapter = new HomePageAdapter(homePageModelFakeList);
        homePageRecyclerView.setAdapter(homePageAdapter);
        List<SliderModel> sliderModelFakeList = new ArrayList<SliderModel>();
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<HorizontalProductScrollModel>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "",""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#dfdfdf", horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(2, "", "#dfdfdf", horizontalProductScrollModelFakeList));
        // home page fake list



        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryConnectBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            if (categoryModelList.size() == 0) {
                loadCategories(categoryRecyclerView, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            };
            categoryRecyclerView.setAdapter(categoryAdapter);
            // home page container

            if (listHomePageCategories.size() == 0) {
                loadCategoriesName.add("HOME");
                listHomePageCategories.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
            } else {
                homePageAdapter = new HomePageAdapter(listHomePageCategories.get(0));
                homePageAdapter.notifyDataSetChanged();
            }
            // home page container
            homePageRecyclerView.setAdapter(homePageAdapter);
        } else {
            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet_image).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryConnectBtn.setVisibility(View.VISIBLE);
        };

        // refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadHomePage();
            }
        });
        retryConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadHomePage();
            }
        });
        // refresh layout
        return view;
    }
    private void reloadHomePage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        // clear on data
        listHomePageCategories.clear();
        categoryModelList.clear();
        loadCategoriesName.clear();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryConnectBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            categoryAdapter = new CategoryAdapter(categoryFakeModelList);
            homePageAdapter = new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(homePageAdapter);

            loadCategories(categoryRecyclerView, getContext());
            loadCategoriesName.add("HOME");
            listHomePageCategories.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
        } else {
            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet_image).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryConnectBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}

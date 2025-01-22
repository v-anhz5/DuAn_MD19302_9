package com.example.appbangiay;

import static com.example.projectfinal.utils.DBQueries.listHomePageCategories;
import static com.example.projectfinal.utils.DBQueries.loadCategoriesName;
import static com.example.projectfinal.utils.DBQueries.loadFragmentData;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.adapter.HomePageAdapter;
import com.example.projectfinal.models.HomePageModel;
import com.example.projectfinal.models.HorizontalProductScrollModel;
import com.example.projectfinal.models.SliderModel;
import com.example.projectfinal.models.WishlistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private HomePageAdapter homePageAdapter;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<HomePageModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        // home page fake list
        List<SliderModel> sliderModelFakeList = new ArrayList<SliderModel>();
        sliderModelFakeList.add(new SliderModel("null", "#e9f0d0"));
        sliderModelFakeList.add(new SliderModel("null", "#e9f0d0"));
        sliderModelFakeList.add(new SliderModel("null", "#e9f0d0"));
        sliderModelFakeList.add(new SliderModel("null", "#e9f0d0"));
        sliderModelFakeList.add(new SliderModel("null", "#e9f0d0"));

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
        homePageModelFakeList.add(new HomePageModel(1, "", "#e9f0d0", horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(2, "", "#e9f0d0", horizontalProductScrollModelFakeList));
        // home page fake list


        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(this );
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLinearLayout);
        homePageAdapter = new HomePageAdapter(homePageModelFakeList);

        int listPosition = 0;
        for (int x = 0; x < loadCategoriesName.size(); x++) {
            if (loadCategoriesName.get(x).equals(title.toUpperCase(Locale.ROOT))) {
                listPosition = x;
            }
        };
        if (listPosition == 0) {
            loadCategoriesName.add(title.toUpperCase(Locale.ROOT));
            listHomePageCategories.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView, this, loadCategoriesName.size() - 1, title);
        } else {
            homePageAdapter = new HomePageAdapter(listHomePageCategories.get(listPosition));
        }
        categoryRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    };
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            // todo: search
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
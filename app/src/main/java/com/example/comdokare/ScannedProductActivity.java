package com.example.comdokare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.comdokare.databinding.ActivityScannedProductBinding;
import com.example.comdokare.ui.slideshow.SlideshowFragment;

import java.io.IOException;
import java.io.InputStream;


public class ScannedProductActivity extends AppCompatActivity implements IngredientsAdapter.RecyclerViewClickListener {
    private ActivityScannedProductBinding binding;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SlideshowFragment.LayoutManagerType mCurrentLayoutManagerType;
    private RecyclerView rvIngredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_product);
        binding = ActivityScannedProductBinding.inflate(getLayoutInflater());
        rvIngredients = findViewById(R.id.recyclerViewIngredients);
        mLayoutManager = new LinearLayoutManager(this);
        setRecyclerViewLayoutManager(SlideshowFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        ActionBar supportActionBar = getSupportActionBar();
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("scanned-product-key");
        if (product != null && supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(product.name);

            ImageView imageView = findViewById(R.id.imageView3);
            TextView textView = findViewById(R.id.textView6);
            textView.setText(product.description);
            try {
                imageView.setImageBitmap(getBitmapFromAssets(product.imageResource));
            } catch (IOException e) {
                e.printStackTrace();
            }
            rvIngredients.setAdapter(new IngredientsAdapter(product.ingredients, this));

        }
    }

    public Bitmap getBitmapFromAssets(String fileName) throws IOException {
        AssetManager assetManager = getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();

        return bitmap;
    }

    public void setRecyclerViewLayoutManager(SlideshowFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (rvIngredients.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) rvIngredients.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, 0);
                mCurrentLayoutManagerType = SlideshowFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = SlideshowFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        rvIngredients.setLayoutManager(mLayoutManager);
        rvIngredients.scrollToPosition(scrollPosition);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        IngredientsAdapter ingredientsAdapter = (IngredientsAdapter)this.rvIngredients.getAdapter();
        assert ingredientsAdapter != null;
        Ingredients ingredients = ingredientsAdapter.getItem(position);
        Intent myIntent = new Intent(this, IngredientDetailsActivity.class);
        myIntent.putExtra("ingredient-selected", ingredients);
        this.startActivity(myIntent);
    }
}
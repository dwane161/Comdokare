package com.example.comdokare.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comdokare.Product;
import com.example.comdokare.ProductAdapter;
import com.example.comdokare.R;
import com.example.comdokare.ScannedProductActivity;
import com.example.comdokare.databinding.FragmentSlideshowBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicolettilu.hiddensearchwithrecyclerview.HiddenSearchWithRecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SlideshowFragment extends Fragment implements ProductAdapter.RecyclerViewClickListener {

    private SlideshowViewModel slideshowViewModel;
    protected RecyclerView.LayoutManager mLayoutManager;
    private FragmentSlideshowBinding binding;
    private RecyclerView rvProducts;
    private SearchView searchView;

    @Override
    public void recyclerViewListClicked(View v, int position) {
        ProductAdapter productAdapter = (ProductAdapter)this.rvProducts.getAdapter();
        assert productAdapter != null;
        Product product = productAdapter.getItem(position);
        Intent myIntent = new Intent(getActivity().getBaseContext(), ScannedProductActivity.class);
        myIntent.putExtra("scanned-product-key", product);
        getActivity().startActivity(myIntent);
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rvProducts = binding.ListItemsProducts;
        searchView = binding.searchView;

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        RecyclerView rvProducts = binding.ListItemsProducts;

        mLayoutManager = new LinearLayoutManager(getActivity());
        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        List<Product> modelObject = getProducts(rvProducts);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    getProducts(rvProducts);
                } else {
                    Product product = modelObject.stream().filter(productFind -> productFind.name.toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))).findAny().orElse(null);
                    if (product != null) {
                        ArrayList<Product> products = new ArrayList<Product>();
                        products.add(product);
                        rvProducts.setAdapter(new ProductAdapter(products, SlideshowFragment.this));
                    } else {
                        rvProducts.setAdapter(new ProductAdapter(new ArrayList<Product>(), SlideshowFragment.this));
                    }

                }
                return false;
            }
        });

        return root;
    }

    @Nullable
    private List<Product> getProducts(RecyclerView rvProducts) {
        String data = getAssetJsonData(getActivity().getBaseContext());
        Type type = new TypeToken<List<Product>>() {
        }.getType();
        List<Product> modelObject = new Gson().fromJson(data, type);
        rvProducts.setAdapter(new ProductAdapter(modelObject, this));
        return modelObject;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Nullable
    public static String getAssetJsonData(@NonNull Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data-products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (rvProducts.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) rvProducts.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 0);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        rvProducts.setLayoutManager(mLayoutManager);
        rvProducts.scrollToPosition(scrollPosition);
    }
}
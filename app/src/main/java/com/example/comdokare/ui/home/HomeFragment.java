package com.example.comdokare.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.comdokare.databinding.FragmentHomeBinding;
import com.example.comdokare.databinding.FragmentSlideshowBinding;
import com.example.comdokare.ui.slideshow.SlideshowFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.RecyclerViewClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvRecents;
    protected SlideshowFragment.LayoutManagerType mCurrentLayoutManagerType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        rvRecents = binding.rvRecents;

        mLayoutManager = new LinearLayoutManager(getActivity());
        setRecyclerViewLayoutManager(SlideshowFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        getProducts(rvRecents);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void setRecyclerViewLayoutManager(SlideshowFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (rvRecents.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) rvRecents.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 0);
                mCurrentLayoutManagerType = SlideshowFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = SlideshowFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        rvRecents.setLayoutManager(mLayoutManager);
        rvRecents.scrollToPosition(scrollPosition);
    }

    @Nullable
    private List<Product> getProducts(RecyclerView rvProducts) {
        String data = getAssetJsonData(getActivity().getBaseContext());
        Type type = new TypeToken<List<Product>>() {
        }.getType();
        List<Product> modelObject = new Gson().fromJson(data, type);
        rvProducts.setAdapter(new ProductAdapter(modelObject, this, getActivity().getAssets()));
        return modelObject;
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
        ProductAdapter productAdapter = (ProductAdapter) this.rvRecents.getAdapter();
        assert productAdapter != null;
        Product product = productAdapter.getItem(position);
        Intent myIntent = new Intent(getActivity().getBaseContext(), ScannedProductActivity.class);
        myIntent.putExtra("scanned-product-key", product);
        getActivity().startActivity(myIntent);
    }
}
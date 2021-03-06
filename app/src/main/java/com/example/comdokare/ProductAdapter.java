package com.example.comdokare;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> localDataSet;
    private static RecyclerViewClickListener itemListener;
    private static AssetManager getAssets;


    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvName;
        private final TextView tvBarCode;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.textView);
            tvBarCode = (TextView) view.findViewById(R.id.textView4);
            imageView = (ImageView) view.findViewById(R.id.imageView4);
            view.setOnClickListener(this);

        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvBarCode() {
            return tvBarCode;
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public ProductAdapter(List<Product> dataSet, RecyclerViewClickListener itListener, AssetManager assets) {
        localDataSet = dataSet;
        itemListener = itListener;
        getAssets = assets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTvName().setText(localDataSet.get(position).name);
        viewHolder.getTvBarCode().setText(localDataSet.get(position).barcodeId);
        try {
            viewHolder.imageView.setImageBitmap(getBitmapFromAssets(localDataSet.get(position).imageResource));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public Product getItem(int position) {
        return localDataSet.get(position);
    }


    public Bitmap getBitmapFromAssets(String fileName) throws IOException {
        AssetManager assetManager = getAssets;

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();

        return bitmap;
    }
}

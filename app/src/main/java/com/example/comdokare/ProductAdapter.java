package com.example.comdokare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> localDataSet;
    private static RecyclerViewClickListener itemListener;


    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvName;
        private final TextView tvBarCode;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.textView);
            tvBarCode = (TextView) view.findViewById(R.id.textView4);
            view.setOnClickListener(this);

        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvBarCode() {
            return tvBarCode;
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
    public ProductAdapter(List<Product> dataSet, RecyclerViewClickListener itListener) {
        localDataSet = dataSet;
        itemListener = itListener;
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
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public Product getItem(int position) {
        return localDataSet.get(position);
    }
}

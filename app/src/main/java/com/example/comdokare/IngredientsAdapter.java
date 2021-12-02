package com.example.comdokare;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<Ingredients> localDataSet;
    private static IngredientsAdapter.RecyclerViewClickListener itemListener;


    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final TextView tvName;
        private final TextView tvDescription;
        private final ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.textView);
            tvDescription = (TextView) view.findViewById(R.id.textView4);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
            view.setOnClickListener(this);
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvDescription() {
            return tvDescription;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
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
    public IngredientsAdapter(List<Ingredients> dataSet, IngredientsAdapter.RecyclerViewClickListener itListener) {
        localDataSet = dataSet;
        itemListener = itListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_ingredients, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTvName().setText(localDataSet.get(position).name);
        viewHolder.getTvDescription().setText(localDataSet.get(position).description);
        int progress = Integer.parseInt(localDataSet.get(position).comedogenity) * 20;
        viewHolder.getProgressBar().setProgress(progress);
        viewHolder.getProgressBar().setProgressTintList(ColorStateList.valueOf(getColor(progress)));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public Ingredients getItem(int position) {
        return localDataSet.get(position);
    }

    public int getColor(int value) {
        if (value <= 20) {
            return Color.GREEN;
        } else if (value <= 40) {
            return Color.BLUE;
        } else if (value <= 60) {
            return Color.YELLOW;
        } else if (value <= 80) {
            return Color.RED;
        } else if (value <= 100) {
            return Color.BLACK;
        }
        return Color.BLUE;
    }

}

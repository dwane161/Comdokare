package com.example.comdokare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IngredientDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);
        TextView tvDescription = (TextView) findViewById(R.id.textView8);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        Intent intent = getIntent();
        Ingredients ingredients = (Ingredients) intent.getSerializableExtra("ingredient-selected");
        ActionBar supportActionBar = getSupportActionBar();
        if (ingredients != null && supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(ingredients.name);
            tvDescription.setText(ingredients.description);
            int value = Integer.parseInt(ingredients.comedogenity) * 20;
            progressBar.setProgress(value);
            progressBar.setProgressTintList(ColorStateList.valueOf(getColorProgressBar(value)));
        }
    }


    public int getColorProgressBar(int value) {
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
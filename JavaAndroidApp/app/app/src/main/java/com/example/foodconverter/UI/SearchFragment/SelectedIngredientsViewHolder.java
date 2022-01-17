package com.example.foodconverter.UI.SearchFragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.R;

public class SelectedIngredientsViewHolder extends RecyclerView.ViewHolder {

    private final TextView ingredientName;

    public SelectedIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientName = itemView.findViewById(R.id.constructed_meal_name);
    }

    public void bindView(String ingredient){
        ingredientName.setText(ingredient);
    }
}

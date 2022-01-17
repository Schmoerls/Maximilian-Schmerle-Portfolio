package com.example.foodconverter.UI.SearchFragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.R;

public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ViewHolderListener listener;


    private final TextView ingredientName;

    public IngredientsViewHolder(@NonNull View itemView, ViewHolderListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        ingredientName = itemView.findViewById(R.id.constructed_meal_name);
        this.listener = listener;
    }

    public void bindView(String ingredient){
        ingredientName.setText(ingredient);
    }

    @Override
    public void onClick(View v) {

        listener.ingredientsSelected(getAdapterPosition());
    }

    public interface ViewHolderListener {

        void ingredientsSelected(int ingredientsPosition);

    }
}

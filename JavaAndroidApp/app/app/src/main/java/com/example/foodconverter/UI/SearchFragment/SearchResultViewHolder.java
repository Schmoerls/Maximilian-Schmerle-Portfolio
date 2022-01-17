package com.example.foodconverter.UI.SearchFragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.R;

public class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final ViewHolderListener listener;

    private final TextView recipeName;
    private final TextView recipeCount;

    public SearchResultViewHolder(View itemView, ViewHolderListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        recipeName = itemView.findViewById(R.id.list_entry_name);
        recipeCount = itemView.findViewById(R.id.list_entry_number);
        this.listener = listener;
    }

    public void bindView(Recipe recipe, String count){
        recipeName.setText(recipe.getName());
        recipeCount.setText(count);
        //recipeName.setBackgroundColor();
        //recipeCount.setBackgroundColor();
    }

    @Override
    public void onClick(View view) {

        listener.recipeSelected(getAdapterPosition());
    }

    public interface ViewHolderListener {

        void recipeSelected(int recipePosition);

    }
}

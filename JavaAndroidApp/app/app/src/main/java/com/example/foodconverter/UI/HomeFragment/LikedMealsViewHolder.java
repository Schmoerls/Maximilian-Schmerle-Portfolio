package com.example.foodconverter.UI.HomeFragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.R;

public class LikedMealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final ViewHolderListener listener;

    private final TextView mealName;

    public LikedMealsViewHolder(View itemView, ViewHolderListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        mealName = itemView.findViewById(R.id.constructed_meal_name);
        this.listener = listener;
    }

    public void bindView(String meal){mealName.setText(meal);}

    @Override
    public void onClick(View view) {
        listener.recipeSelected(getAdapterPosition());
    }

    public interface ViewHolderListener {
        void recipeSelected(int recipePosition);
    }
}

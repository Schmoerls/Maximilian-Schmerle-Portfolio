package com.example.foodconverter.UI.HomeFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class LikedMealsAdapter extends RecyclerView.Adapter<LikedMealsViewHolder> implements LikedMealsViewHolder.ViewHolderListener{

    private final AdapterListener listener;

    private ArrayList<Recipe> recipes;

    public LikedMealsAdapter(AdapterListener listener){
        recipes = new ArrayList<>();
        this.listener = listener;
    }

    public void createMeals(ArrayList<Recipe> mealInput){
        this.recipes = new ArrayList<>(mealInput);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LikedMealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructed_meal, parent, false);
        return new LikedMealsViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(LikedMealsViewHolder holder, int position) {
        String meal = recipes.get(position).getName();
        holder.bindView(meal);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void recipeSelected(int recipePosition) {
        listener.recipeClicked(recipePosition);
        this.notifyDataSetChanged();
    }

    public interface AdapterListener{
        void recipeClicked(int recipePosition);
    }
}

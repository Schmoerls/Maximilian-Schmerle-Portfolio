package com.example.foodconverter.UI.ConstructFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class ConstructedMealsAdapter extends RecyclerView.Adapter<ConstructedMealsViewHolder> implements ConstructedMealsViewHolder.ViewHolderListener{

    private final AdapterListener listener;

    private ArrayList<Recipe> recipes;

    public ConstructedMealsAdapter(AdapterListener listener){
        recipes = new ArrayList<>();
        this.listener = listener;
    }

    public void createMeals(ArrayList<Recipe> mealInput){
        this.recipes = new ArrayList<>(mealInput);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConstructedMealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructed_meal, parent, false);
        return new ConstructedMealsViewHolder(v, this);

    }

    @Override
    public void onBindViewHolder(@NonNull ConstructedMealsViewHolder holder, int position) {
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

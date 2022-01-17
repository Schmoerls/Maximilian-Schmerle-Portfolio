package com.example.foodconverter.UI.SearchFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> implements IngredientsViewHolder.ViewHolderListener {

    private final AdapterListener listener;

    private ArrayList<Ingredient> ingredients;

    public IngredientsAdapter(AdapterListener listener){
        this.listener = listener;
    }

    public void createIngredients(ArrayList<Ingredient> ingredientsInput){
        ingredients = ingredientsInput;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructed_meal, parent, false);
        return new IngredientsViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        String ingredient = ingredients.get(position).getName();
        holder.bindView(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void ingredientsSelected(int ingredientsPos) {
        listener.ingredientsUpdate(ingredientsPos);
    }

    public interface AdapterListener{
        void ingredientsUpdate(int ingredientPos);
    }
}

package com.example.foodconverter.UI.SearchFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class SelectedIngredientsAdapter extends RecyclerView.Adapter<SelectedIngredientsViewHolder>{

    private ArrayList<Ingredient> selectedIngredientsAsClasse;

    public void createSelectedIngredients(ArrayList<Ingredient> ingredientsInput){
        selectedIngredientsAsClasse = ingredientsInput;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectedIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructed_meal, parent, false);
        return new SelectedIngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedIngredientsViewHolder holder, int position) {
        String ingredient = selectedIngredientsAsClasse.get(position).getName();
        holder.bindView(ingredient);
    }

    @Override
    public int getItemCount() {
        return selectedIngredientsAsClasse.size();
    }
}

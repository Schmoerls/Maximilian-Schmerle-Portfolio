package com.example.foodconverter.UI.ConstructFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class IngredientsAmountAdapter extends RecyclerView.Adapter<IngredientsAmountViewHolder> implements IngredientsAmountViewHolder.IngredientsAmountListener {

    private ArrayList<Ingredient> ingredients;

    private AmountListener listener;

    public IngredientsAmountAdapter(AmountListener listener){
        ingredients = new ArrayList<>();
        this.listener = listener;
    }

    public void createIngredientsAmount(ArrayList<Ingredient> input){
        ingredients = input;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsAmountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_amount, parent, false);
        return new IngredientsAmountViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAmountViewHolder holder, int position) {
        String ingredient = ingredients.get(position).getName();
        holder.bindView(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void ingredientAmountChanged(String amount, int position) {
        listener.amountChanged(amount, position);
    }

    public interface AmountListener{
        void amountChanged(String amount, int position);
    }
}

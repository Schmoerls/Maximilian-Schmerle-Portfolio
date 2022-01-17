package com.example.foodconverter.UI.SearchFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultViewHolder> implements SearchResultViewHolder.ViewHolderListener{

    private final AdapterListener listener;

    private ArrayList<Recipe> recipes;
    private ArrayList<Integer> count;

    public SearchResultsAdapter(AdapterListener listener){
        this.listener = listener;
        this.recipes = new ArrayList<>();
        this.count = new ArrayList<>();
    }

    public void createRecipes(ArrayList<Recipe> recipeInput, ArrayList<Integer> countInput){
        recipes = recipeInput;
        count = countInput;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_result_meal_list_entry, parent, false);
        return new SearchResultViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        String singleCount = String.valueOf(count.get(position));
        holder.bindView(recipe, singleCount);
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

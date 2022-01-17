package com.example.foodconverter.Data.Interfaces;

import com.example.foodconverter.Data.Database.Recipe;

import java.util.ArrayList;

//Interface um StartScreenActivity gefilterte Rezepte zu übergeben durch das SearchResultFragment
public interface ListenFromSearchResultFragment {
    void filterApplied(ArrayList<Recipe> filteredRecipes);
}

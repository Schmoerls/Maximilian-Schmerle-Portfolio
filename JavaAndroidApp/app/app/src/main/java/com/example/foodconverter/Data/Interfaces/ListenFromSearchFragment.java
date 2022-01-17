package com.example.foodconverter.Data.Interfaces;

import com.example.foodconverter.Data.Ingredient;

import java.util.ArrayList;

//Interface um StartScreenActivity über ausgewählte Zutaten im SearchFragment zu informieren
public interface ListenFromSearchFragment {
    void convertButtonClicked(ArrayList<Ingredient> selectedIngredients, Boolean constructMode);
}

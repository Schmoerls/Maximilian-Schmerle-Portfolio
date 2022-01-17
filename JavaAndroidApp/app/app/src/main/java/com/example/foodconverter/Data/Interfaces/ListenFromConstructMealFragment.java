package com.example.foodconverter.Data.Interfaces;

import com.example.foodconverter.Data.Database.Recipe;

//Interface um StartScreenActivity über neues Rezept aus ConstructMealFragment zu informieren zu informieren
public interface ListenFromConstructMealFragment {
    void constructButtonClicked(Recipe recipe);
}

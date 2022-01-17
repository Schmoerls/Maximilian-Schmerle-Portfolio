package com.example.foodconverter.Data.Database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;

public class RecipeDatabaseHelper {

    //Variablen, Konstruktor für DB-Helper

    private static final String DATABASE_NAME = "recipe-db";
    private final Context context;
    private RecipeDatabase db;

    public RecipeDatabaseHelper(Context context){
        this.context = context;
        initDatabase();
    }

    private void initDatabase(){
        db = Room.databaseBuilder(context, RecipeDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }

    //Methode zum hinzufügen eines Rezeptes mit Hilfe der DAO
    public void addRecipe(Recipe recipe){
        db.recipeDOA().insertRecipe(recipe);
    }

    //Methode um ein Rezept in der DB, bzgl. Like-Boolean zu updaten mit Hilfe der DAO
    public void updateLike(Boolean input, int ID){db.recipeDOA().updateLike(input, ID);}

    //Methode um ein Rezept in der DB, bzgl. Bookmark-Boolean zu updaten mit Hilfe der DAO
    public void updateBookmarked(Boolean input, int ID){db.recipeDOA().updateBookmarked(input, ID);}

    //Ausgabe aller Rezepte aus der DB
    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> output = new ArrayList<>();
        Recipe[] recipes = db.recipeDOA().getAll();
        for (Recipe r: recipes) {
            output.add(r);
        }
        return output;
    }

    //Löschen aller Rezepte aus der DB
    public void deleteAllRecipes(){
        db.recipeDOA().delete();
    }
}

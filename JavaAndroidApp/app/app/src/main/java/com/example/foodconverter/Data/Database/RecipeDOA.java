package com.example.foodconverter.Data.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface RecipeDOA {

    //DAO mit deren SQL-Befehlen auf die DB zugegriffen wird

    @Insert
    void insertRecipe(Recipe recipe);

    @Update
    void updateRecipe(Recipe recipe);

    @Query("DELETE from recipe")
    public void delete();

    @Query("SELECT * from recipe WHERE id= :inputId")
    Recipe getRecipeForID(int inputId);

    @Query("SELECT * from recipe")
    public Recipe[] getAll();

    @Query("UPDATE recipe SET `like`=:input WHERE id =:ID")
    void updateLike(Boolean input, int ID);

    @Query("UPDATE recipe SET `bookmarked`=:input WHERE id =:ID")
    void updateBookmarked(Boolean input, int ID);
}

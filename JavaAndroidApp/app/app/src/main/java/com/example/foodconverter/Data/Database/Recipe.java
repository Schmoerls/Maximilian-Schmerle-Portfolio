package com.example.foodconverter.Data.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.WorkingStep;

import java.util.ArrayList;

//Recipe Klasse mit deren Hilfe Rezepte erstellt und in DB gespeichert werden können

@Entity(tableName = "recipe")
public class Recipe implements Comparable<Recipe>{

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "ingredients")
    public ArrayList<Ingredient> ingrediens;

    @ColumnInfo(name = "steps")
    public String instruction;

    @ColumnInfo(name = "vegetarian")
    public Boolean vegetarian;

    @ColumnInfo(name = "ownMeal")
    public Boolean ownMeal;

    @ColumnInfo(name = "vegan")
    public Boolean vegan;

    @ColumnInfo(name = "like")
    public Boolean like;

    @ColumnInfo(name = "bookmarked")
    public Boolean bookmarked;

    public Recipe (String name, ArrayList<Ingredient> ingrediens, String instruction, Boolean vegetarian, Boolean ownMeal, Boolean vegan) {
        this.name = name;
        this.ingrediens = ingrediens;
        this.instruction = instruction;
        this.vegetarian = vegetarian;
        this.ownMeal = ownMeal;
        this.vegan = vegan;
        this.like = false;
        this.bookmarked = false;
    }

    //Getter-Methoden

    public ArrayList<Ingredient> getIngrediens(){
        return ingrediens;
    }

    public String getName(){
        return name;
    }

    public String getInstruction(){
        return instruction;
    }

    public Boolean getVegetarian(){return vegetarian;}

    public Boolean getVegan(){return vegan; }

    public Boolean getOwnMeal(){return ownMeal;}

    public int getId(){return id; }

    public Boolean getLike(){return like;}

    public Boolean getBookmarked(){return bookmarked;}

    //Setter-Methoden

    public void setLike(Boolean like) {
        this.like = like;
    }

    public void setBookmarked(Boolean bookmarked){
        this.bookmarked = bookmarked;
    }

    //Diese Methode ist für die Comparable-Implementation um Gerichte miteinander zu vergleichen

    @Override
    public int compareTo(Recipe recipe) {
        if(this.name.charAt(0) < recipe.name.charAt(0)){
            return -1;
        }else if(this.name.charAt(0) == recipe.name.charAt(0)){
            return 0;
        }else {
            return 1;
        }
    }
}

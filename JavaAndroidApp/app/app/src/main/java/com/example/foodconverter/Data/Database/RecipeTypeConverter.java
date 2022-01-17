package com.example.foodconverter.Data.Database;

import android.util.Log;

import androidx.room.TypeConverter;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.WorkingStep;

import java.util.ArrayList;

public class RecipeTypeConverter {

    //TypeConverter für Ingredients ArrayList, welche in den Recipes vorkommt, um diese in der DB zu speichern

    @TypeConverter
    public static ArrayList<Ingredient> fromStringToIngrConstructMeal(String input) {
        ArrayList<Ingredient> output = new ArrayList<>();
        int i = 1;
        for (String s : input.split("@")){
            if(!s.isEmpty()){
                String[] array = s.split("#");
                if(array.length > 1) {
                    output.add(new Ingredient(i, array[0], array[1]));
                }else{
                    output.add(new Ingredient(i, array[0]));
                }
                i++;
            }
        }
        return output;
    }

    @TypeConverter
    public static String fromIngrConstructMealToString(ArrayList<Ingredient> input) {
        String output = "";
        for (Ingredient w : input) {
            output +=  w.getName() + "#" + w.getAmount() + "@";
        }
        return output;
    }

}

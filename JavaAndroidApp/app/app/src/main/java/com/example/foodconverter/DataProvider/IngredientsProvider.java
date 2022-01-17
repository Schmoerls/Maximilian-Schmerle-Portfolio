package com.example.foodconverter.DataProvider;

import android.content.Context;
import android.util.Log;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.WorkingStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//Diese Klasse dient zum lesen des JSON-Files, wenn die App zum ersten mal auf einem Handy gestartet wird
public class IngredientsProvider{

    //Diese Methode läd die JSON Strings und Arrays und verarbeitet diese in Recipe Objekte, welche dann in eine ArrayList überführt werden
    public ArrayList<Recipe> provideRecipes(Context context){
        ArrayList<Recipe> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(loadJSON(context, "FinalRecipeCutted.json"));
            JSONArray array = object.getJSONArray("Rezepte");
            for (int i = 0; i < 455; i++){
                Log.i("info", String.valueOf(i));
                String name = array.getJSONObject(i).getString("name");
                ArrayList<Ingredient> ingr = new ArrayList<>();
                String instruction = array.getJSONObject(i).getString("instruction");
                JSONArray ingArray = array.getJSONObject(i).getJSONArray("ingredients");
                Boolean vegetarian = false;
                Boolean vegan = false;
                String dietary = array.getJSONObject(i).getString("dietary_consideration");
                String[] dietarySplit = dietary.split(",");
                for (String s : dietarySplit) {
                    if(s.equals("Vegetarian") || s.equals("vegetarian") || s.equals(" vegetarian") || s.equals(" Vegetarian")){
                        vegetarian = true;
                    }else if(s.equals("vegan") || s.equals("Vegan")){
                        vegan = true;
                    }
                }
                for (int j = 0; j < ingArray.length(); j++){
                    String ingredientName;
                    String amount;
                    if(!(ingArray.getJSONObject(j).getJSONArray("ingredients").length() == 0)) {
                         ingredientName = ingArray.getJSONObject(j).getJSONArray("ingredients").getString(0);
                    }else{
                         ingredientName = "0Missing";
                    }
                    if(ingArray.getJSONObject(j).getString("line") != null ) {

                        amount = ingArray.getJSONObject(j).getString("line");
                    }else{
                        Log.i("info", "ERROR");
                        amount = "error";
                    }
                    ingr.add(new Ingredient(j, ingredientName, amount));
                }
                list.add(new Recipe(name, ingr, instruction, vegetarian, false, vegan));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Diese Methode läd das grundlegende JSONObject für die obere Methode
    public String loadJSON(Context context, String name) {
        String json = null;
        try {
            InputStream input = context.getAssets().open(name);
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

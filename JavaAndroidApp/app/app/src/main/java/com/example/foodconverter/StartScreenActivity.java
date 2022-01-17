package com.example.foodconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Database.RecipeDatabaseHelper;
import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.Interfaces.ListenFromConstructMealFragment;
import com.example.foodconverter.Data.Interfaces.ListenFromSearchFragment;
import com.example.foodconverter.Data.Interfaces.ListenFromSearchResultFragment;
import com.example.foodconverter.DataProvider.IngredientsProvider;
import com.example.foodconverter.databinding.MainActivityBinding;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class StartScreenActivity extends AppCompatActivity implements ListenFromSearchFragment, ListenFromConstructMealFragment, ListenFromSearchResultFragment {

    //MainActivity, welche die Daten der App hält und die Fragments, SiteNavigation und den DataProvider anstößt

    private boolean constructMode;
    public RecipeDatabaseHelper db;
    private ArrayList<Recipe> dbRecipes;
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;
    private IngredientsProvider provider;
    private ArrayList<Recipe> allRecipes;
    private ArrayList<Recipe> anyRecipes;
    private ArrayList<Integer> comparer;
    private ArrayList<Recipe> filteredRecipes;
    private ArrayList<Ingredient> selectedIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        newInit();
        initData();
    }

    private void initData() {

        //ArrayLists, DB-Helper & Provider werden initialisiert
        allRecipes = new ArrayList<>();
        anyRecipes = new ArrayList<>();
        ingredients = new ArrayList<>();
        selectedIngredients = new ArrayList<>();
        comparer = new ArrayList<>();
        filteredRecipes = new ArrayList<>();
        provider = new IngredientsProvider();
        db = new RecipeDatabaseHelper(getApplicationContext());

        //wenn die App noch nie vorher gestartet wurde ist die DB leer und erhält Daten aus dem JSON-File
        if(db.getAllRecipes().size() == 0){injectDBData();}
        organizeData();
    }

    private void injectDBData() {
        dbRecipes = provider.provideRecipes(getApplicationContext());
        for (Recipe r : dbRecipes) {
            db.addRecipe(r);
        }
    }

    //laden aller Rezepte aus der DB und sortieren, sowie Filtern der Zutaten und sortieren
    private void organizeData(){
        recipes = db.getAllRecipes();
        Collections.sort(recipes);
        reconstructRecipes(recipes);
        Collections.sort(ingredients);
    }

    //mit diesen Methoden holen sich die Fragments die ArrayLists
    public ArrayList<Ingredient> getIngredients(){
        return ingredients;
    }

    public ArrayList<Recipe> getAllRecipes(){
        return allRecipes;
    }

    public ArrayList<Recipe> getAnyRecipes(){
        return anyRecipes;
    }

    public ArrayList<Recipe> getRecipes(){return recipes; }

    public ArrayList<Integer> getComparer(){return comparer; }

    public ArrayList<Recipe> getLikedRecipes(){
        ArrayList<Recipe> output = new ArrayList<>();
        if(recipes == null){return output;}
        for (Recipe r : recipes) {
            if(r.getLike()){
                output.add(r);
            }
        }
        return output;
    }

    public ArrayList<Ingredient> getSelectedIngredients(){return selectedIngredients; }

    public ArrayList<Recipe> getFilteredRecipes(){return filteredRecipes;}

    //Diese Methode nimmt alle Rezepte und Filtert die Zutaten heraus, falls es doppelte Zutaten gibt, werden diese mit der gleichen Zahl belegt und nicht doppelt aufgenommen
    private void reconstructRecipes(ArrayList<Recipe> recipes){
        ingredients.clear();
        for (Recipe r : recipes) {
            boolean newOne;
            for (Ingredient i : r.getIngrediens()) {
                i.setName(i.getName().toLowerCase());
                newOne = true;
                for (Ingredient j : ingredients) {
                    if(j.getName().equals(i.getName())){
                        i.setNumber(j.getNumber());
                        newOne = false;
                    }
                }
                if(newOne){
                    i.setNumber(ingredients.size());
                    ingredients.add(i);
                }
            }
        }
    }

    private void newInit(){
        constructMode = false;

        //Erstellen eines Objekts aus der XML-Datei(main_activity), was UI repräsentiert und setzen als Inhalt der Activity
        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Erstellen der AppBar Configuration
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_construct, R.id.navigation_home, R.id.navigation_search).build();
        NavController navController = Navigation.findNavController(this, R.id.fragment_host); //Refferenzieren des Fragments in main-xml
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.siteNavigation, navController);
    }

    //erste Listener Methode, die aufgerufen wird, wenn im SearchFragment Zutaten ausgewählt wurden, es wird entschieden ob das bei der Meal Such oder konstruktion aufgerufen wurde
    //falls Search wird die Rezept.Filterung aufgerufen
    @Override
    public void convertButtonClicked(ArrayList<Ingredient> selectedIngredientsInput, Boolean constructMode) {
        if(constructMode){
            this.selectedIngredients = selectedIngredientsInput;
            Collections.sort(this.selectedIngredients);
        }else{
            anyRecipes.clear();
            allRecipes.clear();
            comparer.clear();
            constructSelectedRecipes(selectedIngredientsInput);
        }
    }

    //die Rezepte werden hier auf Grund der ausgewählten Gerichtte gefiltert und in zwei ArrayLists aufgeteilt All/Any bei any werden auch die Anzahl der übereinstimmenden Zutaten
    //in einer seperaten ArrayList gespeichert
    private void constructSelectedRecipes(ArrayList<Ingredient> selectedIngredients) {
        for (Recipe r : recipes) {
            ArrayList<Ingredient> compareIngredients = new ArrayList<>();
            compareIngredients.addAll(selectedIngredients);
            int numberOfIngrediens = r.getIngrediens().size();
            int count = 0;
            for (Ingredient i : r.getIngrediens()) {
                for (Ingredient j : compareIngredients) {
                    if(j.getNumber() == i.getNumber()){
                        count++;
                        compareIngredients.remove(j);
                        break;
                    }
                }
            }
            if(count == numberOfIngrediens){
                allRecipes.add(r);
            }else if(count >= 1){
                anyRecipes.add(r);
                comparer.add(count);
            }
        }
        sortTest(0, anyRecipes.size()-1);
        Collections.reverse(anyRecipes);
        Collections.reverse(comparer);
    }

    //diese Methode sortiert die AnyArrayList auf Grund der Anzahl der Zutaten integer ArrayList (rekursiver Algorithmus aus ADP^^)
    private void sortTest(int L, int R) {
        if(L < R){
            int M = (L + R) / 2;
            int Pivot = comparer.get(M);
            int L2 = L;
            int R2 = R;
            do {
                while (comparer.get(L2) < Pivot){L2++;}
                while (comparer.get(R2) > Pivot){R2--;
                }
                if(L2 <= R2){
                    Collections.swap(comparer, L2, R2);
                    Collections.swap(anyRecipes, L2, R2);
                    L2++;
                    R2--;
                }
            }while (L2 <= R2);
            sortTest(L, R2);
            sortTest(L2, R);
        }
    }

    //listener Methode aus ConstructMealFragment um Rezept zur Datenbank hinzu zufügen
    @Override
    public void constructButtonClicked(Recipe recipe) {
        db.addRecipe(recipe);
        organizeData();
    }

    //listener Methode zum Updaten der FilteredRecipes ArrayList durch SearchresultFragment
    @Override
    public void filterApplied(ArrayList<Recipe> filteredRecipes) {
        this.filteredRecipes = filteredRecipes;
    }
}

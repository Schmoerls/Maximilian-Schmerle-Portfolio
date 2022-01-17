package com.example.foodconverter.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Database.RecipeDatabaseHelper;
import com.example.foodconverter.UI.ConstructFragment.ConstructedMealsAdapter;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;

import java.util.ArrayList;

public class ConstructFragment extends Fragment implements ConstructedMealsAdapter.AdapterListener{

    //Diese Klasse ist die Grundklasse der Gerichterstlellung mit ihr werden alle Gerichte in der DB mit einer RecycleView dargstellt und diese können nach ALL/OWN gefiltert werden
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Recipe> ownRecipes = new ArrayList<>();
    private ConstructedMealsAdapter adapter;
    private RecipeDatabaseHelper db;
    private boolean justOwnMeals;

    //Fragment Instanzierung es werden die Recipes aus der StartScreenActivity geladen die View inflatet und weitere Methoden angestoßen
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        recipes.addAll(activity.getRecipes());
        return createAndInitFragment(inflater, container);
    }

    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.meal_construction_overview, root, false);
        initData();
        initUI(fragmentView);
        return fragmentView;
    }

    //Filtern nach eigenen Gerichten und initialisiern der DB
    private void initData() {
        justOwnMeals = false;
        for (Recipe r : recipes) {
            if(r.getOwnMeal()){ownRecipes.add(r);}
        }
        initDatabase();
    }

    private void initDatabase() {
        db = new RecipeDatabaseHelper(getActivity().getApplicationContext());
    }

    //Erstellen und befüllen der RecyclerView und aufsetzen der Button und Switches
    private void initUI(View fragmentView){
        RecyclerView viewOfConstructedMeals = fragmentView.findViewById(R.id.construcedMeals);
        adapter = new ConstructedMealsAdapter(this);
        viewOfConstructedMeals.setAdapter(adapter);
        adapter.createMeals(recipes);
        setUpConvertButton(fragmentView);
        setUpOwnMealSwitch(fragmentView);
    }

    private void setUpConvertButton(View fragmentView) {
        Button constructButton = fragmentView.findViewById(R.id.constructButton);
        constructButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment();
            }
        });
        Button deleteButton = fragmentView.findViewById(R.id.delete_all_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllRecipes();
            }
        });
    }

    private void setUpOwnMealSwitch(View fragmentView) {
        Switch ownMealSwitch = fragmentView.findViewById(R.id.own_meal_switch);
        ownMealSwitch.setText("All");
        ownMealSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                justOwnMeals = b;
                if(justOwnMeals){
                    ownMealSwitch.setText("Own");
                    adapter.createMeals(ownRecipes);
                }else {
                    ownMealSwitch.setText("All");
                    adapter.createMeals(recipes);
                }
            }
        });
    }

    //Start des SearchFragments aufgerufen durch klicken des Construct Buttons
    private void startFragment(){
        SearchFragment nextFrag = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("constructMode", true);
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "search_fragment")
                .addToBackStack(null)
                .commit();
    }

    //Listener Methode aus Adapter, wenn ein Rezept geklickt wurde und sofortiges starten des SelectedMealFragment mit übergabe der Rezept Position
    @Override
    public void recipeClicked(int recipePosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("mealNumber", recipePosition);
        bundle.putBoolean("construct", true);
        bundle.putBoolean("own", justOwnMeals);
        SelectedMealFragment nextFrag= new SelectedMealFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "selected_meal_fragment")
                .addToBackStack(null)
                .commit();
    }
}

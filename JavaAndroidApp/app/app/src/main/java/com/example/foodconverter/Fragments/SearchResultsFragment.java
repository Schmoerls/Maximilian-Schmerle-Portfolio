package com.example.foodconverter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Interfaces.ListenFromSearchResultFragment;
import com.example.foodconverter.UI.SearchFragment.SearchResultsAdapter;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;

import java.util.ArrayList;

public class SearchResultsFragment extends Fragment implements SearchResultsAdapter.AdapterListener{

    //dieses Fragment zeigt die Ergebnisse der Filterung der Rezept-DB auf Grund der ausgewählten Zutaten, außerdem befinden sich hier die Filter
    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private ArrayList<Recipe> anyRecipes = new ArrayList<>();
    private ArrayList<Integer> count = new ArrayList<>();
    private ArrayList<Integer> countForAll = new ArrayList<>();
    private SearchResultsAdapter adapter;
    private boolean any;
    private boolean vegetarian;
    private boolean vegan;
    private ListenFromSearchResultFragment listener;

    //Fragment Instanzierung mit Abruf der Daten aus der StartScreenActivity und erstellung der AllCount ArrayList
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        allRecipes.addAll(activity.getAllRecipes());
        anyRecipes.addAll(activity.getAnyRecipes());
        count.addAll(activity.getComparer());
        for (Recipe r : allRecipes) {
            countForAll.add(r.getIngrediens().size());
        }
        return createAndInitFragment(inflater, container);
    }

    //StartScreenActivity registriert sich als Listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ListenFromSearchResultFragment) getActivity();
    }

    //View wird inflatet und default Werte gesetzt
    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.meal_result_activity, root, false);
        initUI(fragmentView);
        any = true;
        listener.filterApplied(anyRecipes);
        vegetarian = false;
        vegan = false;
        return fragmentView;
    }

    //UI-SetUp RV, Switches werden instanziert
    private void initUI(View fragmentView) {
        RecyclerView viewOfRecipeNames = fragmentView.findViewById(R.id.recipe_names);
        adapter = new SearchResultsAdapter(this);
        viewOfRecipeNames.setAdapter(adapter);
        adapter.createRecipes(anyRecipes, count);
        setUpAllAnySwitch(fragmentView);
        setUpVegetarianSwitch(fragmentView);
        setUpVeganSwitch(fragmentView);
    }

    //Die drei Switches verändern ihre entsprechenden Boolean Werte und es wird immer die redoUI-Methode aufgerufen
    private void setUpAllAnySwitch(View fragmentView) {
        Switch anyAllSwitch = fragmentView.findViewById(R.id.anyAllSwitch);
        anyAllSwitch.setText("Any");
        anyAllSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    any = false;
                    anyAllSwitch.setText("All");
                    redoUI();
                }else {
                    any = true;
                    anyAllSwitch.setText("Any");
                    redoUI();
                }
            }
        });
    }
    private void setUpVegetarianSwitch(View fragmentView) {
        Switch anyAllSwitch = fragmentView.findViewById(R.id.vegatarianScwitch);
        anyAllSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vegetarian = true;
                }else {
                    vegetarian = false;
                }
                redoUI();
            }
        });
    }
    private void setUpVeganSwitch(View fragmentView) {
        Switch anyAllSwitch = fragmentView.findViewById(R.id.veganSwitch);
        anyAllSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vegan = true;
                }else {
                    vegan = false;
                }
                redoUI();
            }
        });
    }

    //hier wird nun auf Grund der Filter die Recipeliste verändert und am Ende dem Adapter und Listener übergeben
    private void redoUI(){
        ArrayList<Recipe> newRecipes = new ArrayList<>();
        ArrayList<Integer> newCount = new ArrayList<>();
        if(any){
            if(vegetarian || vegan){
                if(vegan){
                    for (int i = 0; i < anyRecipes.size(); i++) {
                        if(anyRecipes.get(i).getVegan()){
                            newRecipes.add(anyRecipes.get(i));
                            newCount.add(count.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i < anyRecipes.size(); i++) {
                        if(anyRecipes.get(i).getVegetarian()){
                            newRecipes.add(anyRecipes.get(i));
                            newCount.add(count.get(i));
                        }
                    }
                }
            }else{
                newRecipes = anyRecipes;
                newCount = count;
            }
        }else{
            if(vegetarian || vegan){
                if(vegan){
                    for (int i = 0; i < allRecipes.size(); i++) {
                        if(allRecipes.get(i).getVegan()){
                            newRecipes.add(allRecipes.get(i));
                            newCount.add(count.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i < allRecipes.size(); i++) {
                        if(allRecipes.get(i).getVegetarian()){
                            newRecipes.add(allRecipes.get(i));
                            newCount.add(count.get(i));
                        }
                    }
                }
            }else{
                newRecipes = allRecipes;
                newCount = countForAll;
            }
        }
        adapter.createRecipes(newRecipes, newCount);
        listener.filterApplied(newRecipes);
    }

    //wenn ein Rezept geklickt wird, wird das SelectedMealFragment gestartet und ihm die entsprechenden Daten im Bundel mitgegeben(Informationen bzgl. Modi, GeklicktesMeal, etc)
    @Override
    public void recipeClicked(int recipePosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("mealNumber", recipePosition);
        bundle.putBoolean("any", any);
        bundle.putBoolean("construct", false);
        SelectedMealFragment nextFrag= new SelectedMealFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "selected_meal_fragment")
                .addToBackStack(null)
                .commit();
    }
}

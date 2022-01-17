package com.example.foodconverter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.Interfaces.ListenFromSearchFragment;
import com.example.foodconverter.UI.SearchFragment.IngredientsAdapter;
import com.example.foodconverter.UI.SearchFragment.SelectedIngredientsAdapter;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements IngredientsAdapter.AdapterListener{

    //Dieses Fragment dient der Suche und Auswahl der Zutaten
    private Boolean constructMode;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Ingredient> selectedIngredients = new ArrayList<>();
    private ArrayList<Ingredient> filteredIngredients = new ArrayList<>();
    private IngredientsAdapter adapter;
    private SelectedIngredientsAdapter adapterSI;
    private ListenFromSearchFragment listener;
    private EditText editText;

    //Fragment Instanzierung, Daten laden und festlegen des Modus, weil dieses Fragment von zwei verschiedenen anderen Fragments gestartet werden kann
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        constructMode = getArguments().getBoolean("constructMode");
        ingredients.addAll(activity.getIngredients());
        return createAndInitFragment(inflater, container);
    }

    //StartScreenActivity registriert sich als Listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ListenFromSearchFragment) getActivity();
    }

    //UI SetUp RecyclerViews werden Instanziert ,die Buttons und EditText-Feld für die Suche instanziert
    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.meal_finding_category, root, false);
        modeInfo();
        initUI(fragmentView);
        setUpEditText(fragmentView);
        return fragmentView;
    }

    private void modeInfo() {
        constructMode = (Boolean) getArguments().get("constructMode");
    }

    private void initUI(View fragmentView){
        RecyclerView viewOfConstructedMeals = fragmentView.findViewById(R.id.ingredients_recylcer_view);
        adapter = new IngredientsAdapter(this);
        viewOfConstructedMeals.setAdapter(adapter);
        adapter.createIngredients(ingredients);

        RecyclerView viewOfSelectedIngredients = fragmentView.findViewById(R.id.selected_ingredients_recycler_view);
        adapterSI = new SelectedIngredientsAdapter();
        viewOfSelectedIngredients.setAdapter(adapterSI);
        adapterSI.createSelectedIngredients(selectedIngredients);


        setUpConvertButton(fragmentView);
    }

    //Der Convert Button startet auf Grund des beim start des Fragments übergebenen Modi unterschiedliche Folge-Fragments
    private void setUpConvertButton(View fragmentView) {
        Button convertButton = fragmentView.findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(constructMode)
                    startFragmentConstruct();
                else{
                    startFragmentResults();
                }
            }
        });
    }

    //Start des ConstructMealFragment um mit den ausgewählten Zutaten ein Gericht zu erstellen, die Zutaten werden per Interface der StartScreenActivity übergeben, von welcher
    //sich das Folge-Fragment diese wiederholt
    private void startFragmentConstruct() {
        listener.convertButtonClicked(selectedIngredients, true);
        ConstructMealFragment nextFrag= new ConstructMealFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "constructMealFragment")
                .addToBackStack(null)
                .commit();
    }

    private void startFragmentResults(){
        //Die Main-Activity wird als Listener über die ausgewählten Zutaten benachrichtigt
        listener.convertButtonClicked(selectedIngredients, false);

        //Das Fragment zur Darstellung der gefundenen Gerichte wird gestartet
        SearchResultsFragment nextFrag= new SearchResultsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "searchResultFragment")
                .addToBackStack(null)
                .commit();
    }

    //TextFeld Eingabe für Suchfunktion
    private void setUpEditText(View fragmentView){
        //EditTextFeldSetUp
        editText = fragmentView.findViewById(R.id.ingredients_finding_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.i("info", "before");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.i("info", "On");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.i("info", "after");
                String input = editText.getText().toString();
                textFieldInput(input);
            }
        });
    }

    private boolean searchMode = false;

    //Suchfunktion welche die Zutatenliste auf Grund der Nutzereingabe filtert und dann diese Daten dem Adapter übergibt, der searchMode Boolean dient dazu dem ClickListener unten
    //zu signalisieren, dass gerade eine Zutat aus der gefilterten Liste ausgewählt wurde
    public void textFieldInput(String s){
        filteredIngredients = new ArrayList<>();
        for(int i = 0; i < ingredients.size() ; i++){
            if(s.length() <= ingredients.get(i).getName().length()) {
                if (s.equals(ingredients.get(i).getName().substring(0, s.length()))) {
                    filteredIngredients.add(ingredients.get(i));
                }
            }
        }
        searchMode = true;
        adapter.createIngredients(filteredIngredients);
    }

    //Wird aufgerufen wenn Zutat angeklickt wurde, hier wird dann unterschieden ob die Zutatenliste gefiltert wurde, dann die geklickte Zutat zur unteren RV hinzugefügt
    //und aus der oberen gelöscht, das editTextFeld wird geleert nach der Auswahl
    @Override
    public void ingredientsUpdate(int ingredientPos) {
        if(searchMode){
            selectedIngredients.add(filteredIngredients.get(ingredientPos));
            ingredients.remove(filteredIngredients.get(ingredientPos));
            adapter.createIngredients(ingredients);
            adapterSI.createSelectedIngredients(selectedIngredients);
            editText.setText("");
            searchMode = false;
        }else {
            selectedIngredients.add(ingredients.get(ingredientPos));
            ingredients.remove(ingredientPos);
            adapter.createIngredients(ingredients);
            adapterSI.createSelectedIngredients(selectedIngredients);
        }
    }

}



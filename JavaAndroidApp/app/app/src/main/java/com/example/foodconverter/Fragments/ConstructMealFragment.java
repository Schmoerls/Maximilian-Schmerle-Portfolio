package com.example.foodconverter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.Data.Interfaces.ListenFromConstructMealFragment;
import com.example.foodconverter.Data.WorkingStep;
import com.example.foodconverter.UI.ConstructFragment.IngredientsAmountAdapter;
import com.example.foodconverter.UI.ConstructFragment.WorkingStepsAdapter;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;

import java.util.ArrayList;

public class ConstructMealFragment extends Fragment implements IngredientsAmountAdapter.AmountListener, WorkingStepsAdapter.StepListener{

    //Dieses Fragment dient der Konstruktion eines Gerichts durch anbieten von vielen möglichen Eingabeparametern
    private ArrayList<Ingredient> ingredients;
    private ArrayList<WorkingStep> workingSteps;
    private IngredientsAmountAdapter adapter;
    private WorkingStepsAdapter workingStepsAdapter;
    private RecyclerView workingStepRV;
    private ListenFromConstructMealFragment listener;
    private boolean vegetarian;
    private boolean vegan;

    //Fragment instanzierung und erhalten der ausgewählten Zutaten aus StartScreenActivity
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        ingredients = activity.getSelectedIngredients();
        return createAndInitFragment(inflater, container);
    }

    //In dieser Methode registriert sich die StartScreenActivity als Listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ListenFromConstructMealFragment) getActivity();
    }

    //Inflaten der View und vergeben der Default Werte für die Variablen
    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.meal_construction_working, root, false);
        workingSteps = new ArrayList<>();
        vegetarian = false;
        vegan = false;
        initUI(fragmentView);
        return fragmentView;
    }

    //Beide RecyclerViews werden referrenziert und mit den Adaptern verknüpft
    private void initUI(View fragmentView) {
        RecyclerView ingredientsRV = fragmentView.findViewById(R.id.ingredients_working);
        adapter = new IngredientsAmountAdapter(this);
        ingredientsRV.setAdapter(adapter);
        adapter.createIngredientsAmount(ingredients);

        workingStepRV = fragmentView.findViewById(R.id.working_steps);
        workingStepsAdapter = new WorkingStepsAdapter(this);
        workingStepRV.setAdapter(workingStepsAdapter);
        workingStepsAdapter.createWorkingSteps(workingSteps);

        buttonSetUp(fragmentView);
        switchSetUp(fragmentView);
    }

    //SetUp der Button
    private void buttonSetUp(View fragmentView) {
        Button continueButton = fragmentView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueButton(fragmentView);
            }
        });
        Button addWorkingStep = fragmentView.findViewById(R.id.add_working_step_button);
        addWorkingStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hier wird ein neuer Workingstep hinzugefügt nach unten gescrollt um Verwirrung zu verhindern und der Adapter geupdatet
                workingSteps.add(new WorkingStep(workingSteps.size(),"Insert Description"));
                workingStepRV.scrollToPosition(workingSteps.size()-1);
                workingStepsAdapter.createWorkingSteps(workingSteps);
            }
        });
        Button newIngredient = fragmentView.findViewById(R.id.add_ingredient_button);
        newIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hier wird eine neue Zutat aus dem EditTextString erstellt und zu der Zutatenliste hinzugefügt, sowie der Adapter geupdatet
                EditText ingredientET = fragmentView.findViewById(R.id.add_ingredient_et);
                String ingredient = ingredientET.getText().toString();
                ingredient.toLowerCase();
                ingredientET.setText("");
                ingredients.add(new Ingredient(0, ingredient));
                adapter.createIngredientsAmount(ingredients);
            }
        });
    }

    //Die beiden Switchfunktionalitäten werden erstellt um das Gericht vegetarisch oder vegan zu machen
    private void switchSetUp(View fragmentView) {
        Switch vegetarianSwitch = fragmentView.findViewById(R.id.vegatarianSwitchConstruct);
        vegetarianSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vegetarian = true;
                }else {
                    vegetarian = false;
                }
            }
        });
        Switch veganSwitch = fragmentView.findViewById(R.id.veganSwitchConstruct);
        veganSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vegan = true;
                }else {
                    vegan = false;
                }
            }
        });

    }

    //hier wird das schlussendliche Gericht erstellt aus den in dem Fragment gesammelten Daten und das nächste Fragment gestartet
    private void continueButton(View fragmentView) {
        EditText mealNameET = fragmentView.findViewById(R.id.meal_name_ET);
        String mealName = mealNameET.getText().toString();
        String instructions = "";
        ArrayList<String> instructionsFull = new ArrayList<>();
        for (WorkingStep w : workingSteps) {
            instructions += w.returnText() + "  " + "\n";
        }
        for (Ingredient i : ingredients) {
            instructionsFull.add(i.getAmount());
        }
        Recipe newRecipe = new Recipe(mealName, ingredients, instructions, vegetarian, true, vegan);
        listener.constructButtonClicked(newRecipe);
        startFragment();
    }

    //start der ConstructFragments
    private void startFragment() {
        ConstructFragment nextFrag= new ConstructFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "constructFragment")
                .addToBackStack(null)
                .commit();
    }

    //Listener Methode, damit der Ingredients Amount sofort nach der eingabe geupdatet werden kann
    @Override
    public void amountChanged(String amount, int position) {
        ingredients.get(position).setAmount(amount + "g of " + ingredients.get(position).getName());
    }

    //Listener Methode, damit die Arbeitschritte sofort nach der eingabe geupdatet werden können
    @Override
    public void stepChanged(String text, int position) {
        workingSteps.get(position).setString(text);
    }
}

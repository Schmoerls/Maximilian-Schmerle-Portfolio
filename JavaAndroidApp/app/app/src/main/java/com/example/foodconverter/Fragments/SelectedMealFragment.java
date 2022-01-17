package com.example.foodconverter.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.Data.Database.RecipeDatabaseHelper;
import com.example.foodconverter.Data.Ingredient;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;

import java.util.ArrayList;

public class SelectedMealFragment extends Fragment {

    //mit diesem Fragment kann ein Gericht dargestellt werden, es kann geliked oder gemarked werden und die Swipe-Funktion um zwischen den Gerichten zu wechseln wird realisiert
    //dieses Fragment kann von mehrer Stellen der App gestartet werden und erhält deswegen viele Instanzierungsparameter, damit es unterscheiden kann, von wo es aufgerufen wurde
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private int mealNumber;
    TextView tvHeader;
    TextView tvIngredients;
    TextView tvInstructions;
    ImageView ivHeart;
    ImageView ivBookmark;
    public RecipeDatabaseHelper db;

    //Fragment Instanzierung es werden, die Instanzierungsparameter der Aufrufenden-Fragments geladen ein Datenbank-Zugriff gelegt um die Rezepte zu liken/marken und dann auf
    //Grund der Parameter, die richtige ArrayList ausgewählt aus der mit Hilfe der MealNumber das richtige Rezept angezeigt wird (ich weiß ein switch-Aufbau wäre auch gegangen,
    // aber ich liebe if.else.Statements^^)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mealNumber = getArguments().getInt("mealNumber");
        boolean any = getArguments().getBoolean("any");
        boolean own = getArguments().getBoolean("own");
        boolean likedMeals = getArguments().getBoolean("likedMeals");
        boolean markedMode = getArguments().getBoolean("markedMode");
        boolean construct = getArguments().getBoolean("construct");
        db = new RecipeDatabaseHelper(getActivity().getApplicationContext());
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        allRecipes = activity.getRecipes();
        if(!construct){
            if(likedMeals){
                recipes = activity.getLikedRecipes();
            }else {
                if(markedMode){
                    ArrayList<Recipe> working = activity.getRecipes();
                    for (Recipe r : working) {
                        if(r.getBookmarked()){recipes.add(r);}
                        mealNumber = 0;
                    }
                }else{
                    recipes = activity.getFilteredRecipes();
                }
            }
        }else{
            if(own){
                for (Recipe r : activity.getRecipes()) {
                    if(r.getOwnMeal()){
                        recipes.add(r);
                    }
                }
            }else {
                recipes = activity.getRecipes();
            }
        }
        return createAndInitFragment(inflater, container);
    }

    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.meal_result_single_meal, root, false);
        initUI(fragmentView);
        intitTouch(fragmentView);
        return fragmentView;
    }

    //UI-SetUp, es werden die ganzen View-Objekte referenziert und dann mit Daten befüllt auch wird gecheckt, ob das Gericht geliked oder gemarked ist und dass dann an die
    //UI-Elemente übergeben, es werden auch Strings für die Darstellung aus den Ingredients und ingredientsAmounts gebaut
    private void initUI(View fragmentView) {
        initButton(fragmentView);
        ivHeart = fragmentView.findViewById(R.id.iv_heart);
        ivBookmark = fragmentView.findViewById(R.id.iv_bookmark);
        if(recipes.get(mealNumber).getLike()){
            ivHeart.setImageResource(R.drawable.ic_hearthfilled);
        }else{
            ivHeart.setImageResource(R.drawable.ic_heart_svgrepo_com);
        }
        if(recipes.get(mealNumber).getBookmarked()){
            ivBookmark.setImageResource(R.drawable.ic_bookmark_svgrepo_com);
        }else{
            ivBookmark.setVisibility(View.GONE);
        }
        tvHeader = fragmentView.findViewById(R.id.meal_header);
        tvIngredients = fragmentView.findViewById(R.id.meal_ingredients);
        tvInstructions = fragmentView.findViewById(R.id.meal_instruction);
        tvInstructions.setMovementMethod(new ScrollingMovementMethod());
        tvHeader.setText(recipes.get(mealNumber).getName());
        buildInstructions();
        String ingredients = "";
        for (Ingredient i : recipes.get(mealNumber).getIngrediens()) {
            ingredients += i.getName() + ", ";
        }
        tvIngredients.setText(ingredients);
    }

    private void buildInstructions() {
        String instructions = "";
        for (Ingredient i : recipes.get(mealNumber).getIngrediens()) {
            instructions += i.getAmount() + "\n" + "\n";
        }
        instructions += recipes.get(mealNumber).getInstruction();
        tvInstructions.setText(instructions);
    }

    //Der MarkButton wird instanziert und er kann ein Gericht als marked markieren, gleichzeitig werden alle Gerichte durchsucht und überprüft ob es noch ein marktes Rezept gibt,
    //falls ja wird dieses entmarked, außerdem werden die Daten gleich in die DB geschrieben(deswegen DB-Helper oben) und das marked Bild wird ein und aus geblendet
    private void initButton(View fragmentView) {
        Button markButton = fragmentView.findViewById(R.id.mark_button);
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recipes.get(mealNumber).getBookmarked()){
                    recipes.get(mealNumber).setBookmarked(false);
                    db.updateBookmarked(false, recipes.get(mealNumber).getId());
                    ivBookmark.setVisibility(View.GONE);
                }else {
                    for (Recipe r : allRecipes) {
                        if(r.getBookmarked()){
                            r.setBookmarked(false);
                            //db zugriff zum falsch setzen
                            db.updateBookmarked(false, r.getId());
                        }
                    }
                    recipes.get(mealNumber).setBookmarked(true);
                    db.updateBookmarked(true, recipes.get(mealNumber).getId());
                    ivBookmark.setVisibility(View.VISIBLE);
                    ivBookmark.setImageResource(R.drawable.ic_bookmark_svgrepo_com);
                }
            }
        });
    }

    int x = 0;

    //Touch Funktionalität wird realisiert, zum einen der Doppelklick durch den GestureDetector, als Reaktion folgt ein ähnliches Verfahren wie oben beim marken eines Gerichts
    //es können nur mehrere Gerichte geliked sein, außerdem habe ich die Swipe Funktion realisiert indem ich die x-Werte beim ablegen und hochnehmen des Fingers vom Bildschirm
    //vergleiche
    private void intitTouch(View fragmentView) {
        GestureDetector detector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.i("info", String.valueOf(e.getX()));
                if(recipes.get(mealNumber).getLike()) {
                    ivHeart.setImageResource(R.drawable.ic_heart_svgrepo_com);
                    recipes.get(mealNumber).setLike(false);
                    db.updateLike(false, recipes.get(mealNumber).getId());
                }else{
                    ivHeart.setImageResource(R.drawable.ic_hearthfilled);
                    recipes.get(mealNumber).setLike(true);
                    db.updateLike(true, recipes.get(mealNumber).getId());
                }
                return super.onDoubleTap(e);
            }

        });
        fragmentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int amount = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setX((int) motionEvent.getX());
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int neededAmount = 400;
                    int xUp = (int) motionEvent.getX();
                    int change = getX() - xUp;
                    if(change > neededAmount){
                        mealNumber++;
                        changeMeal();
                    }else if(change < (neededAmount * (-1))){
                        mealNumber--;
                        changeMeal();
                    }
                }
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }
    private void setX(int x){
        this.x = x;
    }
    private int getX(){
        return x;
    }

    //diese Methode wird aufgerufen, wenn ein Swipe bzgl. der benötigten Strecke detectet wurde und sie ändert die Darzustellenden Rezeptdaten bzgl. der MealNummer und der oben
    //ausgewählten ArrayList
    private void changeMeal(){
        if(mealNumber < 0){
            mealNumber = recipes.size()-1;
        }else if(mealNumber > recipes.size()-1){mealNumber = 0;}
        tvHeader.setText(recipes.get(mealNumber).getName());
        buildInstructions();
        String ingredients = "";
        for (Ingredient i : recipes.get(mealNumber).getIngrediens()) {
            ingredients += i.getName() + ", ";
        }
        tvIngredients.setText(ingredients);
        tvInstructions.scrollTo(0, 0);
        if(recipes.get(mealNumber).getLike()){
            ivHeart.setImageResource(R.drawable.ic_hearthfilled);
        }else {
            ivHeart.setImageResource(R.drawable.ic_heart_svgrepo_com);
        }
        if(recipes.get(mealNumber).getBookmarked()){
            ivBookmark.setVisibility(View.VISIBLE);
            ivBookmark.setImageResource(R.drawable.ic_bookmark_svgrepo_com);
        }else {
            ivBookmark.setVisibility(View.GONE);
        }
    }
}

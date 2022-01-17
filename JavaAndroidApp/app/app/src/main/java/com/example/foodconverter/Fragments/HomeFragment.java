package com.example.foodconverter.Fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.Database.Recipe;
import com.example.foodconverter.R;
import com.example.foodconverter.StartScreenActivity;
import com.example.foodconverter.UI.HomeFragment.LikedMealsAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LikedMealsAdapter.AdapterListener{

    //Home Fragment zur Anzeige der Animation, der gelikedten Gerichte und des gemarkten Gerichts
    AnimationDrawable animation;
    private LikedMealsAdapter adapter;
    private ArrayList<Recipe> likedRecipes = new ArrayList<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();

    //Fragment Instanzierung
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createAndInitFragment(inflater, container);
    }

    private View createAndInitFragment(LayoutInflater inflater, ViewGroup root) {
        View fragmentView = inflater.inflate(R.layout.home_screen, root, false);
        animate(fragmentView);
        initButton(fragmentView);
        return fragmentView;
    }

    //Animation wird angestoßen
    private void animate(View fragmentView) {
        ImageView imageView = fragmentView.findViewById(R.id.image_view);
        imageView.setBackgroundResource(R.drawable.animation);
        animation = (AnimationDrawable) imageView.getBackground();
        animation.start();
    }

    //Der Button für die Weiterleitung zum markierten Gericht wird instanziert und startet beim Klicken sofort das Fragment welches dieses anzeigt
    private void initButton(View fragmentView) {
        Button goToMarkedButton = fragmentView.findViewById(R.id.marked_meal_button);
        goToMarkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Recipe r : recipes) {
                    if(r.getBookmarked()){startFragment();}
                }
            }
        });
    }

    private void startFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("markedMode", true);
        SelectedMealFragment nextFrag= new SelectedMealFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "selected_meal_fragment")
                .addToBackStack(null)
                .commit();
    }

    //Es hat sich rausgestellt, dass beim ersten Fragment, was die Activity nach dem Start anzeigt die Daten nur über die onStart Methode geladen werden können, deswegen hier
    //laden der Daten und UI+RecyclerView SetUp
    @Override
    public void onStart() {
        super.onStart();
        StartScreenActivity activity = (StartScreenActivity) getActivity();
        likedRecipes.addAll(activity.getLikedRecipes());
        recipes = activity.getRecipes();
        initUI(getView());
    }

    private void initUI(View fragmentView) {
        RecyclerView likedMealsView = fragmentView.findViewById(R.id.liked_meals);
        adapter = new LikedMealsAdapter(this);
        likedMealsView.setAdapter(adapter);
        adapter.createMeals(likedRecipes);
    }

    //Listener um ein Rezept aus den liked-Recipes zu öffnen, es werden im Bundle die mealNumber und der likedMeals Modi übergeben, damit das SelectedMEalFragment anständig funktioniert
    @Override
    public void recipeClicked(int recipePosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("mealNumber", recipePosition);
        bundle.putBoolean("likedMeals", true);
        SelectedMealFragment nextFrag= new SelectedMealFragment();
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, nextFrag, "selected_meal_fragment")
                .addToBackStack(null)
                .commit();
    }
}

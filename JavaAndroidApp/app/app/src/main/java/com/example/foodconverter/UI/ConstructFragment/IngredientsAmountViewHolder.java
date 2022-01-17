package com.example.foodconverter.UI.ConstructFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.R;

public class IngredientsAmountViewHolder extends RecyclerView.ViewHolder {

    private final TextView ingredientName;
    private final EditText amount;
    private String input;
    private final IngredientsAmountListener listener;

    public IngredientsAmountViewHolder(@NonNull View itemView, IngredientsAmountListener listener) {
        super(itemView);
        this.listener = listener;
        ingredientName = itemView.findViewById(R.id.ingredient_name);
        amount = itemView.findViewById(R.id.amount_ET);
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input = amount.getText().toString();
                listener.ingredientAmountChanged(input, getAdapterPosition());
            }
        });
    }

    public void bindView(String ingredient){
        ingredientName.setText(ingredient);
    }

    public interface IngredientsAmountListener{
        void ingredientAmountChanged(String amount, int position);
    }
}

package com.example.foodconverter.UI.ConstructFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.WorkingStep;
import com.example.foodconverter.R;

public class WorkingStepsViewHolder extends RecyclerView.ViewHolder {

    private final TextView stepHeader;
    private final EditText workingStepText;
    private final WorkingStepListener listener;

    public WorkingStepsViewHolder(@NonNull View itemView, WorkingStepListener listener) {
        super(itemView);
        this.listener = listener;
        stepHeader = itemView.findViewById(R.id.working_step_TV);
        workingStepText = itemView.findViewById(R.id.working_step_ET);
        workingStepText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = workingStepText.getText().toString();
                listener.workingStepTextAdded(input, getAdapterPosition());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void bindView(WorkingStep step){
        stepHeader.setText(step.returnStep());
        workingStepText.setText(step.returnText());

    }

    public interface WorkingStepListener{
        void workingStepTextAdded(String text, int position);
    }
}

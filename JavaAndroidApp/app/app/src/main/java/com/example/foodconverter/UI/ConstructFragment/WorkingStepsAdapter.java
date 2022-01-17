package com.example.foodconverter.UI.ConstructFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodconverter.Data.WorkingStep;
import com.example.foodconverter.R;

import java.util.ArrayList;

public class WorkingStepsAdapter extends RecyclerView.Adapter<WorkingStepsViewHolder> implements WorkingStepsViewHolder.WorkingStepListener {

    private ArrayList<WorkingStep> workingSteps;

    private StepListener listener;

    public WorkingStepsAdapter(StepListener listener){
        workingSteps = new ArrayList<WorkingStep>();
        this.listener = listener;
    }

    public void createWorkingSteps(ArrayList<WorkingStep> input){
        workingSteps = input;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkingStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.working_step, parent, false);
        return new WorkingStepsViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkingStepsViewHolder holder, int position) {
        WorkingStep step = workingSteps.get(position);
        holder.bindView(step);
    }

    @Override
    public int getItemCount() {
        return workingSteps.size();
    }

    @Override
    public void workingStepTextAdded(String text, int position) {
        listener.stepChanged(text, position);
    }

    public interface StepListener{
        void stepChanged(String text, int position);
    }
}

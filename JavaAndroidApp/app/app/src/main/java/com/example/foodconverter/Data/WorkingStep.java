package com.example.foodconverter.Data;

//Klasse wird beim erstellen der Gerichte verwendet
public class WorkingStep {
    private int number;
    private String text;

    public WorkingStep(int number, String text){
        this.number = number;
        this.text = text;
    }

    public int returnNumber(){
        return number;
    }

    public String returnText(){
        return text;
    }

    public String returnStep(){
        int returnNumber = number + 1;
        String output = "Step: " + returnNumber;
        return output;
    }

    public String returnFullWorkingStep(){
        String output = "";
        output += number + "=" + text;
        return output;
    }

    public void setString (String input){
        text = input;
    }

    public String getInfo(){
        String returnString = returnStep() + " withText " + text;
        return returnString;
    }
}

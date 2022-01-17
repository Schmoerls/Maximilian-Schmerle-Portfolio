package com.example.foodconverter.Data;

//Klasse für einzelne Zutat, welche in einer ArrayList in den Recipes gespeichert werden
public class Ingredient implements Comparable<Ingredient>{

    //Variablen, Konstruktor, Setter und Getter
    private String name;
    private String amount;
    private int number;
    private boolean shortVersion;

    public Ingredient(int number, String name, String amount){
        this.name = name;
        this.amount = amount;
        this.number = number;
        this.shortVersion = false;
    }

    public Ingredient(int number, String name){
        this.name = name;
        this.number = number;
        this.shortVersion = true;
    }

    public void setAmount(String input){
        this.amount = input;
    }

    public void setNumber(int input){this.number = input;}

    public void setName(String string){this.name = string;}

    public int getNumber(){return number;}

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getInfo(){
        String info = amount + "Gramm of " + name;
        return info;
    }

    //Comparable Methode zum vergleichen mit anderen Zutaten(hauptsächlich zum sortieren)
    @Override
    public int compareTo(Ingredient ingredient) {
        if(this.name.charAt(0) < ingredient.name.charAt(0)){
            return -1;
        }else if(this.name.charAt(0) == ingredient.name.charAt(0)){
            return 0;
        }else {
            return 1;
        }
    }
}

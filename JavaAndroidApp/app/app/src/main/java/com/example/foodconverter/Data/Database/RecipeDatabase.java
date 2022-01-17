package com.example.foodconverter.Data.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({RecipeTypeConverter.class})

public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDOA recipeDOA();
}

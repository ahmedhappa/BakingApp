package com.example.ahmed.bakingapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ahmed.bakingapp.Model.MyContractClass.IngeridentTable;
import com.example.ahmed.bakingapp.Model.MyContractClass.StepTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 31/01/2018.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABAE_NAME = "recipes.dp";
    private static final int VERSION = 1;
    private Context context;

    public MyDatabase(Context context) {
        super(context, DATABAE_NAME, null, VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("create table " + IngeridentTable.TABLE_NAME + " ( "
                    + IngeridentTable.QUANTITY + " text , "
                    + IngeridentTable.MEASURE + " text , "
                    + IngeridentTable.INGREDIENT + " text );");

            sqLiteDatabase.execSQL("create table " + StepTable.TABLE_NAME + " ( "
                    + StepTable.SHORT_DESCRIPTION + " text , "
                    + StepTable.DESCRIPTION + " text , "
                    + StepTable.VIDEO_URL + " text , "
                    + StepTable.THUMBERL_URL + " text);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertRecipesData(Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + IngeridentTable.TABLE_NAME);
        sqLiteDatabase.execSQL("delete from " + StepTable.TABLE_NAME);
        ContentValues contentValues;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            contentValues = new ContentValues();
            contentValues.put(IngeridentTable.QUANTITY, recipe.getIngredients().get(i).getQuantity());
            contentValues.put(IngeridentTable.MEASURE, recipe.getIngredients().get(i).getMeasure());
            contentValues.put(IngeridentTable.INGREDIENT, recipe.getIngredients().get(i).getIngredient());
            sqLiteDatabase.insert(IngeridentTable.TABLE_NAME, null, contentValues);
        }

        for (int i = 0; i < recipe.getSteps().size(); i++) {
            contentValues = new ContentValues();
            contentValues.put(StepTable.SHORT_DESCRIPTION, recipe.getSteps().get(i).getShortDescription());
            contentValues.put(StepTable.DESCRIPTION, recipe.getSteps().get(i).getDescription());
            contentValues.put(StepTable.VIDEO_URL, recipe.getSteps().get(i).getVideoURL());
            contentValues.put(StepTable.THUMBERL_URL, recipe.getSteps().get(i).getThumbnailURL());
            sqLiteDatabase.insert(StepTable.TABLE_NAME, null, contentValues);
        }

    }

    public Recipe getRecipesData() {
        Recipe recipe = new Recipe();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor;
        List<Step> steps = new ArrayList<>();
        cursor = sqLiteDatabase.rawQuery("select * from " + IngeridentTable.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            ingredients.add(new Ingredient(cursor.getString(cursor.getColumnIndex(IngeridentTable.QUANTITY))
                    , cursor.getString(cursor.getColumnIndex(IngeridentTable.MEASURE))
                    , cursor.getString(cursor.getColumnIndex(IngeridentTable.INGREDIENT))));
        }
        recipe.setIngredients(ingredients);
        cursor = sqLiteDatabase.rawQuery("select * from " + StepTable.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            steps.add(new Step(cursor.getString(cursor.getColumnIndex(StepTable.SHORT_DESCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(StepTable.DESCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(StepTable.VIDEO_URL))
                    , cursor.getString(cursor.getColumnIndex(StepTable.THUMBERL_URL))));
        }
        recipe.setSteps(steps);
        cursor.close();
        return recipe;
    }
}

package com.example.chloerainezeller.project2;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by chloerainezeller on 3/1/18.
 */

public class Recipe implements Parcelable {

    public String title;
    public String imageUrl;
    public String instructionUrl;
    public String description;
    public String label;
    public String servings;
    public String prepTime;

    public static ArrayList<Recipe> getRecipesFromFile(String filename, Context context) {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        try {
            String jsonString = loadJsonFromAsset("recipes.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray recipes = json.getJSONArray("recipes");


            for (int i = 0; i < recipes.length(); i++) {
                Recipe recipe = new Recipe(); // FIXME HOW DO I DO THIS??
                recipe.title = recipes.getJSONObject(i).getString("title");
                recipe.imageUrl = recipes.getJSONObject(i).getString("image");
                recipe.instructionUrl = recipes.getJSONObject(i).getString("url");
                recipe.description = recipes.getJSONObject(i).getString("description");
                recipe.servings = recipes.getJSONObject(i).getString("servings");
                recipe.prepTime = recipes.getJSONObject(i).getString("prepTime");
                recipe.label = recipes.getJSONObject(i).getString("dietLabel");


                // add to the arrayList
                recipeList.add(recipe);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return recipeList;

    }


    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        // loads the file from the assets folder under that context, reads in as a string
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }



    protected Recipe(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        instructionUrl = in.readString();
        description = in.readString();
        label = in.readString();
        servings = in.readString();
        prepTime = in.readString();
    }

    protected Recipe() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(instructionUrl);
        dest.writeString(description);
        dest.writeString(label);
        dest.writeString(servings);
        dest.writeString(prepTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
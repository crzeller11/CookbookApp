package com.example.chloerainezeller.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    private Context myContext;
    private Spinner dietRestrictionSpinner;
    private Spinner servingSpinner;
    private Spinner prepTimeSpinner;
    private Button searchButton;
    private String dietSelection;
    private String servingSelection;
    private String prepTimeSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        myContext = this;
        searchButton = findViewById(R.id.searchButton);


        //get the spinner from the xml.
        dietRestrictionSpinner = findViewById(R.id.dietRestrictionSpinner);
        servingSpinner = findViewById(R.id.servingSpinner);
        prepTimeSpinner = findViewById(R.id.prepTimeSpinner);

        ArrayList<Recipe> recipes = Recipe.getRecipesFromFile("recipes.json", this);

        ArrayList<String> dietOptions = new ArrayList<>();

        dietOptions.add("");
        for (int i = 0; i < recipes.size(); i++) {
            String dietOption = recipes.get(i).label;
            if (!dietOptions.contains(dietOption)) {
                dietOptions.add(dietOption);
            }
        }

        ArrayList<String> servingOptions = new ArrayList<>();

        servingOptions.add("");
        servingOptions.add("less than 4");
        servingOptions.add("4-6");
        servingOptions.add("7-9");
        servingOptions.add("more than 10");

        ArrayList<String> prepTimeOptions = new ArrayList<>();

        prepTimeOptions.add("");
        prepTimeOptions.add("less than 30 minutes");
        prepTimeOptions.add("less than 1 hour");
        prepTimeOptions.add("more than 1 hour");


        ArrayAdapter<String> dietRestrictionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dietOptions);
        ArrayAdapter<String> servingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, servingOptions);
        ArrayAdapter<String> prepTimeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, prepTimeOptions);

        dietRestrictionSpinner.setAdapter(dietRestrictionAdapter);
        servingSpinner.setAdapter(servingAdapter);
        prepTimeSpinner.setAdapter(prepTimeAdapter);

    }

    public void searchQuery(View view) {
        String dietSetting = dietRestrictionSpinner.getSelectedItem().toString();
        String servingSetting = servingSpinner.getSelectedItem().toString();
        String prepTimeSetting = prepTimeSpinner.getSelectedItem().toString();

        ArrayList<Recipe> recipes = Recipe.getRecipesFromFile("recipes.json", this);

        ArrayList<Recipe> searchResults = getSearchResults(dietSetting, servingSetting,
                prepTimeSetting, recipes);

        Bundle extra = new Bundle();
        extra.putSerializable("searchResults", searchResults);

        Intent intent = new Intent(myContext, ResultActivity.class);
        intent.putExtra("searchResults", extra);

        startActivity(intent);
    }

    public ArrayList<String> getUniquePrepTimes(ArrayList<Recipe> recipeList) {
        ArrayList<String> prepOptions = new ArrayList<>();
        // make an arrayList of all the unique prepTime options in the JSON file
        for (int i = 0; i < recipeList.size(); i++) {
            String prep = recipeList.get(i).prepTime;
            if (!prepOptions.contains(prep)) {
                prepOptions.add(prep);
            }
        }
        return prepOptions;
    }

    // creates an arrayList of all the unique types of servings from the JSON file
    public ArrayList<String> getUniqueServings(ArrayList<Recipe> recipeList) {
        ArrayList<String> servingOptions = new ArrayList<>();
        for (int i = 0; i < recipeList.size(); i++) {
            String serving = recipeList.get(i).servings;
            if (!servingOptions.contains(serving)) {
                servingOptions.add(serving);
            }
        }
        return servingOptions;
    }

    // Creates a HashMap that maps from the category dropdowns to an ArrayList of all prepTimes
    // that satisfy that list of dropdowns
    public HashMap<String, ArrayList<String>> makePrepMap(ArrayList<Recipe> recipeList) {
        ArrayList<String> prepOptions = getUniquePrepTimes(recipeList);

        ArrayList<String> thirtyMinsLess = new ArrayList<>();
        ArrayList<String> lessOneHour = new ArrayList<>();
        ArrayList<String> moreOneHour = new ArrayList<>();

        for (int i = 0; i < prepOptions.size(); i++) {
            String currentUniquePrepTime = prepOptions.get(i);
            String[] words = currentUniquePrepTime.split(" ");
            if (words[1].equals("minutes")) { // then it should go in
                lessOneHour.add(currentUniquePrepTime);
                if (Integer.parseInt(words[0]) <= 30) {
                    thirtyMinsLess.add(currentUniquePrepTime);
                }
            }
            else {
                moreOneHour.add(currentUniquePrepTime);
            }
        }

        HashMap<String, ArrayList<String>> prepMap = new HashMap<>();
        prepMap.put("less than 30 minutes", thirtyMinsLess);
        prepMap.put("less than 1 hour", lessOneHour);
        prepMap.put("more than 1 hour", moreOneHour);

        return prepMap;

    }

    // Creates a HashMap that maps from the actual serving tag in JSON, to the user's input
    public HashMap<Integer, String> makeServingMap(ArrayList<Recipe> recipeList) {
        HashMap<Integer, String> servingMap = new HashMap<>();
        ArrayList<String> servingOptions = getUniqueServings(recipeList); // list of unique servings
        for (int i = 0; i < servingOptions.size(); i++) {
            int currentServing = Integer.parseInt(servingOptions.get(i));
            if (currentServing < 4) {
                servingMap.put(currentServing, "less than 4");
            }

            else if (currentServing >= 4) {
                if (currentServing <= 6) {
                    servingMap.put(currentServing, "4-6");
                }
                else if (currentServing > 6) {
                    if (currentServing <= 9) {
                        servingMap.put(currentServing, "7-9");
                    }
                    else if (currentServing >= 10) {
                        servingMap.put(currentServing, "more than 10");
                    }
                }
            }

        }
        return servingMap;
    }


    // when the submit button is clicked.
    // Makes a final ArrayList of recipes that match the search queries
    public ArrayList<Recipe> getSearchResults(String diet, String serving, String prep,
                                              ArrayList<Recipe> recipeList) {

        ArrayList<Recipe> searchResults = new ArrayList<Recipe>(recipeList);
        HashMap<String, ArrayList<String>> prepMap = makePrepMap(recipeList);
        HashMap<Integer, String> servingMap = makeServingMap(recipeList);

        for (int i = 0; i < searchResults.size(); i++) {
            Recipe curRecipe = searchResults.get(i);
            if (!diet.equals("")) {
                if (!diet.equals(curRecipe.label)) {
                    searchResults.remove(curRecipe);
                    i--;
                    continue;
                }
            }
            // gets the arrayList of acceptable prepTimes that match the input
            ArrayList<String> acceptablePrepTimes = prepMap.get(prep);
            if (!prep.equals("")) {
                // if the list of acceptable prepTimes doesn't have the prepTime of the current
                // recipe, then remove it
                if (!acceptablePrepTimes.contains(curRecipe.prepTime)) {
                    searchResults.remove(curRecipe);
                    i--;
                    continue;
                }
            }
            if (!serving.equals("")) {
                // get the category that the current recipe falls into
                int curRecipeServing = Integer.parseInt(curRecipe.servings);
                String curRecipeCategory = servingMap.get(curRecipeServing);
                // if the serving the user chose is the same as the serving corresponding to the
                // current Recipe, then leave it in the list
                if (!curRecipeCategory.equals(serving)) {
                    searchResults.remove(curRecipe);
                    i--;
                    continue;

                }
            }
        }

        return searchResults;
    }



}

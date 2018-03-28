package com.example.chloerainezeller.project2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {
    Context myContext;
    TextView numResultingRecipes;
    ListView myListView;
    ImageButton notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        myContext = this;


        Bundle extra = getIntent().getBundleExtra("searchResults");
        final ArrayList<Recipe> searchResults = (ArrayList<Recipe>) extra.getSerializable("searchResults");

        int recipeCount = searchResults.size();

        ResultAdapter adapter = new ResultAdapter(this, searchResults);

        myListView = findViewById(R.id.searchResultList);
        myListView.setAdapter(adapter);
        notificationButton = findViewById(R.id.notificationButton);
        numResultingRecipes = findViewById(R.id.numRecipesFound);

        String displayString = "Found " + recipeCount + " recipe(s).";

        numResultingRecipes.setText(displayString);

    }
}

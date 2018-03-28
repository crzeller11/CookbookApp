package com.example.chloerainezeller.project2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {

    private Context myContext;
    private ArrayList<Recipe> myRecipeList;
    private LayoutInflater myInflater;
    private Button notificationButton;

    public ResultAdapter(Context myContext, ArrayList<Recipe> myRecipeList) {
        this.myContext = myContext;
        this.myRecipeList = myRecipeList;

        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return myRecipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return myRecipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.list_item_recipe, parent, false);

            holder = new ViewHolder();

            holder.titleTextView = convertView.findViewById(R.id.recipeTitle);
            holder.servingTextView = convertView.findViewById(R.id.recipeServings);
            holder.prepTimeTextView = convertView.findViewById(R.id.recipePrepTime);
            holder.thumbnailImageView = convertView.findViewById(R.id.recipeListThumbnail);
            holder.notificationButton = convertView.findViewById(R.id.notificationButton);

            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView servingTextView = holder.servingTextView;
        TextView prepTimeTextView = holder.prepTimeTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;
        ImageButton notificationButton = holder.notificationButton;

        final Recipe recipe = (Recipe) getItem(position);

        titleTextView.setText(recipe.title);
        titleTextView.setTextSize(18);

        servingTextView.setText(recipe.servings + " serving(s)");
        servingTextView.setTextSize(14);

        prepTimeTextView.setText(recipe.prepTime);
        prepTimeTextView.setTextSize(14);

        // load image into the image view
        Picasso.with(myContext).load(recipe.imageUrl).into(thumbnailImageView);


        // NOTIFICATION BUILDER
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                notificationIntent.setData(Uri.parse(recipe.instructionUrl));

                String message = "The instruction for " + recipe.title + " can be found here!";

                PendingIntent pi = PendingIntent.getActivity(myContext, 0, notificationIntent, 0);
                Notification notification = new NotificationCompat.Builder(myContext, "channel_ID")
                        .setTicker("yortext")
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle("Cooking Instruction")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .build();

                NotificationManager notificationManager2 =  (NotificationManager) myContext.getSystemService(Service.NOTIFICATION_SERVICE);
                notificationManager2.notify(0, notification);
            }
        });


        return convertView;



    }

    private static class ViewHolder{
        public TextView titleTextView;
        public TextView servingTextView;
        public TextView prepTimeTextView;
        public ImageView thumbnailImageView;
        public ImageButton notificationButton;
    }

}

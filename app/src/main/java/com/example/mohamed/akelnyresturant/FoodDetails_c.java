package com.example.mohamed.akelnyresturant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.model.Foods;
import com.example.mohamed.akelnyresturant.model.Order;
import com.example.mohamed.akelnyresturant.model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetails_c extends AppCompatActivity implements RatingDialogListener {


    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceRating;

    DataBase db = new DataBase(this);

    Foods currentFood;

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart, btnRate;
    ElegantNumberButton numberButton;
    RatingBar rateBar;
    Button showComment;

    String foodId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details_c);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Foods");
        databaseReferenceRating = database.getReference("Rating");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        btnRate = (FloatingActionButton) findViewById(R.id.btnrate);
        showComment = (Button) findViewById(R.id.showcomment);


        showComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDetails_c.this,ShowComment_c.class);
                intent.putExtra(Common.Intent_Food_ID,foodId);
                startActivity(intent);
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });



        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addToCart(new Order(
                        Common.currentUser.getPhone(),
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                        ,currentFood.getImage()
                ));

                Toast.makeText(FoodDetails_c.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });


        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_description = (TextView) findViewById(R.id.food_description);
        food_image = (ImageView) findViewById(R.id.img_food);
        rateBar = (RatingBar) findViewById(R.id.ratingBar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExbandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
        }
        if (!foodId.isEmpty()) {
            getDetailas(foodId);
            getRatingFood(foodId);
        }


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getRatingFood(String foodId) {
        com.google.firebase.database.Query foodRating = databaseReferenceRating.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    rateBar.setRating(average);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excelent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorPrimaryDark)
                .setCommentTextColor(android.R.color.black)
                .setCommentBackgroundColor(R.color.colorAccent)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails_c.this)
                .show();
    }


    @Override
    public void onPositiveButtonClicked(int rateValue, String comment) {

        //Get rate and upload to firebase
        final Rating rating = new Rating(Common.currentUser.getPhone(), foodId, String.valueOf(rateValue), comment);

        //multiple comment
        databaseReferenceRating.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(FoodDetails_c.this, "Thank you for submit rating ", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getDetailas(String foodId) {

        databaseReference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Foods.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());

                food_name.setText(currentFood.getName());

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

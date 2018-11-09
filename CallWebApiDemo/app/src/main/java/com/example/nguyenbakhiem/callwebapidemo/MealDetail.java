package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import entity.Meal;
import entity.User;

public class MealDetail extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private ImageButton imageButton;
    User user;
    private boolean isStar = false;
    Meal meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Intent intent = getIntent();
         meal =(Meal) intent.getSerializableExtra("meal");
        imageView = findViewById(R.id.imageViewMealDetail);
        textView = findViewById(R.id.textViewMealDetail);
        imageButton = findViewById(R.id.imageButtonStar);
        user = User.getInstance();
        setData();


    }
    private void setData(){
        new DownloadImageTask(imageView)
                .execute(meal.getImgLink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }
        if(user.getFavoriteMeal() != null && user.getFavoriteMeal().length() > 0){
            String favoriteMeal = user.getFavoriteMeal();
            String substring[] = favoriteMeal.split(",");
            for (int i = 0; i < substring.length;i++){
                //mon da thich
                if(substring[i].equalsIgnoreCase("" + meal.getId())){
                    imageButton.setImageResource(R.drawable.staron);
                    isStar = true;
                }
            }
        }
    }
    public void onStar(View view){
        if(user.getStatusLogin() != null && user.getStatusLogin().equalsIgnoreCase("ok")){
            if(isStar){
                imageButton.setImageResource(R.drawable.staroff);
                //xoa mon an da thich
                isStar = false;
                String favoriteMeal = user.getFavoriteMeal();
                if(favoriteMeal.startsWith("" + meal.getId())){
                    if(favoriteMeal.length() == 1){
                        favoriteMeal =   favoriteMeal.replace("" + meal.getId(), "");
                    }
                    else {
                        favoriteMeal = favoriteMeal.replace("" + meal.getId() + ",", "");
                    }
                }
                else {
                    favoriteMeal = favoriteMeal.replace("," + meal.getId(), "");
                }
                user.setFavoriteMeal(favoriteMeal);
            }
            else{
                imageButton.setImageResource(R.drawable.staron);
                //them vao mon an da thich
                isStar = true;
                String favoriteMeal = user.getFavoriteMeal();
                if(favoriteMeal != null && favoriteMeal.length() > 0){
                    favoriteMeal = favoriteMeal + "," + meal.getId();
                }
                else{
                    favoriteMeal = "" + meal.getId();
                }

                user.setFavoriteMeal(favoriteMeal);
            }
        }
        else{
            Intent intent = new Intent(this,LoginAcitivity.class);
            intent.putExtra("mealdetail","mealdetailchuyensangnhe:)");
            startActivityForResult(intent,100);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 100){
                Toast.makeText(this,"Login done",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search food");
        menu.add("My favorite food");
        User user = User.getInstance();
        if(user.getStatusLogin() != null && user.getStatusLogin().toLowerCase().equals("ok"))
        {
            menu.add("Logout");
        }
        else
        {
            menu.add("Login");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Search food")) {
            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("My favorite food")) {
            if (!this.getClass().equals(FavoriteActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("Login")) {
            if (!this.getClass().equals(LoginAcitivity.class)) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                startActivity(intent);
                finish();
            }
        }else if(item.getTitle().equals("Logout"))
        {
            AuthenLogout authenLogout = AuthenLogout.getInstance();
            boolean check = authenLogout.logoutUser();
            if(check)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", "");
                editor.commit();
                if (!this.getClass().equals(MainActivity.class)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You have not sign in yet", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}

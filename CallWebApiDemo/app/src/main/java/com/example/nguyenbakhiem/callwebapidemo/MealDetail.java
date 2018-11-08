package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
//        String des = meal.getDescription();
//        String subStr[] = des.split("/n");
//
//        for (int i = 0 ; i < subStr.length;i++){
//            textView.append(subStr[i]);
//            textView.append(System.getProperty("line.separator"));
//        }
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
        //imageButton.setImageResource(R.drawable.staron);
        if(user.getStatusLogin() != null && user.getStatusLogin().equalsIgnoreCase("ok")){
            //imageButton.setImageResource(R.drawable.staron);
            if(isStar){
                imageButton.setImageResource(R.drawable.staroff);
                //xoa mon an da thich
                String favoriteMeal = user.getFavoriteMeal();
                if(favoriteMeal.startsWith("" + meal.getId())){
                    favoriteMeal.replace("" + meal.getId() + ",", "");
                }
                else {
                    favoriteMeal.replace("," + meal.getId(), "");
                }
                Toast.makeText(this,favoriteMeal,Toast.LENGTH_LONG).show();
                user.setFavoriteMeal(favoriteMeal);
            }
            else{

                imageButton.setImageResource(R.drawable.staron);
                //them vao mon an da thich
                String favoriteMeal = user.getFavoriteMeal();
                favoriteMeal = favoriteMeal + "," + meal.getId();
                user.setFavoriteMeal(favoriteMeal);
            }
        }
        else{
            Intent intent = new Intent(this,LoginAcitivity.class);
            startActivityForResult(intent,100);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 100){

                Toast.makeText(this,"done",Toast.LENGTH_LONG).show();
            }
        }
    }
}

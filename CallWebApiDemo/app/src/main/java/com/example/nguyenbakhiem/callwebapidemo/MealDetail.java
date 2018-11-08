package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Intent;
import android.os.Build;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Intent intent = getIntent();
        Meal meal =(Meal) intent.getSerializableExtra("meal");
        imageView = findViewById(R.id.imageViewMealDetail);
        textView = findViewById(R.id.textViewMealDetail);
        imageButton = findViewById(R.id.imageButtonStar);
        setData(meal);
    }
    private void setData(Meal meal){
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
    }
    public void onStar(View view){
        User user = User.getInstance();
        //imageButton.setImageResource(R.drawable.staron);
        if(user.getStatusLogin() != null && user.getStatusLogin().equalsIgnoreCase("ok")){
            imageButton.setImageResource(R.drawable.staron);
        }
        else{
            Intent intent = new Intent(this,LoginAcitivity.class);
            startActivityForResult(intent,100);
        }
    }
}

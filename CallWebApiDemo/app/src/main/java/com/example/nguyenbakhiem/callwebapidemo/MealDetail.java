package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import adapter.MealAdapter;
import entity.Meal;
import entity.User;

public class MealDetail extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private ImageButton imageButton;
    private TextView textViewMealName;
    User user;
    private boolean isStar = false;
    Meal meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail_new);
        Intent intent = getIntent();
         meal =(Meal) intent.getSerializableExtra("meal");
        imageView = findViewById(R.id.imageViewMealDetail);
        textView = findViewById(R.id.textViewMealDetail);
        imageButton = findViewById(R.id.imageButtonStar);
        textViewMealName = findViewById(R.id.textViewMealName);
        user = User.getInstance();
        setData();


    }
    private void setData(){
        Picasso.with(this).load(meal.getImage_link()).into(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(meal.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(meal.getDescription()));
        }
        textViewMealName.setText(meal.getName());



        if(User.lstMeal != null && User.lstMeal.size() > 0){
            for (int i = 0; i < User.lstMeal.size();i++){
                //mon da thich
                if(User.lstMeal.get(i).getId() == meal.getId()){
                    imageButton.setImageResource(R.drawable.staron);
                    isStar = true;
                }
            }
        }


    }
    public void onStar(View view){
        if(user.getStatusLogin() != null && user.getStatusLogin().equalsIgnoreCase("ok")){
            if(isStar){
               Toast.makeText(this,"Đã xóa khỏi món ăn ưa thích",Toast.LENGTH_LONG).show();
                imageButton.setImageResource(R.drawable.staroff);
                //xoa mon an da thich
                isStar = false;
                String favoriteMeal = user.getFavoriteMeal();

                String subStr[] = favoriteMeal.split(",");
                favoriteMeal = "";
                if(subStr == null || subStr.length == 1){
                    favoriteMeal = "";
                }
                else{
                    for (int i = 0; i < subStr.length ; i++){
                        if(subStr[i].equalsIgnoreCase("" + meal.getId())){

                        }else {
                            if(i == 0){
                                favoriteMeal += subStr[i];
                            }
                            else{
                                favoriteMeal += "," + subStr[i];
                            }
                        }


                    }
                }

                for (int i = 0 ; i < User.lstMeal.size();i++){
                    if (User.lstMeal.get(i).getId() == meal.getId()) {
                        User.lstMeal.remove(i);
                        i--;
                    }
                }
                user.setFavoriteMeal(favoriteMeal);


            }
            else{
                imageButton.setImageResource(R.drawable.staron);
                Toast.makeText(this,"Đã thêm vào món ăn ưa thích",Toast.LENGTH_LONG).show();
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
                User.lstMeal.add(meal);
//                MealAdapter mealAdapter = MealAdapter.getInstanceFavorite(null,null);
//                mealAdapter.update(User.lstMeal);
//                mealAdapter.notifyDataSetChanged();
            }
            UpdateUser.getInstance().updateUser();
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
                onRestart();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Tìm món ăn");
        menu.add("Món ăn ưa thích");
        User user = User.getInstance();
        if(user.getStatusLogin() != null && user.getStatusLogin().toLowerCase().equals("ok"))
        {
            menu.add("Đăng xuất");
        }
        else
        {
            menu.add("Đăng nhập");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Tìm món ăn")) {
            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (item.getTitle().equals("Món ăn ưa thích")) {
            if (!this.getClass().equals(FavoriteActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("Đăng nhập")) {
            if (!this.getClass().equals(LoginAcitivity.class)) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                intent.putExtra("mealdetail","mealdetailchuyensangnhe:)");
                startActivityForResult(intent,100);
                //finish();
            }
        }else if(item.getTitle().equals("Đăng xuất"))
        {
            AuthenLogout authenLogout = AuthenLogout.getInstance();
            boolean check = authenLogout.logoutUser();
            if(check)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", "");
                editor.commit();
                if (this.getClass().equals(MealDetail.class)) {
                    Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                    onRestart();
                }else if( !this.getClass().equals(MainActivity.class))
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
            Intent intent = getIntent();
            if(intent.getStringExtra("favorite") != null)
            {
                intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

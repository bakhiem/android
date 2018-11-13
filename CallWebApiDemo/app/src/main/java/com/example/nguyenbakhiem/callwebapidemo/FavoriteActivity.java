package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.MealAdapter;
import entity.Meal;
import entity.User;

public class FavoriteActivity extends AppCompatActivity {
    User user;
    List<Meal> lstMeal ;
    private TextView txtResult;
    private ListView listView;
    String[] data;
    private String materialsReceive ="[";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        user = User.getInstance();
        txtResult = findViewById(R.id.textViewFavorite);
        listView = findViewById(R.id.listViewFavorite);
        lstMeal = User.getInstance().lstMeal;
       // String favouriteMeal = user.getFavoriteMeal();
        if(user.getStatusLogin() == null || !user.getStatusLogin().toLowerCase().equalsIgnoreCase("ok")){
            txtResult.setText("Bạn chưa đăng nhập, hãy đăng nhập để xem danh sách món ăn yêu thích");
        }
        else{
            //when favorite > 0
            if(lstMeal != null && lstMeal.size() > 0){
                //MealAdapter mealAdapter = MealAdapter.getInstanceFavorite(FavoriteActivity.this, lstMeal);
                MealAdapter mealAdapter = new MealAdapter(getApplicationContext(), lstMeal);
                listView.setAdapter(mealAdapter);
                txtResult.setText(Integer.toString(lstMeal.size())+" results:");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        sendMealtoDetail(lstMeal.get(position)) ;
                        finish();
                    }
                });
            }
            else {
                txtResult.setText("Bạn chưa có món ăn yêu thích nào");
            }
        }





    }
    public void sendMealtoDetail(Meal meal){
        Intent intent = new Intent(FavoriteActivity.this,MealDetail.class);
        intent.putExtra("meal",meal);
        intent.putExtra("favorite", "favoriteeeeeee");
        startActivity(intent);
        finish();

    }

//    class MyTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = new JSONArray(s);
//                Gson gson = new Gson();
//
//                lstMeal = gson.fromJson(s, new TypeToken<List<Meal>>() { }.getType());
//                MealAdapter mealAdapter = new MealAdapter(FavoriteActivity.this, lstMeal);
//                listView.setAdapter(mealAdapter);
//                txtResult.setText(Integer.toString(lstMeal.size())+" results:");
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        sendMealtoDetail(lstMeal.get(position)) ;
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//        }
//        public void sendMealtoDetail(Meal meal){
//            Intent intent = new Intent(FavoriteActivity.this,MealDetail.class);
//            intent.putExtra("meal",meal);
//            startActivity(intent);
//        }
////        public void sendMealtoDetail(Meal meal){
////            Intent intent = new Intent(MealActivity.this,MealDetail.class);
////            intent.putExtra("meal",meal);
////            startActivity(intent);
////        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/food/search");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//                JSONArray jsonArray = new JSONArray(Arrays.asList(data));
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("list",materialsReceive);
//                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//                wr.writeBytes(jsonObject.toString());
//                wr.flush();
//                wr.close();
//                InputStream inputStream = connection.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//                return sb.toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "";
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Tìm món ăn");
        menu.add("Món ăn ưa thích");
        User user = User.getInstance();
        if (user.getStatusLogin() != null && user.getStatusLogin().toLowerCase().equals("ok")) {
            menu.add("Đăng xuất");
        } else {
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
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("Đăng xuất")) {
            AuthenLogout authenLogout = AuthenLogout.getInstance();
            boolean check = authenLogout.logoutUser();
            if (check) {
                SharedPreferences sharedPreferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", "");
                editor.commit();
                if (!this.getClass().equals(MainActivity.class)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

}

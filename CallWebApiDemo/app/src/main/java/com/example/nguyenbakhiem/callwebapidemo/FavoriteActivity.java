package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
        lstMeal = new ArrayList<>();
        String favouriteMeal = user.getFavoriteMeal();
        //when favorite > 0
        if(favouriteMeal != null && favouriteMeal.length() > 0){
            //when only 1 meal
            if(favouriteMeal.length() == 1){
                materialsReceive = favouriteMeal + "]";
            }
            else {
                String[] subStr = favouriteMeal.split(",");
                data = new String[subStr.length];
                for(int i = 0; i < subStr.length; i++)
                {
                    materialsReceive += ""+subStr[i]+"";
                    if(i < subStr.length - 1)
                    {
                        materialsReceive += ",";
                    }else
                    {
                        materialsReceive += "]";
                    }
                }
            }

            MyTask myTask = new MyTask();
            myTask.execute("", "");
        }

    }
    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                Gson gson = new Gson();

                lstMeal = gson.fromJson(s, new TypeToken<List<Meal>>() { }.getType());
                MealAdapter mealAdapter = new MealAdapter(FavoriteActivity.this, lstMeal);
                listView.setAdapter(mealAdapter);
                txtResult.setText(Integer.toString(lstMeal.size())+" results:");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        sendMealtoDetail(lstMeal.get(position)) ;
                    }
                });
            } catch (Exception e) {

            }
        }
        public void sendMealtoDetail(Meal meal){
            Intent intent = new Intent(FavoriteActivity.this,MealDetail.class);
            intent.putExtra("meal",meal);
            startActivity(intent);
        }
//        public void sendMealtoDetail(Meal meal){
//            Intent intent = new Intent(MealActivity.this,MealDetail.class);
//            intent.putExtra("meal",meal);
//            startActivity(intent);
//        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/food/search");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                JSONArray jsonArray = new JSONArray(Arrays.asList(data));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("list",materialsReceive);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

}

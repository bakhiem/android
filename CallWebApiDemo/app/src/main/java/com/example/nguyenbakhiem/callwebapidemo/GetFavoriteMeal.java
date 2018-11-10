package com.example.nguyenbakhiem.callwebapidemo;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import adapter.MealAdapter;
import entity.Meal;
import entity.User;

public class GetFavoriteMeal {

    private User user;

    public static GetFavoriteMeal instance;

    public static GetFavoriteMeal getInstance() {
        if (instance == null) {
            instance = new GetFavoriteMeal();
        }
        return instance;
    }

    public void getFavoriteMeal() {
        user = User.getInstance();
        MyTask myTask = new MyTask();
        myTask.execute(user.getName());
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
                User.getInstance().lstMeal = gson.fromJson(s, new TypeToken<List<Meal>>() { }.getType());

                User u = User.getInstance();
                String fav = "";
                if(User.getInstance().lstMeal.size() == 1){
                    fav += User.getInstance().lstMeal.get(0).getId() ;
                }
                else if(User.getInstance().lstMeal.size() > 1){
                    for (int i = 0; i < User.getInstance().lstMeal.size();i ++){

                        if(i ==  0  ){
                            fav += User.getInstance().lstMeal.get(i).getId();
                        }
                        else{
                            fav +=  "," + User.getInstance().lstMeal.get(i).getId();
                        }
                    }
                }

                u.setFavoriteMeal(fav);






            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/user/getfavorite");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setDoOutput(true);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username", strings[0]);
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

    private GetFavoriteMeal() {

    }
}

package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import entity.User;

public class AuthenLogout {
    private User user;
    private SharedPreferences preferences;
    private String token = "";
    public static AuthenLogout instance;

    public static AuthenLogout getInstance() {
        if (instance == null) {
            instance = new AuthenLogout();
        }
        return instance;
    }

    public boolean logoutUser() {
        user = User.getInstance();
        if (user.getStatusLogin().toLowerCase().equals("ok")) {
            MyTask myTask = new MyTask();
            myTask.execute(user.getName());
            user.setStatusLogin("");
            user.setName("");
            user.setFavoriteMeal("");
            User.lstMeal.clear();
            return true;
        }
        return false;
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
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/user/logout");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

    }

    private AuthenLogout() {

    }
}

package com.example.nguyenbakhiem.callwebapidemo;

import android.os.AsyncTask;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import entity.User;

public class UpdateUser {
    private User user;

    public static UpdateUser instance;
    private String dataSend  = "[";

    public static UpdateUser getInstance() {
        if (instance == null) {
            instance = new UpdateUser();
        }
        return instance;
    }

    public void checkLoginToken() {
        user = User.getInstance();
        String[] meal = user.getFavoriteMeal().split(",");
        for(int i = 0; i < meal.length; i++)
        {
            dataSend += ""+meal[i]+"";
            if(i < meal.length - 1)
            {
                dataSend += ",";
            }else
            {
                dataSend += "]";
            }
        }
        MyTask myTask = new MyTask();
        myTask.execute(user.getName(), dataSend);
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
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/user/loginacc");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setDoOutput(true);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username", strings[0]);
                jsonObject.addProperty("list", strings[1]);
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

    private UpdateUser() {

    }

}

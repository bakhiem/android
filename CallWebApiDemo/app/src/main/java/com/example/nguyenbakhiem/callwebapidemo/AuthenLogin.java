package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
import java.util.List;

import entity.Material;
import entity.User;

public class AuthenLogin {
    private SharedPreferences preferences;
    private String token="";
    private User user;

    public AuthenLogin(String token) {
        user = User.getInstance();
        MyTask myTask = new MyTask();
        //preferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
      //  token = preferences.getString("token", "");

        if (token.trim().length() > 0) {
            myTask.execute(token);
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
            if(s.trim().length()>0)
            {
                user.setStatusLogin("ok");
            }
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
                jsonObject.addProperty("token",strings[0]);
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

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
    }

}

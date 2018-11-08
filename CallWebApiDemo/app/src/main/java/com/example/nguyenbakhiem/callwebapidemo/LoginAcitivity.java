package com.example.nguyenbakhiem.callwebapidemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapter.MealAdapter;
import entity.Meal;
import entity.User;

public class LoginAcitivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtPwd;

    private String uName;
    private String pwd;
    MyTask myTask;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);
        txtName = (TextView) findViewById(R.id.txtNameLogin);
        txtPwd = (TextView) findViewById(R.id.txtPwdLogin);
        user = User.getInstance();
        myTask = new MyTask();
    }

    public void login(View view)
    {
        uName = txtName.getText().toString();
        pwd = txtPwd.getText().toString();
        myTask.execute(uName,pwd);
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
            if(s.toLowerCase().contains("ok"))
            {
                user.setStatusLogin("OK");
            }else
            {
                Toast.makeText(getApplicationContext(),"Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Toast.makeText(getApplicationContext(), strings[0], Toast.LENGTH_LONG).show();
            try {
                URL url = new URL("https://provideapi.herokuapp.com/thoan");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write("username:"+uName+",password:"+pwd);
                writer.flush();
                outputStream.close();
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
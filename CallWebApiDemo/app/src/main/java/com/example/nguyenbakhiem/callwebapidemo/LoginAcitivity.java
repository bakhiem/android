package com.example.nguyenbakhiem.callwebapidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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

    private EditText txtName;
    private EditText txtPwd;
    private CheckBox cbRemember;

    private String uName;
    private String pwd;
    private MyTask myTask;
    private String token = "";
    private User user;
    private boolean typeLogin = true;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);
        txtName = (EditText) findViewById(R.id.txtNameLogin);
        txtPwd = (EditText) findViewById(R.id.txtPwdLogin);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        myTask = new MyTask();
        user = User.getInstance();
        preferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        //register sang
        Intent intent = getIntent();
        if (intent.getStringExtra("registerSuccess") != null) {

        } else {
            //đã register và từng login remember
            if (user.getStatusLogin() == null) {
                user.setStatusLogin("");
            }
            if (!user.getStatusLogin().toLowerCase().equals("ok")) {
                token = preferences.getString("token", "");
                if (token.trim().length() > 0) {
                    AuthenLogin authenLogin = AuthenLogin.getInstance();
                    authenLogin.checkLoginToken(token);
                }
            }
        }

    }

    public void login(View view) {
        uName = txtName.getText().toString();
        pwd = txtPwd.getText().toString();

        if(uName.length() > 0 && pwd.length() > 0){
            myTask = new MyTask();
            myTask.execute(uName, pwd);
        }
        else{
            Toast.makeText(this, "Tên đăng nhập và mật khẩu trống", Toast.LENGTH_SHORT).show();
        }

    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);

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
            if (s.trim().length() > 4) {
                user.setName(uName);
                user.setStatusLogin("ok");
                GetFavoriteMeal getFavoriteMeal = GetFavoriteMeal.getInstance();
                getFavoriteMeal.getFavoriteMeal();
                if (cbRemember.isChecked()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", s);
                    editor.commit();
                }
                Intent intent = getIntent();
                if (intent.getStringExtra("mealdetail") != null) {
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                typeLogin = true;
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/user/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setDoOutput(true);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username", strings[0]);
                jsonObject.addProperty("password", strings[1]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search food");
        menu.add("My favorite food");
        User user = User.getInstance();
        if (user.getStatusLogin() != null && user.getStatusLogin().toLowerCase().equals("ok")) {
            menu.add("Logout");
        } else {
            menu.add("Login");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Search food")) {
            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("My favorite food")) {
            if (!this.getClass().equals(FavoriteActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("Login")) {
            if (!this.getClass().equals(LoginAcitivity.class)) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                startActivity(intent);
                finish();
            }
        } else if (item.getTitle().equals("Logout")) {
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
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You have not sign in yet", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}
package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import adapter.AutocompleteMaterialAdapter;
import adapter.MaterialAdapter;
import entity.Material;
import entity.User;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    List<Material> materials;
    List<Material> materialsLv;
    private ListView listView;
    private MaterialAdapter materialAdapter;

    private AutoCompleteTextView autoCompleteTextView;
    private User user;
    private SharedPreferences preferences;
    private String token = "";
    private boolean checkLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
//        autoCompleteTextView.setHint("Search material...");
//        autoCompleteTextView.setThreshold(1);
//        autoCompleteTextView.setAdapter();


        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);

        user = User.getInstance();

//        if (user.getStatusLogin() == null || user.getStatusLogin().toLowerCase().equals("ok")) {
//            preferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
//            token = preferences.getString("token", "");
//
//            if (token.trim().length() > 0) {
//                AuthenLogin authenLogin = AuthenLogin.getInstance();
//                authenLogin.checkLoginToken(token);
//            }
//        }

        materials = new ArrayList<>();
        materialsLv = new ArrayList<>();
        MyTask myTask = new MyTask();

        myTask.execute("", "");


        materialAdapter = new MaterialAdapter(this, materialsLv);
        listView.setAdapter(materialAdapter);
    }

    public void searchmeal(View view) {
        Intent intent = new Intent(getApplicationContext(), MealActivity.class);
        int[] arrayId = getListMealId();
        intent.putExtra("arrayId", arrayId);
        startActivity(intent);
    }

    public int[] getListMealId() {
        int[] arrayId = new int[materialsLv.size()];
        for (int i = 0; i < materialsLv.size(); i++) {
            arrayId[i] = materialsLv.get(i).getId();
        }
        return arrayId;
    }

    private void setAdapter() {
        autoCompleteTextView.setHint("Tìm nguyên liệu");
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(new AutocompleteMaterialAdapter(getApplicationContext(), materials));
//        ArrayAdapter<Material> adapter = new ArrayAdapter<Material>(this, android.R.layout.select_dialog_item,materials);
//        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        actv.setThreshold(1);
//        actv.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Material item = (Material) parent.getItemAtPosition(position);
                autoCompleteTextView.setText("");
                if (materialsLv.contains(item)) {

                } else {
                    materials.remove(item);
                    materialsLv.add(0, item);
                    materialAdapter.update(materialsLv);
                    materialAdapter.notifyDataSetChanged();
                }


            }
        });
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

            JSONArray json = null;
            try {
                json = new JSONArray(s);
                Gson gson = new Gson();
                materials = gson.fromJson(s, new TypeToken<List<Material>>() {
                }.getType());
                setAdapter();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://provideapi.herokuapp.com");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        User user = User.getInstance();
        if (user.getStatusLogin() == null) {
            user.setStatusLogin("");
        }
        preferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        if (token.trim().length() > 0) {
            AuthenLogin authenLogin = AuthenLogin.getInstance();
            authenLogin.checkLoginToken(token);
        }
        user = User.getInstance();
        menu.add("Search food");
        menu.add("My favorite food");

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
            if (!this.getClass().equals(MainActivity.class)) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
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
                    Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You have not sign in yet", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}

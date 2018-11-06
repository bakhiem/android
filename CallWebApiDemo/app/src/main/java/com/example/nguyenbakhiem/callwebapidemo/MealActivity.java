package com.example.nguyenbakhiem.callwebapidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapter.MealAdapter;
import entity.Meal;

public class MealActivity extends AppCompatActivity {

    private TextView txtResult;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        txtResult = (TextView) findViewById(R.id.txtMeal);
        listView = (ListView) findViewById(R.id.listViewMeal);
        MyTask myTask = new MyTask();
        myTask.execute("", "");
        //Toast.makeText(this,"move",Toast.LENGTH_LONG).show();

    }

    public void setAdapter() {

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
                List<Meal> lstMeal = new ArrayList<>();
                lstMeal = gson.fromJson(s, new TypeToken<List<Meal>>() { }.getType());
                MealAdapter mealAdapter = new MealAdapter(MealActivity.this, lstMeal);
                listView.setAdapter(mealAdapter);
                txtResult.setText(Integer.toString(lstMeal.size())+" results:");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://provideapi.herokuapp.com/thoan");
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
}

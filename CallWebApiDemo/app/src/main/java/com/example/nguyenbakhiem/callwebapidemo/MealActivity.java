package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Intent;
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

public class MealActivity extends AppCompatActivity {

    private TextView txtResult;
    private ListView listView;
    int[] materials;
    private String materialsReceive ="";
    List<Meal> lstMeal ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        txtResult = (TextView) findViewById(R.id.txtMeal);
        listView = (ListView) findViewById(R.id.listViewMeal);
        lstMeal = new ArrayList<>();
        Intent intent = getIntent();
        materials = intent.getIntArrayExtra("arrayId");
        for(int i = 0; i < materials.length; i++)
        {
            materialsReceive += materials[i];
            if(i < materials.length - 1)
            {
                materialsReceive += ",";
            }
        }
        MyTask myTask = new MyTask();
        myTask.execute("", "");
//        Toast.makeText(this,materialsReceive,Toast.LENGTH_LONG).show();

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

                lstMeal = gson.fromJson(s, new TypeToken<List<Meal>>() { }.getType());
                MealAdapter mealAdapter = new MealAdapter(MealActivity.this, lstMeal);
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
            Intent intent = new Intent(MealActivity.this,MealDetail.class);
            intent.putExtra("meal",meal);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://provideapi.herokuapp.com/thoan");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(materialsReceive);
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

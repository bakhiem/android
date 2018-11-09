package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.Arrays;
import java.util.List;

import adapter.MealAdapter;
import entity.Meal;
import entity.User;

public class MealActivity extends AppCompatActivity {

    private TextView txtResult;
    private ListView listView;
    int[] materials;
    private String materialsReceive ="[";
    List<Meal> lstMeal ;
    List<String> listSend;
    String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        txtResult = (TextView) findViewById(R.id.txtMeal);
        listView = (ListView) findViewById(R.id.listViewMeal);
        lstMeal = new ArrayList<>();
        Intent intent = getIntent();
        materials = intent.getIntArrayExtra("arrayId");
        listSend = new ArrayList<String>();
        data = new String[materials.length];
        for(int i = 0; i < materials.length; i++)
        {
            //data[i] = ""+ materials[i]+"";
            materialsReceive += ""+materials[i]+"";
            if(i < materials.length - 1)
            {
                materialsReceive += ",";
            }else
            {
                materialsReceive += "]";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search food");
        menu.add("My favorite food");
        User user = User.getInstance();
        if(user.getStatusLogin() != null && user.getStatusLogin().toLowerCase().equals("ok"))
        {
            menu.add("Logout");
        }
        else
        {
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
        }else if(item.getTitle().equals("Logout"))
        {
            AuthenLogout authenLogout = AuthenLogout.getInstance();
            boolean check = authenLogout.logoutUser();
            if(check)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", "");
                editor.commit();
                if (!this.getClass().equals(MainActivity.class)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You have not sign in yet", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

}

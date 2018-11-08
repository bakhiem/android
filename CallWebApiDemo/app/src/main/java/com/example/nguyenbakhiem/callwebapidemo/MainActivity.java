package com.example.nguyenbakhiem.callwebapidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
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

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    List<Material> materials;
    List<Material> materialsLv;
    private ListView listView;
    private MaterialAdapter materialAdapter;

    private AutoCompleteTextView autoCompleteTextView;
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

        materials = new ArrayList<>();
        materialsLv = new ArrayList<>();
        MyTask myTask = new MyTask();
        myTask.execute("","");

        materialAdapter = new MaterialAdapter(this,materialsLv);
        listView.setAdapter(materialAdapter);


    }
    public void searchmeal(View view){
        Intent intent = new Intent(getApplicationContext(),LoginAcitivity.class);
        int[] arrayId = getListMealId();
        intent.putExtra("arrayId",arrayId);
        startActivity(intent);
    }
    public int[] getListMealId(){
        int[] arrayId = new int[materialsLv.size()];
        for (int i = 0 ; i < materialsLv.size(); i ++){
            arrayId[i] = materialsLv.get(i).getId();
        }
        return arrayId;
    }
    private void setAdapter(){
        autoCompleteTextView.setHint("Tìm nguyên liệu");
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(new AutocompleteMaterialAdapter(getApplicationContext(),materials));
//        ArrayAdapter<Material> adapter = new ArrayAdapter<Material>(this, android.R.layout.select_dialog_item,materials);
//        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        actv.setThreshold(1);
//        actv.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Material item =(Material) parent.getItemAtPosition(position);
                autoCompleteTextView.setText("");
                if(materialsLv.contains(item)){

                }
                else{
                    materials.remove(item);
                    materialsLv.add(0,item);
                    materialAdapter.update(materialsLv);
                    materialAdapter.notifyDataSetChanged();
                }


            }
        });
    }


    class MyTask extends AsyncTask<String, Void, String>{

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
                materials = gson.fromJson(s, new TypeToken<List<Material>>(){}.getType());
                setAdapter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://provideapi.herokuapp.com" );
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                InputStream inputStream =  connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
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

package com.example.nguyenbakhiem.callwebapidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPwd;
    private EditText txtEmail;

    private String uName;
    private String pwd;
    private String email;

    private MyTask myTask;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtUsername = (EditText) findViewById(R.id.txtNameRegister);
        txtPwd = (EditText) findViewById(R.id.txtPwdRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
        myTask = new MyTask();
    }

    public void register(View v) {
        uName = txtUsername.getText().toString();
        pwd = txtPwd.getText().toString();
        email = txtEmail.getText().toString();
        myTask.execute(uName, pwd,email);
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

            if (s.toLowerCase().trim().length() > 0) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                intent.putExtra("registerSuccess","ok");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Tên đăng nhập đã tồn tại", Toast.LENGTH_LONG).show();
            }
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
                writer.write("{username:" + strings[0] + ",password:" + strings[1] + ",email:" + strings[2] +"}");
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

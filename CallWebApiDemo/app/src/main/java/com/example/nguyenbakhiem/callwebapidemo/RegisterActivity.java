package com.example.nguyenbakhiem.callwebapidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import entity.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPwd;
    private EditText txtEmail;
    private EditText txtRePwd;

    private String uName;
    private String pwd;
    private String email;
    private String rePwd;

    private MyTask myTask;
    private SharedPreferences preferences;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtUsername = (EditText) findViewById(R.id.txtNameRegister);
        txtPwd = (EditText) findViewById(R.id.txtPwdRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
        txtRePwd = (EditText) findViewById(R.id.txtRePwdRegister);
        myTask = new MyTask();
    }

    public void register(View v) {
        uName = txtUsername.getText().toString();
        pwd = txtPwd.getText().toString();
        email = txtEmail.getText().toString();
        rePwd = txtRePwd.getText().toString();

        if(uName.length() == 0 || pwd.length() == 0 || email.length() == 0 || rePwd.length() == 0){
            Toast.makeText(getApplicationContext(), "Điền vào tất cả các trường", Toast.LENGTH_LONG).show();
        }
        else{
            if(rePwd.equalsIgnoreCase(pwd))
            {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    myTask.execute(uName, pwd,email);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Không đúng định dạng email", Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Password is not match", Toast.LENGTH_LONG).show();
            }
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

            if (s.toLowerCase().trim().length() > 0) {
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                intent.putExtra("registerSuccess","ok");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Tên đăng nhập đã tồn tại hoặc password lỗi", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://ec2-13-229-209-209.ap-southeast-1.compute.amazonaws.com:3001/api/user/register");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setDoOutput(true);
                //OutputStream outputStream = connection.getOutputStream();
                //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username",strings[0]);
                jsonObject.addProperty("password",strings[1]);
                jsonObject.addProperty("email",strings[2]);
                //writer.write("username:"+strings[0]+",password:"+strings[1]+",email:"+strings[2]);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();
                //writer.flush();
                //outputStream.close();

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

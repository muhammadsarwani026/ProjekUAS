package com.example.muhammadsarwani.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalStore;
    String url, success;
    SessionManager session;
    Intent a;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                url="http://muhammadsarwani.16mb.com/login.php?" +
                        "username=" + username +
                        "&password=" + password;
                Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
                if(username.trim().length()>0
                        && password.trim().length()>0){
                    new AmbilData().execute();
                }else{
                    alert.showAlertDialog(Login.this,"Login Failed...!",
                            "Silahkan isi username dan password",false);
                }

                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this,Register.class));
                break;
        }
    }

    public void OnLogin(View view){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String type = "login";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username,password);
    }


    public class AmbilData extends AsyncTask<String,String,String> {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<
                HashMap<String, String>>();
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Loading Data...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                success = json.getString("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("login");
                if (success.equals("1")) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        //Storing each json item in variable
                        String username = c.getString("username").trim();
                        String email = c.getString("email").trim();
                        session.createLoginSession(username, email);
                        Log.e("ok", "ambil data");
                    }
                } else {
                    Log.e("Error", "tidak bisa ambil data 0");
                }
            } catch (Exception e) {
                Log.e("Error", "Tidak bisa ambil data 1");
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(success.equals("1")){
                a = new Intent(Login.this, MainActivity.class);
                startActivity(a);
                finish();
            }else{
                Toast.makeText(getBaseContext(), "Username/password incorrect!!", Toast.LENGTH_SHORT).show();
                alert.showAlertDialog(Login.this, "Login Failed..", "Username/Password is incorrect",false);

            }////
        }
    }

}
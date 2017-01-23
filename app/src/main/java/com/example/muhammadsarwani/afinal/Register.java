package com.example.muhammadsarwani.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etUsername, etPassword; ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://muhammadsarwani.16mb.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegister = (Button)findViewById(R.id.bRegister);
        etName = (EditText)findViewById(R.id.etName);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);

        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bRegister:
                String name = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                new InputData().execute(name,username,password);
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                break;
        }
    }

    public class InputData extends AsyncTask<String, String, String> {
        String success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Registering Account...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            String strname = args[0],
                    strusername = args[1],
                    strpassword = args[2];

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",strname));
            params.add(new BasicNameValuePair("username",strusername));
            params.add(new BasicNameValuePair("password",strpassword));
            JSONObject json =
                    jsonParser.makeHttpRequest(url,
                            "POST", params);
            try {
                success = json.getString("success");
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (success.equals("1")) {
                Toast.makeText(getApplicationContext(),"Registration Succesfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_LONG).show();
            }
        }
    }
}

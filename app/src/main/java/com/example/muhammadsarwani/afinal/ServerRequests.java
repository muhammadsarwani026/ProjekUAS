package com.example.muhammadsarwani.afinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muhammad Sarwani on 12/31/2016.
 */

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.muhammadsarwani.16mb.com/public_html/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestsParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestsParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestsParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestsParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity((dataToSend)));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... voids) {
            ArrayList<BasicNameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestsParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestsParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestsParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestsParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity((dataToSend)));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                returnedUser = null;

                if (jsonObject.length() == 0) {
                    returnedUser = null;
                } else {
                    String name = jsonObject.getString("name");

                    returnedUser = new User(name, user.username, user.password);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
package com.example.instabugs_task.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;


public class Internet {

    public boolean hasInternetAccess(Context context) {
       InternetAsyncTask asyncTask=new InternetAsyncTask();

        if (isNetworkAvailable(context)) {
            try {
                return asyncTask.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.d( "Interent", "No network available!" );
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );


        Log.e( "Network", String.valueOf( connectivityManager.getActiveNetworkInfo() != null ) );
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private class InternetAsyncTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute( aBoolean );
            Log.e( "Internet", String.valueOf( aBoolean ) );
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpsURLConnection) (new URL("https://google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return true;
            } catch (IOException e) {
                Log.e( "Internet", e.getMessage() );
                return false;
            } }


    }
}

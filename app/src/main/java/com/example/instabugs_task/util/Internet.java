package com.example.instabugs_task.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;


public class Internet {

    private final StartAsyncTask startAsyncTask;
    private final Context context;
    public Internet(Context context){
        this.context=context;
        startAsyncTask=(StartAsyncTask) context;
    }

    public void hasInternetAccess() {
       InternetAsyncTask asyncTask=new InternetAsyncTask();
        if (isNetworkAvailable()) {
            try {
                asyncTask.execute();
                Log.e( "Interent connection", "No network available!" );
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("network state",e.getMessage());
                startAsyncTask.start(false);
            }
        }
        else{
            Log.d( "Interent", "No network available!" );
            startAsyncTask.start(false);
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        Log.d( "Network", String.valueOf( connectivityManager.getActiveNetworkInfo() != null ) );
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private class InternetAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpsURLConnection) (new URL("https://google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                startAsyncTask.start(true);
                return null;
            } catch (IOException e) {
                Log.e( "Internet exception", e.getMessage() );
                startAsyncTask.start(false);
                return null;
            } }


    }
}

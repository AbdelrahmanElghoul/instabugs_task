package com.example.instabugs_task.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.instabugs_task.R;



public class DatabaseBackup {
    private final static String TAG="SharedPreference";

    public static void saveBackup(Context context, String words){

        SharedPreferences mPreferences= context.getSharedPreferences(
                context.getResources().getString(R.string.sharedPrefernce_Name), Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(context.getResources().getString(R.string.sharedPrefernce_Key)
                , words);
        preferencesEditor.apply();
        Log.d(TAG,"save backup");
    }
    public static String getBackup(Context context){
        Log.d(TAG,"get backup");
        SharedPreferences mPreferences= context.getSharedPreferences(
                context.getResources().getString(R.string.sharedPrefernce_Name), Context.MODE_PRIVATE);
        return mPreferences.getString(context.getResources().getString(R.string.sharedPrefernce_Key),null);
    }
    public static void deleteBackup(Context context){

        SharedPreferences mPreferences= context.getSharedPreferences(
                context.getResources().getString(R.string.sharedPrefernce_Name), Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
        Log.d(TAG,"delete backup");
    }
}

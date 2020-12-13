package com.example.instabugs_task.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.instabugs_task.R;



public class DatabaseBackup {

    public static void saveBackup(Context context, String words){

        SharedPreferences mPreferences= context.getSharedPreferences(
                context.getResources().getString(R.string.sharedPrefernce_Name), Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(context.getResources().getString(R.string.sharedPrefernce_Key)
                , words);
        preferencesEditor.apply();
    }

    public static String getBackup(Context context){
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
    }
}

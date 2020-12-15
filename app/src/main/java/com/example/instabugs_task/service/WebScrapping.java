package com.example.instabugs_task.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instabugs_task.MainActivity;
import com.example.instabugs_task.WordsAdapter;
import com.example.instabugs_task.database.Database;
import com.example.instabugs_task.database.DatabaseBackup;
import com.example.instabugs_task.util.UpdateUI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

 public class WebScrapping extends AsyncTask<Boolean, Void,  HashMap<String,Integer>> {

     private final Context context;

     Database db;
     String TAG="WebScrapping";
     UpdateUI updateUI;
     public WebScrapping(Context context){
         this.context=context;
         db=new Database(context,1 );
         updateUI=(UpdateUI) context;
     }

    @Override
    protected  HashMap<String,Integer> doInBackground(Boolean... booleans) {
        Log.d(TAG, "start scrapping");
        HashMap<String,Integer> wordsMap=new HashMap<>();
        try {
            //Has internet access
            if(booleans[0]){
                Document document = Jsoup.connect("https://instabug.com").get();
                String words = document.text().toLowerCase().replaceAll("[^a-z0-9\\s]", "");
//                   Log.d(TAG + " jsoup doc", document.toString());
//                   Log.d(TAG + " jsoup words", words);
                DatabaseBackup.saveBackup(context,words);
                wordsMap=word2map(words);
            }
            else {
                wordsMap = db.getAll();
                // no internet + no data in db
                if (wordsMap.size() == 0) {
                    String backup = DatabaseBackup.getBackup(context);
                    if (backup != null) {
                        wordsMap=word2map(backup);
                    }
                    else{
                        throw new Exception("offline mode and no data to display");
                    }
                }
            }

            return wordsMap;

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG+"exception",e.getMessage());
            String backup = DatabaseBackup.getBackup(context);
            if (backup != null) {
                word2map(backup);
            }
        }
        return wordsMap;
    }

    @Override
    protected void onPostExecute(HashMap wordsMap) {
        super.onPostExecute(wordsMap);

        Log.d(TAG,String.valueOf(wordsMap.size()));
        Log.d(TAG,"onPostExecute");
        if(wordsMap.size()==0){
            Toast.makeText(context, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
            return;
        }
        db.AddAll(wordsMap);
        updateUI.update(wordsMap);
        Log.d(TAG, String.valueOf(wordsMap.size()));
    }

    HashMap<String,Integer> word2map(String words){
         HashMap<String,Integer> wordsMap=new HashMap<>();
        Log.d(TAG,"Word2Map");
        String[] wordList= words.split(" ");
        for (String word : wordList){
            if(word.trim().length()==0) continue;
            if(wordsMap.get(word)==null){
                wordsMap.put(word,1);
            }
            else wordsMap.put(word, wordsMap.get(word) +1);
        }
        return wordsMap;
    }

}
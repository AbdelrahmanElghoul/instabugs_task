package com.example.instabugs_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instabugs_task.database.Database;
import com.example.instabugs_task.database.DatabaseBackup;
import com.example.instabugs_task.util.Internet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    WebScrapping scrapping;
    String TAG=MainActivity.class.getSimpleName();
    ProgressBar progressBar;
    HashMap<String,Integer> wordsMap;
    RecyclerView rv_words;
    WordsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        init();
        scrapping.execute();
    }

    void bindViews(){
        progressBar=findViewById(R.id.progress_bar);
        rv_words=findViewById(R.id.rv_words);
    }

    void init(){
        progressBar.setVisibility(View.VISIBLE);
        scrapping=new WebScrapping();

        wordsMap= new HashMap<>();
        rv_words.setHasFixedSize(true);
        rv_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStop() {
        super.onStop();
        scrapping.cancel(true);
    }

    private class WebScrapping extends AsyncTask<Void, Void, Void> {


        Database db=new Database( getApplicationContext(),1 );
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "start");
            try {
                //Has internet access
                boolean isConnected=new Internet().isNetworkAvailable(getApplicationContext());
                Log.d(TAG+" network state", String.valueOf(isConnected));
               if(isConnected){
                   Document document = Jsoup.connect("https://instabug.com").get();
                   String words = document.text().toLowerCase().replaceAll("[^a-z0-9\\s]", "");

                   Log.d(TAG + " jsoup doc", document.toString());
                   Log.d(TAG + " jsoup words", words);

                   DatabaseBackup.saveBackup(getApplicationContext(),words);
                   word2map(words);
               }
               else {
                   wordsMap = db.getAll();
                   // no internet + no data in db
                   if (wordsMap.size() == 0) {
                       String backup = DatabaseBackup.getBackup(getApplicationContext());
                       if (backup != null) {
                           word2map(backup);
                       }
                       else{
                         throw new Exception("offline mode and no data to display");
                       }
                   }
               }

            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG+"exception",e.getMessage());
                String backup = DatabaseBackup.getBackup(getApplicationContext());
                if (backup != null) {
                    word2map(backup);
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Log.d(TAG,String.valueOf(wordsMap.size()));
            Log.d(TAG,"onPostExecute");
            if(wordsMap.size()==0){
                Toast.makeText(MainActivity.this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
                return;
            }
            db.AddAll(wordsMap);
            List<String> wordList = new ArrayList<String>(wordsMap.keySet());
            List<Integer> wordCountList = new ArrayList<Integer>(wordsMap.values());
            adapter=new WordsAdapter(getApplicationContext(),wordList,wordCountList);
            rv_words.setAdapter(adapter);
            Log.d(TAG, String.valueOf(wordsMap.size()));
        }

        void word2map(String words){
            Log.d(TAG,"Word2Map");
            String[] wordList= words.split(" ");
            for (String word : wordList){
                if(word.trim().length()==0) continue;
                if(wordsMap.get(word)==null){
                    wordsMap.put(word,1);
                }
                else wordsMap.put(word, wordsMap.get(word) +1);
            }

        }

    }
}


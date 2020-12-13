package com.example.instabugs_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

        rv_words.setHasFixedSize(true);
        rv_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStop() {
        super.onStop();
        scrapping.cancel(true);
    }

    private class WebScrapping extends AsyncTask<Void, Void, HashMap<String, Integer>> {

        private final HashMap<String,Integer> wordsMap= new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<String, Integer> doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect("https://instabug.com").get();
                String words = document.text().toLowerCase().replaceAll("[^a-z0-9\\s]", "");
//                String text=document.toString().toLowerCase().replaceAll("[^a-z0-9\\s]", "");

                Log.d(TAG+" jsoup doc",document.toString());
                Log.d(TAG+" jsoup words", words);
//                Log.d(TAG+" jsoup regex",text);
//                Pattern wordPattern = Pattern.compile("\\s[_A-Za-z0-9-]\\s");
//                Matcher matcher=wordPattern.matcher(document.toString());
//                Log.d(TAG+"matcher count", String.valueOf(matcher.groupCount()));

                String[] wordList= words.split(" ");
                for (String word : wordList){
                    if(word.trim().length()==0) continue;
                    if(wordsMap.get(word)==null){
                        wordsMap.put(word,1);
                    }
                    else wordsMap.put(word, wordsMap.get(word) +1);
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG+"exception",e.getMessage());
            }
            return wordsMap;
        }


        @Override
        protected void onPostExecute(HashMap<String, Integer> aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            List<String> wordList = new ArrayList<String>(wordsMap.keySet());
            List<Integer> wordCountList = new ArrayList<Integer>(wordsMap.values());
            adapter=new WordsAdapter(getApplicationContext(),wordList,wordCountList);
            rv_words.setAdapter(adapter);
            Log.d(TAG, String.valueOf(wordsMap.size()));
        }
    }
}
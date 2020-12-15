package com.example.instabugs_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instabugs_task.database.Database;
import com.example.instabugs_task.database.DatabaseBackup;
import com.example.instabugs_task.service.Internet;
import com.example.instabugs_task.service.WebScrapping;
import com.example.instabugs_task.util.StartAsyncTask;
import com.example.instabugs_task.util.UpdateUI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StartAsyncTask, UpdateUI {

    WebScrapping scrapping;
    String TAG=MainActivity.class.getSimpleName();
    ProgressBar progressBar;
//    HashMap<String,Integer> wordsMap;
    RecyclerView rv_words;
    WordsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        init();
//        scrapping.execute();
    }

    void bindViews(){
        progressBar=findViewById(R.id.progress_bar);
        rv_words=findViewById(R.id.rv_words);
    }

    void init(){
        progressBar.setVisibility(View.VISIBLE);
        scrapping=new WebScrapping(this);
        new Internet(this).hasInternetAccess();
//
//        wordsMap= new HashMap<>();
        rv_words.setHasFixedSize(true);
        rv_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(scrapping!=null)scrapping.cancel(true);
    }

    @Override
    public void getData(boolean connected) {
        Log.d("startAsyncTask",String.valueOf(connected));
        if(scrapping!=null && !scrapping.isCancelled())scrapping.execute(connected);
    }



    @Override
    public void update(HashMap<String,Integer> wordsMap) {
        progressBar.setVisibility(View.GONE);
        List<String> wordList = new ArrayList<String>(wordsMap.keySet());
        List<Integer> wordCountList = new ArrayList<Integer>(wordsMap.values());
        adapter=new WordsAdapter(getApplicationContext(),wordList,wordCountList);

        rv_words.setAdapter(adapter);
        Log.d(TAG, String.valueOf(wordsMap.size()));
    }
}


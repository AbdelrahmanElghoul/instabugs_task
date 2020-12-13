package com.example.instabugs_task;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder> {

    public WordsAdapter(Context context, List<String> wordList, List<Integer> wordCountList) {
        this.context = context;
        this.wordList = wordList;
        this.wordCountList = wordCountList;
    }

    Context context;
    List<String> wordList;
    List<Integer> wordCountList;


    @NonNull
    @Override
    public WordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.words_layout,parent,false);
        return new WordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsViewHolder holder, int position) {
        holder.txt_word.setText(wordList.get(position));
        holder.txt_count.setText(wordCountList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    class WordsViewHolder extends RecyclerView.ViewHolder{

        TextView txt_word;
        TextView txt_count;
        public WordsViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_word=itemView.findViewById(R.id.txt_word);
            txt_count=itemView.findViewById(R.id.txt_count);
        }
    }

}

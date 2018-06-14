package com.aniketvishal.commonindianwords.Adapters;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aniketvishal.commonindianwords.Database.DatabaseHelper;
import com.aniketvishal.commonindianwords.Models.WordListModel;
import com.aniketvishal.commonindianwords.R;
import com.aniketvishal.commonindianwords.ViewHolders.WordListViewHolder;

import java.util.List;

/**
 * Created by aniketvishal on 26/11/17.
 */

public class WordListAdapter extends RecyclerView.Adapter<WordListViewHolder> {

    TextToSpeech ttsObject;

    private List<WordListModel> mWordList;
    private Context context;
    private Activity activity;
    private int result;

    public WordListAdapter(TextToSpeech ttsObject, int result) {
        this.ttsObject = ttsObject;
        this.result = result;
    }

    public WordListAdapter(List<WordListModel> mWordList, Context context, TextToSpeech ttsObject, int result) {
        this.mWordList = mWordList;
        this.context = context;
        activity = (Activity)context;
        this.result = result;
        this.ttsObject = ttsObject;
    }

    @Override
    public WordListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_list_layout,parent,false);

        return new WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WordListViewHolder holder, final int position) {

        final WordListModel wordmeaning = mWordList.get(position);
        holder.mNameTv.setText(wordmeaning.getmWord());
        holder.mNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(activity,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }else {
                    ttsObject.speak(wordmeaning.getmWord(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        holder.mMeaningTv.setText(wordmeaning.getmMeaning());
        holder.mMeaningTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(activity,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }else {
                    ttsObject.speak(wordmeaning.getmMeaning(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        holder.mFavIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper mydb = new DatabaseHelper(context);

                try {
                    if (wordmeaning.getIsFav().equals("false")){
                        boolean check = mydb.updateData("true",wordmeaning.getmWord(),wordmeaning.getDbTable());
                        if (check){
                            holder.mFavIv.setImageResource(R.drawable.ic_star_black_24dp);
                            wordmeaning.setIsFav("true");
                        }
                        Log.d("TAG",Boolean.toString(check));
                    }else {
                        boolean check = mydb.updateData("false",wordmeaning.getmWord(),wordmeaning.getDbTable());
                        if (check){
                            holder.mFavIv.setImageResource(R.drawable.ic_star_border_black_24dp);
                            wordmeaning.setIsFav("false");
                        }
                        Log.d("TAG",Boolean.toString(check));
                    }
                }catch (Exception e){
                    Log.d("TAG",e.toString());
                }

            }
        });

        if (wordmeaning.getIsFav().equals("false")){
            holder.mFavIv.setImageResource(R.drawable.ic_star_border_black_24dp);
        }else {
            holder.mFavIv.setImageResource(R.drawable.ic_star_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

}

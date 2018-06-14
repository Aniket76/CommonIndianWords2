package com.aniketvishal.commonindianwords;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aniketvishal.commonindianwords.Adapters.WordListAdapter;
import com.aniketvishal.commonindianwords.Database.DatabaseHelper;
import com.aniketvishal.commonindianwords.Models.WordListModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

public class WordListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<WordListModel> mDataList;
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout mOops;
    private static int result;

    private Toolbar mToolbar;

    private AdView mAdView;

    private static TextToSpeech ttsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        String s = getIntent().getStringExtra("Category");
        String f = getIntent().getStringExtra("Fragment");

        if (f.equals("AllWordsFragment")){
            SpeakTip.launch_speaktip(this);
        }

        mAdView = (AdView)findViewById(R.id.wordlist_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //ToolBar Settings
        mToolbar = (Toolbar) findViewById(R.id.status_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(s);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOops = (LinearLayout)findViewById(R.id.oops_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.all_word_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mDatabaseHelper = new DatabaseHelper(this);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {

            mDatabaseHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Copy Failed", Toast.LENGTH_LONG).show();
                return;
            }

        }

        switch (s) {
            case "Family Members": {
                mDataList = mDatabaseHelper.getWordList("family_members", f);
                break;
            }

            case "Cricket": {
                mDataList = mDatabaseHelper.getWordList("cricket", f);
                break;
            }

            case "The Environment": {
                mDataList = mDatabaseHelper.getWordList("the_environment", f);
                break;
            }

            case "Jobs": {
                mDataList = mDatabaseHelper.getWordList("jobs", f);
                break;
            }

            case "Words For Emotions": {
                mDataList = mDatabaseHelper.getWordList("words_for_emotions", f);
                break;
            }

            case "Parts Of Your Body": {
                mDataList = mDatabaseHelper.getWordList("parts_of_your_body", f);
                break;
            }

            case "Time Adverbs": {
                mDataList = mDatabaseHelper.getWordList("time_adverbs", f);
                break;
            }

            case "Health": {
                mDataList = mDatabaseHelper.getWordList("health", f);
                break;
            }

            case "Personality Types": {
                mDataList = mDatabaseHelper.getWordList("personality_types", f);
                break;
            }

            case "Periods Of Time": {
                mDataList = mDatabaseHelper.getWordList("periods_of_time", f);
                break;
            }

            case "Academic Writing": {
                mDataList = mDatabaseHelper.getWordList("academic_writing", f);
                break;
            }

            case "College And University Life": {
                mDataList = mDatabaseHelper.getWordList("college_and_university_life", f);
                break;
            }

            case "Weather": {
                mDataList = mDatabaseHelper.getWordList("weather", f);
                break;
            }

            case "Employment": {
                mDataList = mDatabaseHelper.getWordList("employment", f);
                break;
            }

            case "Economic": {
                mDataList = mDatabaseHelper.getWordList("economics", f);
                break;
            }

            case "Non-Count Nouns": {
                mDataList = mDatabaseHelper.getWordList("noncount", f);
                break;
            }

            case "Football": {
                mDataList = mDatabaseHelper.getWordList("football", f);
                break;
            }
        }

        if (mDataList.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
            mOops.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mOops.setVisibility(View.GONE);
        }
        ttsObject = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    result = ttsObject.setLanguage(Locale.UK);
                }else {
                    Toast.makeText(getApplicationContext(),"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });

        mAdapter = new WordListAdapter(mDataList, this,ttsObject,result);
        mRecyclerView.setAdapter(mAdapter);
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outputFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outputFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mute_sound, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.mute_sound:
                ttsObject.stop();
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onDestroy() {


        //Close the Text to Speech Library
        if(ttsObject != null) {

            ttsObject.stop();
            ttsObject.shutdown();
            //   Log.d(TAG, "TTS Destroyed");
        }
        super.onDestroy();
    }
}





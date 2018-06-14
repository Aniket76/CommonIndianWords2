package com.aniketvishal.commonindianwords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aniketvishal.commonindianwords.Database.DatabaseHelper;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private android.support.v7.widget.Toolbar mToolbar;

    TextToSpeech ttsObject;
    private DatabaseHelper mDatabaseHelper;

    private BottomNavigationView mBottomNavigationItemView;
//    Button btnviewAll;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RateUs.app_launched(this);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Common English Vocabulary");

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.main_side_nav);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        MobileAds.initialize(this,"ca-app-pub-9180372532679866~6661459582");

        mDatabaseHelper = new DatabaseHelper(this);

        mAuth = FirebaseAuth.getInstance();

        try {
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            SharedPreferences prefs = getSharedPreferences("lastUpdate", MODE_PRIVATE);;
            if (prefs.getInt("lastUpdate", 0) != versionCode) {
                try {

                    mDatabaseHelper.getReadableDatabase();
                    if (copyDatabase(this)){
                        Toast.makeText(this,"Words are Added",Toast.LENGTH_LONG).show();
                        prefs.edit().putBoolean("firstrun", false).commit();
                    }else {
                        Toast.makeText(this,"Copy Failed",Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Commiting in the preferences, that the update was successful.
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("lastUpdate", versionCode);
                    editor.commit();
                } catch(Throwable t) {
                    // update failed, or cancelled
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ttsObject = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    int result = ttsObject.setLanguage(Locale.getDefault());
                }else {
                    Toast.makeText(MainActivity.this,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });

        mBottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationItemView);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_cont, new AllWordsFragment()).commit();
        mBottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                FirebaseUser currentUser = mAuth.getCurrentUser();

                switch (item.getItemId()) {

                    case R.id.nav_allwords_btn:
                        transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                        transaction.replace(R.id.nav_cont, new AllWordsFragment());
                        transaction.commit();
                        return true;

                    case R.id.nav_fav_btn:
                        transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                        transaction.replace(R.id.nav_cont, new FavouritesFragment());
                        transaction.commit();
                        return true;

                    case R.id.nav_mywords_btn:

                        if (currentUser == null){

                            Intent intent = new Intent(MainActivity.this,StartActivity.class);
                            startActivity(intent);

                        }else {

                            transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                            transaction.replace(R.id.nav_cont, new MyWordsFragment());
                            transaction.commit();
                        }

                        return true;

                    case R.id.nav_pron_btn:
                        transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                        transaction.replace(R.id.nav_cont, new PrononciationFragment());
                        transaction.commit();
                        return true;
                }

                return false;
            }
        });
    }

    private boolean copyDatabase(Context context){
        try{

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outputFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outputFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff))>0){
                outputStream.write(buff,0 , length);
            }
            outputStream.flush();
            outputStream.close();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id){

            case R.id.main_menu_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Common English Vocabulary App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.side_nav_btn1:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Common English Vocabulary App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.side_nav_btn2:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords")));
                break;

            case R.id.side_nav_btn3:
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "contact@aniketvishal.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Common English Vocabulary App - Feedback");
                startActivity(intent);
                break;

            case R.id.otherapp1:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords")));
                break;
        }

        return false;
    }
}

package com.aniketvishal.commonindianwords;

import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aniketvishal.commonindianwords.Models.MyWordsListModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class MyWordsListActivity extends AppCompatActivity {

    private FloatingActionButton mWordAdd;

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;

    private RecyclerView mMyWordsRecyclerView;

    TextToSpeech ttsObject;

    private Toolbar mToolbar;

    private AdView mAdView;

    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_words_list);

        final String dbCategory = getIntent().getStringExtra("DbCategoryId");

        mAdView = (AdView)findViewById(R.id.mywordlist_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //ToolBar Settings
        mToolbar = (Toolbar) findViewById(R.id.allwordList_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(dbCategory);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentuser.getUid();

        mFirebase = FirebaseDatabase.getInstance().getReference().child("MyWords").child(uid).child(dbCategory);

        mWordAdd = (FloatingActionButton)findViewById(R.id.mywordlist_fab_add);

        mWordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(MyWordsListActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_category_dialog,null);

                final TextInputLayout mCategory = (TextInputLayout)mView.findViewById(R.id.add_categoryName);
                final TextInputLayout mWord = (TextInputLayout)mView.findViewById(R.id.add_firstword);
                final TextInputLayout mMeaning = (TextInputLayout)mView.findViewById(R.id.add_firstmeaning);
                final TextView mTitleTxt = (TextView)mView.findViewById(R.id.add_title_txt);
                final TextView mCatMsg = (TextView)mView.findViewById(R.id.cat_msg);
                mCatMsg.setVisibility(View.GONE);
                mCategory.setVisibility(View.GONE);
                mTitleTxt.setText("Add Word");
                mWord.setHint("Word");
                mMeaning.setHint("Meaning");

                mBulider.setView(view)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String category = mCategory.getEditText().getText().toString();
                                String word = mWord.getEditText().getText().toString();
                                String meaning = mMeaning.getEditText().getText().toString();

                                if(!TextUtils.isEmpty(word) && !TextUtils.isEmpty(meaning)){

                                    HashMap<String, String> addcat = new HashMap<>();
                                    addcat.put("dbWord",word);
                                    addcat.put("dbMeaning",meaning);

                                    mFirebase.push().setValue(addcat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MyWordsListActivity.this,"Done",Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(MyWordsListActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    dialogInterface.dismiss();
                                }else {
                                    Toast.makeText(MyWordsListActivity.this,"Fill all the fields and try again",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();
            }
        });

        mMyWordsRecyclerView = (RecyclerView)findViewById(R.id.mywordlist_rv);
        mMyWordsRecyclerView.setHasFixedSize(true);
        mMyWordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ttsObject = new TextToSpeech(MyWordsListActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    result = ttsObject.setLanguage(Locale.UK);
                }else {
                    Toast.makeText(MyWordsListActivity.this,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<MyWordsListModel, MyWordsListCategoryViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyWordsListModel, MyWordsListCategoryViewholder>(

                MyWordsListModel.class,
                R.layout.myword_list_layout,
                MyWordsListCategoryViewholder.class,
                mFirebase

        ) {
            @Override
            protected void populateViewHolder(MyWordsListCategoryViewholder viewHolder, final MyWordsListModel model, int position) {

                viewHolder.setDbWord(model.getDbWord());
                viewHolder.setDbMeaning(model.getDbMeaning());

                final String dbwordId = getRef(position).getKey();

                viewHolder.mDbWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                            Toast.makeText(MyWordsListActivity.this,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                        }else {
                            ttsObject.speak(model.getDbWord(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });

                viewHolder.mDbMeaning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                            Toast.makeText(MyWordsListActivity.this,"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                        }else {
                            ttsObject.speak(model.getDbMeaning(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });

                viewHolder.mMyWordsListDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 1. Instantiate an AlertDialog.Builder with its constructor
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MyWordsListActivity.this);

                        // 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("Are you sure you want to delete this word?")
                                .setTitle("Delete Word");

                        // 3. Add the buttons
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked YES button


                                        mFirebase.child(dbwordId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MyWordsListActivity.this,"Deleted",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();

                            }
                        });

                        // 4. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });


            }
        };

        mMyWordsRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MyWordsListCategoryViewholder extends RecyclerView.ViewHolder{

        View mView;
        public ImageView mMyWordsListDelete,mMyWordsListEdit;
        public TextView mDbWord,mDbMeaning;

        public MyWordsListCategoryViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            mMyWordsListDelete = (ImageView)itemView.findViewById(R.id.myword_list_delete);
            mMyWordsListEdit = (ImageView)itemView.findViewById(R.id.myword_list_edit);

        }

        public void setDbWord(String word){
            mDbWord = (TextView)mView.findViewById(R.id.myword_list_word);
            mDbWord.setText(word);
        }

        public void setDbMeaning(String meaning){
            mDbMeaning = (TextView)mView.findViewById(R.id.myword_list_meaning);
            mDbMeaning.setText(meaning);
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
    public void onDestroy() {


        //Close the Text to Speech Library
        if(ttsObject != null) {

            ttsObject.stop();
            ttsObject.shutdown();
            //   Log.d(TAG, "TTS Destroyed");
        }
        super.onDestroy();
    }

}

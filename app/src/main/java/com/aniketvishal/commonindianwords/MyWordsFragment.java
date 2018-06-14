package com.aniketvishal.commonindianwords;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aniketvishal.commonindianwords.Models.MyWordsCategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWordsFragment extends Fragment {

    private FloatingActionButton mUp,mLogout,mAdd;
    private Animation FabOpen, FabClose, FabClockwise, FabAntiClockwise;
    boolean isOpen = false;

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;

    private RecyclerView mMyWordsRecyclerView;

    public MyWordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_my_words, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mUp = (FloatingActionButton) getActivity().findViewById(R.id.myword_fab_up);
        mLogout = (FloatingActionButton) getActivity().findViewById(R.id.myword_fab_logout);
        mLogout.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#212121")));
        mAdd = (FloatingActionButton) getActivity().findViewById(R.id.myword_fab_add);
        mAdd.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#212121")));

        FabOpen = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_clockwise);
        FabAntiClockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anticlockwise);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentuser.getUid();

        mFirebase = FirebaseDatabase.getInstance().getReference().child("MyWords").child(uid);
        mFirebase.keepSynced(true);

        mMyWordsRecyclerView = (RecyclerView)getActivity().findViewById(R.id.myword_category_rv);
        mMyWordsRecyclerView.setHasFixedSize(true);
        mMyWordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.add_category_dialog,null);

                final TextInputLayout mCategory = (TextInputLayout)mView.findViewById(R.id.add_categoryName);
                final TextInputLayout mWord = (TextInputLayout)mView.findViewById(R.id.add_firstword);
                final TextInputLayout mMeaning = (TextInputLayout)mView.findViewById(R.id.add_firstmeaning);

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

                                if(!TextUtils.isEmpty(category) && !TextUtils.isEmpty(word) && !TextUtils.isEmpty(meaning)){

                                    HashMap<String, String> addcat = new HashMap<>();
                                    addcat.put("dbWord",word);
                                    addcat.put("dbMeaning",meaning);

                                    mFirebase.child(category).push().setValue(addcat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(),"Category is Added",Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    mFirebase.keepSynced(true);

                                    dialogInterface.dismiss();
                                }else {
                                    Toast.makeText(getActivity(),"Fill all the fields and try again",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();
            }
        });



        mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen){

                    mLogout.startAnimation(FabClose);
                    mAdd.startAnimation(FabClose);
                    mUp.startAnimation(FabAntiClockwise);
                    mLogout.setClickable(false);
                    mAdd.setClickable(false);
                    isOpen = false;

                }else {

                    mLogout.startAnimation(FabOpen);
                    mAdd.startAnimation(FabOpen);
                    mUp.startAnimation(FabClockwise);
                    mLogout.setClickable(true);
                    mAdd.setClickable(true);
                    isOpen = true;
                }

            }
        });




        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. Instantiate an AlertDialog.Builder with its constructor
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Are you sure you want to logout?")
                        .setTitle("Logout");

                // 3. Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked YES button

                        mAuth.signOut();
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
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






    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<MyWordsCategoryModel, MyWordsCategoryViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyWordsCategoryModel, MyWordsCategoryViewholder>(

                MyWordsCategoryModel.class,
                R.layout.mywords_rv_layout,
                MyWordsCategoryViewholder.class,
                mFirebase

        ) {
            @Override
            protected void populateViewHolder(MyWordsCategoryViewholder viewHolder, MyWordsCategoryModel model, int position) {

                final String dbcategoryId = getRef(position).getKey();
                viewHolder.setCategory(dbcategoryId);

                viewHolder.mCategoryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 1. Instantiate an AlertDialog.Builder with its constructor
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        // 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("Are you sure you want to delete this category?")
                                .setTitle("Delete Category");

                        // 3. Add the buttons
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked YES button

                                // 1. Instantiate an AlertDialog.Builder with its constructor
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                // 2. Chain together various setter methods to set the dialog characteristics
                                builder.setMessage("All the words inside this category will be deleted")
                                        .setTitle("Delete All Words");

                                // 3. Add the buttons
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked YES button

                                        mFirebase.child(dbcategoryId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.cancel();

                                    }
                                });

                                // 4. Get the AlertDialog from create()
                                AlertDialog dialogfinal = builder.create();
                                dialogfinal.show();

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


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        if (mInterstitialAd.isLoaded()){
//                            mInterstitialAd.show();
//
//
//                            mInterstitialAd.setAdListener(new AdListener()
//
//                                                          {
//                                                              @Override
//                                                              public void onAdClosed() {
//                                                                  Intent profileIntant = new Intent(getActivity(), MyWordsListActivity.class);
//                                                                  profileIntant.putExtra("DbCategoryId", dbcategoryId);
//                                                                  startActivity(profileIntant);
//
//                                                                  mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                                                              }
//                                                          }
//
//                            );
//
//
//                        }else {

                        Intent profileIntant = new Intent(getActivity(), MyWordsListActivity.class);
                        profileIntant.putExtra("DbCategoryId", dbcategoryId);
                        startActivity(profileIntant);

//                        }
                    }
                });

            }
        };

        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(firebaseRecyclerAdapter);
        alphaAdapter.setDuration(800);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setFirstOnly(false);

        mMyWordsRecyclerView.setAdapter(alphaAdapter);

    }

    public static class MyWordsCategoryViewholder extends RecyclerView.ViewHolder{

        View mView;
        public ImageView mCategoryDelete;

        public MyWordsCategoryViewholder(View itemView) {
            super(itemView);

            mView = itemView;

            mCategoryDelete = (ImageView)itemView.findViewById(R.id.mywords_rv_cross);

        }

        public void setCategory(String category){
            TextView cat = (TextView)mView.findViewById(R.id.mywords_rv_Category);
            cat.setText(category);
        }

    }





}

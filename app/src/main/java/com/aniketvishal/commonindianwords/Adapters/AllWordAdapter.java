package com.aniketvishal.commonindianwords.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aniketvishal.commonindianwords.Models.AllWordsModel;
import com.aniketvishal.commonindianwords.R;
import com.aniketvishal.commonindianwords.ViewHolders.AllWordViewHolder;
import com.aniketvishal.commonindianwords.WordListActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

/**
 * Created by aniketvishal on 26/11/17.
 */

public class AllWordAdapter extends RecyclerView.Adapter<AllWordViewHolder> {

    private List<AllWordsModel> mCategoryList;
    private Context context;
    private String fragmentName;

//    private InterstitialAd mInterstitialAd;

    public AllWordAdapter(List<AllWordsModel> mCategoryList, Context context,String fragmentName) {
        this.mCategoryList = mCategoryList;
        this.context = context;
        this.fragmentName = fragmentName;
    }

    @Override
    public AllWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_words_layout,parent,false);
        return new AllWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllWordViewHolder holder, int position) {
        final AllWordsModel word = mCategoryList.get(position);
        holder.mCategory.setText(word.getmWordCategory());
        holder.mBackgroung.setImageResource(word.getmImageRes());

        if (fragmentName.equals("AllWordsFragment")){
            holder.mStar.setVisibility(View.GONE);
        }else {
            holder.mStar.setVisibility(View.VISIBLE);
        }

        final Activity activity = (Activity)context;

//        mInterstitialAd = new InterstitialAd(activity);
//        mInterstitialAd.setAdUnitId("ca-app-pub-9180372532679866/1983137539");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        holder.mAllWordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (mInterstitialAd.isLoaded()){
//                    mInterstitialAd.show();
//
//
//                    mInterstitialAd.setAdListener(new AdListener()
//
//                                                  {
//                                                      @Override
//                                                      public void onAdClosed() {
//                                                          Intent myintent = new Intent(activity, WordListActivity.class);
//                                                          myintent.putExtra("Category", word.getmWordCategory());
//                                                          myintent.putExtra("Fragment",fragmentName);
//                                                          context.startActivity(myintent);
//
//                                                          mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                                                      }
//                                                  }
//
//                    );
//
//
//                }else {

                    Intent myintent = new Intent(activity, WordListActivity.class);
                    myintent.putExtra("Category", word.getmWordCategory());
                    myintent.putExtra("Fragment",fragmentName);
                    context.startActivity(myintent);

//                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}

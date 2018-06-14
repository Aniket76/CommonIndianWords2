package com.aniketvishal.commonindianwords.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aniketvishal.commonindianwords.R;

/**
 * Created by aniketvishal on 26/11/17.
 */


public class AllWordViewHolder extends RecyclerView.ViewHolder{

    public TextView mCategory;
    public RelativeLayout mAllWordLayout;
    public ImageView mBackgroung;
    public ImageView mStar;

    public AllWordViewHolder(View itemView) {
        super(itemView);

        mCategory = (TextView)itemView.findViewById(R.id.word_Category);
        mAllWordLayout = (RelativeLayout) itemView.findViewById(R.id.all_words_layout);
        mBackgroung = (ImageView)itemView.findViewById(R.id.all_words_background);
        mStar = (ImageView)itemView.findViewById(R.id.all_words_star);
    }

}

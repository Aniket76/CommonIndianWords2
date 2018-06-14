package com.aniketvishal.commonindianwords.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aniketvishal.commonindianwords.R;

/**
 * Created by aniketvishal on 26/11/17.
 */

public class WordListViewHolder extends RecyclerView.ViewHolder{

    public TextView mNameTv,mMeaningTv;
    public ImageView mFavIv;

    public WordListViewHolder(View itemView) {
        super(itemView);

        mNameTv = (TextView)itemView.findViewById(R.id.word_word);
        mMeaningTv = (TextView)itemView.findViewById(R.id.word_meaning);
        mFavIv = (ImageView) itemView.findViewById(R.id.word_fav);
    }
}

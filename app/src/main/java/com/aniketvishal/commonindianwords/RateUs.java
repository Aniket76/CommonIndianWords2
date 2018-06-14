package com.aniketvishal.commonindianwords;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

/**
 * Created by aniketvishal on 17/02/18.
 */

public class RateUs {

    // Day until the Rate Us Dialog Prompt(Default 2 Days)
    private final static int DAYS_UNTIL_PROMPT = 0;

    // App launches until Rate Us Dialog Prompt(Default 5 Launches)
    private final static int LAUNCHES_UNTIL_PROMPT = 2;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext,
                                      final SharedPreferences.Editor editor) {

        Activity actvity = (Activity)mContext;

        final Dialog dialog = new Dialog(mContext);
        // Set Dialog Title

        LayoutInflater inflater = actvity.getLayoutInflater();
        View v = inflater.inflate(R.layout.rateus_dialog,null);

        Button mRateUs = (Button)v.findViewById(R.id.rateus_rateus_btn);
        mRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords")));
                dialog.dismiss();

            }
        });


        Button mLater = (Button)v.findViewById(R.id.rateus_later_btn);
        mLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });


        Button mNaver = (Button)v.findViewById(R.id.rateus_never_btn);
        mNaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();

            }
        });

        dialog.setContentView(v);

        // Show Dialog
        dialog.show();

    }
}

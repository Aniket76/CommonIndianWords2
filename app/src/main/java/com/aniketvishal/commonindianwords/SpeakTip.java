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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

/**
 * Created by aniketvishal on 18/02/18.
 */

public class SpeakTip {

    public static void launch_speaktip(final Context mContext) {

        Activity actvity = (Activity)mContext;

        SharedPreferences prefs = mContext.getSharedPreferences("speaktip", 0);
        final SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getBoolean("showtip", true)) {

            final AlertDialog.Builder mBulider = new AlertDialog.Builder(actvity);
            LayoutInflater inflater = actvity.getLayoutInflater();
            View mView = inflater.inflate(R.layout.speaktip_dialog,null);

            mBulider.setView(mView)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            editor.putBoolean("showtip",false);
                            editor.commit();
                            dialogInterface.dismiss();

                        }
                    });

            mBulider.setView(mView);
            AlertDialog dialog = mBulider.create();
            dialog.show();
        }else {
            return;
        }
    }
}

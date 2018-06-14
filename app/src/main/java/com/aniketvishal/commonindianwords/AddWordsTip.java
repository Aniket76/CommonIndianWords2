package com.aniketvishal.commonindianwords;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aniketvishal on 18/02/18.
 */

public class AddWordsTip {

    private static TextView mTitle,mMsg;
    private static ImageView mImage;

    public static void launch_addwordtip(final Context mContext) {

        Activity actvity = (Activity)mContext;

        SharedPreferences prefs = mContext.getSharedPreferences("addwordtip", 0);
        final SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getBoolean("showaddtip", true)) {

            final AlertDialog.Builder mBulider = new AlertDialog.Builder(actvity);
            LayoutInflater inflater = actvity.getLayoutInflater();
            View mView = inflater.inflate(R.layout.speaktip_dialog,null);

            mTitle  = (TextView)mView.findViewById(R.id.tip_title);
            mTitle.setText("Create Vocabulary");

            mMsg  = (TextView)mView.findViewById(R.id.tip_msg);
            mMsg.setText("You can create your own vocabulary. Just tap on tha add(+) button");

            mImage  = (ImageView) mView.findViewById(R.id.tip_img);
            mImage.setImageResource(R.drawable.login_wt5);

            mBulider.setView(mView)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            editor.putBoolean("showaddtip",false);
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

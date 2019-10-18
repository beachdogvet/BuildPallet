package com.lockheedmartin.buildpallet;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;


public class Common {

    final public static int EXIT_APPLICATION = 0;
    final public static int OPTIONS_MENU_ITEM_ID_1 = 1;
    final public static int OPTIONS_MENU_ITEM_ID_2 = 2;
    public final static int MESSAGE_TYPE_ERROR = 0;
    public final static int MESSAGE_TYPE_INFORMATION = 1;
    public final static int MESSAGE_TYPE_SUCCESS = 2;

    public final static int SCAN_BEEP = 1;



    public static void showMessage(String title, String message, Context context, int messageType) {

        AlertDialog.Builder alertDialog = new Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        switch(messageType) {

            case MESSAGE_TYPE_ERROR:
                alertDialog.setIcon(R.drawable.error_icon);
                break;
            case MESSAGE_TYPE_INFORMATION:
                alertDialog.setIcon(R.drawable.info_icon);
                break;
            case MESSAGE_TYPE_SUCCESS:
                alertDialog.setIcon(R.drawable.check);
                break;


            default:
                break;
        }

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public static void PlaySounnd(Context context, int soundType)
    {
        int uri = 0;

        switch (soundType) {
            case SCAN_BEEP:
                uri = R.raw.beep;
                break;
        }

        try {
            MediaPlayer mp = MediaPlayer.create(context, uri);
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }



}

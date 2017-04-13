package com.gyansagarji.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyansagarji.android.R;

/**
 * Created by New android on 28-03-2017.
 */

public class NetworkError {


    public static void networkConnectionError(final Activity activity){

        final Dialog dialog      =       new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.networkalertdialog);
        dialog.setCancelable(false);
        dialog.show();

        LinearLayout rightLL        =       (LinearLayout)dialog.findViewById(R.id.rightLL);
        LinearLayout leftLL         =       (LinearLayout)dialog.findViewById(R.id.leftLL);
        TextView errortitleTV       =       (TextView)dialog.findViewById(R.id.errortitleTV);
        TextView errorTV            =       (TextView)dialog.findViewById(R.id.errorTV);
        final TextView leftTV       =       (TextView)dialog.findViewById(R.id.leftTV);
        final TextView rightTV      =       (TextView)dialog.findViewById(R.id.rightTV);


        leftLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){

                    dialog.dismiss();
                    }


            }
        });

        rightLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()){
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    activity.startActivityForResult(intent,100);
                    dialog.dismiss();
                }
            }
        });

    }
}

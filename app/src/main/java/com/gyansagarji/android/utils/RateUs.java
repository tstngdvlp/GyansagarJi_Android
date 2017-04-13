package com.gyansagarji.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.gyansagarji.android.R;

/**
 * Created by New android on 18-03-2017.
 */

public class RateUs {

    public void alertDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        dialog.show();

        LinearLayout leftLL     =       (LinearLayout)dialog.findViewById(R.id.leftLL);
        LinearLayout rightLL    =       (LinearLayout)dialog.findViewById(R.id.rightLL);
        leftLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

        rightLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.gyansaar");
                    Intent linktomarket = new Intent(Intent.ACTION_VIEW,uri);
                    activity.startActivity(linktomarket);
                    dialog.dismiss();

                }
            }
        });


    }
}

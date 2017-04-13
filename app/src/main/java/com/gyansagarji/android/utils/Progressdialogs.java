package com.gyansagarji.android.utils;

import android.app.Activity;
import android.graphics.Color;

import com.kaopiz.kprogresshud.KProgressHUD;


/**
 * Created by New android on 24-03-2017.
 */

public class Progressdialogs {
    private static final Progressdialogs progressdialogs=new Progressdialogs();
    public static Progressdialogs getInstance(){
        return progressdialogs;
    }


    static KProgressHUD hud;

    public static void getDialog(Activity activity1){

        hud             =       KProgressHUD.create(activity1)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .setWindowColor(Color.parseColor("#fe6700"))
                                .setMaxProgress(100);


    }


    public static void showDialog()
    {
            hud.show();
    }

    public static void closeDialog(){
        if(hud!=null) {
            if (hud.isShowing()) {
                hud.dismiss();
            }
        }
    }

}

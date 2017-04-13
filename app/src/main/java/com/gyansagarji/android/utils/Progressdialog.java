package com.gyansagarji.android.utils;

import android.app.Activity;
import android.graphics.Color;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by New android on 24-03-2017.
 */

public class Progressdialog {
    KProgressHUD hud;
    public void dialog(Activity activity){

        hud             =       KProgressHUD.create(activity)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .setWindowColor(Color.parseColor("#fe6700"))
                                .setMaxProgress(100);



    }

    public void showDialog()
    {
        hud.show();
    }

    public void closeDialog(){

        if(hud.isShowing()){
            hud.dismiss();
        }
    }

}

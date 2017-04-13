package com.gyansagarji.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by New android on 23-03-2017.
 */

public class PrefUtils {

    public static String NewsList           =       "newsList";
    public static String EventsList         =       "eventslist";
    public static String ParichayList       =       "prichaylist";
    public static String MediaList          =       "medialist";
    public static String AboutList          =       "aboutlist";
    public static String DEVICETOKEN        =       "devicetoken";
    public static String EbooksList         =       "ebookslist";
    public static String OrganizationList   =       "organizationlist";
    public static String GalleryList        =       "gallerylist";
    public static String SubGalleryList     =       "SubGalleryList";
    public static String GalleryImagesList  =       "galleryimages";
    public static String LocationList       =       "locationlist";

    public static void saveToPrefs(Context cotext, String key, String value){
        SharedPreferences prefs     = PreferenceManager.getDefaultSharedPreferences(cotext);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getFromPrefs(Context context,String key,String defaultvalue){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
           return sharedPreferences.getString(key, defaultvalue);
        }catch (Exception e){
            return defaultvalue;
        }
    }

    public static void removePrefs(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.commit();
    }
}

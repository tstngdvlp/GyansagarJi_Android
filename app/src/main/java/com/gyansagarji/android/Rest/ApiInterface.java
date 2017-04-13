package com.gyansagarji.android.Rest;

import com.gyansagarji.android.Response.AboutUs;
import com.gyansagarji.android.Response.Ebooks;
import com.gyansagarji.android.Response.Event;
import com.gyansagarji.android.Response.Gallery;
import com.gyansagarji.android.Response.Maps;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.Response.News;
import com.gyansagarji.android.Response.Organization;
import com.gyansagarji.android.Response.Parichay;
import com.gyansagarji.android.Response.Tokenresponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by New android on 22-03-2017.
 */

public interface ApiInterface {
    @GET("news.php")
    Call<News> NewsList();

    @GET("event.php")
    Call<Event> EventsList();

    @GET("jeevan.php")
    Call<Parichay> ParichayList();

    @GET("media.php")
    Call<MediaAV> MeadiaList();

    @GET("about_us.php")
    Call<AboutUs> AboutUsList();

    @GET("ebooks.php")
    Call<Ebooks> EbooksList();

    @GET("organization.php")
    Call<Organization> OrganizationList();

    @FormUrlEncoded
    @POST("token.php")
    Call<Tokenresponse> deviceToken(@Field("token") String token);

    @GET("gallery.php")
    Call<Gallery>   getGalleryImages();
    @GET("location.php")
    Call<Maps> mapData();
}

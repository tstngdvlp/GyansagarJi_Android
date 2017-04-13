package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.EbooksAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Ebooks;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by New android on 20-02-2017.
 */

public class EbooksActivity extends Activity {
    ArrayList<Ebooks.EbooksList> list;
    ArrayList<Ebooks.EbooksList> listClone=new ArrayList<>();
    EbooksAdapter ebooksAdapter;
    TextView titleTV;
    ImageView backIMGV;
    ListView ebooksLV;
    Progressdialog progressdialog;
    ApiInterface apiServices;
    EditText searchET;
    LinearLayout backLL;
    public static LruCache<String,Bitmap> mcache = new LruCache<String, Bitmap>(1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ebooks);

        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        ebooksLV            =       (ListView)findViewById(R.id.ebooksLV);
        searchET            =       (EditText)findViewById(R.id.searchET);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);

        titleTV.setText(getResources().getString(R.string.ebooks));

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EbooksActivity.this,MainActivity.class));
                finish();
            }
        });


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                list.clear();
                if (editable.toString().trim().length() == 0) {
                    list.addAll(listClone);
                }
                else
                {

                    String st=editable.toString().replace(" ","");

                    for (Ebooks.EbooksList wp : listClone)
                    {
                        Log.d("title123",wp.getTitle()+",,,");
                        if (wp.getTitle().toLowerCase().contains(st.toLowerCase()))
                        {
                            list.add(wp);
                        }
                    }
                }
                ebooksAdapter.notifyDataSetChanged();

            }
        });

        checkNetworkConnection();

    }

    private void checkNetworkConnection() {
        if(NetworkConnectivity.isNetworkAvailable(this)){

            loadInitials();

        }else{
            if( PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.EbooksList,"").toString()!=""){
                loadEbooks();
            }

        }
    }

    private void loadInitials() {
        progressdialog      =       new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();
        Log.d("ebooks","success");
        apiServices         = ApiClient.getClient().create(ApiInterface.class);
        Call<Ebooks> call =  apiServices.EbooksList();
        call.enqueue(new Callback<Ebooks>() {
            @Override
            public void onResponse(Call<Ebooks> call, Response<Ebooks> response) {
                progressdialog.closeDialog();
                if(response.code() == 200){
                    if(response.body().getStatus().equals("1")){
                        Log.d("ebooks1","success");
                        Gson gson = new Gson();

                        String ebookslist = gson.toJson(response.body());
                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.EbooksList,ebookslist);
                        loadEbooks();
                    }
                }

            }

            @Override
            public void onFailure(Call<Ebooks> call, Throwable t) {
                progressdialog.closeDialog();
            }
        });



    }

    private void loadEbooks() {
        String ebookslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.EbooksList,"");
        Gson gson  = new Gson();
        Ebooks ebooks = gson.fromJson(ebookslist,Ebooks.class);
        list        =   ebooks.getEbookslist();
        listClone.addAll(list);
        if(list.size()>0){

            ebooksAdapter       =       new EbooksAdapter(this,list);
            ebooksLV.setAdapter(ebooksAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EbooksActivity.this,MainActivity.class));
        finish();
    }
}

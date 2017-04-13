package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.OrganizationAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Organization;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by New android on 17-03-2017.
 */

public class OrganizationActivity extends Activity {
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    ListView orgnizationLV;
    LinearLayout organizationLL;
    Progressdialog progressDialog;
    ApiInterface apiServices;
    ArrayList<Organization.OrganizationList> list;
    OrganizationAdapter organizationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);
        orgnizationLV       =       (ListView)findViewById(R.id.orgnizationLV);
        organizationLL      =       (LinearLayout)findViewById(R.id.organizationLL);

        titleTV.setText(getResources().getString(R.string.organization));
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganizationActivity.this,MainActivity.class));
                finish();
            }
        });

        orgnizationLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(OrganizationActivity.this,OrganizationDetailview.class));
                Constants.OrganizationId = i;
            }
        });



        loadInitials();

    }

    private void loadInitials() {
        progressDialog          =   new Progressdialog();
        progressDialog.dialog(this);
        progressDialog.showDialog();
        apiServices         = ApiClient.getClient().create(ApiInterface.class);
       Call<Organization> call = apiServices.OrganizationList();

        call.enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                progressDialog.closeDialog();
                if(response.code() == 200){
                    if(response.body().getStatus().equals("1")){
                        Gson gson   =   new Gson();
                        String orglist = gson.toJson(response.body());

                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.OrganizationList,orglist);

                        organizationData();
                    }
                }
            }



            @Override
            public void onFailure(Call<Organization> call, Throwable t) {
                progressDialog.closeDialog();
            }
        });



    }

    private void organizationData() {

        String orglist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.OrganizationList,"");
        Gson gson   =   new Gson();

        Organization organization        =   gson.fromJson(orglist,Organization.class);
        list             =  organization.getOraganizationlist();
        if(list.size()>0) {
            organizationAdapter = new OrganizationAdapter(this, list);
            orgnizationLV.setAdapter(organizationAdapter);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrganizationActivity.this,MainActivity.class));
        finish();
    }
}

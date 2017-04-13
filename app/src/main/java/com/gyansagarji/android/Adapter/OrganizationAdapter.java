package com.gyansagarji.android.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.gyansagarji.android.Activity.OrganizationActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Organization;
import com.gyansagarji.android.utils.CircularNetworkImageView2;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by New android on 25-03-2017.
 */

public class OrganizationAdapter extends BaseAdapter {
    OrganizationActivity activity;
ArrayList<Organization.OrganizationList> data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;

    public OrganizationAdapter(OrganizationActivity activity, ArrayList<Organization.OrganizationList> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        requestQueue            =       VolleySingleton.getInstance().getRequestQueue();
        imageLoader             =       VolleySingleton.getInstance().getImageLoader();
        if(view == null){
            view        =       activity.getLayoutInflater().inflate(R.layout.organization_row,null);
            holder.descTV   =   (TextView) view.findViewById(R.id.descTV);
            holder.titleTV  =   (TextView)view.findViewById(R.id.titleTV);
            holder.placeTV  =   (TextView)view.findViewById(R.id.placeTV);
            holder.listIMGV =   (CircularNetworkImageView2)view.findViewById(R.id.listIMGV);

            holder.titleTV.setText(data.get(position).getTitle());
            holder.descTV.setText(data.get(position).getShort_desc());

            holder.placeTV.setText(data.get(position).getPlace());
            holder.listIMGV.setDefaultImageResId(R.drawable.loadingsmall);
            holder.listIMGV.setErrorImageResId(R.drawable.ic_error);
            holder.listIMGV.setImageUrl(Constants.ImageBaseUrl+data.get(position).getTiny_image().toString().replace(" ","%20"),imageLoader);
        }


        return view;
    }
    public class ViewHolder{
        TextView titleTV;
        TextView descTV;
        TextView placeTV;
        LinearLayout clickLL;
        CircularNetworkImageView2 listIMGV;
    }
}

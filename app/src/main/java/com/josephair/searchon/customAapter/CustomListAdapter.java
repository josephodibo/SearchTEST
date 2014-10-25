package com.josephair.searchon.customAapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.josephair.searchon.R;
import com.josephair.searchon.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph_Air on 9/30/14.
 */
public class CustomListAdapter extends BaseAdapter {
    Activity mActivity;
    LayoutInflater inflater;
    private List<Result> mResults;

    public CustomListAdapter(Activity activity, ArrayList<Result> results) {
        this.mActivity=activity;
        this.mResults = results;


    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int i) {
        return mResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i ;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)

        convertView = inflater.inflate(R.layout.single_row, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView address =(TextView)convertView.findViewById(R.id.address);
        TextView city=(TextView) convertView.findViewById(R.id.city);
        TextView phone=(TextView) convertView.findViewById(R.id.phone);
        TextView distance=(TextView)convertView.findViewById(R.id.distance);
        TextView state=(TextView)convertView.findViewById(R.id.state);

        // getting Result data for the row
         Result r = mResults.get(i);
        // title
        title.setText(r.getTitle());
        //Address
        address.setText(r.getAddress());
        //city
        city.setText("City:"+r.getCity());
        //state
        state.setText("State:"+r.getState());
        //phone
        phone.setText(r.getPhone());
        //distance
        distance.setText("Distance is:"+r.getDistance()+" miles from your ZipCode.");
        return convertView;
    }

}

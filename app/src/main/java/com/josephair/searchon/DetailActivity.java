package com.josephair.searchon;


//Android System Import

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josephair.searchon.result.Result;

//Google class Import

/**
 * Created by Joseph_Air on 10/2/14.
 * This is called the Detail Activity
 * The main objective of this activity is to display the list item in a detailed form.
 * This has a Map Fragment to display a static Map on the upper part.
 */
public class DetailActivity extends Activity  implements GooglePlayServicesClient.ConnectionCallbacks{

    GoogleMap mGoogleMap;
    protected Double mLat;
    protected Double mLng;
    //Global
    ImageView mPhone;
    ImageButton mWeb;
    //Result Class Object
    Result mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        //Starting Up MapFragment;
        MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = mapFrag.getMap();


        //retrieve object passed from first Activity
         mModel = (Result) getIntent().getSerializableExtra("theResultsValue");

        //function to set all Values
             setValues();

        //Assigning the value to mLat  mLng which will be pass to the GoTo Location;
        mLat=Double.valueOf(mModel.getLatitude());
        mLng=Double.valueOf(mModel.getLongitude());
        //Logging the tha value

        /*Locating the address with the given Latitude and longitude value
        * By call the GoTo location function*/

         goToLocation(mLat,mLng);

        //option to lunch the maps on your devices
        ImageButton lunchMap = (ImageButton)findViewById(R.id.lunchMaps);
        lunchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+mModel.getAddress()));
                startActivity(intent);
            }
        });
        //function to share with friends
        ImageButton socialShare =(ImageButton)findViewById(R.id.socialShare);
          socialShare.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent socialShare = new Intent(Intent.ACTION_SEND);
                  socialShare.setType("text/plain");
                  socialShare.putExtra(Intent.EXTRA_TEXT,mModel.getTitle()+"\n"+mModel.getAddress()+"\n"+mModel.getPhone());
                  Intent chooser = Intent.createChooser(socialShare,"Select your Text sender");
                  startActivity(chooser);

              }
          });

       //Lunching the Phone caller with an Intent
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mModel.getPhone()));
                Intent chooser =  Intent.createChooser(intent,"");
                startActivity(chooser);
            }
        });
     //Lunching the WebSite WebView with and Intent
        mWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url =mModel.getUrl();
                //Starting up the Dialog of
                if(url.equals("null")) {
                    //dialog Builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setTitle("Oops Sorry!")
                            .setMessage(getString(R.string.website_error_message))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Action to perform
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    * This  Function helps to set the Values which is pass from the list and placed on the correct TextView :)*/

     public void setValues(){
        //title
         TextView mDetailsTitle =(TextView)findViewById(R.id.detailTitle);
         mDetailsTitle.setText(mModel.getTitle());
        //address
        TextView address =(TextView)findViewById(R.id.detailAddress);
        address.setText(mModel.getAddress());
        //city
           TextView city = (TextView)findViewById(R.id.detailCity);
           city.setText("City:"+mModel.getCity());
        //state
            TextView state =(TextView)findViewById(R.id.detailState);
            state.setText("State:"+mModel.getState());
        //distance
            TextView miles =(TextView)findViewById(R.id.detailMile);
            miles.setText("Distance:"+mModel.getDistance()+"mi");
         //Ratings
           TextView ratings =(TextView)findViewById(R.id.detailRating);

         //Checking if Rating is Available
            String ratingCheck = mModel.getRating();
          if(ratingCheck.equals("NaN")){
              ratings.setText("No Rating");
          }else {
              ratings.setText("Ratings:" + mModel.getRating());
          }
         //phone
         mPhone=(ImageButton)findViewById(R.id.detailPhone);
         mWeb =(ImageButton)findViewById(R.id.detailWeb);

         //Reviews
         TextView reviews =(TextView)findViewById(R.id.detailReview);
         String reviewsCheck =mModel.getReview();
         if(reviewsCheck.equals("null")){
             reviews.setText("No Reviews");
         }else {
             reviews.setText("Reviews:" + "\n" + mModel.getReview());
         }

    }

    public void goToLocation(double lat, double lng) {
        this.mLat=lat;
        this.mLng=lng;
        LatLng ll = new LatLng(lat, lng);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mGoogleMap.setMapType(3);
        mGoogleMap.moveCamera(update);
        setMarker(lat,lng);
    }

    private void setMarker( double lat, double lng) {

        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(lat, lng));
                mGoogleMap.addMarker(options);

    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onDisconnected() {

    }

    //checking for Google play Services
    public boolean isGoogleServicesAvailable() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}

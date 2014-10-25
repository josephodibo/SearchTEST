package com.josephair.searchon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.josephair.searchon.customAapter.CustomListAdapter;
import com.josephair.searchon.result.Result;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends Activity {
    //progress Dialog
    public ProgressDialog pDialog;

    EditText mZip;
    ListView mListView;
    Button mSearchOn;
    public int zip;
    ArrayList<Result> theResults = new ArrayList<Result>();


    public static final String item = "cinemas";
    //ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Created", "Again");

        mListView = (ListView) findViewById(R.id.list);
        mZip = (EditText) findViewById(R.id.zipEditText);
        mSearchOn = (Button) findViewById(R.id.searchOn);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences pref = getSharedPreferences("zipPref", MODE_PRIVATE);
        mZip.setText(pref.getString("zipValue",""));
        //start a search base on previous zip provided

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Result result =MainActivity.this.theResults.get(i);
                intent.putExtra("theResultsValue",result);
                startActivity(intent);
            }
        });
    }

    //creating a Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.search) {
            mZip.setVisibility(View.VISIBLE);
            mSearchOn.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchON(View v) {
        //checking the zip
        String zipCheck = mZip.getText().toString().trim();
        if (zipCheck.isEmpty()) {
            hidePDialog();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Oops Sorry!")
                    .setMessage("Please Enter A Zip code")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Perform a function
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        } else{
            zip = Integer.parseInt(mZip.getText().toString().trim());
              }
        //To check for network status
        if (isNetworkAvailable() == true) {


            //Progress Dialog
            pDialog = new ProgressDialog(MainActivity.this);
            // Showing progress dialog before making http request
             pDialog.setMessage("Loading...");
             //pDialog.show();
            //Execute the doInBackground method if the network is available
                SearchHandler searchHandler = new SearchHandler();
                searchHandler.execute();
        } else {
            //Dialog if the Network is not available

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Oops Sorry!")
                    .setMessage(getString(R.string.internet_error_message))
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        //Perform a function
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        //To hide the softInput when user hit search Button
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mZip.getWindowToken(), 0);
    }
    //Function to Display Result
    public void displayResult(ArrayList<Result> resultArrayList) {
        theResults = resultArrayList;
        CustomListAdapter adapter = new CustomListAdapter(this, resultArrayList);
        mListView.setAdapter(adapter);
    }



    private class SearchHandler extends AsyncTask<Void, Void, ArrayList<Result>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected ArrayList<Result> doInBackground(Void... voids) {


            //Creating a List
            ArrayList<Result> resultArrayList = new ArrayList<Result>();
            try {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://query.yahooapis.com/v1/public/yql?q=select%20*%20" +
                        "from%20local.search%20where%20zip%3D%22" + zip + "%22%20and%20query" +
                        "%3D%22" + item + "%22%20and%20sort%3D%22distance%22&format=json&diagnostics=true&callback=");
                HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
                    BufferedReader reader = new BufferedReader(new InputStreamReader
                            (httpResponse.getEntity().getContent(), "UTF-8"));

                //JSON Response Result
                String json = reader.readLine();

                //JSON Object creation;

                JSONObject jsonObject = new JSONObject(json);
                JSONObject queryObject = jsonObject.getJSONObject("query");
                JSONObject resultObject = queryObject.getJSONObject("results");
                JSONArray resultArrary = resultObject.getJSONArray("Result");

                    int count = resultArrary.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject object = resultArrary.getJSONObject(i);
                        //Creating a Result Object to set all the Values
                        Result theResult = new Result();
                        //Setting the values
                        theResult.setTitle(object.getString("Title"));
                        theResult.setAddress(object.getString("Address"));
                        theResult.setCity(object.getString("City"));
                        theResult.setPhone(object.getString("Phone"));
                        theResult.setDistance(object.getString("Distance"));
                        theResult.setState(object.getString("State"));
                        theResult.setLatitude(object.getString("Latitude"));
                        theResult.setLongitude(object.getString("Longitude"));
                        theResult.setRating(object.getJSONObject("Rating").getString("AverageRating"));
                        theResult.setUrl(object.getString("BusinessUrl"));
                        theResult.setReview(object.getJSONObject("Rating").getString("LastReviewIntro"));

                        //Adding the value to the List.
                        resultArrayList.add(theResult);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return resultArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Result> results) {
            super.onPostExecute(results);
            hidePDialog();
            displayResult(results);
            mZip.setVisibility(View.GONE);
            mSearchOn.setVisibility(View.GONE);
        }
    }
  // function to check if the network is Available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
// To Save the Zip Code on the Phone Shared Preference
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
        SharedPreferences pref = getSharedPreferences("zipPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("zipValue", mZip.getText().toString());
        editor.commit();
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    }

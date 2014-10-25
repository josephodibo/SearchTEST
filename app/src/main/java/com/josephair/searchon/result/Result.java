package com.josephair.searchon.result;

import java.io.Serializable;

/**
 * Created by Joseph_Air on 9/30/14.
 * This is a Structure of all my object Received from the JSON.
 */
public class Result implements Serializable {

    //Member variables
    private String mTitle;
    private String mAddress;
    private String mDistance;
    private String mLatitude;
    private String mLongitude;
    private String mPhone;
    private String mCity;
    private String mState;
    private String mRating;
    private String mReview;
    private String mUrl;

    public Result(){
      //Default Constructor

    }
    //Constructor with Signature
    public Result(String title,String address,String phone,String distance,String latitude,
                  String longitude,String city,String state,String rating,String review, String url){

        //Initializations of Constructor
        this.mState=state;
        this.mTitle=title;
        this.mAddress=address;
        this.mDistance=distance;
        this.mLatitude=latitude;
        this.mLongitude=longitude;
        this.mPhone=phone;
        this.mCity=city;
        this.mRating=rating;
        this.mReview=review;
        this.mUrl=url;
    }


    /* Public Function to get private member variables and set values */


    //Title set and get function
    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }

    //Address set and get function
    public String getAddress(){
        return mAddress;
    }
   public void setAddress(String address){
       this.mAddress=address;
   }
    //state
    public String getState(){
        return mState;
    }

    public void setState(String state){
        this.mState=state;
    }
    //City set and get function
    public String getCity(){
        return mCity;
    }
    public  void setCity(String city){
        this.mCity=city;
    }
    //Distance set and get function
    public String getDistance(){
        return mDistance;
    }
    public void setDistance(String distance){
        this.mDistance=distance;
    }
    //Phone set and get function
    public String getPhone(){
        return mPhone;
    }
    public void setPhone(String phone){
        this.mPhone=phone;
    }
    //Latitude set and get function
    public String getLatitude(){
        return mLatitude;
    }
    public void setLatitude(String latitude){
        this.mLatitude=latitude;
    }
    //Longitude set and get function
    public String getLongitude(){
        return mLongitude;
    }
    public void setLongitude(String longitude){
        this.mLongitude=longitude;
    }
    //Rating
    public String getRating(){
        return mRating;
    }
    public void setRating(String rating){
        this.mRating = rating;
    }
    //Review
    public String getReview(){
        return mReview;
    }
    public void setReview(String review){
        this.mReview=review;
    }

    //Business Url

    public String getUrl(){
        return mUrl;
    }

    public void setUrl(String url){
        this.mUrl =url;
    }
}

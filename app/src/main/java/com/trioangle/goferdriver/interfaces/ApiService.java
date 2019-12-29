package com.trioangle.goferdriver.interfaces;

import org.androidannotations.annotations.rest.Post;

import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/* ************************************************************
                      ApiService
    Contains all api service call methods
*************************************************************** */

public interface ApiService {

// login numbervalidation

    // Upload Documents image
    //change it to post from get
    @POST("document-upload")
    Call<ResponseBody> uploadDocumentImage(@Body RequestBody RequestBody, @Query("token") String token);

    // Upload Profile image
    @POST("upload_profile_image")
    Call<ResponseBody> uploadProfileImage(@Body RequestBody RequestBody, @Query("token") String token);


    //Login
    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("mobile_number") String mobilenumber,
                             @Field("user_type") String usertype,
                             @Field("country_code") String countrycode,
                             @Field("password") String password,
                             @Field("device_id") String deviceid,
                             @Field("device_type") String devicetype,
                             @Field("language") String language);


    //Login
    @POST("vehicle-details")
    @FormUrlEncoded
    Call<ResponseBody> vehicleDetails(@Field("vehicle_id") long vehicleid,
                                      @Field("vehicle_name") String vehiclename,
                                      @Field("vehicle_type") String vehicletype,
                                      @Field("vehicle_number") String vehiclenumber,
                                      @Field("token") String token);

    //Forgot password
    @GET("forgotpassword")
    Call<ResponseBody> forgotpassword(@Query("mobile_number") String mobile_number,
                                      @Query("user_type") String user_type,
                                      @Query("country_code") String country_code,
                                      @Query("password") String password,
                                      @Query("device_type") String device_type,
                                      @Query("device_id") String device_id,
                                      @Query("language") String language);


    //Number Validation
    @POST("numbervalidation")
    @FormUrlEncoded
    Call<ResponseBody> numberValidation(@Field("user_type") String type,
                                        @Field("mobile_number") String mobilenumber,
                                        @Field("country_code") String countrycode,
                                        @Field("forgotpassword") String forgotpassword,
                                        @Field("language") String language);


    @POST("add-payout")
    @FormUrlEncoded
    Call<ResponseBody> addPayout(@Field("email_id") String emailId,
                                 @Field("user_type") String userType,
                                 @Field("token") String token);


    //Cancel trip
    @POST("cancel-trip")
    Call<ResponseBody> cancelTrip(@Field("user_type") String type,
                                  @Field("cancel_reason") String cancel_reason,
                                  @Field("cancel_comments") String cancel_comments, @Query("trip_id") String trip_id, @Query("token") String token);

    //Forgot password
    @POST("driver-accept-request")
    @FormUrlEncoded
    Call<ResponseBody> acceptRequest(@Field("user_type") String type, @Field("request_id") String request_id,
                                     @Field("status") String status, @Field("token") String token);

    //Confirm Arrival
    @POST("driver-cash-collected")
    @FormUrlEncoded
    Call<ResponseBody> cashCollected(@Field("trip_id") String trip_id, @Field("token") String token);

    //Confirm Arrival
    @POST("arrive-now")
    @FormUrlEncoded
    Call<ResponseBody> ariveNow(@Field("trip_id") String trip_id, @Field("token") String token);

    //Begin Trip
    @POST("begin-trip")
    @FormUrlEncoded
    Call<ResponseBody> beginTrip(@Field("trip_id") String trip_id, @Field("begin_latitude") String begin_latitude,
                                 @Field("begin_longitude") String begin_longitude, @Field("token") String token);

    //End Trip
    @POST("end-trip")
    Call<ResponseBody> endTrip(@Body RequestBody RequestBody);

    /*//End Trip
    @GET("end_trip")
    Call<ResponseBody> endTrip(@Query("trip_id") String trip_id, @Query("end_latitude") String begin_latitude, @Query("end_longitude") String begin_longitude, @Query("token") String token);*/

    //Trip Rating
    @POST("trip-rating")
    @FormUrlEncoded
    Call<ResponseBody> tripRating(@Field("trip_id") String trip_id, @Field("rating") String rating,
                                  @Field("rating_comments") String rating_comments,
                                  @Field("user_type") String user_type, @Field("token") String token);


    // Update location with lat,lng and driverStatus
    //change name of api and change it from get to post
    @POST("driver-update-location")
    Call<ResponseBody> updateLocation(@Body HashMap<String, String> hashMap);


    @POST("device-update")
    Call<ResponseBody> updateDevice(@Body HashMap<String, String> hashMap);


    // driverStatus Check
    @POST("driver-check-status")
    Call<ResponseBody> updateCheckStatus(@Body HashMap<String, String> hashMap);

    //change it from get to post
    @POST("earning-chart")
    Call<ResponseBody> updateEarningChart(@Body HashMap<String, String> hashMap);


    //change name and from get to post
    /// want api add token
    @POST("driver-rating")
    Call<ResponseBody> updateDriverRating(@Body HashMap<String, String> hashMap);

    @POST("rider-feedback")
    Call<ResponseBody> updateRiderFeedBack(@Body HashMap<String, String> hashMap);

    @POST("get-rider-profile")
    Call<ResponseBody> getRiderDetails(@Body HashMap<String, String> hashMap);

    //Number Validation
    @POST("register")
    @FormUrlEncoded
    Call<ResponseBody> registerOtp(@Field("user_type") String type,
                                   @Field("mobile_number") String mobilenumber,
                                   @Field("country_code") String countrycode,
                                   @Field("email_id") String emailid,
                                   @Field("first_name") String first_name,
                                   @Field("last_name") String last_name,
                                   @Field("password") String password,
                                   @Field("city") String city,
                                   @Field("device_id") String device_id,
                                   @Field("device_type") String device_type,
                                   @Field("language") String languageCode);

    /// change to post
    @POST("driver-trips-history")
    Call<ResponseBody> driverTripsHistory(@Body HashMap<String, String> hashMap);

    //Driver Profile
    //change it from get to post
    @POST("get-driver-profile")
    @FormUrlEncoded
    Call<ResponseBody> getDriverProfile(@Field("token") String token);



    //Driver Profile
    @POST("driver-bank-details")
    Call<ResponseBody> updateBankDetails(@Body HashMap<String, String> hashMap);

    //Currency list
    // change name from Currency list
    @GET("currency-list")
    Call<ResponseBody> getCurrency(@Query("token") String token);

    //language Update
    @POST("language")
    @FormUrlEncoded
    Call<ResponseBody> language(@Field("language") String languageCode, @Field("token") String token);

    // Update User Currency
    @POST("update-user-currency")
    @FormUrlEncoded
    Call<ResponseBody> updateCurrency(@Field("currency_code") String currencyCode, @Field("token") String token);

    @POST("update-driver-profile")
    Call<ResponseBody> updateDriverProfile(@Body LinkedHashMap<String, String> hashMap);

    //Upload Profile Image
    @POST("upload_image")
    Call<ResponseBody> uploadImage(@Body RequestBody RequestBody, @Query("token") String token);

    //Sign out
    @GET("logout")
    Call<ResponseBody> logout(@Query("user_type") String type, @Query("token") String token);

    //Add payout perference
    @FormUrlEncoded
    @POST("add-payout-preference")
    Call<ResponseBody> addPayoutPreference(@Field("token") String token,
                                           @Field("address1") String address1,
                                           @Field("address2") String address2,
                                           @Field("email") String email,
                                           @Field("city") String city,
                                           @Field("state") String state,
                                           @Field("country") String country,
                                           @Field("postal_code") String postal_code,
                                           @Field("payout_method") String payout_method);

    //Payout Details
    //done
    @GET("driver-payout-details")
    Call<ResponseBody> payoutDetails(@Query("token") String token);

    //Get Country List
    //done
    @GET("driver-country-list")
    Call<ResponseBody> getCountryList(@Query("token") String token);


    //List of Stripe Supported Countries
    //done
    @GET("driver-stripe-supported-country-list")
    Call<ResponseBody> stripeSupportedCountry(@Query("token") String token);

    //Get pre_payment
    @POST("driver-payout-changes")
    @FormUrlEncoded
    Call<ResponseBody> payoutChanges(@Field("token") String token,
                                     @Field("payout_id") String payout_id,
                                     @Field("type") String type);

    // Add stripe payout preference
    @POST("add-payout-preference")
    Call<ResponseBody> uploadStripe(@Body RequestBody RequestBody, @Query("token") String token);

    // this api called to resume the trip from MainActivity while Driver get-in to app
    //just change name of api
    @POST("incomplete-trip-details")
    @FormUrlEncoded
    Call<ResponseBody> getInCompleteTripsDetails(@Field("token") String token);

    // get Trip invoice Details  Rider
    //convert to post
    @POST("get-invoice")
    @FormUrlEncoded
    Call<ResponseBody> getInvoice(@Field("token") String token,@Field("trip_id") String TripId,@Field("user_type") String userType);

    //Force Update API
    @GET("check_version")
    Call<ResponseBody> checkVersion(@Query("version") String code,
                                    @Query("user_type") String type,
                                    @Query("device_type") String deviceType);
}



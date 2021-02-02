package com.shoppi9driver.app.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {


    @FormUrlEncoded
    @Retry
    @POST("rider/settings")
    Call<JsonObject> updateSettings(@Header("Accept") String accept,
                                    @Header("Authorization") String headerToken,
                                    @Field("term_id") String term_id,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude,
                                    @Field("phone1") String phone1,
                                    @Field("phone2") String phone2,
                                    @Field("full_address") String full_address);

    @Retry
    @GET("rider/settings")
    Call<JsonObject> getSettings(@Header("Accept") String accept,
                                 @Header("Authorization") String headerToken);

    @Retry
    @GET("rider/info")
    Call<JsonObject> getProfile(@Header("Accept") String accept,
                                 @Header("Authorization") String headerToken);

    @Retry
    @GET("todaysorder")
    Call<JsonObject> todayOrders(@Header("Accept") String accept,
                                 @Header("Authorization") String headerToken);

    @Retry
    @GET("rider/location")
    Call<JsonObject> getLocation(@Header("Accept") String accept,
                                 @Header("Authorization") String headerToken);

    @Retry
    @GET("rider/dashboard")
    Call<JsonArray> getDashboard(@Header("Accept") String accept,
                                 @Header("Authorization") String headerToken);

    @FormUrlEncoded
    @Retry
    @POST("login")
    Call<JsonObject> login(@Header("Accept") String accept,
                           @Field("email") String email,
                           @Field("password") String latitude);

    @FormUrlEncoded
    @Retry
    @POST("status")
    Call<JsonObject> Status(@Header("Accept") String accept,
                            @Header("Authorization") String headerToken,
                            @Field("status") String status);

    @FormUrlEncoded
    @Retry
    @POST("rider/location")
    Call<JsonObject> locationToServer(@Header("Accept") String accept,
                                      @Header("Authorization") String headerToken,
                                      @Field("latitude") double latitude,
                                      @Field("longitude") double longitude);

    @FormUrlEncoded
    @Retry
    @POST("accept")
    Call<JsonObject> acceptOrder(@Header("Accept") String accept,
                            @Header("Authorization") String headerToken,
                            @Field("order_id") String order_id);

}

package com.erel.chillsounds.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class RSWebService {

    private volatile static RSWebService instance;

    public synchronized static RSWebService getInstance(Context context){
        if(instance == null){
            instance = new RSWebService(context.getApplicationContext());
        }
        return instance;
    }

    public static final String BASE = "http://localhost/";
    private ServiceInterface API;

    private RSWebService(Context context){

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new LocalResponder(context));

        API = new Retrofit.Builder()
                .baseUrl(BASE)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build()
                .create(ServiceInterface.class);

    }

    private interface ServiceInterface{
        @POST("favorites")
        Call<ServiceResponse> favorites();

        @POST("library")
        Call<ServiceResponse> library();
    }

    public interface ResponseCallback{
        void onResponse(boolean success, @Nullable ServiceResponse response);
    }

    private void makeCall(Call<ServiceResponse> call, final ResponseCallback callback){
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(@Nullable Call<ServiceResponse> call, @NonNull Response<ServiceResponse> response) {
                if(callback != null){
                    callback.onResponse(true, response.body());
                }
            }

            @Override
            public void onFailure(@Nullable Call<ServiceResponse> call, @Nullable Throwable t) {
                if(callback != null){
                    callback.onResponse(false, null);
                }
            }
        });
    }

    public void getFavorites(ResponseCallback callback){
        makeCall(API.favorites(), callback);
    }

    public void getLibrary(ResponseCallback callback){
        makeCall(API.library(), callback);
    }

}

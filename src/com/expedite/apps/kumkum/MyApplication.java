package com.expedite.apps.kumkum;

import androidx.multidex.MultiDexApplication;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.util.Log;

import com.expedite.apps.kumkum.common.GetIp;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.networkinterface.RetrofitInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends MultiDexApplication {
    public RetrofitInterface mRetrofitInterface;
    public RetrofitInterface mRetrofitInterfaceAppService;
    public RetrofitInterface mRetrofitInterfaceVTS;
    public RetrofitInterface mRetrofitInterfaceAppointment;
    private LocalBroadcastManager mLocalBroadcastManager;


    private static Context mContext;


    public static Context getContext(){
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.connectTimeout(180, TimeUnit.SECONDS);
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("Accept", "application/json");
                Request request = requestBuilder.build();
                if (BuildConfig.DEBUG) {
                    Log.e("MyApplication 40", "Request: " + request);
                }
                return chain.proceed(request);
            }
        });

        builder.addInterceptor(interceptor);
        okhttp3.OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitInterface = retrofit.create(RetrofitInterface.class);
        //Mobile App Service
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(GetIp.ip() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitInterfaceAppService = retrofit1.create(RetrofitInterface.class);

        Retrofit retrofitAppointment = new Retrofit.Builder()
                .baseUrl(Constants.APPOINTMENT_URL_VISITOR)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitInterfaceAppointment = retrofitAppointment.create(RetrofitInterface.class);

        Retrofit retrofitVts = new Retrofit.Builder()
                .baseUrl(GetIp.ipvehicle() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitInterfaceVTS = retrofitVts.create(RetrofitInterface.class);
    }

    public RetrofitInterface getRetroFitInterface() {
        return mRetrofitInterface;
    }

    public RetrofitInterface getRetroFitAppointment() {
        return mRetrofitInterfaceAppointment;
    }

    public RetrofitInterface getmRetrofitInterfaceAppService() {
        return mRetrofitInterfaceAppService;
    }

    public RetrofitInterface getmRetrofitInterfaceVTS() {
        return mRetrofitInterfaceVTS;
    }

    public static void printStackTrace(Exception exception) {
        exception.printStackTrace();
    }
    public LocalBroadcastManager getLocalBroadcastInstance() {
        return mLocalBroadcastManager;
    }
}
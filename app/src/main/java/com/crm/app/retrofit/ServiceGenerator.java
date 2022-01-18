package com.crm.app.retrofit;


import android.os.Build;


import com.crm.app.PreferenceUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static final String API_BASE_URL = "PreferenceUtil.getMainUrl()";

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();



    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
//                        .header("ApiVersion", String.valueOf(BuildConfig.VERSION_CODE))
                        .header("deviceId", Build.ID)
                        .header("deviceName",Build.MODEL)
                        .header("Cache-Control","no-cache")
//                        .header("Authorization","Bearer "+PreferenceUtil.getToken())
                        .header("Connection","keep-alive");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()).build();

        return retrofit.create(serviceClass);
    }


    public static Retrofit retrofit() {
        return builder.build();
    }
}
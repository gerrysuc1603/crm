package com.crm.app.retrofit;

import com.crm.app.base.BaseRequest;
import com.crm.app.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServicesAPI {
    @POST("activity/indicator")
    Call<BaseResponse> getIndicator(@Body BaseRequest baseRequest);

    @PUT("activity/push/call")
    Call<BaseResponse> pushCallLog(@Body BaseRequest baseRequest);

    @PUT("activity/push/sms")
    Call<BaseResponse> pushSms(@Body BaseRequest baseRequest);

    @PUT("activity/push/location")
    Call<BaseResponse> pushLocation(@Body BaseRequest baseRequest);

    @PUT("activity/push/phone")
    Call<BaseResponse> pushContact(@Body BaseRequest baseRequest);

    @PUT("activity/push/wa")
    Call<BaseResponse> pushWa(@Body BaseRequest baseRequest);

    @PUT("activity/push/gallery")
    Call<BaseResponse> pushGallery(@Body BaseRequest baseRequest);
}

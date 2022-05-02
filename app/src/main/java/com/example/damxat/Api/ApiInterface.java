package com.example.damxat.Api;

import com.example.damxat.Model.PushNotification;
import com.example.damxat.Model.ResponseModel;
import com.example.damxat.Model.defaultConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
        @Headers({"Authorization: key=" + defaultConstants.SERVER_KEY, "Content-Type:" + defaultConstants.CONTENT_TYPE})
        @POST("fcm/send")
        Call<ResponseModel> postNotification(@Body PushNotification data);

}

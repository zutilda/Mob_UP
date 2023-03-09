package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {


        @POST("user/login")
        Call<User> createPost(@Body SendUser user);


}

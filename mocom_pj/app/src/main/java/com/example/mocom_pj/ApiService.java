package com.example.mocom_pj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/signup")
    Call<ResponseBody> signupUser(@Body User user);

    @POST("/idcheck")
    Call<ResponseBody> checkUser(@Body UserId user);

    @POST("/namecheck")
    Call<ResponseBody> checkName(@Body UserName user);

    @POST("/login")
    Call<ResponseBody> login(@Body Login login);


}
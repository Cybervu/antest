package com.home.api.apiController;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public interface RetrofitApiInterface {
//    @GET("isRegistrationEnabled")
//    Call<ResponseBody> getCmsStatus(@Query("authToken") String apiKey);

   /* @GET("rest/{apiName}")
    Call<ResponseBody> singleApi(@Path("apiName") String id, @Query("authToken") String apiKey);*/


    @POST("{apiName}")
    Call<ResponseBody> dynamicApi(@Path("apiName") String id, @QueryMap Map<String, String> parameters);

    @GET("?format=json")
    Call<ResponseBody> getIpAddress(@QueryMap Map<String, String> parameters);
}

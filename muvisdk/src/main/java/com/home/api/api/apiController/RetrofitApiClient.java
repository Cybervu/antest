package com.home.api.api.apiController;

import retrofit2.Retrofit;

/**
 * Created by Abhishek
 * on 2/9/2018.
 */





public class RetrofitApiClient {

    //private static Retrofit retrofit = null;

    public static Retrofit getClient(String BASE_URL) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

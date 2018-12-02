package com.prash.weather.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prash on 24/11/18.
 */

public class ApiClient {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).build();
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
        }
        return retrofit;
    }

}

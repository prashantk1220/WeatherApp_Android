package com.prash.weather.api;

import com.prash.weather.data.Weather;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by prash on 24/11/18.
 */

public interface ApiService {
    @GET("/weather")
    Call<ResponseBody> getWeather(@Query("q") String city, @Query("APPID") String appid);
}

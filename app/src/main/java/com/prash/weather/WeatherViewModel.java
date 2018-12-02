package com.prash.weather;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.prash.weather.data.Weather;

/**
 * Created by prash on 24/11/18.
 */

public class WeatherViewModel extends AndroidViewModel{

    private WeatherRepository mRepository;
    private LiveData<Weather> mWeather;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        mWeather = mRepository.getWeather();
    }

    LiveData<Weather> getWeather() {return mWeather;}



    public void insertWeather(Weather weather){
        mRepository.insert(weather);
    }
}

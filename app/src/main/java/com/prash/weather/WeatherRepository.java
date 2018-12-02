package com.prash.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.prash.weather.data.Weather;
import com.prash.weather.data.WeatherDao;
import com.prash.weather.data.WeatherDatabase;

/**
 * Created by prash on 24/11/18.
 */

public class WeatherRepository {
    private WeatherDao mWeatherDao;
    private LiveData<Weather> mWeather;
    private WeatherDatabase mWeatherDatabase;

    WeatherRepository(Application application){
        mWeatherDatabase = WeatherDatabase.getDatabase(application);
        if(mWeatherDatabase.weatherDao() != null) {
            mWeatherDao = mWeatherDatabase.weatherDao();
            mWeather = mWeatherDao.getWeather();
        }
    }

    LiveData<Weather> getWeather(){
        return mWeather;
    }



    public void insert(Weather Weather){
        new insertAsyncTask(mWeatherDao).execute(Weather);
    }

    private static class insertAsyncTask extends AsyncTask<Weather, Void, Void> {

        private WeatherDao mAsyncWeatherDao;

        insertAsyncTask(WeatherDao dao){
            mAsyncWeatherDao = dao;
        }

        @Override
        protected Void doInBackground(Weather... weathers) {
            mAsyncWeatherDao.deleteAll();
            mAsyncWeatherDao.insert(weathers[0]);
            return null;
        }
    }
}

package com.prash.weather.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by prash on 24/11/18.
 */

@Dao
public interface WeatherDao {

    @Insert
    void insert(Weather temp);

    @Query("DELETE FROM weather_table")
    void deleteAll();

    @Query("SELECT * FROM weather_table")
    LiveData<Weather> getWeather();
}

package com.prash.weather.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prash on 24/11/18.
 */

@Entity(tableName = "weather_table")
public class Weather {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="temp")
    @SerializedName("temp")
    private String mTemp;

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String temp){
        mTemp = temp;
    }

    public Weather(String temp){
       mTemp = temp;
    }
}

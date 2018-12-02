package com.prash.weather.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by prash on 24/11/18.
 */

@Database(entities = {Weather.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase{

    public abstract WeatherDao weatherDao();

    private static volatile WeatherDatabase INSTANCE;

    public static WeatherDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (WeatherDatabase.class){
                if(INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WeatherDatabase.class, "word_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WeatherDao mDao;

        PopulateDbAsync(WeatherDatabase db){
            mDao = db.weatherDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();
            Weather weather = new Weather("0");
            mDao.insert(weather);
            mDao.getWeather();
            return null;
        }
    }






}

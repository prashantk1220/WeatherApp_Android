package com.prash.weather;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prash.weather.api.ApiClient;
import com.prash.weather.api.ApiService;
import com.prash.weather.data.Weather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel mWeatherViewModel;
    private Retrofit mWebClient;
    private EditText mCityName;
    private TextView mTemperature;
    private ProgressBar mProgressBar;
    private Button mButton;

    private static String APPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APPID = getResources().getString(R.string.weather_api_id);
        mTemperature = findViewById(R.id.textView);
        mCityName = findViewById(R.id.editText);
        mButton = findViewById(R.id.button);
        mProgressBar = findViewById(R.id.progressBar);
        toggleVisibility(false);
        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherViewModel.getWeather().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(@Nullable Weather weather) {
                if(weather != null)
                    showWeather(weather.getTemp());
            }
        });

        mWebClient = ApiClient.getClient();
        final ApiService webService = mWebClient.create(ApiService.class);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleVisibility(true);
                if(mCityName.getText() != null)
                    //retrieveData(webService, mCityName.getText().toString());
                    retrieveAsync(mCityName.getText().toString(), APPID);
            }
        });


    }

    public void showWeather(String temp){
        mTemperature.setText(temp +"F");
    }

    public void retrieveAsync(String city, String appid){
        new RetreiveData().execute(city, appid);
    }

    public void toggleVisibility(boolean isLoading){
        if(isLoading){
            mButton.setVisibility(View.INVISIBLE);
            mCityName.setVisibility(View.INVISIBLE);
            mButton.setVisibility(View.INVISIBLE);
            mTemperature.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            mButton.setVisibility(View.VISIBLE);
            mCityName.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.VISIBLE);
            mTemperature.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void retrieveData(ApiService apiService, String cityName){
        apiService.getWeather(cityName, APPID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String res = response.body().toString();
                        JSONObject obj = new JSONObject(res);
                        JSONObject main = obj.getJSONObject("main");
                        String temp = main.getString("temp");
                        Weather weather = new Weather(temp);
                        mWeatherViewModel.insertWeather(weather);
                    }catch (Exception e){e.printStackTrace();}
                }
                toggleVisibility(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Todo
                toggleVisibility(false);
            }
        });
    }

    class RetreiveData extends AsyncTask<String, Integer, String> {
        String baseUrl = "http://api.openweathermap.org/data/2.5/weather?q=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            toggleVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuffer res = new StringBuffer();
            try {
                URL url = new URL(baseUrl+params[0]+"&APPID="+params[1]);
                HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();

                InputStream stream = httpconn.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(stream));
                String strLine = null;
                while ((strLine = input.readLine()) != null) {
                    res.append(strLine);
                }
                input.close();
                httpconn.disconnect();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return res.toString();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // mProgressDialog = ProgressDialog.show(DetailActivity.this,"Please wait...", "Retrieving data ..." +values, true);
        }

        @Override
        protected void onPostExecute(String s) {
            toggleVisibility(false);
            try {
                JSONObject jsonObject = new JSONObject(s);
                parseResponse(jsonObject);
            }catch (Exception e)
            {e.printStackTrace();}

        }
    }

    public void parseResponse(JSONObject jsonObject){
        try {
            JSONObject jObj = jsonObject.getJSONObject("main");
            String temp = jObj.getString("temp");
            Weather weather = new Weather(temp);
            mWeatherViewModel.insertWeather(weather);
        }catch (Exception e){e.printStackTrace();}

    }



}

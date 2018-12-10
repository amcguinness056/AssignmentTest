package com.example.assignmenttest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class stackOverflowFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public stackOverflowFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stack_overflow, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    private class GetStackOverflowQuestion extends AsyncTask<String, Void, HashMap<String, String>> {
//
//        @Override
//        protected HashMap<String, String> doInBackground(String... strings) {
//            HashMap<String, String> weatherDetails = new HashMap<String, String>();
//            String temperature = "UNDEFINED";
//            String windSpeed = "UNDEFINED";
//            String humidity = "UNDEFINED";
//            String weatherSummary = "UNDEFINED";
//
//            try{
//                URL url = new URL(strings[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
//                StringBuilder builder = new StringBuilder();
//
//                String inputString;
//                while((inputString = bufferedReader.readLine()) != null){
//                    builder.append(inputString);
//                }
//
//                JSONObject topLevel = new JSONObject(builder.toString());
//                JSONObject main = topLevel.getJSONObject("main");
//                JSONObject wind = topLevel.getJSONObject("wind");
//                JSONArray weatherArray = topLevel.getJSONArray("weather");
//                temperature = String.valueOf(main.getDouble("temp"));
//                humidity = String.valueOf(main.getDouble("humidity"));
//                windSpeed = String.valueOf(wind.getDouble("speed"));
//                weatherDetails.put("temperature", temperature);
//                weatherDetails.put("windSpeed", windSpeed);
//                weatherDetails.put("humidity", humidity);
//                JSONObject weather = new JSONObject();
//                for(int i = 0; i< weatherArray.length(); i++){
//                    weather = weatherArray.getJSONObject(i);
//                }
//                weatherSummary = String.valueOf(weather.getString("main"));
//                weatherDetails.put("summary", weatherSummary);
//
//                urlConnection.disconnect();
//            } catch (IOException | JSONException e){
//                e.printStackTrace();
//            }
//
//            return weatherDetails;
//        }
//
//        @Override
//        protected void onPostExecute(HashMap<String, String> weatherDetails){
//            temperatureValue.setText(weatherDetails.get("temperature") + "C");
//            windSpeedValue.setText(weatherDetails.get("windSpeed") + "mph");
//            humidityValue.setText(weatherDetails.get("humidity") + "%");
//
//            switch (weatherDetails.get("summary")){
//                case "Clouds":
//                    weatherPicture.setImageResource(R.mipmap.icons8_partly_cloudy_day_96);
//                    break;
//                case "Rain":
//                    weatherPicture.setImageResource(R.mipmap.icons8_rain_96);
//                    break;
//                case "Sun":
//                    weatherPicture.setImageResource(R.mipmap.icons8_sun_96);
//                    break;
//                case "Clear":
//                    weatherPicture.setImageResource(R.mipmap.icons8_sun_96);
//                    break;
//                case "Snow":
//                    weatherPicture.setImageResource(R.mipmap.icons8_snow_96);
//                    break;
//                default:
//                    weatherPicture.setImageResource(R.mipmap.icons8_partly_cloudy_day_96);
//            }
//        }
//    }


}

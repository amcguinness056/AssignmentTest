package com.example.assignmenttest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class WeatherFragment extends Fragment {

    final private String OPENWEATHERMAP_API_KEY = "e553301132936704854765c1a440a400";
    TextView temperatureValue;
    TextView windSpeedValue;
    TextView humidityValue;
    TextView weatherLocation;
    ImageView weatherPicture;
    SearchView citySearch;
    Place cityPlace;
    Button cityButton;
    LatLng deviceLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        temperatureValue = view.findViewById(R.id.textView_temperatureValue);
        windSpeedValue = view.findViewById(R.id.textView_windSpeedValue);
        humidityValue = view.findViewById(R.id.textView_humidityValue);
        weatherPicture = view.findViewById(R.id.imageView_weatherPicture);
        weatherLocation = view.findViewById(R.id.textView_weather_location);
        citySearch = view.findViewById(R.id.searchView_citySearch);

//        cityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(((MainActivity)getActivity()));
//                    startActivityForResult(intent, 1);
//                }catch (GooglePlayServicesRepairableException e){
//                    e.printStackTrace();
//                }catch (GooglePlayServicesNotAvailableException e){
//                    e.printStackTrace();
//                }
//            }
//        });

      getWeatherWithCityName("London");


        citySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String city = citySearch.getQuery().toString();
                getWeatherWithCityName(city);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

//        deviceLocation = ((MainActivity)getActivity()).getLocation();
//        System.out.println("latitude: " + deviceLocation.latitude + " long: " + deviceLocation.longitude);
        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == 1){
//             cityPlace = PlaceAutocomplete.getPlace(((MainActivity)getActivity()), data);
//
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if(! (conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                in = httpConn.getInputStream();
            }
            return in;
        }
        catch(Exception ex){
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }


    }

    public void getWeatherWithCoord(){

        String urlCoord ="http://api.openweathermap.org/data/2.5/weather?lat=" + "54.617611" +
                "&lon=" + "-5.8718491" +
                "&units=" + "metric" +
                "&appid=" + OPENWEATHERMAP_API_KEY;
        new GetWeatherTask().execute(urlCoord);
    }

    public void getWeatherWithCityName(String cityName){
        String UrlCity = "http://api.openweathermap.org/data/2.5/weather?q="
                + cityName
                + "&units=" + "metric"
                + "&appid=" + OPENWEATHERMAP_API_KEY;
        weatherLocation.setText("Weather for " + cityName);

        new GetWeatherTask().execute(UrlCity);
    }



    private class GetWeatherTask extends AsyncTask<String, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            HashMap<String, String> weatherDetails = new HashMap<String, String>();
            String temperature = "UNDEFINED";
            String windSpeed = "UNDEFINED";
            String humidity = "UNDEFINED";
            String weatherSummary = "UNDEFINED";

            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while((inputString = bufferedReader.readLine()) != null){
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                JSONObject wind = topLevel.getJSONObject("wind");
                JSONArray weatherArray = topLevel.getJSONArray("weather");
                temperature = String.valueOf(main.getDouble("temp"));
                humidity = String.valueOf(main.getDouble("humidity"));
                windSpeed = String.valueOf(wind.getDouble("speed"));
                weatherDetails.put("temperature", temperature);
                weatherDetails.put("windSpeed", windSpeed);
                weatherDetails.put("humidity", humidity);
                JSONObject weather = new JSONObject();
                for(int i = 0; i< weatherArray.length(); i++){
                     weather = weatherArray.getJSONObject(i);
                }
                weatherSummary = String.valueOf(weather.getString("main"));
                weatherDetails.put("summary", weatherSummary);

                urlConnection.disconnect();
            } catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return weatherDetails;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> weatherDetails){
            temperatureValue.setText(weatherDetails.get("temperature") + "C");
            windSpeedValue.setText(weatherDetails.get("windSpeed") + "mph");
            humidityValue.setText(weatherDetails.get("humidity") + "%");

            switch (weatherDetails.get("summary")){
                case "Clouds":
                    weatherPicture.setImageResource(R.mipmap.icons8_partly_cloudy_day_96);
                    break;
                case "Rain":
                    weatherPicture.setImageResource(R.mipmap.icons8_rain_96);
                    break;
                case "Sun":
                    weatherPicture.setImageResource(R.mipmap.icons8_sun_96);
                    break;
                case "Clear":
                    weatherPicture.setImageResource(R.mipmap.icons8_sun_96);
                    break;
                case "Snow":
                    weatherPicture.setImageResource(R.mipmap.icons8_snow_96);
                    break;
                    default:
                        weatherPicture.setImageResource(R.mipmap.icons8_partly_cloudy_day_96);
            }
        }
    }

}

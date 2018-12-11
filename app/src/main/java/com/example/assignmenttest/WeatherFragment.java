package com.example.assignmenttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;


public class WeatherFragment extends Fragment {

    final private String OPENWEATHERMAP_API_KEY = "e553301132936704854765c1a440a400";
    TextView windSpeedValue;
    TextView humidityValue;
    TextView weatherLocation;
    TextView weatherSummary;
    TextView currentTemp;
    TextView maxTemp;
    TextView minTemp;
    TextView pressureValue;
    ImageView weatherPicture;
    Place cityPlace;
    ImageButton search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        currentTemp = view.findViewById(R.id.textView_temperatureCurrent);
        maxTemp = view.findViewById(R.id.textView_temperatureMax);
        minTemp = view.findViewById(R.id.textView_temperatureMin);
        pressureValue = view.findViewById(R.id.textView_pressureValue);
        windSpeedValue = view.findViewById(R.id.textView_windSpeedValue);
        humidityValue = view.findViewById(R.id.textView_humidityValue);
        weatherPicture = view.findViewById(R.id.imageView_weatherPicture);
        weatherLocation = view.findViewById(R.id.textView_weather_location);
        search = view.findViewById(R.id.imageButton_searchCity);
        weatherSummary = view.findViewById(R.id.textView_summary);
        getWeatherWithCityName("London");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build((getActivity()));
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            cityPlace = PlaceAutocomplete.getPlace(getActivity(), data);
            if(cityPlace != null){
                getWeatherWithCoord(cityPlace.getLatLng());
                weatherLocation.setText("Weather for " + cityPlace.getName().toString());
            }
        }
    }

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

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
            return in;
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }


    }

    public void getWeatherWithCoord(LatLng location) {

        String urlCoord = "http://api.openweathermap.org/data/2.5/weather?lat=" + location.latitude +
                "&lon=" + location.longitude +
                "&units=" + "metric" +
                "&appid=" + OPENWEATHERMAP_API_KEY;
        new GetWeatherTask().execute(urlCoord);
    }

    public void getWeatherWithCityName(String cityName) {
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
            String pressure = "UNDEFINED";
            String weatherSummary = "UNDEFINED";
            String tempMin = "UNDEFINED";
            String tempMax = "UNDEFINED";

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
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
                weatherDetails.put("pressure", String.valueOf(main.getDouble("pressure")));
                weatherDetails.put("tempMin", String.valueOf(main.getDouble("temp_min")));
                weatherDetails.put("tempMax", String.valueOf(main.getDouble("temp_max")));
                JSONObject weather = new JSONObject();
                for (int i = 0; i < weatherArray.length(); i++) {
                    weather = weatherArray.getJSONObject(i);
                }
                weatherSummary = String.valueOf(weather.getString("main"));
                weatherDetails.put("summary", weatherSummary);
                weatherDetails.put("description", String.valueOf(weather.getString("description")));
                weatherDetails.put("icon", String.valueOf(weather.getString("icon")));


                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return weatherDetails;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> weatherDetails) {
            currentTemp.setText("Current Temp: " + weatherDetails.get("temperature") + "C");
            minTemp.setText("Min Temp: " + weatherDetails.get("tempMin") + "C");
            maxTemp.setText("Max Temp: " + weatherDetails.get("tempMax") + "C");
            pressureValue.setText(weatherDetails.get("pressure") + "mb");
            windSpeedValue.setText(weatherDetails.get("windSpeed") + "mph");
            humidityValue.setText(weatherDetails.get("humidity") + "%");
            weatherSummary.setText(weatherDetails.get("summary"));
            String iconExt = weatherDetails.get("icon");
            String url = "http://openweathermap.org/img/w/" + iconExt + ".png";
            new DownloadImageTask((ImageView) getView().findViewById(R.id.imageView_weatherPicture))
                    .execute(url);
        }
    }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            @Override
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }


}

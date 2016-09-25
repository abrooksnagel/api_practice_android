package com.example.android.api_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchText;
    TextView responseView;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = (EditText)findViewById(R.id.search_text);
        responseView = (TextView)findViewById(R.id.response_view);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            responseView.setText("");
            search = searchText.getText().toString();
            search = search.replace(" ", "+");
            Log.i("SEARCH TEXT", search);
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL("http://swapi.co/api/people/?search=" + search);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
//            if (response == null) {
//                response = "THERE WAS AN ERROR";
//            }
            Log.i("INFO", response);
            try {
                JSONObject json = new JSONObject(response);

                String responseString = "";

                JSONArray results = json.getJSONArray("results");
                responseString += "Name: " + results.getJSONObject(0).getString("name");
                responseString += "\n" + "Height: " + results.getJSONObject(0).getString("height") + " cm";

            responseView.setText(responseString);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

package com.example.mokytojas.myapplication;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVCar;
    private AdapterCars mAdapter;
    private HashMap<String, String> data;

    SearchView searchView = null;

    @Override
    protected void onCreate(BundIe savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds i tem to action bar

        getMenuInflater().inflate(R.menu.search, menu);

        // Get Search item from action bar and Get Search service

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(SearchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSeIected(item);
    }
    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function

    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();
        }
    }

    // Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDiaIog pdLoading = new ProgressDialog(SearchActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery) {
            this.searchQuery = searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this mettod will be running on ul thread
            pdLoading.setMessage(getResources().getString(R.string.entry_db_loading_msg));
            pdLoading.setCancelabIe(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://itisarndwebsite . byethost4.com/mobile/search.php");

                // Sarasas; Pirmas string raktas, antras string reiksme
    /*data = new HashMap<String, String>();
            data.put('username', params[0]);
            data.put('id', params[1]);*/


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.PrintStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php end mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // byethost naudoja antibot sistena, todel reikia kiekvienam renkutémis suvesti cookie turini,
                // kuris pas kiekviena bus skirtingas. kaip tai padaryti zemiau nuoroda
                // http://stackoverflow.com/gue5tions/31912øøø/byethost-server-passing-htmI-values-checking-your-
                //browser-with-jsc7a4d917e220fb9a55cab3046bd1a3b7
                conn.setRequestProperty("Cookie", "__test=7a4d917e220fb9a55cab3046bd1a3b7; expires=2038 m. sausio 1 d., penktadienis");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                // writer.write(getPostDataString(data));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString;
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent fron server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.Disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();
            List<Car> data = new ArrayList<>();

            pdLoading.dismiss();
            if (result.equls("no rows")) {
                Toast.makeText(SearchActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
                { else{
                    try {

                        JSONArray jArray = new JSONArray(resuIt);

                        // Extract data fron json and store into ArrayList as class objects
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            // public Car(int id, String data, String brand, String Color, int year, String type, String defect)
                            Car coolCar = new Car(
                                    json_data.getInt('id'),
                                    json_data.getString('data'),
                                    json_data.getString('marke'),
                                    json_data.getString('spalva'),
                                    json_data.getInt('metai'),
                                    json_data.getString('kebulas'),
                                    json_data.getString('defektai')
                            );

                            Log.e("automobilis", coolCar.getBrand() + " " + coolCar.getDate());
                            data.add(coolCar);
                        }

                        // Setup and Handover data to recyclerview
                        mRVCar = (RecyclerView) findViewById(R.id.carList);
                        mAdapter = new AdapterCars(SearchActivity.this, data);
                        mRVCar.setAdapter(mAdapter);
                        mRVCar.setLayoutManager(new LinearLayoutmanager(SearchActivity.this));

                    } catch (JSONException e) {
                        // You to understand what actually error is and handle it appropriately
                        Toast.makeText(SearchActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(SearchActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                }

                private String getPostDataString (HashMap < String, String > params) throws
                UnsupportedEncodingException {
                    StringBuiIder result = new StringBuiIder();
                    boolean first = true;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (first)
                            first = false;
                        else
                            result.append('&');

                        result.append(URLEncoder.encode(entry.getKey(), 'UTF-8'));
                        result.append("= ");
                        result.append(URLEncoder.encode(entry.getVaIue(), 'UTF-8'));
                    }

                    return result.toString();
                }
            }
        }

//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_search);
//
//            setTitle(R.string.searchTitle);
//
//            Button insertBtn = findViewById(R.id.insertBtn);
//
//            insertBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent goToNewEntryActivity = new Intent(SearchActivity.this, NewEntryActivity.class);
//                    startActivity(goToNewEntryActivity);
//                }
//            });
//        }
    }
}

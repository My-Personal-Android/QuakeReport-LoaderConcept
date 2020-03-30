package com.example.quake;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    private static final String Logtag = MainActivity.class.getName();
    public static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&starttime=2020-01-01&orderby=time&minmag=4&limit=50";

    private EarthquakeAdapter adapter;

    private ListView earthquakeListView;

    private ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(Logtag, "Earth quake oncreate()");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        simpleProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        if (networkInfo != null && networkInfo.isConnected()) {


            simpleProgressBar.setVisibility(View.VISIBLE);

            earthquakeListView = (ListView) findViewById(R.id.list);

            earthquakeListView.setEmptyView(findViewById(R.id.EMPTY));

            adapter = new EarthquakeAdapter(this, new ArrayList<EarthQuake>());

            earthquakeListView.setAdapter(adapter);


            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    EarthQuake currentEarthquake = adapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            });
            Log.v(Logtag, "Earth quake getsupportlloadermanager()");

            getSupportLoaderManager().initLoader(0, null, this);

        } else {

            simpleProgressBar.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.EMPTY);
            textView.setText("No Internet Connection");
        }
    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.i("", "Earth quake oncreateLoader()");

        return new EarthQuakeLoader(this, URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        Log.i("", "Earth quake onloadfinished()");

        simpleProgressBar.setVisibility(View.GONE);

        adapter.clear();

        if (earthQuakes != null && !earthQuakes.isEmpty()) {
            adapter.addAll(earthQuakes);
        } else {
            TextView textView = findViewById(R.id.EMPTY);
            textView.setText("No EarthQuake Data Founded");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
        Log.i("", "Earth quake onloaderreset()");

        adapter.clear();
        adapter.addAll(new ArrayList<EarthQuake>());
    }

}

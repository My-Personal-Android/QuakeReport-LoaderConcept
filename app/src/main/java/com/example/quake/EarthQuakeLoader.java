package com.example.quake;

import android.content.Context;
import java.util.List;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {


    private String mUrl;

    public EarthQuakeLoader(Context context,String mUrl) {
        super(context);
        this.mUrl=mUrl;
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.v("","EarthquakeLoader onstartloading()");

        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {

        Log.v(null,"In main EarthQuakeLoader after LoadIN Background is called");

        List<EarthQuake> earthQuakes = QueryList.fetchEarthquakeData(mUrl);
        return earthQuakes;
    }
}



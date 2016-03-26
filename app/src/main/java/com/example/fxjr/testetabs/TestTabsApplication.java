package com.example.fxjr.testetabs;

import java.util.Arrays;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class TestTabsApplication extends Application {

    private final static String TAG = "TestsTabApplication";

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "finishing application");
        DatabaseHelper.closeDatabase();
    }

    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

        Log.d(TAG, "starting application");
		DatabaseHelper.setApplicationContext(getApplicationContext());


//		FilterModel.initFilterModel(
//				Arrays.asList(getResources().getStringArray(R.array.clans)),
//				Arrays.asList(getResources().getStringArray(R.array.types)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
//
		
		
	}

}

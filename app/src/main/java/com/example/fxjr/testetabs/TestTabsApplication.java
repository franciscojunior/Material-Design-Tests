package com.example.fxjr.testetabs;

import java.util.Arrays;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

//        new UpdateDatabaseOperation().execute();


//		FilterModel.initFilterModel(
//				Arrays.asList(getResources().getStringArray(R.array.clans)),
//				Arrays.asList(getResources().getStringArray(R.array.types)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
//
		
		
	}


    private class UpdateDatabaseOperation extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                DatabaseHelper.getDatabase();
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Log.d(TAG, "onPostExecute... ");

            if (result) {





                //theAdapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, "Database Updated", Toast.LENGTH_SHORT).show();
            }
            else {

            }


        }
    }

}

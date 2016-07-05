package com.example.fxjr.testetabs;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CardImageActivity extends AppCompatActivity {

    private static final String TAG = "CardImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_image);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //fullScreen();


        ImageView imageView = (ImageView) findViewById(R.id.cardImage);


        setupToolbarImage(imageView);



    }


    private void setupToolbarImage(ImageView cardImage) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Resources res = getResources();
        cardImage.setImageDrawable(new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.gold_back, options)));
    }

    public void fullScreen() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // BEGIN_INCLUDE (get_current_ui_flags)
            // The UI options currently enabled are represented by a bitfield.
            // getSystemUiVisibility() gives us that bitfield.
            int uiOptions = 0;
            int newUiOptions = uiOptions;
            uiOptions = getWindow().getDecorView().getSystemUiVisibility();

            // END_INCLUDE (get_current_ui_flags)
            // BEGIN_INCLUDE (toggle_ui_flags)
            boolean isImmersiveModeEnabled =
                    ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
            if (isImmersiveModeEnabled) {
                Log.i(TAG, "Turning immersive mode mode off. ");
            } else {
                Log.i(TAG, "Turning immersive mode mode on.");
            }

//            // Navigation bar hiding:  Backwards compatible to ICS.
//            if (Build.VERSION.SDK_INT >= 14) {
//                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            }

            // Status bar hiding: Backwards compatible to Jellybean
            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            // Immersive mode: Backward compatible to KitKat.
            // Note that this flag doesn't do anything by itself, it only augments the behavior
            // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
            // all three flags are being toggled together.
            // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
            // Sticky immersive mode differs in that it makes the navigation and status bars
            // semi-transparent, and the UI flag does not get cleared when the user interacts with
            // the screen.
//            if (Build.VERSION.SDK_INT >= 18) {
//                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            }

            getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            //END_INCLUDE (set_ui_flags)
        }
    }
}
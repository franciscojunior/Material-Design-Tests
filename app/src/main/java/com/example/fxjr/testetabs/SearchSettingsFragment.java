package com.example.fxjr.testetabs;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by francisco on 4/27/16.
 */
public class SearchSettingsFragment extends DialogFragment implements View.OnClickListener {


    public interface SearchSettingsHandler {

        public void filter(String filter);
    }

    private static final String TAG = "SearchSettingsFragment";

    SearchSettingsHandler searchHandler;

    StringBuilder filterText = new StringBuilder();

    CheckBox checkBoxGroup_1;

    public static SearchSettingsFragment newInstance() {
        SearchSettingsFragment frag = new SearchSettingsFragment();

        return frag;
    }

    @Override
    public void onAttach(Activity activity) {

        Log.d(TAG, "onAttach() called with: " + "activity = [" + activity + "]");

        super.onAttach(activity);

        if (activity instanceof SearchSettingsHandler) {
            searchHandler = (SearchSettingsHandler) activity;

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_filter_dialog, container);


        final View disciplinesHeader = v.findViewById(R.id.disciplinesHeader);
        final View disciplinesLayout = v.findViewById(R.id.disciplinesLayout);

        final ImageView imgDisciplinesLayoutArrow = (ImageView) v.findViewById(R.id.imgDisciplinesLayoutArrow);



        disciplinesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disciplinesLayout.isShown()) {
                    disciplinesLayout.setVisibility(View.GONE);
                    imgDisciplinesLayoutArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                } else {

                    imgDisciplinesLayoutArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);


//                    Reference: http://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
                    // Prepare the View for the animation
                    disciplinesLayout.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                        disciplinesLayout.setAlpha(0.0f);

                        // Start the animation
                        disciplinesLayout.animate()
                                .alpha(1.0f);
                    }
                }

            }
        });




        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Search settings");

    }

    @Override
    public void onClick(View v) {

        updateFiltering();

    }

    private void updateFiltering() {

        if (checkBoxGroup_1.isChecked())
            filterText.append("g1");


    }
}

package com.example.fxjr.testetabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

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
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss() called with: " + "dialog = [" + dialog + "]");

        if (searchHandler != null) {
            searchHandler.filter(filterText.toString());
        }
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_filter_dialog, container);

        checkBoxGroup_1 = (CheckBox) v.findViewById(R.id.checkBox1);

        checkBoxGroup_1.setOnClickListener(this);

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

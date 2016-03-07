package com.example.fxjr.testetabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fxjr on 27/02/16.
 */
public class DummyFragment extends Fragment {
    public DummyFragment(int color) {

    }

    public DummyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #");
        return view;
    }
}

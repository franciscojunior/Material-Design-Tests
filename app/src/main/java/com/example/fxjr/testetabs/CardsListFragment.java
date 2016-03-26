package com.example.fxjr.testetabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fxjr on 17/03/16.
 */
public class CardsListFragment extends Fragment{

    private final static String TAG = "CardsListFragment";

    public CursorRecyclerViewAdapter getCardsAdapter() {
        return cardsAdapter;
    }

    private CursorRecyclerViewAdapter cardsAdapter;

    public CardsListFragment() {
    }

    public static CardsListFragment newInstance(int cardType, String listQuery) {
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("CardType", cardType );
        args.putString("ListQuery", listQuery);

        CardsListFragment f = new CardsListFragment();
        f.setArguments(args);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView... ");
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_cards_list, container, false);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // specify an adapter (see also next example)

        int cardType = getArguments().getInt("CardType");
        String query = getArguments().getString("ListQuery");

        SQLiteDatabase db = DatabaseHelper.getDatabase();

        Cursor c = db.rawQuery(query, null);

        if (cardType == 0)
            cardsAdapter = new CryptCardsListViewAdapter(getContext(), c);
        else
            cardsAdapter = new LibraryCardsListViewAdapter(getContext(), c);

        recyclerView.setAdapter(cardsAdapter);

        return recyclerView;
    }
}

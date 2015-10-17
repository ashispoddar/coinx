package com.x.coin.coinx2.views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.x.coin.coinx2.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardListFragment extends Fragment {

    public CardListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_list, container, false);
    }
}

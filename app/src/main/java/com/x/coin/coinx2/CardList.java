package com.x.coin.coinx2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardList extends ActionBarActivity {

    private List<CardInfo> mCardList = new ArrayList<CardInfo>();
    private ListView mCardListView;
    private CardArrayAdapter mCardTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        // Inflate the layout for this fragment
        mCardListView = (ListView)findViewById(R.id.listViewCards);

        CardInfo card = new CardInfo("Ashis Poddar" , "1234 1234 1234 1234" , "12 / 15");
        mCardList.add(card);

        CardInfo card2  = new CardInfo("Venkat Surya" , "9999 9999 9999 9999" , "12 / 16");
        mCardList.add(card2);

        CardInfo card3  = new CardInfo("Kanishk parasar" , "2222 2999 9999 9999" , "12 / 16");
        mCardList.add(card3);

        CardInfo card4 = new CardInfo("John Doe" , "2222 2999 9999 9999" , "12 / 16");
        mCardList.add(card4);

        //mCardList = getCards();

        mCardTableAdapter = new CardArrayAdapter(this, mCardList);

        mCardListView.setAdapter(mCardTableAdapter);

    }

}

package com.x.coin.coinx2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.x.coin.coinx2.model.CardInfo;

import java.util.List;

/**
 * Created by ashispoddar on 10/15/15.
 */
public class CardArrayAdapter extends ArrayAdapter<CardInfo> {
    private Context context;
    private List<CardInfo> card_list;
    //private ManageCardsFragment mParent;
    TextView mCardNumber;
    TextView mCardName;
    TextView mCardExp;

    public CardArrayAdapter(Context context, /*ManageCardsFragment parent,*/ List<CardInfo> plist) {
        super(context, R.layout.fragment_card_row, plist);
        this.context = context;
        this.card_list = plist;
        //this.mParent = parent;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_card_row, parent, false);
        mCardNumber = (TextView) rowView.findViewById(R.id.textViewCardNumber);
        mCardName = (TextView) rowView.findViewById(R.id.textViewName);
        mCardExp = (TextView) rowView.findViewById(R.id.textViewExpiry);

        if (position < card_list.size()) {
            setCard(position);
        }
        return rowView;
    }

    public void setCard(int cardNo) {
        CardInfo card = card_list.get(cardNo);
        mCardName.setText(card.getName());
        mCardExp.setText(card.getExpiry());
        mCardNumber.setText(card.getCardNumber());
    }
}
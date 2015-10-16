package com.x.coin.coinx2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.coin.coinx2.model.CardInfo;

import java.util.List;

/**
 * Created by ashispoddar on 10/15/15.
 */
public class CardArrayAdapter extends ArrayAdapter<CardInfo> {
    private Context context;
    private List<CardInfo> cardInfoList;
    //private ManageCardsFragment mParent;

    LinearLayout mCardLayout;
    TextView mCardNumber;
    TextView mCardName;
    TextView mCardExp;

    public CardArrayAdapter(Context context, /*ManageCardsFragment parent,*/ List<CardInfo> plist) {
        super(context, R.layout.fragment_card_row, plist);
        this.context = context;
        this.cardInfoList = plist;
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
        mCardLayout = (LinearLayout)rowView.findViewById(R.id.ll_cc);

        if (position < cardInfoList.size()) {
            setCard(position);
        }
        return rowView;
    }

    public void setCard(int cardNo) {

        CardInfo card = cardInfoList.get(cardNo);
        mCardName.setText(card.getName());
        mCardExp.setText(card.getExpiry());
        mCardNumber.setText(card.getCardNumber());

        //// TODO: 10/16/15 : dynamica allocation is failing when scrolling, need to debug and fix 
        int cardImageId = R.drawable.card_background_mastercard;
        /*
        switch(card.getCardImage()) {
            case "card_background_visa.png":
                cardImageId = R.drawable.card_background_visa;
                break;
            case "card_background_mastercard.png":
                cardImageId = R.drawable.card_background_mastercard;
                break;
            case "card_background_amex.png":
                cardImageId = R.drawable.card_background_amex;
                break;
            case "card_background_discover.png":
                cardImageId = R.drawable.card_background_discover;
                break;

            default :
                cardImageId = -1;
                break;
        }*/
        if(cardImageId != -1)
            mCardLayout.setBackground(getContext().getResources().getDrawable(cardImageId));
    }
}
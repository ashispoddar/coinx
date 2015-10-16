package com.x.coin.coinx2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.x.coin.coinx2.model.AsyncResult;
import com.x.coin.coinx2.model.CardInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
/*
        CardInfo card = new CardInfo("Ashis Poddar" , "1234 1234 1234 1234" , "12 / 15");
        mCardList.add(card);

        CardInfo card2  = new CardInfo("Venkat Surya" , "9999 9999 9999 9999" , "12 / 16");
        mCardList.add(card2);

        CardInfo card3  = new CardInfo("Kanishk parasar" , "2222 2999 9999 9999" , "12 / 16");
        mCardList.add(card3);

        CardInfo card4 = new CardInfo("John Doe" , "2222 2999 9999 9999" , "12 / 16");
        mCardList.add(card4);
*/
        getCards();

        mCardTableAdapter = new CardArrayAdapter(this, mCardList);

        mCardListView.setAdapter(mCardTableAdapter);

    }
    public List<CardInfo> getCards() {
        List<CardInfo> cards = new ArrayList<CardInfo>();
        CardService async_task = new CardService();
        async_task.execute();
        return cards;
    }
    private class CardService extends AsyncTask<Void, Void, AsyncResult> {

        @Override
        protected AsyncResult doInBackground(Void... params) {
            AsyncResult result = getCardsFromServer();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncResult result) {
            processCardServiceResponse(result);
        }
    }
    void processCardServiceResponse(AsyncResult result){
        if(result.isSuccess()) {

            try {
                JSONObject data = (JSONObject)result.getData();

                if(data != null) {
                    String message  = data.getString("message");
                    JSONArray cards = data.getJSONArray("results");
                    if(cards.length() > 0) {
                        for(int i=0; i< cards.length(); i++){
                            JSONObject card = cards.getJSONObject(i);
                            if(card != null) {
                                String cardNumber = card.getString("card_number");
                                String fName = card.getString("first_name");
                                String lName = card.getString("last_name");
                                String expiryDate = card.getString("expiration_date");
                                String created = card.getString("created");
                                Boolean enbaled = card.getBoolean("enabled");
                                String background_image_url = card.getString("background_image_url");

                                CardInfo cardInfo = new CardInfo(fName, lName, background_image_url,cardNumber,expiryDate);
                                mCardList.add(cardInfo);
                            }
                        }
                        mCardTableAdapter.notifyDataSetChanged();
                    }
                }
            }catch (JSONException e) {
                //ignore the for now
                //// TODO: 10/16/15 add proper error handling
            }
        }
    }
    private AsyncResult getCardsFromServer() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 15000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 15000);
        //// TODO: 10/16/15 : Read from config file
        HttpGet httpGet = new HttpGet("http://s3.amazonaws.com/mobile.coin.vc/ios/assignment/data.json");

        String result = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (ClientProtocolException e) {
            return new AsyncResult(false, e.getCause());
        } catch (IOException e) {
            return new AsyncResult(false, e.getCause());
        }
        JSONObject cardsJSON;

        try {

            cardsJSON = new JSONObject(result);

        } catch (JSONException e) {
            return new AsyncResult(false, e.getCause());
        }
        return new AsyncResult(true, cardsJSON);
    }

}

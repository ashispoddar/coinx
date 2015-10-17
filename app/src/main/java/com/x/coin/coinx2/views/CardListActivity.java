package com.x.coin.coinx2.views;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.x.coin.coinx2.R;
import com.x.coin.coinx2.dal.LocalDBHelper;
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

public class CardListActivity extends ActionBarActivity {

    private List<CardInfo> mCardList = new ArrayList<CardInfo>();
    private ListView mCardListView;
    private CardArrayAdapter mCardTableAdapter;
    private LocalDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        //setup DB if not already, would go to main app
        mDBHelper = new LocalDBHelper(getBaseContext());
        //load cards either from backend or locally
        populateCards();

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        // Inflate the layout for this fragment
        mCardListView = (ListView)findViewById(R.id.listViewCards);

        //set up row adapter
        mCardTableAdapter = new CardArrayAdapter(this, mCardList);
        mCardListView.setAdapter(mCardTableAdapter);

    }
    public void addCard2LocalStorage(CardInfo cardInfo) {

        //use GUID to load before insert , if exist , do an update instead
        CardInfo cardInfoExists = mDBHelper.getCard(cardInfo.getGuid());
        if(cardInfoExists != null)
            mDBHelper.updateCard(cardInfo);
        else
            mDBHelper.insertCard(cardInfo);
    }
    public void populateCards() {
        CardService async_task = new CardService();
        async_task.execute();
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
                                String cardGuid = card.getString("guid");

                                CardInfo cardInfo = new CardInfo(fName, lName, background_image_url,cardNumber,expiryDate);
                                cardInfo.setGuid(cardGuid);

                                mCardList.add(cardInfo);
                                addCard2LocalStorage(cardInfo);
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

        //// TODO: 10/16/15 : Read from config file

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
        //retrieve JSON structure
        JSONObject cardsJSON = null;
        try {

            cardsJSON = new JSONObject(result);

        } catch (JSONException e) {

            return new AsyncResult(false, e.getCause());
        }
        return new AsyncResult(true, cardsJSON);
    }

}
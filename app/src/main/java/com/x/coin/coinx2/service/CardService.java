package com.x.coin.coinx2.service;

import android.os.AsyncTask;

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

/**
 * Created by ashispoddar on 10/17/15.
 */
public class CardService extends AsyncTask<Void, Void, AsyncResult> {

    @Override
    protected AsyncResult doInBackground(Void... params) {
        AsyncResult result = getCardsFromServer();
        return result;
    }

    @Override
    protected void onPostExecute(AsyncResult result) {
        processCardServiceResponse(result);
    }

    void processCardServiceResponse(AsyncResult result) {
        if (result.isSuccess()) {

            try {
                JSONObject data = (JSONObject) result.getData();

                if (data != null) {
                    String message = data.getString("message");
                    JSONArray cards = data.getJSONArray("results");
                    if (cards.length() > 0) {
                        for (int i = 0; i < cards.length(); i++) {
                            JSONObject card = cards.getJSONObject(i);
                            if (card != null) {
                                String cardNumber = card.getString("card_number");
                                String fName = card.getString("first_name");
                                String lName = card.getString("last_name");
                                String expiryDate = card.getString("expiration_date");
                                String created = card.getString("created");
                                Boolean enbaled = card.getBoolean("enabled");
                                String background_image_url = card.getString("background_image_url");
                                String cardGuid = card.getString("guid");

                                CardInfo cardInfo = new CardInfo(fName, lName, background_image_url, cardNumber, expiryDate);
                                cardInfo.setGuid(cardGuid);

                                //// TODO: 10/17/15 :need to pass these variables rather than memeber  
                                //mCardList.add(cardInfo);
                                //addCard2LocalStorage(cardInfo);
                            }
                        }
                        //mCardTableAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
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
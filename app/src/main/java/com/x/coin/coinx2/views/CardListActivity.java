package com.x.coin.coinx2.views;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.coin.coinx2.R;
import com.x.coin.coinx2.dal.LocalDBHelper;
import com.x.coin.coinx2.model.AsyncResult;
import com.x.coin.coinx2.model.CardInfo;
import com.x.coin.coinx2.model.ServiceError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class CardListActivity extends ActionBarActivity {

    private List<CardInfo> mCardList = new ArrayList<CardInfo>();
    private ListView mCardListView;
    private CardArrayAdapter mCardTableAdapter;
    private LocalDBHelper mDBHelper;
    private ProgressBar mSpinner;
    private Context mAppContext;
    final static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        mAppContext = getApplicationContext();
        //setup DB if not already, would go to main app
        mDBHelper = new LocalDBHelper(getBaseContext());
        //mDBHelper.onUpgrade(mDBHelper.getWritableDatabase(),1,2);
        mSpinner = (ProgressBar)findViewById(R.id.progressBar);
        mSpinner.setVisibility(View.INVISIBLE);

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
        try {
            CardInfo cardInfoExists = mDBHelper.getCard(cardInfo.getGuid());
            if (cardInfoExists != null) {
                //check whether timeUpdated is recent than what exist in local db

                Date recentUpdateDate = df1.parse(cardInfo.getTimeUpdated());
                Date lastUpdateDate = df1.parse(cardInfoExists.getTimeUpdated());

                if (recentUpdateDate.after(lastUpdateDate))
                    mDBHelper.updateCard(cardInfo);
            } else {
                mDBHelper.insertCard(cardInfo);
            }
        }catch(Exception e) {
            //// TODO: 10/17/15 handle exception gracefully here
            String errMsg = e.getLocalizedMessage();
        }
    }
    public void populateCards() {
        mSpinner.setVisibility(View.VISIBLE);
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
                                Boolean enabled = card.getBoolean("enabled");
                                String background_image_url = card.getString("background_image_url");
                                String cardGuid = card.getString("guid");
                                String timeCreated = card.getString("created");
                                String timeUpdated = card.getString("updated");

                                CardInfo cardInfo = new CardInfo(fName, lName,
                                        background_image_url,cardNumber,
                                        expiryDate,cardGuid);
                                cardInfo.setTimeCreated(timeCreated);
                                cardInfo.setTimeUpdated(timeUpdated);

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
        }else {
            //service error condition , so we gracefully handle with an alert/toast

            String serviceError = getString(R.string.generic_service_error);
            Toast toast = Toast.makeText(mAppContext, serviceError, Toast.LENGTH_SHORT);
            toast.show();
        }
        mSpinner.setVisibility(View.GONE);
    }
    private AsyncResult getCardsFromServer() {

        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            String cardServiceUrl = getString(R.string.card_service_url);
            URL url = new URL(cardServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //setup the connection object
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //conn.setDoOutput(true);

            //connect the backend service
            conn.connect();

            int statusCode = conn.getResponseCode();
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String result = readIt(is, len);
            JSONObject jsonData = new JSONObject(result);
            if(statusCode == 200) {
                return new AsyncResult(true, jsonData);
            }else {
                //for 400 & 500
                //todo : aysnc result to throw exception if we dont have proper JSON
                return new AsyncResult(false, jsonData);
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            //return service error
            return new AsyncResult(false,new ServiceError(-1,e.getMessage()));

        }finally {

            try {
                if (is != null) {
                    is.close();
                }
            }catch(Exception e){
                //ignore the error here
            }
        }
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

        /*Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
        */

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }
    /*
    private AsyncResult getCardsFromServer() {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            //// TODO: 10/16/15 : Read from config file
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 15000);

            String cardServiceUrl = getString(R.string.card_service_url);
            HttpGet httpGet = new HttpGet(cardServiceUrl);


            HttpResponse httpResponse = httpClient.execute(httpGet);
            String result = EntityUtils.toString(httpResponse.getEntity());
            JSONObject cardsJSON = new JSONObject(result);
            return new AsyncResult(true, cardsJSON);

        } catch (ClientProtocolException e) {

            return new AsyncResult(false, e.getCause());

        } catch (IOException e) {

            return new AsyncResult(false, e.getCause());
        }catch (JSONException e) {

            return new AsyncResult(false, e.getCause());
        }
    }
    */
}

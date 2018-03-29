package com.warpigs_online.ravencointracker;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView txtJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtJson = findViewById(R.id.txtJSON);

        new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/ravencoin/");

    }



    private class JsonTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Log.d("INFO", params[0]);
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jsonarray = new JSONArray(result);

                // Only one array so ZERO!
                JSONObject jsonobject = jsonarray.getJSONObject(0);

                String id       = jsonobject.getString("id");
                int rank    = jsonobject.getInt("rank");
                double price_usd    = jsonobject.getDouble("price_usd");
                double price_btc    = jsonobject.getDouble("price_btc");
                int tfh_volume_usd    = jsonobject.getInt("24h_volume_usd");
                int market_cap_usd    = jsonobject.getInt("market_cap_usd");
                int total_supply    = jsonobject.getInt("total_supply");
                int percent_change_1h    = jsonobject.getInt("percent_change_1h");
                int percent_change_24h    = jsonobject.getInt("percent_change_24h");
                int percent_change_7d    = jsonobject.getInt("percent_change_7d");
                long last_updated    = jsonobject.getLong("last_updated");

                // convert seconds to milliseconds
                Date date = new java.util.Date(last_updated*1000L);
                // the format of your date
                SimpleDateFormat sdf =  new SimpleDateFormat("MMM dd, YYYY hh:mm:ss a",
                        java.util.Locale.getDefault());
                String formattedDate = sdf.format(date);
                System.out.println(formattedDate);

                txtJson.setText("Rank: " + rank + "\n" +
                                "Price: $" + price_usd + "\n" +
                                "Price BTC: " + price_btc + " \n" +
                                "24H Volume: $" + tfh_volume_usd + "\n" +
                                "Market Cap USD: $" + market_cap_usd + "\n" +
                                "Supply: " + total_supply + "/ 21,000,000,000" + "\n" +
                                "Change 1H: " + percent_change_1h + "%" + "\n" +
                                "Change 24H: " + percent_change_24h + "%" + "\n" +
                                "Change 7D: " + percent_change_7d + "%" + "\n" +
                                "Last Updated: " + formattedDate);
            }catch (JSONException e){
                e.printStackTrace();
            }






            Log.d("INFO", "msg: " + result);
        }
    }
}

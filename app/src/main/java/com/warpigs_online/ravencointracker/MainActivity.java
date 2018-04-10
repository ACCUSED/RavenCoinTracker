package com.warpigs_online.ravencointracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static int urlNumber = 0;
    static double combo = 0;
    static boolean brank, bprice, bwallet, btfhour, bmarket, bsupply, bpercent1h, bpercent24h, bpercent7d;
    static String WalletAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtRank = findViewById(R.id.txtRank);
        TextView txtUSD = findViewById(R.id.txtUSD);
        TextView txtCap = findViewById(R.id.txtCap);
        TextView txtVol24 = findViewById(R.id.txtVol24);
        TextView txt1H = findViewById(R.id.txt1h);
        TextView txt24h = findViewById(R.id.txt24h);
        TextView txt7D = findViewById(R.id.txt7D);
        TextView txtSupply = findViewById(R.id.txtSupply);
        TextView txtUpdate = findViewById(R.id.txtUpdate);


        new JsonTask(txtRank, txtUSD, txtSupply, txtCap, txtVol24, txt1H, txt24h, txt7D, txtUpdate).execute("https://api.coinmarketcap.com/v1/ticker/ravencoin/");

        WhatToShow();

    }

    private void WhatToShow() {

        SharedPreferences settings = getSharedPreferences("Settings", 0);
        brank = settings.getBoolean("Rank", true);
        bprice = settings.getBoolean("Price", true);
        bwallet = settings.getBoolean("Wallet", false);
        btfhour = settings.getBoolean("24Hour", true);
        bmarket = settings.getBoolean("Market", true);
        bsupply = settings.getBoolean("Supply", true);
        bpercent1h = settings.getBoolean("Percent1h", true);
        bpercent24h = settings.getBoolean("Percent24h", true);
        bpercent7d = settings.getBoolean("Percent7d", true);
        WalletAddress = settings.getString("walletAddress", "Raven Coin Wallet Address");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                GoToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GoToSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    static private class JsonTask extends AsyncTask<String, String, String> {

        private final WeakReference<TextView> txtRank, txtUSD, txtSupply, txtCap, txtVol24, txt1H, txt24h, txt7D, txtUpdate;

        private JsonTask(TextView txtRank, TextView txtUSD, TextView txtSupply, TextView txtCap, TextView txtVol24, TextView txt1H, TextView txt24h, TextView txt7D, TextView txtUpdate) {

            this.txtRank = new WeakReference<>(txtRank);
            this.txtUSD = new WeakReference<>(txtUSD);
            this.txtSupply = new WeakReference<>(txtSupply);
            this.txtCap = new WeakReference<>(txtCap);
            this.txtVol24 = new WeakReference<>(txtVol24);
            this.txt1H = new WeakReference<>(txt1H);
            this.txt24h = new WeakReference<>(txt24h);
            this.txt7D = new WeakReference<>(txt7D);
            this.txtUpdate = new WeakReference<>(txtUpdate);

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
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

        private String addCommas (String digits) {

            String result = digits;

            if (digits.length() <= 3) return digits; // If the original value has 3 digits or  less it returns that value

            for (int i = 0; i < ((digits.length() - 1) / 3); i++) {

                int commaPos = digits.length() - 3 - (3 * i);

                result = result.substring(0, commaPos) + "," + result.substring(commaPos);
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                if (urlNumber == 0) {

                JSONArray jsonarray = new JSONArray(result);

                // Only one array so ZERO!
                JSONObject jsonobject = jsonarray.getJSONObject(0);


                    int rank = jsonobject.getInt("rank");
                    double price_usd = jsonobject.getDouble("price_usd");
                    //double price_btc    = jsonobject.getDouble("price_btc");
                    int tfh_volume_usd = jsonobject.getInt("24h_volume_usd");
                    int market_cap_usd = jsonobject.getInt("market_cap_usd");
                    int total_supply = jsonobject.getInt("total_supply");
                    long percent_change_1h = jsonobject.getInt("percent_change_1h");
                    long percent_change_24h = jsonobject.getInt("percent_change_24h");
                    long percent_change_7d = jsonobject.getInt("percent_change_7d");
                    long last_updated = jsonobject.getLong("last_updated");

                    // convert seconds to milliseconds
                    Date date = new java.util.Date(last_updated * 1000L);
                    // the format of your date
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, YYYY hh:mm a",
                            java.util.Locale.getDefault());
                    String formattedDate = sdf.format(date);
                    System.out.println(formattedDate);

                    final TextView txtRank = this.txtRank.get();
                    final TextView txtUSD = this.txtUSD.get();
                    final TextView txtSupply = this.txtSupply.get();
                    final TextView txtCap = this.txtCap.get();
                    final TextView txtVol24 = this.txtVol24.get();
                    final TextView txt1H = this.txt1H.get();
                    final TextView txt24h = this.txt24h.get();
                    final TextView txt7D = this.txt7D.get();
                    final TextView txtUpdate = this.txtUpdate.get();

                    combo = price_usd;
                    String tfhC = addCommas("" + tfh_volume_usd);
                    String tsC = addCommas("" + total_supply);
                    String mcuC = addCommas("" + market_cap_usd);

                    txtRank.setText("Rank: " + rank);
                    if (!brank) { txtRank.setVisibility(View.GONE); }
                    else { txtRank.setVisibility(View.VISIBLE); }
                    if(!bprice) { txtUSD.setText(""); }
                    else { txtUSD.setText("$" + price_usd);}
                    if (!btfhour) { txtVol24.setVisibility(View.GONE); }
                    else { txtVol24.setVisibility(View.VISIBLE); }
                    txtVol24.setText("24H Vol\n" + tfhC);
                    if (!btfhour) { txt24h.setVisibility(View.GONE); }
                    else { txt24h.setVisibility(View.VISIBLE); }
                    txt1H.setText("Change 1 Hour\n" + percent_change_1h + "%");
                    if (!bpercent1h) { txt1H.setVisibility(View.GONE); }
                    else { txt1H.setVisibility(View.VISIBLE); }
                    txt24h.setText("Change 24 Hour\n" + percent_change_24h + "%");
                    if (!bpercent24h) { txt24h.setVisibility(View.GONE); }
                    else { txt24h.setVisibility(View.VISIBLE); }
                    txt7D.setText("Change 7 Day\n" + percent_change_7d + "%");
                    if (!bpercent7d) { txt7D.setVisibility(View.GONE); }
                    else { txt7D.setVisibility(View.VISIBLE); }
                    txtCap.setText("Market Cap\n" + mcuC);
                    if (!bmarket) { txtCap.setVisibility(View.GONE); }
                    else { txtCap.setVisibility(View.VISIBLE); }
                    txtSupply.setText("Supply:\n" + tsC + "/\n21,000,000,000");
                    if (!bsupply) { txtSupply.setVisibility(View.GONE); }
                    else { txtSupply.setVisibility(View.VISIBLE); }
                    txtUpdate.setText(formattedDate);

                    if (txt1H.getText().toString().contains("-")) {
                        txt1H.setTextColor(Color.RED);
                        txtUSD.setTextColor(Color.RED);
                    } else {
                        txt1H.setTextColor(Color.GREEN);
                        txtUSD.setTextColor(Color.GREEN);
                    }
                    if (txt24h.getText().toString().contains("-")) {
                        txt24h.setTextColor(Color.RED);
                    } else txt24h.setTextColor(Color.GREEN);
                    if (txt7D.getText().toString().contains("-")) {
                        txt7D.setTextColor(Color.RED);
                    } else txt7D.setTextColor(Color.GREEN);

                    urlNumber = 1;
                    // To get amount use has.
                    // http://explorer.threeeyed.info/ext/getaddress/
                    new JsonTask(txtRank, txtUSD, txtSupply, txtCap, txtVol24, txt1H, txt24h, txt7D, txtUpdate).execute("http://explorer.threeeyed.info/ext/getaddress/" + WalletAddress);

                } else {

                    JSONObject jsonobject = new JSONObject(result);

                    double price_usd = jsonobject.getDouble("balance");
                    final TextView txtUSD = this.txtUSD.get();

                    combo = combo * price_usd;
                    // Round it
                    combo = Math.round(combo * 100.0) / 100.0;

                    if (bwallet && bprice) { txtUSD.setText(txtUSD.getText() + "\n$" + combo); }
                    if (bwallet && !bprice) { txtUSD.setText(txtUSD.getText() + "$" + combo); }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}

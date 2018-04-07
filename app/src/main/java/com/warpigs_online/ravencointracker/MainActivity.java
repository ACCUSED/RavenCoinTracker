package com.warpigs_online.ravencointracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
                Log.d("INFO", "GO TO SETTINGS");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    Log.d("Response: ", "> " + line);

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
                    txtUSD.setText("$" + price_usd);
                    txtVol24.setText("24H Vol\n" + tfhC);
                    txt1H.setText("Change 1 Hour\n" + percent_change_1h + "%");
                    txt24h.setText("Change 24 Hour\n" + percent_change_24h + "%");
                    txt7D.setText("Change 7 Day\n" + percent_change_7d + "%");
                    txtCap.setText("Market Cap\n" + mcuC);
                    txtSupply.setText("Supply:\n" + tsC + "/\n21,000,000,000");
                    txtUpdate.setText(formattedDate);

                    if (txt1H.getText().toString().contains("-")) {
                        txt1H.setTextColor(Color.RED);
                        txtUSD.setTextColor(Color.RED);
                    } else txt1H.setTextColor(Color.GREEN);
                    txtUSD.setTextColor(Color.GREEN);
                    if (txt24h.getText().toString().contains("-")) {
                        txt24h.setTextColor(Color.RED);
                    } else txt24h.setTextColor(Color.GREEN);
                    if (txt7D.getText().toString().contains("-")) {
                        txt7D.setTextColor(Color.RED);
                    } else txt7D.setTextColor(Color.GREEN);

                    urlNumber = 1;
                    // To get amount use has.
                    // http://explorer.threeeyed.info/ext/getaddress/
                    new JsonTask(txtRank, txtUSD, txtSupply, txtCap, txtVol24, txt1H, txt24h, txt7D, txtUpdate).execute("http://explorer.threeeyed.info/ext/getaddress/RPkJsE3vU5kvjNUUSBhVrdeoG9krR1vf2Y");

                } else {

                    JSONObject jsonobject = new JSONObject(result);

                    Log.d("info","I ran");
                    double price_usd = jsonobject.getDouble("balance");
                    final TextView txtUSD = this.txtUSD.get();

                    combo = combo * price_usd;
                    // Round it
                    combo = Math.round(combo * 100.0) / 100.0;

                    txtUSD.setText(txtUSD.getText() + "\n$" + combo);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}

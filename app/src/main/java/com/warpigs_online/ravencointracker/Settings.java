package com.warpigs_online.ravencointracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Prevents keyboard from opening because the Edit text.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Switch sRank = findViewById(R.id.sRank);
        Switch sPrice = findViewById(R.id.sPrice);
        Switch sWallet = findViewById(R.id.sWallet);
        Switch stfhour = findViewById(R.id.stfhour);
        Switch sMarket = findViewById(R.id.sMarket);
        Switch sSupply = findViewById(R.id.sSupply);
        Switch sPercent1h = findViewById(R.id.sPercent1h);
        Switch sPercent7d = findViewById(R.id.sPercent7d);
        Switch sPercent24h = findViewById(R.id.sPercent24h);
        final EditText eWallet = findViewById(R.id.eWallet);

        Button saveButton = findViewById(R.id.bSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences settings = getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("walletAddress", eWallet.getText().toString());
                editor.apply();
            }
        });

        SharedPreferences settings = getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = settings.edit();

        sRank.setChecked(settings.getBoolean("Rank", true));
        sPrice.setChecked(settings.getBoolean("Price", true));
        sWallet.setChecked(settings.getBoolean("Wallet", false));
        stfhour.setChecked(settings.getBoolean("24Hour", true));
        sMarket.setChecked(settings.getBoolean("Market", true));
        sSupply.setChecked(settings.getBoolean("Supply", true));
        sPercent1h.setChecked(settings.getBoolean("Percent1h", true));
        sPercent7d.setChecked(settings.getBoolean("Percent24h", true));
        sPercent24h.setChecked(settings.getBoolean("Percent7d", true));
        eWallet.setText(settings.getString("walletAddress", "Raven Coin Wallet Address"));

        sRank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Rank", true);
                } else {
                    editor.putBoolean("Rank", false);
                }
                editor.apply();
            }
        });

        sPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Price", true);
                } else {
                    editor.putBoolean("Price", false);
                }
                editor.apply();
            }
        });

        sWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Wallet", true);
                } else {
                    editor.putBoolean("Wallet", false);
                }
                editor.apply();
            }
        });

        stfhour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("24Hour", true);
                } else {
                    editor.putBoolean("24Hour", false);
                }
                editor.apply();
            }
        });

        sMarket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Market", true);
                } else {
                    editor.putBoolean("Market", false);
                }
                editor.apply();
            }
        });

        sSupply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Supply", true);
                } else {
                    editor.putBoolean("Supply", false);
                }
                editor.apply();
            }
        });

        sPercent1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Percent1h", true);
                } else {
                    editor.putBoolean("Percent1h", false);
                }
                editor.apply();
            }
        });

        sPercent7d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Percent24h", true);
                } else {
                    editor.putBoolean("Percent24h", false);
                }
                editor.apply();
            }
        });

        sPercent24h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Percent7d", true);
                } else {
                    editor.putBoolean("Percent7d", false);
                }
                editor.apply();
            }
        });



    }

}

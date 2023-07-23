package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;

public class CurrencyActivity extends AppCompatActivity {

    ImageView convertButton, depositButton, withdrawButton;
    TextView showAmount;
    EditText currencyFrom, currencyTo, amountInput;
    ActivityCurrencyBinding currencyBinding;
    Set<String> currenciesList = CurrenciesList.currenciesList;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyBinding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        setSupportActionBar(currencyBinding.toolbar);

        queue = Volley.newRequestQueue(this);

        // binding corresponding View items
        currencyFrom = currencyBinding.currencyFrom;
        currencyTo = currencyBinding.currencyTo;
        amountInput = currencyBinding.amountFrom;
        showAmount = currencyBinding.amountTo;

        // get the saved values in shared preferences
        SharedPreferences prefs = getSharedPreferences("MyCurrencyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        prefs.getString("CurrencyFrom", "");
        prefs.getString("CurrencyTo", "");
        prefs.getString("AmountFrom", "");

        // set the saved shared preferences to show up while reopening the app
        currencyFrom.setText(prefs.getString("CurrencyFrom", ""));
        currencyTo.setText(prefs.getString("CurrencyTo", ""));
        amountInput.setText(prefs.getString("AmountFrom",""));

        // binding corresponding buttons
        depositButton = currencyBinding.deposit;
        withdrawButton = currencyBinding.withdraw;
        convertButton = currencyBinding.convertButtonImage;

        /*depositButton.setOnClickListener( click -> {
            String curFrom = currencyFrom.getText().toString();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, "Please input a correct original currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }

        });*/

        /*withdrawButton.setOnClickListener( click -> {
            String curTo = currencyTo.getText().toString();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, "Please input a correct target currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyTo", curTo);
                editor.apply();
            }
        });*/

        // set the retrieved value from API to show on screen
        convertButton.setOnClickListener( click -> {
            // check the input currency code before passing the data to connect API
            String curFrom = currencyFrom.getText().toString().toUpperCase();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, "Please input a correct original currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }

            // check the input currency code before passing the data to connect API
            String curTo = currencyTo.getText().toString().toUpperCase();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, "Please input a correct target currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyTo", curTo);
                editor.apply();
            }

            String amtFrom = amountInput.getText().toString();
            editor.putString("AmountFrom", amtFrom);
            editor.apply();

            // start to retrieve data through API
            String apiKey = "c5172e86dcc4fa44ea0e08ff0d4c8a2522995839";

            String url = null;
            try {
                url = "https://api.getgeoapi.com/v2/currency/convert?api_key=" + apiKey +
                        "&from=" + curFrom +
                        "&to=" + curTo +
                        "&amount=" + amtFrom +
                        "&format=json";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            JSONObject curFromInput = rates.getJSONObject(curTo);
                            String amtTo = curFromInput.getString("rate_for_amount");

                            showAmount.setText(amtTo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {});

            queue.add(request);
        });
    }

    private boolean checkCurrency(String currency) {
        String convertedCurrency = currency.toUpperCase();

        if (currenciesList.contains(convertedCurrency)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_currency));
            builder.setPositiveButton("OK", ((dialog, click) -> {}));
            builder.setNegativeButton("", ((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(CurrencyActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(CurrencyActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(CurrencyActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(showAmount, getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }

}
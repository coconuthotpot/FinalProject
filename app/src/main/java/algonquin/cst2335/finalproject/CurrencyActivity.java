package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;

public class CurrencyActivity extends AppCompatActivity {

    ImageView convertButton, depositButton, withdrawButton;

    TextView showAmount;
    EditText currencyFrom, currencyTo, amountInput;
    ActivityCurrencyBinding currencyBinding;
    Set<String> currenciesList = CurrenciesList.currenciesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyBinding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        setSupportActionBar(currencyBinding.toolbar);

        currencyFrom = currencyBinding.currencyFrom;
        currencyTo = currencyBinding.currencyTo;
        amountInput = currencyBinding.amountFrom;
        showAmount = currencyBinding.amountTo;

        SharedPreferences prefs = getSharedPreferences("MyCurrencyData", Context.MODE_PRIVATE);
        prefs.getString("CurrencyFrom", "");
        prefs.getString("CurrencyTo", "");
        prefs.getString("AmountFrom", "");

        currencyFrom.setText(prefs.getString("CurrencyFrom", ""));
        currencyTo.setText(prefs.getString("CurrencyTo", ""));
        amountInput.setText(prefs.getString("AmountFrom",""));

        SharedPreferences.Editor editor = prefs.edit();

        depositButton = currencyBinding.deposit;
        withdrawButton = currencyBinding.withdraw;
        convertButton = currencyBinding.convertButtonImage;

        depositButton.setOnClickListener( click -> {
            String curFrom = currencyFrom.getText().toString();
            if (checkCurrency(curFrom) == false) {
                Snackbar.make(currencyFrom, "Please input a correct original currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyFrom", curFrom);
                editor.apply();
            }
        });


        withdrawButton.setOnClickListener( click -> {
            String curTo = currencyTo.getText().toString();
            if (checkCurrency(curTo) == false) {
                Snackbar.make(currencyTo, "Please input a correct target currency", Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("CurrencyTo", curTo);
                editor.apply();
            }
        });

        convertButton.setOnClickListener( click -> {
            String amtFrom = amountInput.getText().toString();
            editor.putString("AmountFrom", amtFrom);
            editor.apply();

            showAmount.setText(amtFrom);
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

        builder.setMessage("Change Page?");
        builder.setTitle("Stay or Go");

        if (item.getItemId() == R.id.aviation) {
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent aviationPage = new Intent(CurrencyActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent triviaPage = new Intent(CurrencyActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            Toast.makeText(this, "You are at the current page", Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent bearPage = new Intent(CurrencyActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(showAmount, "Mail Us to Address: 1234 Woodroffe Ave", Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, "4 Modules Android App", Toast.LENGTH_LONG).show();
        }

        return true;
    }

}
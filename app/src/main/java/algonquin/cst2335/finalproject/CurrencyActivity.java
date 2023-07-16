package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;

public class CurrencyActivity extends AppCompatActivity {

    ImageView convertButton;

    ActivityCurrencyBinding currencyBinding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyBinding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        setSupportActionBar(currencyBinding.toolbar);

        convertButton = currencyBinding.convertButtonImage;

        convertButton.setOnClickListener( click -> {
            Toast.makeText(this, "Clicked on Conversion", Toast.LENGTH_LONG).show();
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.aviation) {
            Intent aviationPage = new Intent(CurrencyActivity.this, AviationActivity.class);
            startActivity(aviationPage);
        } else if (item.getItemId() == R.id.quiz) {
            Intent triviaPage = new Intent(CurrencyActivity.this, TriviaActivity.class);
            startActivity(triviaPage);
        } else if (item.getItemId() == R.id.currency) {
            Toast.makeText(this, "You are at the current page", Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.bear) {
            Intent bearPage = new Intent(CurrencyActivity.this, BearActivity.class);
            startActivity(bearPage);
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, "4 Modules Android App", Toast.LENGTH_LONG).show();
        }

        return true;
    }

}
package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

/**
 * Class to load launching page of the app
 * @author Ka Yan Ieong
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * a binding object for connect layout elements to this MainActivity class for app logic implementation
     */
    ActivityMainBinding binding;

    /**
     * ImageView objects to bind with corresponding layout elements
     */
    ImageView aviation, trivia, currency, bear;

    /**
     * "main" method to start the app
     * everything starts from here
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.mainToolbar);

        aviation = findViewById(R.id.aviationIcon);
        aviation.setOnClickListener( click -> {
            Intent aviationPage = new Intent(MainActivity.this, AviationActivity.class);
            startActivity(aviationPage);
        });

        trivia = findViewById(R.id.quizIcon);
        trivia.setOnClickListener( click -> {
            Intent triviaPage = new Intent(MainActivity.this, TriviaActivity.class);
            startActivity(triviaPage);
        });

        currency = findViewById(R.id.currencyIcon);
        currency.setOnClickListener( click -> {
            Intent currencyPage = new Intent(MainActivity.this, CurrencyActivity.class);
            startActivity(currencyPage);
        });

        bear = findViewById(R.id.bearIcon);
        bear.setOnClickListener( click -> {
            Intent bearPage = new Intent(MainActivity.this, BearActivity.class);
            startActivity(bearPage);
        });
    }

    /**
     * method to start option menu along with the app launched
     * @param menu The options menu in which you place your items.
     *
     * @return true to show options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * method to decide which option user picked and take action with corresponding option selected
     * @param item The menu item that was selected.
     *
     * @return true to let user successfully select the option
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_main));
            builder.setPositiveButton(getString(R.string.ok),((dialog, click) -> {}));
            builder.setNegativeButton("",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(MainActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(MainActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent currencyPage = new Intent(MainActivity.this, CurrencyActivity.class);
                startActivity(currencyPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(MainActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(aviation, getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }
}
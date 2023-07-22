package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ImageView aviation, trivia, currency, bear;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle("Page Navigation");
            builder.setMessage("Click any module you need\n" +
                    "Can change to other page by clicking top right help bar");
            builder.setPositiveButton("OK",((dialog, click) -> {}));
            builder.setNegativeButton("",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage("Change Page?");
            builder.setTitle("Stay or Go");
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent aviationPage = new Intent(MainActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage("Change Page?");
            builder.setTitle("Stay or Go");
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent triviaPage = new Intent(MainActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            builder.setMessage("Change Page?");
            builder.setTitle("Stay or Go");
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent currencyPage = new Intent(MainActivity.this, CurrencyActivity.class);
                startActivity(currencyPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage("Change Page?");
            builder.setTitle("Stay or Go");
            builder.setPositiveButton("Yes",((dialog, click) -> {
                Intent bearPage = new Intent(MainActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton("No",((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(aviation, "Mail Us to Address: 1234 Woodroffe Ave", Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, "4 Modules Android App", Toast.LENGTH_LONG).show();
        }

        return true;
    }
}
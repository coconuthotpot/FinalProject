package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ImageView aviation, trivia, currency, bear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AviationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_TEXT = "textInput";
    private EditText airportCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation);

        airportCode = findViewById(R.id.editText);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = prefs.getString(KEY_TEXT, "");
        airportCode.setText(savedText);

        Button showToastButton = findViewById(R.id.button);

        showToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AviationActivity.this, "This is a Toast notification", Toast.LENGTH_SHORT).show();

                String inputText = airportCode.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(KEY_TEXT, inputText);
                editor.apply();
            }
        });


        Button secondPageButton = findViewById(R.id.button4);
        secondPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AviationActivity.this, AviationActivity2.class);
                startActivity(intent);
            }
        });

    }
}
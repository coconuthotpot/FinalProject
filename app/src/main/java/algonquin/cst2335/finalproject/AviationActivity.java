package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.ActivityAviationBinding;
import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class AviationActivity extends AppCompatActivity {
    ActivityAviationBinding binding;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_TEXT = "textInput";
    private EditText airportCode;

    private static final String BASE_URL = "https://api.aviationstack.com/v1/flights";
    private static final String ACCESS_KEY = "ed55a3cfd604d3ba99660f9d1d88c323";
    private static final String AIRPORT_CODE_KEY = "dep_iata";

    private RecyclerView recyclerView;
    /*
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAviationBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_aviation);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*
        flightList = new ArrayList<>();
        flightAdapter = new FlightAdapter(this, flightList);
        recyclerView.setAdapter(flightAdapter);*/


        airportCode = findViewById(R.id.editText);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = prefs.getString(KEY_TEXT, "");
        airportCode.setText(savedText);

        Button searchButton = findViewById(R.id.button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText = airportCode.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(KEY_TEXT, inputText);
                editor.apply();
/*
                if (!TextUtils.isEmpty(inputText)) {
                    fetchFlights(inputText);
                } else {
                    Toast.makeText(AviationActivity.this, "Please enter an airport code.", Toast.LENGTH_SHORT).show();
                }

 */
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
/*
    private void fetchFlights(String airportCode) {
        String url = BASE_URL + "?access_key=" + ACCESS_KEY + "&" + AIRPORT_CODE_KEY + "=" + airportCode;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Parse the JSON response and populate the flightList
                    flightList.clear();
                    JSONArray flightsArray = response.optJSONArray("data");
                    if (flightsArray != null) {
                        for (int i = 0; i < flightsArray.length(); i++) {
                            JSONObject flightObject = flightsArray.optJSONObject(i);
                            if (flightObject != null) {
                                String destination = flightObject.optString("flight_date");
                                String terminal = flightObject.optString("departure");
                                String gate = flightObject.optString("arrival");
                                String delay = flightObject.optString("flight_status");
                                Flight flight = new Flight(destination, terminal, gate, delay);
                                flightList.add(flight);
                            }
                        }
                        flightAdapter.notifyDataSetChanged();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error retrieving flights: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }
*/

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(AviationActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_flight));
            builder.setPositiveButton("OK", ((dialog, click) -> {}));
            builder.setNegativeButton("", ((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.quiz) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(AviationActivity.this, TriviaActivity.class);
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(AviationActivity.this, BearActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.currency) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(AviationActivity.this, CurrencyActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            Snackbar.make(binding.getRoot(), getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }
}
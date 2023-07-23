package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AviationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_TEXT = "textInput";
    private EditText airportCode;

    private static final String BASE_URL = "https://api.aviationstack.com/v1/flights";
    private static final String ACCESS_KEY = "ed55a3cfd604d3ba99660f9d1d88c323";
    private static final String AIRPORT_CODE_KEY = "dep_iata";

    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightList = new ArrayList<>();
        flightAdapter = new FlightAdapter(this, flightList);
        recyclerView.setAdapter(flightAdapter);


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
                if (!TextUtils.isEmpty(inputText)) {
                    fetchFlights(inputText);
                } else {
                    Toast.makeText(AviationActivity.this, "Please enter an airport code.", Toast.LENGTH_SHORT).show();
                }
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
}
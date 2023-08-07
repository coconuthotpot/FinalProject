package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import algonquin.cst2335.finalproject.databinding.ActivityAviationBinding;

/**
 * This activity displays aviation flight information using an external API.
 * It allows the user to search for flights by airport code and view flight details.
 * @author Ying Tu
 * @version 1.0
 */
public class AviationActivity extends AppCompatActivity {

    /**
     * Declare the binding
     */
    ActivityAviationBinding binding;

    /**
     * Constants for SharedPreferences
     */
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_TEXT = "textInput";

    /**
     * Views and variables
     */
    protected EditText airportCode;
    protected RequestQueue queue = null;
    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;

    /**
     * Called when the activity is created.
     * Initializes the layout, views, and RecyclerView.
     * Retrieves previously saved input text from SharedPreferences.
     * Sets up the search and show buttons' click listeners.
     *
     * @param savedInstanceState The saved instance state bundle (not used here).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityAviationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        airportCode = findViewById(R.id.editText);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = prefs.getString(KEY_TEXT, "");
        airportCode.setText(savedText);

        queue = Volley.newRequestQueue(this);

        //set RecyclerView
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        setSupportActionBar(binding.myToolbar);

        flightList = new ArrayList<>();
        //bind RecyclerView
        flightAdapter = new FlightAdapter(flightList, this::onItemClick);
        recyclerView.setAdapter(flightAdapter);

        Button searchButton = findViewById(R.id.search);
        Button showButton = findViewById(R.id.showList);

        //show in RecycleView -ItemView
        searchButton.setOnClickListener(view -> {

            String inputText = airportCode.getText().toString();
            Log.d("AviationActivity", "Input Text: " + inputText);
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(KEY_TEXT, inputText);
            editor.apply();

           String url ="http://api.aviationstack.com/v1/flights?access_key=5f48cb2845197a1cb2ffa193112e7920&dep_iata="
                   + URLEncoder.encode(inputText);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        Log.d("AviationActivity", "Response: " + response.toString());
                        try {
                            JSONArray data=response.getJSONArray("data");
                            Log.d("AviationActivity", "Data Array: " + data.toString());

                            flightList.clear();
                            int len=data.length();
                            for (int i = 0; i < 5; i++) {
                                JSONObject thisObj = data.getJSONObject (i) ;
                                Log.d("AviationActivity", "Flight Object " + i + ": " + thisObj.toString());

                                JSONObject departure = thisObj.getJSONObject( "departure");
                                JSONObject arrival = thisObj.getJSONObject( "arrival");
                                JSONObject flight = thisObj.getJSONObject( "flight");
                                String flightNumber=flight.getString("number");
                                String departure_airport = departure.getString("airport");
                                String destination_airport = arrival.getString("airport");
                                String departure_terminal = departure.getString("terminal");
                                String gate = departure.getString(  "gate");
                                int delay = departure.isNull("delay") ? 0 : departure.getInt("delay");

                                Flight flightObject = new Flight(flightNumber, departure_airport,destination_airport,departure_terminal,gate,delay);
                                flightList.add(flightObject);
                            }
                            runOnUiThread(() -> {
                                flightAdapter.notifyDataSetChanged();
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AviationActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }

                    }, // call this for success
                    (error)-> {
                        Log.e("AviationActivity", "Error: " + error.getMessage());
                        Toast.makeText(AviationActivity.this, "Request failed:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    } );
            // call this for error
            queue.add(request); //send request to server

        });

        showButton.setOnClickListener(view -> {

            Intent intent = new Intent(AviationActivity.this, AviationActivity2.class);
            startActivity(intent);

        });

    }

    /**
     * Handles the click event when a flight item in the RecyclerView is clicked.
     * Opens a new FlightDetailsFragment to display the selected flight details.
     *
     * @param flight The Flight object representing the selected flight.
     */

    public void onItemClick(Flight flight) {
        // Create a new instance of FlightDetailsFragment and pass the selected flight details
        FlightDetailsFragment fragment = new FlightDetailsFragment(flight.getFlightDetails());

        // Use FragmentManager to add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation, fragment)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Initializes the options menu (toolbar menu) of the activity.
     *
     * @param menu The menu to be initialized.
     * @return true to display the menu; false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * Handles the selection of an item from the options menu (toolbar menu).
     * Shows different dialogs or navigates to other activities based on the selected menu item.
     *
     * @param item The selected menu item.
     * @return true if the menu item is handled; false otherwise.
     */
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
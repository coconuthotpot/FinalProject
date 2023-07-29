package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.databinding.ActivityAviationBinding;
import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class AviationActivity extends AppCompatActivity {
    ActivityAviationBinding binding;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_TEXT = "textInput";
    protected EditText airportCode;
    protected RequestQueue queue = null;
    private RecyclerView recyclerView;

    private FlightAdapter flightAdapter;
    private List<Flight> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation);

        airportCode = findViewById(R.id.editText);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = prefs.getString(KEY_TEXT, "");
        airportCode.setText(savedText);

        flightList = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);

        //set RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding = ActivityAviationBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);
        setContentView(binding.getRoot());

        Button searchButton = findViewById(R.id.button);

        //bind RecyclerView
        flightAdapter = new FlightAdapter(flightList, flight -> {
            // 处理航班列表项点击事件
            Toast.makeText(AviationActivity.this, "Click flight" + flight.getFlightNumber(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(flightAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText = airportCode.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(KEY_TEXT, inputText);
                editor.apply();

               String url = "https://api.aviationstack.com/v1/flights?access_key="
                       + "edf8ac1275ff1eef1c96f37cbc64083"+"&dep_iata="
                       + inputText.toUpperCase();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                JSONArray data=response.getJSONArray("data");
                                int len=data.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject thisObj = data.getJSONObject (i) ;
                                    JSONObject departure = thisObj.getJSONObject( "departure");
                                    JSONObject arrival = thisObj.getJSONObject( "arrival");
                                    JSONObject flight = thisObj.getJSONObject( "flight");
                                    String flightNumber=flight.getString("number");
                                    String departure_airport = departure.getString("airport");
                                    String destination_airport = arrival.getString("airport");
                                    String terminal = departure.getString("terminal");
                                    String status = thisObj.getString("flight_status");
                                    String gate = departure.getString(  "gate");
                                    int delay= departure.getInt("delay");

                                    Flight flightObject = new Flight(flightNumber, destination_airport);
                                    flightList.add(flightObject);
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            flightAdapter.notifyDataSetChanged();

                        }, // call this for success
                        (error)-> {
                            Toast.makeText(AviationActivity.this, "Request failed:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } );
                // call this for error
                queue.add(request); //send request to server

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
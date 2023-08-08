package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * This activity displays a list of saved aviation flight details from a local database.
 * It extends the {@link AviationActivity} class and inherits its functionality.
 * The list of flight details is displayed using a RecyclerView with a custom adapter.
 * When a flight item in the RecyclerView is clicked, it opens a new {@link FlightDetailsFragment2}
 * to display the selected flight details.
 * @author Ying Tu
 * @version 1.0
 */
public class AviationActivity2 extends AviationActivity{

    /**
     * Called when the activity is created.
     * Initializes the layout, RecyclerView, and database.
     * Retrieves all flight details from the local database and displays them in the RecyclerView.
     *
     * @param savedInstanceState The saved instance state bundle (not used here).
     */
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation2);

        RecyclerView recyclerView2 = findViewById(R.id.recycleView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            FlightDatabase database = FlightDatabase.getInstance(AviationActivity2.this);
            FlightDetailsDAO flightDetailsDao = database.fDAO();
            List<FlightDetails> flightDetailsList = flightDetailsDao.getAllFlight(); // 获取所有航班信息

            runOnUiThread(() -> {
                FlightAdapter2 adapter2 = new FlightAdapter2(flightDetailsList, this::onItemClick);
                recyclerView2.setAdapter(adapter2);
            });
        }).start();

    }

    /**
     * Handles the click event when a flight item in the RecyclerView is clicked.
     * Opens a new FlightDetailsFragment2 to display the selected flight details.
     *
     * @param flightDetails The FlightDetails object representing the selected flight details.
     */
    public void onItemClick(FlightDetails flightDetails) {
        // Create a new instance of FlightDetailsFragment and pass the selected flight details
        FlightDetailsFragment2 fragment = new FlightDetailsFragment2(flightDetails.getFlightDetails());

        // Use FragmentManager to add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation2, fragment)
                .addToBackStack(null)
                .commit();
    }
}


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

public class AviationActivity2 extends AviationActivity{

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


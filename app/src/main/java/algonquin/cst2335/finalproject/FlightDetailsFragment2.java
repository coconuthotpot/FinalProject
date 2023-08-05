package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.databinding.ActivityAviation2Binding;
import algonquin.cst2335.finalproject.databinding.FlightDetailsLayout2Binding;
import algonquin.cst2335.finalproject.databinding.FlightDetailsLayoutBinding;

public class FlightDetailsFragment2 extends Fragment {

    FlightDetails selected;
    private FlightDetailsLayout2Binding binding; // Add this class-level member for binding

    private FlightAdapter2 flightAdapter2;
    public FlightDetailsFragment2(FlightDetails flightDetails) {
        selected=flightDetails;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FlightDetailsLayout2Binding.inflate(inflater);

        flightAdapter2 = new FlightAdapter2(new ArrayList<>(), this::onItemClick);

        binding.flightNumber.setText ("Flight Number:"+selected.number );
        binding.departureAirport.setText("Departure Airport:"+selected.departure_airport);
        binding.arrivalAirport.setText("Destination Airport:"+selected.destination_airport);
        binding.terminal.setText("Departure Terminal:"+selected.departure_terminal);
        binding.gate.setText("Gate:"+selected.gate);
        binding.delay.setText("Delay Time:"+String.valueOf(selected.delay));

        binding.deleteButton.setOnClickListener(v -> {

            deleteFlightDetailsToDatabase(selected);
        });

        return binding.getRoot ();
    }
    private void deleteFlightDetailsToDatabase(FlightDetails flightDetails) {


        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this flight information?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Execute the deletion when the user clicks the confirmation button

            new Thread(() -> {
                FlightDatabase database = FlightDatabase.getInstance(getContext());
                FlightDetailsDAO flightDetailsDao = database.fDAO();
                flightDetailsDao.deleteFlight(flightDetails); // Delete the flight information
                List<FlightDetails> updatedFlightDetailsList = flightDetailsDao.getAllFlight();

                // Notify the adapter to update the data set with the latest information and refresh the RecyclerView
                flightAdapter2.setData(updatedFlightDetailsList);

                // Show the "Undo" Snackbar message on the main thread
                getActivity().runOnUiThread(() -> {

                    Snackbar.make(binding.getRoot(), "Flight information has been deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", view -> {
                                flightAdapter2.addFlight(selected);
                                // Execute the "Undo" action when the user clicks the "Undo" button to restore the deleted flight information
                                new Thread(() -> {
                                    FlightDatabase undoDatabase = FlightDatabase.getInstance(getContext());
                                    FlightDetailsDAO undoFlightDetailsDao = undoDatabase.fDAO();
                                    undoFlightDetailsDao.insertFlight(flightDetails);
                                    List<FlightDetails> undoFlightDetailsList = flightDetailsDao.getAllFlight();

                                    // Notify the adapter to update the data set with the latest information and refresh the RecyclerView
                                    flightAdapter2.setData(undoFlightDetailsList);// Insert the flight information
                                }).start();
                            })
                            .show();
                });
            }).start();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Execute this when the user clicks the "Cancel" button to cancel the deletion operation
            dialog.dismiss();
        });
        builder.show();
    }
    public void onItemClick(FlightDetails flightDetails) {
        // Create a new instance of FlightDetailsFragment and pass the selected flight details
        FlightDetailsFragment2 fragment = new FlightDetailsFragment2(flightDetails.getFlightDetails());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation2, fragment) // Replace "R.id.fragmentContainer" with the ID of the container where you want to display the new fragment
                .addToBackStack(null)
                .commit();
    }

}

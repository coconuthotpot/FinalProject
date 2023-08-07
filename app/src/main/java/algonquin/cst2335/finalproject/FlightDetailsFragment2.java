package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.databinding.FlightDetailsLayout2Binding;

/**
 * A Fragment class to display the details of a flight.
 * It takes a FlightDetails object as input and displays its information in a layout.
 * Users can delete the flight details from the database by clicking the "Delete" button.
 * An AlertDialog is shown to confirm the deletion, and a Snackbar is displayed to offer an "Undo" option.
 * If the user chooses to undo the deletion, the deleted flight details are restored to the database.
 * @author Ying Tu
 * @version 1.0
 */
public class FlightDetailsFragment2 extends Fragment {

    // The selected FlightDetails object to display
    FlightDetails selected;
    // ArrayList to store the FlightDetails objects for the RecyclerView
    ArrayList<FlightDetails> flightDetails;

    // Add this class-level member for binding
    private FlightDetailsLayout2Binding binding; // Add this class-level member for binding

    // Adapter for the RecyclerView to display the list of flight details
    private FlightAdapter2 flightAdapter2;

    /**
     * Constructs a new FlightDetailsFragment2 with the given FlightDetails object.
     *
     * @param flightDetails The FlightDetails object to display.
     */
    public FlightDetailsFragment2(FlightDetails flightDetails) {
        selected=flightDetails;
    }

    /**
     * Called to create the view for the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using binding
        binding = FlightDetailsLayout2Binding.inflate(inflater);

        // Create a new FlightAdapter2 with an empty ArrayList and set the item click listener
        flightAdapter2 = new FlightAdapter2(new ArrayList<>(), this::onItemClick);

        // Display the flight details in the layout
        binding.flightNumber.setText ("Flight Number:"+selected.number );
        binding.departureAirport.setText("Departure Airport:"+selected.departure_airport);
        binding.arrivalAirport.setText("Destination Airport:"+selected.destination_airport);
        binding.terminal.setText("Departure Terminal:"+selected.departure_terminal);
        binding.gate.setText("Gate:"+selected.gate);
        binding.delay.setText("Delay Time:"+String.valueOf(selected.delay));

        // Set the delete button click listener
        binding.deleteButton.setOnClickListener(v -> {
            deleteFlightDetailsToDatabase(selected);
        });

        return binding.getRoot ();
    }

    /**
     * Deletes the selected FlightDetails object from the database.
     * It shows an AlertDialog to confirm the deletion and displays a Snackbar with an "Undo" option.
     * If the user chooses to undo the deletion, the deleted flight details are restored to the database.
     *
     * @param flightDetails The FlightDetails object to be deleted.
     */
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
                flightDetailsDao.deleteFlight(selected);
                // Delete the flight information from the adapter
                flightAdapter2.removeFlight(selected);
                // Get the updated flight details list from the database
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
                                    // Insert the flight information back to the database
                                    FlightDatabase undoDatabase = FlightDatabase.getInstance(getContext());
                                    FlightDetailsDAO undoFlightDetailsDao = undoDatabase.fDAO();
                                    undoFlightDetailsDao.insertFlight(selected);
                                    // Get the updated flight details list from the database
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

    /**
     * Handles the item click event in the RecyclerView.
     * Creates a new instance of FlightDetailsFragment2 and passes the selected flight details to display.
     *
     * @param flightDetails The FlightDetails object that was clicked in the RecyclerView.
     */
    public void onItemClick(FlightDetails flightDetails) {
        // Create a new instance of FlightDetailsFragment and pass the selected flight details
        FlightDetailsFragment2 fragment = new FlightDetailsFragment2(flightDetails.getFlightDetails());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation2, fragment) // Replace "R.id.fragmentContainer" with the ID of the container where you want to display the new fragment
                .addToBackStack(null)
                .commit();
    }

}

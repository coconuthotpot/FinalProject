package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.FlightDetailsLayoutBinding;

/**
 * A Fragment class to display the details of a flight.
 * It takes a FlightDetails object as input and displays its information in a layout.
 * Users can save the flight details to the database by clicking the "Save" button.
 *@author Ying Tu
 *@version 1.0
 */
public class FlightDetailsFragment extends Fragment {
    /**
     *The selected FlightDetails object to display
     */
    FlightDetails selected;

    /**
     * Constructs a new FlightDetailsFragment with the given FlightDetails object.
     *
     * @param flightDetails The FlightDetails object to display.
     */
    public FlightDetailsFragment (FlightDetails flightDetails) {
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
       FlightDetailsLayoutBinding binding = FlightDetailsLayoutBinding.inflate(inflater);

        // Display the flight details in the layout
        binding.flightNumber.setText ("Flight Number:"+selected.number );
        binding.departureAirport.setText("Departure Airport:"+selected.departure_airport);
        binding.arrivalAirport.setText("Destination Airport:"+selected.destination_airport);
        binding.terminal.setText("Departure Terminal:"+selected.departure_terminal);
        binding.gate.setText("Gate:"+selected.gate);
        binding.delay.setText("Delay Time:"+String.valueOf(selected.delay));

        // Save the flight details to the database on button click
        binding.save.setOnClickListener(v -> {

            saveFlightDetailsToDatabase(selected);
            Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();

        });
        return binding.getRoot ();
    }

    /**
     * Saves the FlightDetails object to the database.
     *
     * @param flightDetails The FlightDetails object to be saved to the database.
     */
    private void saveFlightDetailsToDatabase(FlightDetails flightDetails) {
        FlightDetails flightDetailsEntity = new FlightDetails();
        flightDetailsEntity.setNumber(flightDetails.getNumber());
        flightDetailsEntity.setDeparture_airport(flightDetails.getDeparture_airport());
        flightDetailsEntity.setDestination_airport(flightDetails.getDestination_airport());
        flightDetailsEntity.setDeparture_terminal(flightDetails.getDeparture_terminal());
        flightDetailsEntity.setGate(flightDetails.getGate());
        flightDetailsEntity.setDelay(flightDetails.getDelay());

        // Run the database insertion operation on a separate thread
        new Thread(() -> {
            FlightDatabase database = FlightDatabase.getInstance(requireContext());
            FlightDetailsDAO flightDetailsDao = database.fDAO();
            flightDetailsDao.insertFlight(flightDetailsEntity);
        }).start();

    }

}

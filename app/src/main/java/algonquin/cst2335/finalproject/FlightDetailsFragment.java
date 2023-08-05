package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.FlightDetailsLayoutBinding;

public class FlightDetailsFragment extends Fragment {

    FlightDetails selected;
    public FlightDetailsFragment (FlightDetails flightDetails) {
        selected=flightDetails;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       FlightDetailsLayoutBinding binding = FlightDetailsLayoutBinding.inflate(inflater);

        binding.flightNumber.setText ("Flight Number:"+selected.number );
        binding.departureAirport.setText("Departure Airport:"+selected.departure_airport);
        binding.arrivalAirport.setText("Destination Airport:"+selected.destination_airport);
        binding.terminal.setText("Departure Terminal:"+selected.departure_terminal);
        binding.gate.setText("Gate:"+selected.gate);
        binding.delay.setText("Delay Time:"+String.valueOf(selected.delay));

        binding.save.setOnClickListener(v -> {

            saveFlightDetailsToDatabase(selected);
            Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();

        });
        return binding.getRoot ();
    }

    private void saveFlightDetailsToDatabase(FlightDetails flightDetails) {
        FlightDetails flightDetailsEntity = new FlightDetails();
        flightDetailsEntity.setNumber(flightDetails.getNumber());
        flightDetailsEntity.setDeparture_airport(flightDetails.getDeparture_airport());
        flightDetailsEntity.setDestination_airport(flightDetails.getDestination_airport());
        flightDetailsEntity.setDeparture_terminal(flightDetails.getDeparture_terminal());
        flightDetailsEntity.setGate(flightDetails.getGate());
        flightDetailsEntity.setDelay(flightDetails.getDelay());

        new Thread(() -> {
            FlightDatabase database = FlightDatabase.getInstance(requireContext());
            FlightDetailsDAO flightDetailsDao = database.fDAO();
            flightDetailsDao.insertFlight(flightDetailsEntity);
        }).start();

    }

}

package algonquin.cst2335.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return binding.getRoot ();
    }
}

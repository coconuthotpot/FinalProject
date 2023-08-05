package algonquin.cst2335.finalproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlightAdapter2 extends RecyclerView.Adapter<FlightAdapter2.FlightViewHolder> {

    private List<FlightDetails> flightDetailsList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(FlightDetails flightDetails);
    }

    public void setData(List<FlightDetails> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }


    public FlightAdapter2(List<FlightDetails> flightDetailsList, OnItemClickListener onItemClickListener) {
        this.flightDetailsList = flightDetailsList;
        this.onItemClickListener = onItemClickListener;
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFlightNumber;
        private TextView textViewDestination;

        private  TextView textVIewDeparture;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFlightNumber = itemView.findViewById(R.id.textViewFlightNumber);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            textVIewDeparture= itemView.findViewById(R.id.textViewDeparture);
        }

        public void bind(final FlightDetails flightDetails, final OnItemClickListener listener) {

            itemView.setOnClickListener(v -> listener.onItemClick(flightDetails));
        }
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightDetails flightDetails = flightDetailsList.get(position);

        holder.bind(flightDetails, onItemClickListener);
        holder.textViewFlightNumber.setText("Flight Number:" + flightDetails.getNumber());
        holder.textVIewDeparture.setText("Departure Airport"+ flightDetails.getDeparture_airport());
        holder.textViewDestination.setText("Destination Airport:" + flightDetails.getDestination_airport());

    }

    @Override
    public int getItemCount() {
        return flightDetailsList.size();
    }

    public void removeFlight(FlightDetails flight) {
        int position = flightDetailsList.indexOf(flight);
        if (position != -1) {
            flightDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Method to add a flight to the list
    public void addFlight(FlightDetails flightDetails) {
        flightDetailsList.add(flightDetails);
        notifyDataSetChanged();
    }

}




package algonquin.cst2335.finalproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Flight flight);
    }

    private List<FlightDetails> flightDetailsList;

    public void setData(List<FlightDetails> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }

    public FlightAdapter(List<Flight> flightList, OnItemClickListener onItemClickListener) {
        this.flightList = flightList;
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

        public void bind(final Flight flight, final OnItemClickListener listener) {

            itemView.setOnClickListener(v -> listener.onItemClick(flight));
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
        Flight flight = flightList.get(position);
        holder.bind(flight, onItemClickListener);
        holder.textViewFlightNumber.setText("Flight Number:" + flight.getFlightNumber());
        holder.textVIewDeparture.setText("Departure Airport"+ flight.getDeparture_airport());
        holder.textViewDestination.setText("Destination Airport:" + flight.getDestination_airport());

    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

}


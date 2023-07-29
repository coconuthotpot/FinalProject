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

    public FlightAdapter(List<Flight> flightList, OnItemClickListener onItemClickListener) {
        this.flightList = flightList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_aviation, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.bind(flight, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFlightNumber;
        private TextView textViewDestination;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFlightNumber = itemView.findViewById(R.id.textViewFlightNumber);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
        }

        public void bind(final Flight flight, final OnItemClickListener listener) {
            textViewFlightNumber.setText("Flight Number:" + flight.getFlightNumber());
            textViewDestination.setText("Destination Airport:" + flight.getDestination_airport());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(flight);
                }
            });
        }
    }
}


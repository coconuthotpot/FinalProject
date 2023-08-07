package algonquin.cst2335.finalproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is a custom RecyclerView adapter for displaying a list of Flight objects.
 * It binds the Flight data to the RecyclerView items and handles item click events.
 *@author Ying Tu
 *@version 1.0
 */
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private OnItemClickListener onItemClickListener;

    /**
     * Interface for handling click events on Flight items in the RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Called when a Flight item is clicked.
         *
         * @param flight The clicked Flight object.
         */
        void onItemClick(Flight flight);
    }

    private List<FlightDetails> flightDetailsList;

    /**
     * Sets the FlightDetails data for the adapter.
     *
     * @param flightDetailsList The list of FlightDetails to be displayed.
     */
    public void setData(List<FlightDetails> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }

    /**
     * Constructs a new FlightAdapter with the given Flight list and click listener.
     *
     * @param flightList          The list of Flight objects to be displayed.
     * @param onItemClickListener The click listener for Flight item clicks.
     */
    public FlightAdapter(List<Flight> flightList, OnItemClickListener onItemClickListener) {
        this.flightList = flightList;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * The ViewHolder class that represents individual items in the RecyclerView.
     */
    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFlightNumber;
        private TextView textViewDestination;

        private  TextView textVIewDeparture;

        /**
         * Constructs a new FlightViewHolder with the provided itemView.
         *
         * @param itemView The view representing a single Flight item in the RecyclerView.
         */
        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFlightNumber = itemView.findViewById(R.id.textViewFlightNumber);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            textVIewDeparture= itemView.findViewById(R.id.textViewDeparture);
        }

        /**
         * Binds the Flight data to the ViewHolder and sets a click listener on the item.
         *
         * @param flight   The Flight object to bind to the ViewHolder.
         * @param listener The click listener for Flight item clicks.
         */
        public void bind(final Flight flight, final OnItemClickListener listener) {

            itemView.setOnClickListener(v -> listener.onItemClick(flight));
        }
    }

    /**
     * Called when the RecyclerView needs a new FlightViewHolder to represent an item.
     *
     * @param parent   The parent ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new FlightViewHolder that holds a View of the item layout.
     */
    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }
    /**
     * Called to bind the Flight object to the views in the item layout at the given position.
     *
     * @param holder   The FlightViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.bind(flight, onItemClickListener);
        holder.textViewFlightNumber.setText("Flight Number:" + flight.getFlightNumber());
        holder.textVIewDeparture.setText("Departure Airport"+ flight.getDeparture_airport());
        holder.textViewDestination.setText("Destination Airport:" + flight.getDestination_airport());

    }
    /**
     * Returns the total number of items in the list.
     *
     * @return The total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return flightList.size();
    }

}


package algonquin.cst2335.finalproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is a custom RecyclerView adapter for displaying a list of FlightDetails objects.
 * It binds the FlightDetails data to the RecyclerView items and handles item click events.
 *@author Ying Tu
 *@version 1.0
 */
public class FlightAdapter2 extends RecyclerView.Adapter<FlightAdapter2.FlightViewHolder> {

    private List<FlightDetails> flightDetailsList;
    private OnItemClickListener onItemClickListener;

    /**
     * Interface for handling click events on FlightDetails items in the RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Called when a FlightDetails item is clicked.
         *
         * @param flightDetails The clicked FlightDetails object.
         */
        void onItemClick(FlightDetails flightDetails);
    }

    /**
     * Sets the FlightDetails data for the adapter and notifies data change.
     *
     * @param flightDetailsList The list of FlightDetails to be displayed.
     */
    public void setData(List<FlightDetails> flightDetailsList) {
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }

    /**
     * Constructs a new FlightAdapter2 with the given FlightDetails list and click listener.
     *
     * @param flightDetailsList  The list of FlightDetails objects to be displayed.
     * @param onItemClickListener The click listener for FlightDetails item clicks.
     */
    public FlightAdapter2(List<FlightDetails> flightDetailsList, OnItemClickListener onItemClickListener) {
        this.flightDetailsList = flightDetailsList;
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
         * @param itemView The view representing a single FlightDetails item in the RecyclerView.
         */
        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFlightNumber = itemView.findViewById(R.id.textViewFlightNumber);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            textVIewDeparture= itemView.findViewById(R.id.textViewDeparture);
        }
        /**
         * Binds the FlightDetails data to the ViewHolder and sets a click listener on the item.
         *
         * @param flightDetails The FlightDetails object to bind to the ViewHolder.
         * @param listener      The click listener for FlightDetails item clicks.
         */
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

    /**
     * Removes a specified FlightDetails object from the list and notifies data change.
     *
     * @param flight The FlightDetails  object to be removed.
     */
    public void removeFlight(FlightDetails flight) {
        int position = flightDetailsList.indexOf(flight);
        if (position != -1) {
            flightDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Adds a new FlightDetails object to the list and notifies data change.
     *
     * @param flightDetails The FlightDetails object to be added.
     */
    public void addFlight(FlightDetails flightDetails) {
        flightDetailsList.add(flightDetails);
        notifyDataSetChanged();
    }



}




package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FlightDetails {
    @ColumnInfo(name = "number")
    String number;

    @ColumnInfo(name = "departure_airport")
    String departure_airport;

    @ColumnInfo(name = "destination_airport")
    String destination_airport;

    @ColumnInfo(name = "departure_terminal")
    String departure_terminal;

    @ColumnInfo(name = "gate")
    String gate;

    @ColumnInfo(name = "delay")
    int delay;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;


    public FlightDetails(String number, String departure_airport, String destination_airport, String departure_terminal, String gate, int delay, long id) {
        this.number = number;
        this.departure_airport = departure_airport;
        this.destination_airport = destination_airport;
        this.departure_terminal = departure_terminal;
        this.gate = gate;
        this.delay = delay;
        this.id = id;
    }

    public FlightDetails() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
    }

    public String getDestination_airport() {
        return destination_airport;
    }

    public void setDestination_airport(String destination_airport) {
        this.destination_airport = destination_airport;
    }

    public String getDeparture_terminal() {
        return departure_terminal;
    }

    public void setDeparture_terminal(String departure_terminal) {
        this.departure_terminal = departure_terminal;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FlightDetails getFlightDetails() {
        FlightDetails flightDetails = new FlightDetails();
        flightDetails.setNumber(number);
        flightDetails.setDeparture_airport(departure_airport);
        flightDetails.setDestination_airport(destination_airport);
        flightDetails.setDeparture_terminal(departure_terminal);
        flightDetails.setGate(gate);
        flightDetails.setDelay(delay);
        return flightDetails;
    }
}


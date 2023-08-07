package algonquin.cst2335.finalproject;

/**
 * This class represents a flight with its associated details.
 * It stores information such as flight number, destination airport, departure airport,
 * departure terminal, gate, and delay.
 *@author Ying Tu
 *@version 1.0
 */
public class Flight {
    private String flightNumber;
    private String destination_airport;

    private String departure_airport;

    private String departure_terminal;

    private String gate;

    private int delay;

    /**
     * Constructs a new Flight object with the specified details.
     *
     * @param flightNumber       The flight number.
     * @param departure_airport  The airport code of the departure airport.
     * @param destination_airport The airport code of the destination airport.
     * @param departure_terminal The terminal for departure.
     * @param gate               The gate number for departure.
     * @param delay              The delay time in minutes, if any (0 for no delay).
     */
    public Flight(String flightNumber, String departure_airport,String destination_airport,String departure_terminal,String gate,int delay) {
        this.flightNumber=flightNumber;
        this.departure_airport=departure_airport;
        this.destination_airport=destination_airport;
        this.departure_terminal=departure_terminal;
        this.gate=gate;
        this.delay=delay;

    }

    /** Getters and Setters for flight details (flightNumber, destination_airport, etc.)...
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDestination_airport() {
        return destination_airport;
    }

    public void setDestination_airport(String destination_airport) {
        this.destination_airport = destination_airport;
    }


    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
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


    /**
     * Retrieves a FlightDetails object with the flight information.
     * This is useful for passing the flight details to other components or activities.
     *
     * @return A FlightDetails object with the flight information.
     */
    public FlightDetails getFlightDetails() {
        FlightDetails flightDetails = new FlightDetails();
        flightDetails.setNumber(flightNumber);
        flightDetails.setDeparture_airport(departure_airport);
        flightDetails.setDestination_airport(destination_airport);
        flightDetails.setDeparture_terminal(departure_terminal);
        flightDetails.setGate(gate);
        flightDetails.setDelay(delay);
        return flightDetails;
    }
}



package algonquin.cst2335.finalproject;

public class Flight {
    private String flightNumber;
    private String destination_airport;

    private String departure_airport;

    public Flight(String flightNumber, String departure_airport,String destination_airport) {
        this.flightNumber=flightNumber;
        this.departure_airport=departure_airport;
        this.destination_airport=destination_airport;


    }

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

}


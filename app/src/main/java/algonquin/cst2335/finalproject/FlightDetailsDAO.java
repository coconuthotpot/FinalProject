package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface FlightDetailsDAO {
    @Insert
    public long insertFlight(FlightDetails f);
    @Query("Select * from FlightDetails")

    public List<FlightDetails> getAllFlight();

    @Delete
    void deleteFlight (FlightDetails f);
}
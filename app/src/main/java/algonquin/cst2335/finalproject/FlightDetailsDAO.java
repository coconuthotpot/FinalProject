package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This is a Data Access Object (DAO) interface for managing the FlightDetails entities in the database.
 * It contains methods for inserting, querying, and deleting FlightDetails records.
 *@author Ying Tu
 *@version 1.0
 */
@Dao
public interface FlightDetailsDAO {
    /**
     * Inserts a new FlightDetails record into the database.
     *
     * @param f The FlightDetails object to be inserted.
     * @return The unique identifier (id) of the inserted FlightDetails record.
     */
    @Insert
    public long insertFlight(FlightDetails f);

    /**
     * Retrieves a list of all FlightDetails records from the database.
     *
     * @return A List of FlightDetails objects containing all the flight details stored in the database.
     */
    @Query("Select * from FlightDetails")
    public List<FlightDetails> getAllFlight();

    /**
     * Deletes a FlightDetails record from the database.
     *
     * @param f The FlightDetails object to be deleted.
     */
    @Delete
    void deleteFlight (FlightDetails f);
}
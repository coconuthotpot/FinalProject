package algonquin.cst2335.finalproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * This is the Room database class for managing the FlightDetails entities.
 * It provides access to the FlightDetails Data Access Object (DAO) and manages the database instance.
 *@author Ying Tu
 *@version 1.0
 */
@Database(entities = {FlightDetails.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {
    /**
     * Gets the FlightDetails Data Access Object (DAO) to access the FlightDetails entities in the database.
     *
     * @return The FlightDetailsDAO instance for accessing the FlightDetails entities.
     */
    public abstract FlightDetailsDAO fDAO();

    private static FlightDatabase instance;

    /**
     * Creates or retrieves the singleton instance of the FlightDatabase.
     * If the database instance doesn't exist, it creates a new one.
     *
     * @param context The application context.
     * @return The singleton instance of the FlightDatabase.
     */
    public static synchronized FlightDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FlightDatabase.class, "app_database")
                    .build();
        }
        return instance;
    }


}
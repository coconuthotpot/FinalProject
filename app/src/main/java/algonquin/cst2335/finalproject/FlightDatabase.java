package algonquin.cst2335.finalproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlightDetails.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {
    public abstract FlightDetailsDAO fDAO();

    private static FlightDatabase instance;

    public static synchronized FlightDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FlightDatabase.class, "app_database")
                    .build();
        }
        return instance;
    }


}
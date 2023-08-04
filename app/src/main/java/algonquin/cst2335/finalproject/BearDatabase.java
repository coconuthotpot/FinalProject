package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * this is the database of the bear image
 */
@Database(entities = {BearImage.class}, version = 1)
public abstract class BearDatabase extends RoomDatabase {
    /**
     * this is the abstract function
     * @return return bear image dao
     */
    public abstract BearImageDAO bDAO();
}

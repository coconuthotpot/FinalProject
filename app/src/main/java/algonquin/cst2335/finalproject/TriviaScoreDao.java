package algonquin.cst2335.finalproject;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.android.material.color.utilities.Score;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing TriviaScore entities in the database.
 * This interface provides methods for inserting, deleting, and querying TriviaScore data.
 */
@Dao
public interface TriviaScoreDao {

    /**
     * Inserts a new TriviaScore into the database.
     *
     * @param score The Score object to be inserted.
     */
    @Insert
    void insert(TriviaScore score);

    /**
     * Deletes a TriviaScore from the database.
     *
     * @param score The Score object to be deleted.
     */
    @Delete
    void delete(TriviaScore score);


    /**
     * Retrieves the top 10 TriviaScore entries from the database, ordered by score in descending order.
     *
     * @return A list of the top 10 Score objects.
     */
    @Query("SELECT * FROM TriviaScore ORDER BY score DESC LIMIT 10")
    List<Score> getTop10Scores();

}

package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);

    @Delete
    void delete(Score score);

    @Query("DELETE FROM score WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM score ORDER BY score DESC LIMIT 10")
    List<Score> getTop10Scores();

}

package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class represents a data model for storing Trivia scores in a database using Room.
 * It defines the structure of the database table for Trivia scores.
 */
@Entity
public class TriviaScore {

    /**
     * The name of the player who achieved the Trivia score.
     */
    @ColumnInfo(name = "name")
    private String name;

    /**
     * The Trivia score achieved by the player.
     */
    @ColumnInfo(name="score")
    private int score;

    /**
     * The unique identifier for each Trivia score entry in the database.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * Constructor to create a new instance of TriviaScore with a name and score.
     * @param name The name of the player who achieved the Trivia score.
     * @param score The Trivia score achieved by the player.
     */
    public TriviaScore(String name, int score) {

        this.name = name;
        this.score = score;
    }

    /**
     * Gets the Trivia score achieved by the player.
     * @return The Trivia score achieved by the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the Trivia score achieved by the player.
     * @param score The Trivia score achieved by the player to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }


    /**
     * Gets the name of the player who achieved the Trivia score.
     * @return The name of the player who achieved the Trivia score.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player who achieved the Trivia score.
     * @param name The name of the player who achieved the Trivia score to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier for the Trivia score entry in the database.
     * @return The unique identifier for the Trivia score entry in the database.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the Trivia score entry in the database.
     * @param id The unique identifier for the Trivia score entry to be set.
     */
    public void setId(int id) {
        this.id = id;
    }
}

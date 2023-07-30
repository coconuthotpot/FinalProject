package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Score {

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name="score")
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

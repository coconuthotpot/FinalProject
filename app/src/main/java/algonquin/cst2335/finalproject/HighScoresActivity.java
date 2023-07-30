package algonquin.cst2335.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HighScoresAdapter adapter;
    private List<Score> highScoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        recyclerView = findViewById(R.id.recyclerViewHighScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the top 10 high scores from the database
        highScoresList = fetchTop10HighScores();

        // Create and set the adapter with the high scores list
        adapter = new HighScoresAdapter(highScoresList);
        recyclerView.setAdapter(adapter);
    }

    private List<Score> fetchTop10HighScores() {
        // Fetch the top 10 high scores from the database (use your database implementation)
        // For example, you can use SQLiteOpenHelper or Room database
        // Here, we assume you have a ScoreDatabaseHelper class to handle database operations
        ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);
        return databaseHelper.getTop10Scores();
    }
}

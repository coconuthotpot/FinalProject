package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HighScoresActivity extends AppCompatActivity implements HighScoresAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private HighScoresAdapter adapter;
    private List<Score> highscoreList;

    ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

//        recyclerView = findViewById(R.id.recyclerViewHighscore);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Fetch the top 10 high score from the database
//        highscoreList = fetchTop10Highscore();
//
//        // Create and set the adapter with the high score list
//        adapter = new HighscoreAdapter(highscoreList);
//        recyclerView.setAdapter(adapter);

        // Call setupRecyclerView() to set up the RecyclerView and adapter
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewHighScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the top 10 high score from the database
        highscoreList = fetchTop10Highscore();

//        // Create and set the adapter with the high score list and the item click listener
//        adapter = new HighScoresAdapter(highscoreList, this);
//        recyclerView.setAdapter(adapter);
        // Create the adapter with the high score list and the item click listener if it doesn't exist
        if (adapter == null) {
            adapter = new HighScoresAdapter(highscoreList, this);
            recyclerView.setAdapter(adapter);
        } else {
            // Update the adapter's dataset with the new highscoreList
            adapter.setHighScoresList(highscoreList);
            adapter.notifyDataSetChanged();
        }
    }


    private List<Score> fetchTop10Highscore() {
        // Fetch the top 10 high score from the database (use your database implementation)
        // For example, you can use SQLiteOpenHelper or Room database
        // Here, we assume you have a ScoreDatabaseHelper class to handle database operations
//        ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);
        return databaseHelper.getTop10score();
    }

    public void onItemClick(Score score) {
        Log.d("HighScoresActivity", "Delete score with id: " + score.getName());
        Executor thread = Executors.newSingleThreadExecutor();
        // Handle item click here, e.g., delete the score from the database and update the RecyclerView
     //   ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);


        // Remove the score from the list
//        highscoreList.remove(score);
//        databaseHelper.deleteScore(score.getId());
        // Notify the adapter about the item removal
//        adapter.notifyDataSetChanged();

        thread.execute(() -> {
            databaseHelper.deleteScore(score.getName());

            // Update the RecyclerView on the main/UI thread after deleting from the database
            runOnUiThread(() -> {
                highscoreList.remove(score);
                adapter.notifyDataSetChanged();
            });
        });
    }

}

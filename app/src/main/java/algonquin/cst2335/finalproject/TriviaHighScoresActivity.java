package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * An activity that displays the top 10 high scores in a RecyclerView.
 * It allows users to see the high scores, delete scores, and view the details of individual scores.
 */
public class TriviaHighScoresActivity extends AppCompatActivity {
    /**
     * The RecyclerView used to display the list of high scores.
     */
    private RecyclerView recyclerView;
    /**
     * The adapter for the RecyclerView that handles the high score list.
     */
    private TriviaHighScoresAdapter adapter;
    /**
     * The list of TriviaScore objects representing the top 10 high scores.
     */
    private List<TriviaScore> highscoreList;
    /**
     * The database helper instance for handling high scores database operations.
     */
    TriviaScoreDatabaseHelper databaseHelper = new TriviaScoreDatabaseHelper(this);

    /**
     * The item click listener for handling item click events in the RecyclerView.
     * It shows a dialog to confirm score deletion or displays the details of the selected score.
     */
    private TriviaHighScoresAdapter.OnItemClickListener itemClickListener = new TriviaHighScoresAdapter.OnItemClickListener() {
        /**
         *  Update the existing onItemClick method to show a dialog before deleting
         * @param score The database table of user and score
         */
        public void onItemClick(TriviaScore score) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaHighScoresActivity.this);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete this score or see the detail of the item?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteScore(score);
                }
            });
            builder.setNegativeButton("See Detail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showItemDetailFragment(score);
            }
            });
            builder.show();
        }
    };

    /**
     * Called when the activity is created.
     * Sets up the RecyclerView to display the high scores and sets the adapter.
     * @param savedInstanceState The saved instance state of the activity (not used in this case).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);


        // Call setupRecyclerView() to set up the RecyclerView and adapter
        setupRecyclerView();

    }

    /**
     * Sets up the RecyclerView to display the high scores and sets the adapter.
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewHighScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the top 10 high score from the database
        highscoreList = fetchTop10Highscore();

        // Create the adapter with the high score list and the item click listener if it doesn't exist
        if (adapter == null) {
            adapter = new TriviaHighScoresAdapter(highscoreList, itemClickListener);
            recyclerView.setAdapter(adapter);
        } else {
            // Update the adapter's dataset with the new highscoreList
            adapter.setHighScoresList(highscoreList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Fetches the top 10 high scores from the database.
     * @return The list of TriviaScore objects representing the top 10 high scores.
     */
    private List<TriviaScore> fetchTop10Highscore() {
        // Fetch the top 10 high score from the database (use your database implementation)
        // For example, you can use SQLiteOpenHelper or Room database
        // Here, we assume you have a ScoreDatabaseHelper class to handle database operations

        return databaseHelper.getTop10score();
    }

    /**
     * Deletes the specified score from the database and updates the RecyclerView.
     * @param score The TriviaScore object representing the score to be deleted.
     */
    private void deleteScore(TriviaScore score) {
        Log.d("HighScoresActivity", "Delete score with id: " + score.getName());
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            databaseHelper.deleteScore(score.getName());

            runOnUiThread(() -> {
                highscoreList.remove(score);
                adapter.notifyDataSetChanged();
            });
        });
    }

    /**
     * Displays the details of the selected score in a fragment.
     * @param score The TriviaScore object representing the selected score.
     */
    private void showItemDetailFragment(TriviaScore score) {
        // Create the fragment instance and pass the selected Score as an argument
        TriviaDetailsFragment fragment = new TriviaDetailsFragment(score);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation, fragment) // Replace 'fragmentContainer' with the ID of the container in your activity layout
                .addToBackStack(null)
                .commit();
    }
}

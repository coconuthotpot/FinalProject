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

public class HighScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HighScoresAdapter adapter;
    private List<Score> highscoreList;

    ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);

    private HighScoresAdapter.OnItemClickListener itemClickListener = new HighScoresAdapter.OnItemClickListener() {

        // Update the existing onItemClick method to show a dialog before deleting
        public void onItemClick(Score score) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HighScoresActivity.this);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete this score?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteScore(score);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }




//        @Override
//        public void onItemClick(Score score) {
//            // Handle the item click here, similar to the original onItemClick method
//            Log.d("HighScoresActivity", "Delete score with id: " + score.getName());
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() -> {
//                databaseHelper.deleteScore(score.getName());
//
//                runOnUiThread(() -> {
//                    highscoreList.remove(score);
//                    adapter.notifyDataSetChanged();
//                });
//            });
//        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);


        // Call setupRecyclerView() to set up the RecyclerView and adapter
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewHighScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the top 10 high score from the database
        highscoreList = fetchTop10Highscore();

        // Create the adapter with the high score list and the item click listener if it doesn't exist
        if (adapter == null) {
            adapter = new HighScoresAdapter(highscoreList, itemClickListener);
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

        return databaseHelper.getTop10score();
    }

    private void deleteScore(Score score) {
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



}

package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyBinding;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;

public class TriviaActivity extends AppCompatActivity {

    ActivityTriviaBinding triviaBinding;
    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;
    private RecyclerView recyclerView;
    private TriviaList adapter;
    protected RequestQueue queue = null;

    private String correctAnswer;
    private JSONArray results;


    int score = 0;
    /*int totalQuestion = TriviaActivityQA.question.length;*/
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        triviaBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(triviaBinding.getRoot());
        queue = Volley.newRequestQueue(this);
        setSupportActionBar(triviaBinding.triviatoolbar);

        recyclerView = triviaBinding.triviaRecycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of items to display in the RecyclerView
        List<String> items = new ArrayList<>();
        // Add your trivia items to the list

        // Create the adapter with the list of items
        adapter = new TriviaList(items);
        recyclerView.setAdapter(adapter);

        EditText inputEditText = triviaBinding.inputEditText;
        Button saveButton = triviaBinding.saveButton;
        totalQuestionsTextView = triviaBinding.totalQuestion;
        questionTextView = triviaBinding.question;
        ansA = triviaBinding.ansA;
        ansB = triviaBinding.ansB;
        ansC = triviaBinding.ansC;
        ansD = triviaBinding.ansD;
        submitBtn = triviaBinding.submitBtn;

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String savedText = preferences.getString("savedText", "");
        inputEditText.setText(savedText);

        String submitText = getString(R.string.submit);
        String saveText = getString(R.string.save);

        triviaBinding.saveButton.setText(saveText);
        triviaBinding.saveButton.setOnClickListener(clk-> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to save this name?");
            builder.setPositiveButton("Yes", (dialog, click) -> {
                String userInput = triviaBinding.inputEditText.getText().toString();

                Toast.makeText(this, "Name saved!", Toast.LENGTH_SHORT).show();

                Snackbar.make(triviaBinding.getRoot(), "Your name is: " + userInput, Snackbar.LENGTH_LONG)
                        .show();

                editor.putString("savedText", userInput);
                editor.apply();
            });

            builder.setNegativeButton("No", (dialog2, click2) -> {});
            builder.create().show();




        });
        triviaBinding.buttonGeography.setText(getString(R.string.geography));
        triviaBinding.buttonGeography.setOnClickListener( click -> {
            String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            results = response.getJSONArray("results");
                            fetchNextQuestion( currentQuestionIndex);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                        int i = 0;
                        // Handle error if necessary
                    });            // call this for error
            queue.add(request); //send request to server


        });
        triviaBinding.ansA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChoiceButtonClick((Button) view);
            }
        });

        triviaBinding.ansB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChoiceButtonClick((Button) view);
            }
        });

        triviaBinding.ansC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChoiceButtonClick((Button) view);
            }
        });

        triviaBinding.ansD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChoiceButtonClick((Button) view);
            }
        });

        triviaBinding.submitBtn.setText(submitText);
        triviaBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(selectedAnswer)) {
                    if (selectedAnswer.equals(correctAnswer)) {
                        // Increment score if the answer is correct
                        score++;
                    }
                    currentQuestionIndex++;
                    fetchNextQuestion( currentQuestionIndex);
                } else {
                    Toast.makeText(TriviaActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void fetchNextQuestion(int currentQuestionIndex) {
        try {
            if (currentQuestionIndex < results.length()) {
                JSONObject question = results.getJSONObject(currentQuestionIndex);
                String questionString = question.getString("question");
                correctAnswer = question.getString("correct_answer"); // Correct answer is assigned to the class-level variable
               // String correctAnswer = question.getString("correct_answer");
                JSONArray incorrectAs = question.getJSONArray("incorrect_answers");
                int incorrectLength = incorrectAs.length();
                ArrayList<String> incorrectTexts = new ArrayList<>();
                for (int j = 0; j < incorrectLength; j++) {
                    incorrectTexts.add(incorrectAs.getString(j));
                }

                String[] choices = new String[4];
                choices[0] = correctAnswer;
                for (int k = 1; k < 4; k++) {
                    choices[k] = incorrectTexts.get(k - 1);
                }

                runOnUiThread(() -> {
                    totalQuestionsTextView.setText("Total Question: " + results.length());
                    questionTextView.setText(questionString);
                    ansA.setText(choices[0]);
                    ansB.setText(choices[1]);
                    ansC.setText(choices[2]);
                    ansD.setText(choices[3]);
                    selectedAnswer = "";
                    ansA.setBackgroundColor(Color.WHITE);
                    ansB.setBackgroundColor(Color.WHITE);
                    ansC.setBackgroundColor(Color.WHITE);
                    ansD.setBackgroundColor(Color.WHITE);

                    Log.d("TriviaActivity", "Next question fetched. Index: " + currentQuestionIndex);
                });
            } else {
                // Handle the end of trivia (no more questions)
                runOnUiThread(() -> {
                    Toast.makeText(this, "End of trivia. Your score: " + score, Toast.LENGTH_LONG).show();
                    showEndOfTriviaDialog();

                    Log.d("TriviaActivity", "End of trivia. Score: " + score);
                });
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleChoiceButtonClick(Button clickedButton) {
        triviaBinding.ansA.setBackgroundColor(Color.WHITE);
        triviaBinding.ansB.setBackgroundColor(Color.WHITE);
        triviaBinding.ansC.setBackgroundColor(Color.WHITE);
        triviaBinding.ansD.setBackgroundColor(Color.WHITE);

        selectedAnswer = clickedButton.getText().toString();
        clickedButton.setBackgroundColor(Color.MAGENTA);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(TriviaActivity.this);

        if (item.getItemId() == R.id.help) {
            builder.setTitle(getString(R.string.help_navi));
            builder.setMessage(getString(R.string.help_trivia));
            builder.setPositiveButton("OK", ((dialog, click) -> {}));
            builder.setNegativeButton("", ((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.aviation) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent aviationPage = new Intent(TriviaActivity.this, AviationActivity.class);
                startActivity(aviationPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.quiz) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();

        } else if (item.getItemId() == R.id.currency) {
            Toast.makeText(this, getString(R.string.current_page), Toast.LENGTH_LONG).show();
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent triviaPage = new Intent(TriviaActivity.this,CurrencyActivity.class );
                startActivity(triviaPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.bear) {
            builder.setMessage(getString(R.string.change_page_decision));
            builder.setTitle(getString(R.string.change_page));
            builder.setPositiveButton(getString(R.string.yes),((dialog, click) -> {
                Intent bearPage = new Intent(TriviaActivity.this, BearActivity.class);
                startActivity(bearPage);
            }));
            builder.setNegativeButton(getString(R.string.no),((dialog, click) -> {}));
            builder.create().show();
        } else if (item.getItemId() == R.id.contact) {
            //    Snackbar.make(showAmount, getString(R.string.contact_msg), Snackbar.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(this, getString(R.string.about_msg), Toast.LENGTH_LONG).show();
        }

        return true;
    }


    private void showEndOfTriviaDialog() {
        // Show a dialog to ask for the user's name when trivia ends
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Trivia End");
        builder.setMessage("Please enter your name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!TextUtils.isEmpty(name)) {
                // Save the name and score to the database
                saveScoreToDatabase(name, score);
            }
            // Show the high scores activity
            showHighScoresActivity();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void saveScoreToDatabase(String name, int score) {
        // Save the user's name and score to the database (use your database implementation)
        // For example, you can use SQLiteOpenHelper or Room database
        // Here, we assume you have a ScoreDatabaseHelper class to handle database operations
        ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(this);
        databaseHelper.addScore(new Score(name, score));
    }

    private void showHighScoresActivity() {
        // Launch the HighScoresActivity
        Log.d("TriviaActivity", "Launching HighScoresActivity");
        Intent highScoresIntent = new Intent(TriviaActivity.this, HighScoresActivity.class);
        startActivity(highScoresIntent);
    }






}
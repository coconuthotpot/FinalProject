package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;
/**
 * The TriviaActivity class represents the main activity for the Trivia Quiz application.
 * This activity allows users to play a trivia quiz game with questions fetched from an external API.
 * Users can choose the quiz category, specify the number of questions, answer the questions, and view their score at the end.
 * They can also navigate to other activities like AviationActivity, CurrencyActivity, and BearActivity.
 * The TriviaActivity class extends the AppCompatActivity class and implements various methods for UI interaction and handling quiz logic.
 *
 * The activity layout is defined in the activity_trivia.xml file using the ActivityTriviaBinding class.
 * The trivia quiz questions are fetched from the Open Trivia Database API using Volley library for network requests.
 * The user's score is saved to a local SQLite database using the TriviaScoreDatabaseHelper class.
 * The HighScoresActivity displays the top scores from the database.
 *
 * Note: For the application to work correctly, it requires an internet connection to fetch questions from the API.
 * Also, the TriviaScoreDatabaseHelper class handles the SQLite database operations for saving and querying scores.
 *
 * @see AppCompatActivity
 * @see JsonObjectRequest
 * @see Volley
 * @see TriviaScore
 * @see TriviaScoreDatabaseHelper
 */
public class TriviaActivity extends AppCompatActivity {

    /**
     * View binding variables
     */
    ActivityTriviaBinding triviaBinding;
    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;
    /**
     * Request queue for making API calls using Volley library
     */
    protected RequestQueue queue = null;
    /**
     *  Variables to store information about the current question and game state
     */
    private String correctAnswer;
    private JSONArray results;
    int score = 0;
    /*int totalQuestion = TriviaActivityQA.question.length;*/
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    /**
     * Called when the activity is created. Initializes the views, sets up click listeners,
     * and handles user interactions with the quiz.
     * @param savedInstanceState The saved instance state of the activity (not used in this case).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        triviaBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(triviaBinding.getRoot());
        queue = Volley.newRequestQueue(this);
        setSupportActionBar(triviaBinding.triviatoolbar);


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

        triviaBinding.totalQuestion.setText(getString(R.string.total_question) );
        triviaBinding.textCategory.setText(getString(R.string.textCategory));
        triviaBinding.textQuestionAmount.setText(getString(R.string.textQuestionAmount));

        String submitText = getString(R.string.submit);
        String saveText = getString(R.string.save);


        triviaBinding.saveButton.setText(saveText);
        triviaBinding.saveButton.setOnClickListener(clk-> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to choose choose this number of questions?");
            builder.setPositiveButton("Yes", (dialog, click) -> {
                String userInput = triviaBinding.inputEditText.getText().toString();
                Snackbar.make(triviaBinding.getRoot(), "Your question number is: " + userInput, Snackbar.LENGTH_LONG)
                        .show();

                editor.putString("savedText", userInput);
                editor.apply();
            });

            builder.setNegativeButton("No", (dialog2, click2) -> {});
            builder.create().show();
        });

        triviaBinding.buttonSports.setText(getString(R.string.sports));
        triviaBinding.buttonSports.setOnClickListener( click -> {
            String amount = inputEditText.getText().toString();
            String url = "https://opentdb.com/api.php?amount="+amount+"&category=21&type=multiple";
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

                    });
            queue.add(request);
        });


        triviaBinding.buttonGeography.setText(getString(R.string.geography));
        triviaBinding.buttonGeography.setOnClickListener( click -> {
            String amount = inputEditText.getText().toString();
            String url = "https://opentdb.com/api.php?amount="+amount+"&category=22&type=multiple";

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
                    });
            queue.add(request);
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

    /**
     * Fetches the next trivia question and updates the UI to display the question and answer choices.
     * If there are no more questions, it displays the final score and prompts the user to enter their name.
     * @param currentQuestionIndex The index of the current question in the results array.
     */
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
                    totalQuestionsTextView.setText(getString(R.string.total_question) + ": " + results.length());
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

    /**
     * Handles the user's choice button click by highlighting the selected answer and storing it.
     * @param clickedButton The button representing the user's selected answer.
     */
    private void handleChoiceButtonClick(Button clickedButton) {
        triviaBinding.ansA.setBackgroundColor(Color.WHITE);
        triviaBinding.ansB.setBackgroundColor(Color.WHITE);
        triviaBinding.ansC.setBackgroundColor(Color.WHITE);
        triviaBinding.ansD.setBackgroundColor(Color.WHITE);
        selectedAnswer = clickedButton.getText().toString();
        clickedButton.setBackgroundColor(Color.MAGENTA);
    }

    /**
     * Called to create the options menu of the activity.
     * Inflates the app_menu.xml layout as the options menu.
     * @param menu The menu to be created.
     * @return True if the menu is created successfully, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * Handles the selection of items in the options menu.
     * Displays appropriate dialogs or navigates to other activities based on the user's selection.
     * @param item The selected menu item.
     * @return True if the menu item is handled successfully, false otherwise.
     */
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
            Snackbar.make(triviaBinding.getRoot(), getString(R.string.contact_msg), Snackbar.LENGTH_LONG)
                    .show();
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
                // Check if the name already exists in the database
                if (!isNameAlreadyExist(name)) {
                // Save the name and score to the database
                saveScoreToDatabase(name, score);
                    // Show the high scores activity
                    showHighScoresActivity();
                } else {
                    Toast.makeText(this, "Name already exists. Please enter a different name.", Toast.LENGTH_SHORT).show();
                    // Show the dialog again to ask for a different name
                    showEndOfTriviaDialog();
                }
            }   else {
                Toast.makeText(this, "Please enter a valid name.", Toast.LENGTH_SHORT).show();
                // Show the dialog again to ask for a valid name
                showEndOfTriviaDialog();
            }

        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Saves the user's name and score to the database.
     * @param name The user's name.
     * @param score The user's score in the trivia game.
     */
    private void saveScoreToDatabase(String name, int score) {
        TriviaScoreDatabaseHelper databaseHelper = new TriviaScoreDatabaseHelper(this);
        databaseHelper.addScore(new TriviaScore(name, score));
    }

    /**
     * Shows the HighScoresActivity to display the top scores of previous trivia games.
     */
    private void showHighScoresActivity() {
        // Launch the HighScoresActivity
        Log.d("TriviaActivity", "Launching HighScoresActivity");
        Intent highScoresIntent = new Intent(TriviaActivity.this, TriviaHighScoresActivity.class);
        startActivity(highScoresIntent);
    }

    /**
     * Checks if a name already exists in the database, indicating a duplicate entry.
     * @param name The name to check for duplicates.
     * @return True if the name already exists in the database, false otherwise.
     */
    private boolean isNameAlreadyExist(String name) {

        TriviaScoreDatabaseHelper databaseHelper = new TriviaScoreDatabaseHelper(this);
        return databaseHelper.isNameAlreadyExist(name);
    }

}
package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener {


    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;
    private RecyclerView recyclerView;
    private TriviaList adapter;


    int score=0;
    int totalQuestion = TriviaActivityQA.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        recyclerView = findViewById(R.id.triviaRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of items to display in the RecyclerView
        List<String> items = new ArrayList<>();
        // Add your trivia items to the list

        // Create the adapter with the list of items
        adapter = new TriviaList(items);
        recyclerView.setAdapter(adapter);

        EditText inputEditText = findViewById(R.id.inputEditText);
        Button saveButton = findViewById(R.id.saveButton);
        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions : "+totalQuestion);

        loadNewQuestion();

        // Add Toast notification
        Toast.makeText(this, "Welcome to the Trivia Activity!", Toast.LENGTH_SHORT).show();

        // Add Snackbar notification
        Snackbar.make(submitBtn, "Good luck with the trivia!", Snackbar.LENGTH_LONG).show();



        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedText = preferences.getString("savedText", "");
        inputEditText.setText(savedText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = inputEditText.getText().toString();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("savedText", userInput);
                editor.apply();
                Toast.makeText(TriviaActivity.this, "Input saved!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onClick(View view) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            if(selectedAnswer.equals(TriviaActivityQA.correctAnswers[currentQuestionIndex])){
                score++;
            }
            currentQuestionIndex++;
            loadNewQuestion();


        }else{
            //choices button clicked
            selectedAnswer  = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);

        }

    }

    void loadNewQuestion(){

        if(currentQuestionIndex == totalQuestion ){
            finishQuiz();
            return;
        }

        questionTextView.setText(TriviaActivityQA.question[currentQuestionIndex]);
        ansA.setText(TriviaActivityQA.choices[currentQuestionIndex][0]);
        ansB.setText(TriviaActivityQA.choices[currentQuestionIndex][1]);
        ansC.setText(TriviaActivityQA.choices[currentQuestionIndex][2]);
        ansD.setText(TriviaActivityQA.choices[currentQuestionIndex][3]);


        // Clear the list and add the new items
        List<String> items = new ArrayList<>();
        // Add the new items to the list

        adapter.setItems(items);

    }

    void finishQuiz(){
        String passStatus = "";
        if(score > totalQuestion*0.60){
            passStatus = "Passed";
        }else{
            passStatus = "Failed";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is "+ score+" out of "+ totalQuestion)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();



    }

    void restartQuiz(){
        score = 0;
        currentQuestionIndex =0;
        loadNewQuestion();
    }
}
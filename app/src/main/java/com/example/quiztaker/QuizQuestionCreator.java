package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiztaker.models.QuizInfo;
import com.example.quiztaker.models.QuizOption;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QuizQuestionCreator extends AppCompatActivity {
    private Button mQuit;
    private Button mNext;
    private TextView mquestionNumber;
    private EditText mcorrectAnswer;
    private EditText mfalseAnswer1;
    private EditText mfalseAnswer2;
    private EditText mfalseAnswer3;
    private EditText mquestion;
    private FirebaseFirestore database;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question_creator);
        database = FirebaseFirestore.getInstance();
        mQuit = (Button) findViewById(R.id.quit);
        mNext = (Button) findViewById(R.id.next);
        mquestion = (EditText) findViewById(R.id.question);
        mquestionNumber = (TextView) findViewById(R.id.questionNumber);
        mcorrectAnswer = (EditText) findViewById(R.id.correctAnswer);
        mfalseAnswer1 = (EditText) findViewById(R.id.falseAnswer1);
        mfalseAnswer2 = (EditText) findViewById(R.id.falseAnswer2);
        mfalseAnswer3 = (EditText) findViewById(R.id.falseAnswer3);
        mNext.setOnClickListener(new View.OnClickListener(){
            private int count = 1;
            String message = "";
            @Override
            public void onClick(View v) {
                String inputQuestion = mquestion.getText().toString().trim();
                String inputCorrectAnswer = mcorrectAnswer.getText().toString().trim();
                String inputfalseAnswer1 = mfalseAnswer1.getText().toString().trim();
                String inputfalseAnswer2 = mfalseAnswer2.getText().toString().trim();
                String inputfalseAnswer3 = mfalseAnswer3.getText().toString().trim();
                String quizid;

                //retrieves quizid from QuizCreator
                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    if(extras == null) {
                        quizid = null;
                    } else {
                        quizid = extras.getString("quizname");
                    }
                }
                else {
                    quizid = (String) savedInstanceState.getSerializable("quizname");
                }
                if (inputQuestion.equals("") || inputCorrectAnswer.equals("") || inputfalseAnswer1.equals("") || inputfalseAnswer2.equals("") || inputfalseAnswer3.equals("")) {
                    message = "Please fill out the information";
                }
                else {
                    count++;
                    CollectionReference ref1 = database.collection("Quizzes");
                    QuizInfo info = new QuizInfo(inputQuestion);

                    //Maps created to write data into collection
                    Map<String, Object> answer1 = new HashMap<>();
                    answer1.put("optionName", inputCorrectAnswer);
                    answer1.put("isCorrect", true);

                    Map<String, Object> answer2 = new HashMap<>();
                    answer2.put("optionName", inputfalseAnswer1);
                    answer2.put("isCorrect", false);

                    Map<String, Object> answer3 = new HashMap<>();
                    answer3.put("optionName", inputfalseAnswer2);
                    answer3.put("isCorrect", false);

                    Map<String, Object> answer4 = new HashMap<>();
                    answer4.put("optionName", inputfalseAnswer3);
                    answer4.put("isCorrect", false);

                    ref1.document(quizid).collection("Questions").document("Question " + (count - 1)).set(info);
                    ref1.document(quizid).collection("Questions").document("Question " + (count - 1)).collection("Options").document("Option 1").set(answer1);
                    ref1.document(quizid).collection("Questions").document("Question " + (count - 1)).collection("Options").document("Option 2").set(answer2);
                    ref1.document(quizid).collection("Questions").document("Question " + (count - 1)).collection("Options").document("Option 3").set(answer3);
                    ref1.document(quizid).collection("Questions").document("Question " + (count - 1)).collection("Options").document("Option 4").set(answer4);
                    if(count == 11) {
                        mquestionNumber.setText(Integer.toString(count-1));
                    }
                    else{
                        mquestionNumber.setText(Integer.toString(count));
                        mquestion.getText().clear();
                        mcorrectAnswer.getText().clear();
                        mfalseAnswer1.getText().clear();
                        mfalseAnswer2.getText().clear();
                        mfalseAnswer3.getText().clear();
                    }
                    message = "Question " + Integer.toString(count);
                    if (count == 11) {
                        Intent intent = new Intent(QuizQuestionCreator.this, MainActivity.class);
                        startActivity(intent);
                        message = "Quiz saved successfully";
                    }
                }
                Toast.makeText(QuizQuestionCreator.this, message, Toast.LENGTH_LONG).show();
            }
        });
        mQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizQuestionCreator.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiztaker.models.QuizInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
                String[] options  = new String[]{inputCorrectAnswer, inputfalseAnswer1, inputfalseAnswer2, inputfalseAnswer3};
                if (inputQuestion.equals("") || inputCorrectAnswer.equals("") || inputfalseAnswer1.equals("") || inputfalseAnswer2.equals("") || inputfalseAnswer3.equals("")) {
                    message = "Please fill out the information";
                }
                else {
                    count++;
                    CollectionReference databaseQuizzes = database.collection("Quizzes");
                    if (count <= 10) {
                        QuizInfo info = new QuizInfo(inputQuestion, inputCorrectAnswer, inputfalseAnswer1, inputfalseAnswer2, inputfalseAnswer3);
                        databaseQuizzes.add(info).addOnSuccessListener(documentReference -> {
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                      /*  QuizInfo optionInfo = new QuizInfo(options);
                        databaseQuizzes.add(optionInfo).addOnSuccessListener(documentReference -> {
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });*/
                        mquestionNumber.setText(Integer.toString(count));
                        mquestion.getText().clear();
                        mcorrectAnswer.getText().clear();
                        mfalseAnswer1.getText().clear();
                        mfalseAnswer2.getText().clear();
                        mfalseAnswer3.getText().clear();
                        message = "Question " +Integer.toString(count);
                    } else {
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

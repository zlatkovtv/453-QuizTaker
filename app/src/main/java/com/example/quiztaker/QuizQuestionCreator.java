package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QuizQuestionCreator extends AppCompatActivity {
    private Button mQuit;
    private Button mNext;
    private TextView mquestionNumber;
    private EditText mcorrectAnswer;
    private EditText mfalseAnswer1;
    private EditText mfalseAnswer2;
    private EditText mfalseAnswer3;
    private EditText mquestion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question_creator);
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
            String inputQuestion = "";
            String inputCorrectAnswer = "";
            String inputfalseAnswer1 = "";
            String inputfalseAnswer2 = "";
            String inputfalseAnswer3 = "";
            String message = "";
            @Override
            public void onClick(View v) {
                inputQuestion = mquestion.getText().toString();
                inputCorrectAnswer = mcorrectAnswer.getText().toString();
                inputfalseAnswer1 = mfalseAnswer1.getText().toString();
                inputfalseAnswer2 = mfalseAnswer2.getText().toString();
                inputfalseAnswer3 = mfalseAnswer3.getText().toString();
                if (inputQuestion.equals("") || inputCorrectAnswer.equals("") || inputfalseAnswer1.equals("") || inputfalseAnswer2.equals("") || inputfalseAnswer3.equals("")) {
                message = "Please fill out the information";
                }
                else {
                    count++;
                    if (count <= 10) {
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

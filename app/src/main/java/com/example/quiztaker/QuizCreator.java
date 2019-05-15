package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiztaker.models.QuizCreateData;
import com.example.quiztaker.models.QuizData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizCreator extends AppCompatActivity {
    private Button mSave;
    private Button mCancel;
    private EditText mquizName;
    private FirebaseFirestore database;
    private String quizID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_creator);
        database = FirebaseFirestore.getInstance();
        mSave = (Button) findViewById(R.id.save);
        mCancel = (Button) findViewById(R.id.cancel);
        mquizName = (EditText) findViewById(R.id.quizName);
        mSave.setOnClickListener(new View.OnClickListener() {
            String message = "";

            public void onClick(View v) {
                if(!mquizName.getText().toString().matches("")){
                    String quizname = mquizName.getText().toString();
                    CollectionReference ref1 = database.collection("Quizzes");
                    message = "Question 1";
                    QuizCreateData data = new QuizCreateData( "Admin 1", quizname);
                    ref1.document(quizname).set(data);
                    Intent intent = new Intent(QuizCreator.this, QuizQuestionCreator.class);
                    intent.putExtra("quizname", quizname);
                    startActivity(intent);
                }
                else{
                    message = "Please enter a quiz name";
                }
                Toast.makeText(QuizCreator.this, message, Toast.LENGTH_LONG).show();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizCreator.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}

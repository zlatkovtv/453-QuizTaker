package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QuizCreator extends AppCompatActivity {
    private Button mSave;
    private Button mCancel;
    private EditText mquizName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_creator);
        mSave = (Button) findViewById(R.id.save);
        mCancel = (Button) findViewById(R.id.cancel);
        mquizName = (EditText) findViewById(R.id.quizName);
        mSave.setOnClickListener(new View.OnClickListener() {
            String message = "";
            public void onClick(View v) {
                if(!mquizName.getText().toString().matches("")){
                    message = "Saved information";
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
                Intent intent = new Intent(QuizCreator.this, QuizListFragment.class);
                startActivity(intent);
            }
        });

    }

}

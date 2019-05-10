package com.example.quiztaker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiztaker.models.QuizOption;
import com.example.quiztaker.models.QuizQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QuizTakingFragment extends Fragment {
    private String quizName;
    private CollectionReference questionsRef;
    private List<QuizQuestion> questions;
    private int currentQuestionIndex;
    private List<Boolean> results;

    private TextView questionName;
    private LinearLayout container;

    public QuizTakingFragment() {
        // Mock id
        this.quizName = "Quiz 1";
        this.currentQuestionIndex = 0;
        this.questions = new ArrayList<>();
        this.results = new ArrayList<>();
        this.questionsRef = FirebaseFirestore
                .getInstance()
                .collection("Quizzes")
                .document(this.quizName)
                .collection("Questions");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_taking, container, false);

        this.questionName = view.findViewById(R.id.questionName);
        this.container = view.findViewById(R.id.container);
        this.getQuestions();
        return view;
    }

    private void getQuestions() {
        questionsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                            questions.add(new QuizQuestion(document.getId(), document.get("questionString").toString(), new ArrayList<QuizOption>()));
                        }

                        getNextOptions();
                    }
        });
    }

    private void getNextOptions() {
        if((container).getChildCount() > 0) {
            (container).removeAllViews();
        }

        String questionId = questions.get(currentQuestionIndex).getId();
        questionName.setText(questions.get(currentQuestionIndex).getQuestionName());

        questionsRef
                .document(questionId)
                .collection("Options")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                String optionText = document.get("optionName").toString();
                                boolean isCorrect = (boolean) document.get("isCorrect");
                                addButton(optionText, isCorrect);
                            }

                        } else {
                            Exception a = task.getException();
                        }

                    }
                });
    }

    private void addButton(String optionText, boolean isCorrect) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.Button_Center_Primary);
        Button button = new Button(wrapper, null, 0);
        button.setText(optionText);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        lp.setMargins(0, 0, 0, 20);
        button.setLayoutParams(lp);

        if(isCorrect) {
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer(true);
                }
            });
        } else {
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer(false);
                }
            });
        }

        this.container.addView(button);
    }

    private void answer(boolean answer) {
        results.add(answer);
        currentQuestionIndex++;
        if(currentQuestionIndex >= questions.size()) {
            List<Boolean> correct = results.stream()
                    .filter(line -> line == true)
                    .collect(Collectors.toList());
            String message = String.format("You got %d out of %d correct", correct.size(), results.size());
            Toast.makeText(getActivity(), message,
                    Toast.LENGTH_LONG).show();
            return;
        }

        getNextOptions();
    }

}
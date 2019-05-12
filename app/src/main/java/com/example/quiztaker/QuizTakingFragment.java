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
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;


public class QuizTakingFragment extends BaseFragment {
    private String quizName;
    private CollectionReference questionsRef;
    private List<QuizQuestion> questions;
    private int currentQuestionIndex;
    private Stack<Boolean> results;
    private TextView questionName;
    private LinearLayout container;
    private ProgressBar progressBar;

    public QuizTakingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.quizName = bundle.getString("ID");
        }

        this.currentQuestionIndex = 0;
        this.questions = new ArrayList<>();
        this.results = new Stack<>();
        this.questionsRef = FirebaseFirestore
                .getInstance()
                .collection("Quizzes")
                .document(this.quizName)
                .collection("Questions");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_taking, container, false);

        this.questionName = view.findViewById(R.id.questionName);
        this.container = view.findViewById(R.id.container);
        this.progressBar = view.findViewById(R.id.question_spinner);
        this.progressBar.setVisibility(ProgressBar.VISIBLE);
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

                    if(questions.size() == 0) {
                        popNoQuestionsToast();
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        return;
                    }

                    getNextOptions();
                }
        });
    }

    /**
     * Pops a toast if quiz has no questions
     */
    private void popNoQuestionsToast() {
        Toast.makeText(getActivity(), "Quiz has no questions.",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Gets the options of the next unanswered question, in a random order
     */
    private void getNextOptions() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
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
                            List<QueryDocumentSnapshot> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                list.add(document);
                            }

                            Collections.shuffle(list);
                            for (QueryDocumentSnapshot document: list) {
                                String optionText = document.get("optionName").toString();
                                boolean isCorrect = (boolean) document.get("isCorrect");
                                addButton(optionText, isCorrect);
                            }

                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }
                });
    }

    /**
     * Adds a button corresponding to a possible option
     * @param optionText The text of the button
     * @param isCorrect Whether it is correct or not
     */
    private void addButton(String optionText, boolean isCorrect) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.Button_Center_Primary);
        Button button = new Button(wrapper, null, 0);
        button.setText(optionText);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        lp.setMargins(0, 0, 0, 20);
        button.setElevation(8);
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

    /**
     * When an option/button is clicked, this method is invoked.
     * Stores the answered value in a list
     * @param answer The answer as a boolean value (whether it is correct or not)
     */
    private void answer(boolean answer) {
        results.push(answer);
        currentQuestionIndex++;
        if(currentQuestionIndex >= questions.size()) {
            List<Boolean> correct = results.stream()
                    .filter(line -> line == true)
                    .collect(Collectors.toList());
            goToResults(toPrimitiveArray(results), toPrimitiveArray(correct));
            return;
        }

        getNextOptions();
    }

    /**
     * Navigates to the results view
     * @param results All results to be passed to results fragment
     * @param correct Correct results to be passed to results fragment
     */
    private void goToResults(boolean[] results, boolean[] correct) {
        Fragment frag = new QuizResultsFragment();
        Bundle args = new Bundle();
        args.putBooleanArray("results", results);
        args.putBooleanArray("correct", correct);
        frag.setArguments(args);
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Converts a list of booleans to an array of booleans
     * @param booleanList
     * @return
     */
    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

    @Override
    public boolean onBackPressed(){
        if(currentQuestionIndex == 0) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }

        results.pop();
        currentQuestionIndex--;
        getNextOptions();

        return true;
    }
}

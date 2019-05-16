package com.example.quiztaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Fragment that is inflated when a user completes the quiz.
 * Purpose is to show results - how many they got correct.
 */
public class QuizResultsFragment extends BaseFragment implements View.OnClickListener {
    private boolean[] results;
    private boolean[] correct;
    private TextView resultsMessage;
    private GridView iconContainer;
    private Button closeResultsButton;

    public QuizResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.results = bundle.getBooleanArray("results");
            this.correct = bundle.getBooleanArray("correct");
        }

        View view = inflater.inflate(R.layout.fragment_quiz_results, container, false);
        getViews(view);
        this.closeResultsButton.setOnClickListener(this);
        this.iconContainer.setAdapter(new QuizResultsAdapter(getContext(), results));
        this.resultsMessage.setText(getMessage(results.length, correct.length));
        return view;
    }

    @Override
    public boolean onBackPressed() {
        navigateToQuizList();
        return true;
    }

    /**
     * Gets views needed for this fragment
     * @param view
     */
    private void getViews(View view) {
        this.resultsMessage = view.findViewById(R.id.results_message);
        this.iconContainer = view.findViewById(R.id.icon_container);
        this.closeResultsButton = view.findViewById(R.id.button_close_results);
    }

    /**
     * Gets the title of the fragment depending on the ratio of the total and correct answers
     * @param total The total number of answers
     * @param correct Only the answers that are "true"
     * @return
     */
    private String getMessage(int total, int correct) {
        double percent = correct / (double) total;
        if(percent  == 0) {
            return getResources().getString(R.string.quiz_results_bad);
        }

        if(percent <= 0.5) {
            return getResources().getString(R.string.quiz_results_ok);
        }

        if(percent <= 0.99) {
            return getResources().getString(R.string.quiz_results_good);
        }

        if(percent <= 1) {
            return getResources().getString(R.string.quiz_results_excellent);
        }

        return "Quiz completed!";
    }

    /**
     * Closes the fragment and navigates to the Quiz List Fragment
     * @param view
     */
    public void closeResults(View view) {
        navigateToQuizList();
    }

    private void navigateToQuizList() {
        Fragment frag = new QuizListFragment();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_close_results:
                closeResults(v);
                break;
        }
    }
}

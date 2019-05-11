package com.example.quiztaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

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
        this.resultsMessage = view.findViewById(R.id.results_message);
        this.iconContainer = view.findViewById(R.id.icon_container);
        this.closeResultsButton = view.findViewById(R.id.button_close_results);
        this.closeResultsButton.setOnClickListener(this);
        this.iconContainer.setAdapter(new QuizResultsAdapter(getContext(), results));
        this.resultsMessage.setText(getMessage(results.length, correct.length));
        return view;
    }

    private String getMessage(int total, int correct) {
        double percent = correct / (double) total;
        if(percent  == 0) {
            return getResources().getString(R.string.quiz_results_bad);
        }

        if(correct <= 0.5) {
            return getResources().getString(R.string.quiz_results_ok);
        }

        if(correct <= 0.99) {
            return getResources().getString(R.string.quiz_results_good);
        }

        if(correct <= 1) {
            return getResources().getString(R.string.quiz_results_excellent);
        }

        return null;
    }

    public void closeResults(View view) {
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

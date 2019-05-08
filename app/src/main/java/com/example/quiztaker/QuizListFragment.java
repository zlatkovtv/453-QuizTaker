package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QuizListFragment extends Fragment {
    private static final String TAG = "QuizListFragment";
    RecyclerView mRecyclerView;
    List<QuizData> mQuizList;
    QuizData quizData;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_quiz_list, container, false);

        mRecyclerView = view.findViewById(R.id.quizListRecyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mQuizList = new ArrayList<QuizData>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Quizzes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mQuizList.add(new QuizData((String) document.get("quizTitle"), (String) document.get("quizCreator")));
                                Log.i(TAG, "Index: ");
                            }
                            QuizListAdapter mAdapter = new QuizListAdapter(view.getContext(), mQuizList);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        /*
        // Populate with sample data
        mQuizList.add(new QuizData("Quiz 2", "c"));
        mQuizList.add(new QuizData("Stuff Quiz", "c"));
        mQuizList.add(new QuizData("Quiz 5", "c"));
        mQuizList.add(new QuizData("Quiz 6", "c"));
        mQuizList.add(new QuizData("Quiz 7", "c"));
        mQuizList.add(new QuizData("Quiz 8", "c"));
        mQuizList.add(new QuizData("Quiz 9", "c"));
        mQuizList.add(new QuizData("Quiz 10", "c"));
        mQuizList.add(new QuizData("Quiz 11", "c"));
        mQuizList.add(new QuizData("Quiz 12", "c"));
        mQuizList.add(new QuizData("Quiz 13", "c"));
        */






        FloatingActionButton mfab = (FloatingActionButton) view.findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizCreator.class);
                startActivity(intent);
                }
        });

        return view;
    }
}

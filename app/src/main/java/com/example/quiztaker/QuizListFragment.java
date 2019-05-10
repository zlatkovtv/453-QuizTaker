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

import com.example.quiztaker.models.QuizData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QuizListFragment extends Fragment {
    private static final String TAG = "QuizListFragment";
    private RecyclerView mRecyclerView;
    private FloatingActionButton mfab;
    private List<QuizData> mQuizList;

    private FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_quiz_list, container, false);

        mRecyclerView = view.findViewById(R.id.quizListRecyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mfab = (FloatingActionButton) view.findViewById(R.id.fab);
        mfab.hide();

        mQuizList = new ArrayList<QuizData>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Quizzes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mQuizList.add(new QuizData(document.getId(), document.getString("quizTitle"), document.getString("quizCreator")));
                            }
                            QuizListAdapter.ItemClickListener listener = (view, position) -> {
                                //Code to start quiz
                                Toast.makeText(getContext(), mQuizList.get(position).getQuizName(), Toast.LENGTH_SHORT).show();

                                Fragment frag = new QuizTakingFragment();
                                Bundle args = new Bundle();
                                args.putString("ID", mQuizList.get(position).getID());
                                frag.setArguments(args);
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, frag)
                                        .addToBackStack(null)
                                        .commit();
                            };
                            QuizListAdapter mAdapter = new QuizListAdapter(getActivity(), mQuizList, listener);

                            mRecyclerView.setAdapter(mAdapter);

                        } else {
                            Log.w(TAG, "Error getting quiz documents.", task.getException());
                        }
                    }
                });

        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        db.collection("Users")
                .document(loggedUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getBoolean("isAdmin"))
                            {
                                mfab.show();
                                mfab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), QuizCreator.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            Log.w(TAG, "Error getting user document.", task.getException());
                            mfab.hide();
                        }
                    }
                });



        return view;
    }
}

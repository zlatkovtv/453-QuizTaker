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

/**
 * Fragment to get and display list of quizzes
 */
public class QuizListFragment extends BaseFragment {
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

        //Retrieves quiz information from Firestore
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

                            //Set each quiz card to open quiz menu for the specified quiz
                            QuizListAdapter.ItemClickListener listener = (view, position) -> {
                                Fragment frag = new QuizMenuFragment();
                                Bundle args = new Bundle(); //Quiz info to pass to new fragment
                                args.putString("ID", mQuizList.get(position).getID());
                                args.putString("Title", mQuizList.get(position).getQuizName());
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

        //Get user info to decide whether to show fab
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
                                        //Starts QuizQuestionCreator activity
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

    /**
     * Leave empty, as this is default fragment and the event needs to be handled by Home Activity
     * @return
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}

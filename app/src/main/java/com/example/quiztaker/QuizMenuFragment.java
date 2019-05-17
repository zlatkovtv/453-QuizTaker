package com.example.quiztaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Fragment that provides controls to start or delete a quiz
 */
public class QuizMenuFragment extends BaseFragment {
    private static final String TAG = "QuizMenuFragment";
    private LinearLayout mLinearLayout;
    private TextView mTextViewTitle;
    private Button mButtonStart, mButtonDelete;
    private String quizID;
    private String quizTitle;

    private FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        //Gets and saves info passed from old fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.quizID = bundle.getString("ID");
            this.quizTitle = bundle.getString("Title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_quiz_menu, container, false);
        mLinearLayout = view.findViewById(R.id.quiz_menu_linear_layout);

        mButtonStart = view.findViewById(R.id.quiz_menu_button_start);
        mButtonDelete = view.findViewById(R.id.quiz_menu_button_delete);
        mTextViewTitle = view.findViewById(R.id.quiz_menu_title);

        mTextViewTitle.setText(quizTitle);

        //Set up listener for start button
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsSetEnabled(false);
                Fragment frag = new QuizTakingFragment();
                Bundle args = new Bundle();
                args.putString("ID", quizID);
                frag.setArguments(args);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, frag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Set up listener for delete button
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsSetEnabled(false);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Quizzes")
                        .document(quizID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Fragment frag = new QuizListFragment();
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, frag)
                                        .commit();
                            }
                        });
            }
        });

        return view;
    }

    //Disables buttons to not be clickable
    public void buttonsSetEnabled(boolean bool)
    {
        mButtonStart.setEnabled(bool);
        mButtonDelete.setEnabled(bool);
    }

    //Invoked when user presses back button to go to popped fragment from stack.
    @Override
    public boolean onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}

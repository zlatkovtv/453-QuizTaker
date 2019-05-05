package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class QuizListFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_quiz_list, container, false);

        FloatingActionButton mfab = (FloatingActionButton) view.findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            String message = "";
            @Override
            public void onClick(View view) {
                message = "Clicked";
                //Not sure why this is not working, change intent to QuizCreator
               /* Intent intent = new Intent(getActivity(), QuizCreator.class);
                startActivity(intent);*/
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

        });
        return view;
    }
}

package com.example.quiztaker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {
    private Context mContext;
    private List<QuizData> mQuizList;

    public QuizListAdapter(Context context, List<QuizData> quizList)
    {
        this.mContext = context;
        this.mQuizList = quizList;
    }

    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quizlist_item_row,viewGroup,false);
        return new QuizListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListViewHolder quizListViewHolder, int i) {
        quizListViewHolder.mTitle.setText(mQuizList.get(i).getQuizName());
    }

    @Override
    public int getItemCount() {
        return mQuizList.size();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        public QuizListViewHolder(View itemView)
        {
            super(itemView);

            mTitle = itemView.findViewById(R.id.quizTitle);
        }
    }
}

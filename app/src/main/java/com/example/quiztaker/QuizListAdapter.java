package com.example.quiztaker;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiztaker.models.QuizData;

import java.util.List;

/**
 * Adapter for quiz list in Quiz List Fragment
 */
public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {
    private Context mContext;
    private List<QuizData> mQuizList;
    private ItemClickListener mListener;

    /**
     * Constructor
     */
    public QuizListAdapter(Context context, List<QuizData> quizList, ItemClickListener listener)
    {
        this.mContext = context;
        this.mQuizList = quizList;
        mListener = listener;
    }


    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quizlist_item_row,viewGroup,false);
        return new QuizListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListViewHolder quizListViewHolder, int i) {
        quizListViewHolder.mTitle.setText(mQuizList.get(i).getQuizName());
    }

    /**
     * Displays toast using a given string
     * @param str   string to display
     */
    private void showToast(String str)
    {
        Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public int getItemCount() {
        return mQuizList.size();
    }

    //Listener interface for when a quiz item is clicked
    public interface ItemClickListener
    {
        void OnClick(View view, int position);
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitle;
        private ItemClickListener mListener;
        public QuizListViewHolder(View itemView, ItemClickListener listener)
        {
            super(itemView);
            mTitle = itemView.findViewById(R.id.quizTitle);

            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(v, getAdapterPosition());
        }
    }
}

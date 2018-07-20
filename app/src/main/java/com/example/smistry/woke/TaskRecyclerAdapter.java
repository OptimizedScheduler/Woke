package com.example.smistry.woke;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smistry.woke.models.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>{
    Context context;
    private List<Task> mTasks;

    public TaskRecyclerAdapter(List<Task> tasks){
        mTasks=tasks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_task,viewGroup,false);
        ViewHolder holder = new ViewHolder(postView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Task task = mTasks.get(i);
        viewHolder.tvTaskName.setText(task.getTaskTitle());
        viewHolder.tvTime.setText(task.getTime().toString());
        if(task.getCategory()=="work")
            viewHolder.ivCategory.setBackgroundResource(R.color.colorPrimary);
        else
            viewHolder.ivCategory.setBackgroundResource(R.color.colorAccent);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        @BindView(R.id.tvTask) TextView tvTaskName;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.ivCategory)
        ImageView ivCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

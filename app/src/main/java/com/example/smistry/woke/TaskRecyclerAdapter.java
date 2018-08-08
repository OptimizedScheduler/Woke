package com.example.smistry.woke;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.smistry.woke.models.Task;

import java.sql.Time;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if(mTasks!= null){
        Task task = mTasks.get(i);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTasks.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i,mTasks.size());
                return false;
            }
        });

            RequestOptions options = new RequestOptions();
            options.centerCrop();

            viewHolder.tvTaskName.setText(task.getTaskTitle());
        viewHolder.tvCategory.setText(task.getCategory());
        viewHolder.tvTime.setText(task.getTime().toString().substring(0,task.getTime().toString().length()-3)+" - ");
        int endHr = task.getTime().getHours()*60+task.getTime().getMinutes()+task.getDuration();
        Time endTime = new Time(endHr/60,endHr%60,00);
        viewHolder.tvTimeEnd.setText(endTime.toString().substring(0,endTime.toString().length()-3));
        if(task.getCategory().equals("Entertainment"))
            Glide.with(context)
                    .load(R.drawable.orange0)
                    .apply(options)
                    .into(viewHolder.ivCategory);
        else if (task.getCategory().equals("Fitness"))
            Glide.with(context)
                    .load(R.drawable.orange1)
                    .apply(options)
                    .into(viewHolder.ivCategory);
        else if (task.getCategory().equals("Work"))
            Glide.with(context)
                    .load(R.drawable.orange2)
                    .apply(options)
                    .into(viewHolder.ivCategory);
        else if (task.getCategory().equals("Social"))
            Glide.with(context)
                    .load(R.drawable.orange3)
                    .apply(options)
                    .into(viewHolder.ivCategory);
        else
            Glide.with(context)
                    .load(R.drawable.orange4)
                    .apply(options)
                    .into(viewHolder.ivCategory);
        }
        else{
            viewHolder.tvTaskName.setText("L");
            viewHolder.tvTime.setText("L");
        }

        if (mTasks.get(i).isCompleted()){
            viewHolder.tvTaskName.setChecked(true);
        }
        else{
            viewHolder.tvTaskName.setChecked(false);
        }

        viewHolder.tvTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.tvTaskName.isChecked()){
                    viewHolder.tvTaskName.setChecked(false);
                    mTasks.get(i).setCompleted(false);
                }
                else {
                    viewHolder.tvTaskName.setChecked(true);
                    mTasks.get(i).setCompleted(true);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mTasks!=null)
        return mTasks.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        @BindView(R.id.tvTask) CheckedTextView tvTaskName;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.ivCategory) ImageView ivCategory;
        @BindView(R.id.tvTimeEnd) TextView tvTimeEnd;
        @BindView(R.id.tvCategory) TextView tvCategory;


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

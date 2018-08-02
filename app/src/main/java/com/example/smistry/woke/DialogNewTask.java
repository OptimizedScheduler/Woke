package com.example.smistry.woke;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DialogNewTask extends DialogFragment {
    boolean nextMorning;
    TextView tvAct1,tvAct2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dialog_new_task,container,false);
        tvAct1=view.findViewById(R.id.tvAct1);
        tvAct2=view.findViewById(R.id.tvAct2);

        tvAct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DIALOG", "Act 1");
            }
        });

        tvAct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DIALOG", "Act 1");
                ((newTask)getActivity()).nextMorning=false;

               // getDialog().dismiss();
            }


        });

        return view;
    }
}

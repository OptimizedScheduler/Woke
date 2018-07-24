package com.example.smistry.woke;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class settingsFreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<String>DOW;
    HashMap<String, ArrayList<Free>> weekFrees;
//    ArrayList<Free> enteredFrees;

    public settingsFreeAdapter(ArrayList<String> DOW, HashMap<String, ArrayList<Free>> weekFrees) {
        this.DOW = DOW;
        this.weekFrees = weekFrees;
        Log.d("SettingsAdapterInnit", weekFrees.toString());
    }


    @Override
    public int getItemCount() {
        return DOW.size();
    }

    public class editInterval extends RecyclerView.ViewHolder {
        public EditText startTime;
        public EditText endTime;
        public TextView tvDOW;
        public TextView enteredFrees;
        public Button send;

        public editInterval(View itemView) {
            super(itemView);
            startTime= (EditText)itemView.findViewById(R.id.etStartTime);
            endTime= (EditText)itemView.findViewById(R.id.etEndTime);
            tvDOW= (TextView)itemView.findViewById(R.id.tvDOW);
            enteredFrees= (TextView) itemView.findViewById(R.id.tvFreeBlocks);
            send= (Button) itemView.findViewById(R.id.btSend);
        }
    }

    public class viewInterval extends RecyclerView.ViewHolder {
        TextView numberFree;
        TextView startFree;
        TextView endFree;

        public viewInterval(View itemView) {
            super(itemView);
            numberFree= (TextView)itemView.findViewById(R.id.tvIntervalNumber);
            startFree= (TextView)itemView.findViewById(R.id.tvStartTimeInterval);
            endFree= (TextView)itemView.findViewById(R.id.tvEndTimeInterval);

        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        int MondaySize=weekFrees.get("Monday").size();
//        int TuesdaySize=weekFrees.get("Tuesday").size();
//        int WednesdaySize=weekFrees.get("Wednesday").size();
//        int ThursdaySize=weekFrees.get("Thursday").size();
//        int FridaySize=weekFrees.get("Friday").size();
//        int SaturdaySize=weekFrees.get("Saturday").size();
//        int SundaySize=weekFrees.get("Sunday").size();
//
//
//        //first editText day;
//        if (position == 0){
//            return 0;
//        }
//        else if (position<=MondaySize){
//            return 2;
//        }
//
//        else if (position== MondaySize +1){
//            return 0;
//        }
//        else if (position<= MondaySize+TuesdaySize+1){
//            return 2;
//        }
//        else if (position ==MondaySize+TuesdaySize+2 ){
//            return 0;
//        }
//        else if (position<= MondaySize+TuesdaySize+WednesdaySize+2){
//            return 2;
//        }
//        else if (position== MondaySize+TuesdaySize+WednesdaySize+3){
//            return 0;
//        }
//        else if (position<= MondaySize+TuesdaySize+WednesdaySize+ThursdaySize+3){
//            return 2;
//        }
//
//        // Just as an example, return 0 or 2 depending on position
//        // Note that unlike in ListView adapters, types don't have to be contiguous
//        return position % 2 * 2;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 0:
                Log.d("SettingsAdapterBegin", weekFrees.toString());

                context= parent.getContext();
                LayoutInflater inflater= LayoutInflater.from(context);

                View editView= inflater.inflate(R.layout.fixed_time_picker_item, parent, false);
                RecyclerView.ViewHolder viewHolder= new editInterval(editView);
                return viewHolder;
//            case 2:
//                context= parent.getContext();
//                LayoutInflater inflater2= LayoutInflater.from(context);
//
//                View textView= inflater2.inflate(R.layout.item_free_interval, parent, false);
//                RecyclerView.ViewHolder viewHolder2= new viewInterval(textView);
//                return viewHolder2;
//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
//        switch (viewHolder.getItemViewType()) {
//            case 0:
                final editInterval viewHolder0 = (editInterval) viewHolder;
                viewHolder0.tvDOW.setText(DOW.get(i));
                String frees="";

                for(Free free: weekFrees.get(DOW.get(i))){
                    frees+=free.toString()+" ";
                }

                ((editInterval) viewHolder).enteredFrees.setText(frees);


                viewHolder0.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String startTime = viewHolder0.startTime.getText().toString();
                        String endTime = viewHolder0.endTime.getText().toString();

                        //check if there is any input
                        if (startTime.equals(null)) {
                            Toast.makeText(context, "Please enter the start time of your free duration", Toast.LENGTH_SHORT).show();
                            }
                        else if (endTime.equals(null)) {
                            Toast.makeText(context, "Please enter the end time of your free duration", Toast.LENGTH_SHORT).show();
                            }

                        else {
                            String[] startSplit = startTime.split(":");
                            String[] endSplit = endTime.split(":");


                            //check if there is any input
                            if (startTime.equals("")) {
                                Toast.makeText(context, "Please enter the start time of your free duration", Toast.LENGTH_SHORT).show();
                            } else if (endTime.equals("")) {
                                Toast.makeText(context, "Please enter the end time of your free duration", Toast.LENGTH_SHORT).show();
                            }


                            //check to make sure there is no characters inputted
                            else if (startTime.contains("[a-zA-Z]+") == true) {
                                Toast.makeText(context, "Please enter only numbers into the start time of your free duration", Toast.LENGTH_SHORT).show();
                            } else if (endTime.contains("[a-zA-Z]+") == true) {
                                Toast.makeText(context, "Please enter only numbers into the end time of your free duration", Toast.LENGTH_SHORT).show();

                            }

                            //check if the time entered is valid
                            else if (startTime.split(":").length != 2) {
                                Toast.makeText(context, "Please enter a valid start time of your free duration", Toast.LENGTH_SHORT).show();
                            } else if (endTime.split(":").length != 2) {
                                Toast.makeText(context, "Please enter a valid end time of your free duration", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                // check the time entered is valid
                                Time start = new Time(Integer.valueOf(startSplit[0]), Integer.valueOf(startSplit[1]), 0);
                                Time end = new Time(Integer.valueOf(endSplit[0]), Integer.valueOf(endSplit[1]), 0);
                                int duration = (int) ((start.getTime() - end.getTime()) / 60000) * -1;

                                if (Integer.valueOf(startSplit[1]) > 59 || Integer.valueOf(startSplit[0]) > 24) {
                                    Toast.makeText(context, "Please enter a valid start time of your free duration", Toast.LENGTH_SHORT).show();
                                } else if (Integer.valueOf(endSplit[1]) > 59 || Integer.valueOf(endSplit[0]) > 24) {
                                    Toast.makeText(context, "Please enter a valid end time of your free duration", Toast.LENGTH_SHORT).show();
                                }

                                //check that the end duration comes after the start time
                                else if (duration < 0) {
                                    Toast.makeText(context, "Please enter an end time that comes after the start time ", Toast.LENGTH_SHORT).show();

                                } else if (duration == 0) {
                                    Toast.makeText(context, "Please enter a different end and start time ", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(context, "Free Made!", Toast.LENGTH_SHORT).show();
                                    viewHolder0.startTime.setText("");
                                    viewHolder0.endTime.setText("");

                                    Free toAdd=new Free(new ArrayList<Task>(), start, end, duration);
                                    //add the new task into the hashMap
                                    SettingsActivity.addFree(DOW.get(i),toAdd );


                                    //weekFrees.get(DOW.get(i)).add(toAdd);
                                    String frees = "";
                                    for (Free free : weekFrees.get(DOW.get(i))) {
                                        frees += free.toString() + " ";
                                    }
                                    viewHolder0.enteredFrees.setText(frees);
                                    Log.d("SettingsAdapter", weekFrees.toString());


                                }
                            }
                        }
                    }
                });


//                break;
//            case 2:
//                viewInterval viewHolder2 = (viewInterval) viewHolder;
//
//                break;
//        }

    }


}

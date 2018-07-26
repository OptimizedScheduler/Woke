//package com.example.smistry.woke;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.smistry.woke.models.Free;
//import com.example.smistry.woke.models.Task;
//
//import java.sql.Time;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class settingsFreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    Context context;
//    ArrayList<String> DOW;
//    ArrayList<ArrayList<Free>> weekFrees;
////    ArrayList<Free> enteredFrees;
//
//
//    public int MondaySize;
//    public int TuesdaySize;
//    public int WednesdaySize;
//    public int ThursdaySize;
//    public int FridaySize;
//    public int SaturdaySize;
//    public int SundaySize;
//
//    public int MondayPosition;
//    public int TuesdayPosition;
//    public int WednesdayPosition;
//    public int ThursdayPosition;
//    public int FridayPosition;
//    public int SaturdayPosition;
//    public int SundayPosition;
//
//
//    public settingsFreeAdapter(ArrayList<String> DOW,  ArrayList<ArrayList<Free>>  weekFrees) {
//        this.DOW = DOW;
//        this.weekFrees = weekFrees;
//        calcPositions();
//
//        Log.d("SettingsAdapterInnit", weekFrees.toString());
//
//    }
//
//    public void calcPositions() {
//        MondaySize = weekFrees.get(1).size();
//        TuesdaySize = weekFrees.get(2).size();
//        WednesdaySize = weekFrees.get(3).size();
//        ThursdaySize = weekFrees.get(4).size();
//        FridaySize = weekFrees.get(5).size();
//        SaturdaySize = weekFrees.get(6).size();
//        SundaySize = weekFrees.get(0).size();
//
//        MondayPosition = 0;
//        TuesdayPosition = MondaySize + 1;
//        WednesdayPosition = MondaySize + TuesdaySize + 2;
//        ThursdayPosition = MondaySize + TuesdaySize + WednesdaySize + 3;
//        FridayPosition = MondaySize + TuesdaySize + WednesdaySize + ThursdaySize + 4;
//        SaturdayPosition = MondaySize + TuesdaySize + WednesdaySize + ThursdaySize + FridaySize + 5;
//        SundayPosition = MondaySize + TuesdaySize + WednesdaySize + ThursdaySize + FridaySize + SaturdaySize + 6;
//    }
//
//    @Override
//    public int getItemCount() {
//        int size = 7;
//        for (int i=0; i<weekFrees.size(); i++) {
//            size += weekFrees.get(i).size();
//        }
//        return size;
//    }
//
//    public class editInterval extends RecyclerView.ViewHolder {
//        public EditText startTime;
//        public EditText endTime;
//        public TextView tvDOW;
//        public TextView enteredFrees;
//        public Button send;
//
//        public TextView tvStartTime;
//        public TextView tvEndTime;
//        public Button delete;
//
//        public editInterval(View itemView) {
//            super(itemView);
//            startTime = (EditText) itemView.findViewById(R.id.etStartTime);
//            endTime = (EditText) itemView.findViewById(R.id.etEndTime);
//            tvDOW = (TextView) itemView.findViewById(R.id.tvDOW);
//            enteredFrees = (TextView) itemView.findViewById(R.id.tvFreeBlocks);
//            send = (Button) itemView.findViewById(R.id.btSend);
//
//            tvStartTime = (TextView) itemView.findViewById(R.id.tvStartTime);
//            tvEndTime = (TextView) itemView.findViewById(R.id.tvEndTime);
//            delete = (Button) itemView.findViewById(R.id.btDelete);
//        }
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("SettingsAdapterBegin", weekFrees.toString());
//        context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View editView = inflater.inflate(R.layout.fixed_time_picker_item, parent, false);
//        RecyclerView.ViewHolder viewHolder = new editInterval(editView);
//        return viewHolder;
//
//    }
//
//    public void editMode(editInterval viewHolder0) {
//        viewHolder0.startTime.setEnabled(true);
//        viewHolder0.endTime.setEnabled(true);
//        viewHolder0.send.setEnabled(true);
//        viewHolder0.enteredFrees.setEnabled(true);
//        viewHolder0.send.setVisibility(View.VISIBLE);
//        viewHolder0.startTime.setVisibility(View.VISIBLE);
//        viewHolder0.endTime.setVisibility(View.VISIBLE);
//
//        viewHolder0.tvStartTime.setVisibility(View.INVISIBLE);
//        viewHolder0.tvEndTime.setVisibility(View.INVISIBLE);
//        viewHolder0.delete.setVisibility(View.INVISIBLE);
//        viewHolder0.tvStartTime.setEnabled(false);
//        viewHolder0.tvEndTime.setEnabled(false);
//        viewHolder0.delete.setEnabled(false);
//    }
//
//    public void viewMode(editInterval viewHolder0) {
//        viewHolder0.startTime.setEnabled(false);
//        viewHolder0.endTime.setEnabled(false);
//        viewHolder0.send.setEnabled(false);
//        viewHolder0.send.setVisibility(View.INVISIBLE);
//        viewHolder0.startTime.setVisibility(View.INVISIBLE);
//        viewHolder0.endTime.setVisibility(View.INVISIBLE);
//
//        viewHolder0.tvStartTime.setVisibility(View.VISIBLE);
//        viewHolder0.tvEndTime.setVisibility(View.VISIBLE);
//        viewHolder0.delete.setVisibility(View.VISIBLE);
//        viewHolder0.tvStartTime.setEnabled(true);
//        viewHolder0.tvEndTime.setEnabled(true);
//        viewHolder0.delete.setEnabled(true);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
//        final editInterval viewHolder0 = (editInterval) viewHolder;
//
//
//        String frees = "";
//        if (i < TuesdayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(0));
//            if (i == MondayPosition) {
//                editMode(viewHolder0);
//
//                for (Free free : weekFrees.get(1)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//                Free toFill = weekFrees.get(1).get(i - 1);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//            }
//
//        } else if (i < WednesdayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(1));
//            if (i == TuesdayPosition) {
//                editMode(viewHolder0);
//
//                for (Free free : weekFrees.get(2)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(2).get(i - MondaySize - 2);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//            }
//        } else if (i < ThursdayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(2));
//            if (i == WednesdayPosition) {
//                editMode(viewHolder0);
//
//                for (Free free : weekFrees.get(3)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(3).get(i - MondaySize - TuesdaySize - 3);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//            }
//        } else if (i < FridayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(3));
//            if (i == ThursdayPosition) {
//                editMode(viewHolder0);
//
//                for (Free free : weekFrees.get(4)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(4).get(i - MondaySize - TuesdaySize - WednesdaySize - 4);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//
//            }
//        } else if (i < SaturdayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(4));
//            if (i == FridayPosition) {
//                editMode(viewHolder0);
//
//                for (Free free : weekFrees.get(5)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(5).get(i - MondaySize - TuesdaySize - WednesdaySize - ThursdaySize - 5);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//            }
//        } else if (i < SundayPosition) {
//            viewHolder0.tvDOW.setText(DOW.get(5));
//
//            if (i == SaturdayPosition) {
//                editMode(viewHolder0);
//
//
//                for (Free free : weekFrees.get(6)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(6).get(i - MondaySize - TuesdaySize - WednesdaySize - ThursdaySize - FridaySize - 6);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//            }
//        } else {
//            viewHolder0.tvDOW.setText(DOW.get(6));
//            if (i == SundayPosition) {
//                editMode(viewHolder0);
//
//
//                for (Free free : weekFrees.get(0)) {
//                    frees += free.toString() + " ";
//                }
//                ((editInterval) viewHolder).enteredFrees.setText(frees);
//            } else {
//                viewMode(viewHolder0);
//
//                Free toFill = weekFrees.get(0).get(i - MondaySize - TuesdaySize - WednesdaySize - ThursdaySize - FridaySize - SaturdaySize - 7);
//                viewHolder0.tvStartTime.setText(toFill.getStart().toString());
//                viewHolder0.tvEndTime.setText(toFill.getEnd().toString());
//                viewHolder0.enteredFrees.setText(toFill.getFreeBlockDuration() + " minutes");
//
//            }
//        }
//
//
//        viewHolder0.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        viewHolder0.send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String startTime = viewHolder0.startTime.getText().toString();
//                String endTime = viewHolder0.endTime.getText().toString();
//
//                //check if there is any input
//                if (startTime.equals(null)) {
//                    Toast.makeText(context, "Please enter the start time of your free duration", Toast.LENGTH_SHORT).show();
//                } else if (endTime.equals(null)) {
//                    Toast.makeText(context, "Please enter the end time of your free duration", Toast.LENGTH_SHORT).show();
//                } else {
//                    String[] startSplit = startTime.split(":");
//                    String[] endSplit = endTime.split(":");
//
//
//                    //check if there is any input
//                    if (startTime.equals("")) {
//                        Toast.makeText(context, "Please enter the start time of your free duration", Toast.LENGTH_SHORT).show();
//                    } else if (endTime.equals("")) {
//                        Toast.makeText(context, "Please enter the end time of your free duration", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                    //check to make sure there is no characters inputted
//                    else if (startTime.contains("[a-zA-Z]+") == true) {
//                        Toast.makeText(context, "Please enter only numbers into the start time of your free duration", Toast.LENGTH_SHORT).show();
//                    } else if (endTime.contains("[a-zA-Z]+") == true) {
//                        Toast.makeText(context, "Please enter only numbers into the end time of your free duration", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    //check if the time entered is valid
//                    else if (startTime.split(":").length != 2) {
//                        Toast.makeText(context, "Please enter a valid start time of your free duration", Toast.LENGTH_SHORT).show();
//                    } else if (endTime.split(":").length != 2) {
//                        Toast.makeText(context, "Please enter a valid end time of your free duration", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // check the time entered is valid
//                        Time start = new Time(Integer.valueOf(startSplit[0]), Integer.valueOf(startSplit[1]), 0);
//                        Time end = new Time(Integer.valueOf(endSplit[0]), Integer.valueOf(endSplit[1]), 0);
//                        int duration = (int) ((start.getTime() - end.getTime()) / 60000) * -1;
//
//                        if (Integer.valueOf(startSplit[1]) > 59 || Integer.valueOf(startSplit[0]) > 24) {
//                            Toast.makeText(context, "Please enter a valid start time of your free duration", Toast.LENGTH_SHORT).show();
//                        } else if (Integer.valueOf(endSplit[1]) > 59 || Integer.valueOf(endSplit[0]) > 24) {
//                            Toast.makeText(context, "Please enter a valid end time of your free duration", Toast.LENGTH_SHORT).show();
//                        }
//
//                        //check that the end duration comes after the start time
//                        else if (duration < 0) {
//                            Toast.makeText(context, "Please enter an end time that comes after the start time ", Toast.LENGTH_SHORT).show();
//
//                        } else if (duration == 0) {
//                            Toast.makeText(context, "Please enter a different end and start time ", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(context, "Free Made!", Toast.LENGTH_SHORT).show();
//                            viewHolder0.startTime.setText("");
//                            viewHolder0.endTime.setText("");
//
//
//                            Free toAdd = new Free(new ArrayList<Task>(), start, end, duration);
//                            String frees = "";
//
//                            //add the new task into the hashMap
//                            if (i < TuesdayPosition) {
//                                SettingsActivity.addFree(1, toAdd);
//                                for (Free free : weekFrees.get(1)) {
//                                    frees += free.toString() + " ";
//                                }
//
//                            } else if (i < WednesdayPosition) {
//                                SettingsActivity.addFree(2, toAdd);
//                                for (Free free : weekFrees.get(2)) {
//                                    frees += free.toString() + " ";
//                                }
//
//                            } else if (i < ThursdayPosition) {
//                                SettingsActivity.addFree(3, toAdd);
//                                for (Free free : weekFrees.get(3)) {
//                                    frees += free.toString() + " ";
//                                }
//
//
//                            } else if (i < FridayPosition) {
//                                SettingsActivity.addFree(4, toAdd);
//                                for (Free free : weekFrees.get(4)) {
//                                    frees += free.toString() + " ";
//                                }
//
//                            } else if (i < SaturdayPosition) {
//                                SettingsActivity.addFree(5, toAdd);
//                                for (Free free : weekFrees.get(5)) {
//                                    frees += free.toString() + " ";
//                                }
//
//                            } else if (i < SundayPosition) {
//                                SettingsActivity.addFree(6, toAdd);
//                                for (Free free : weekFrees.get(6)) {
//                                    frees += free.toString() + " ";
//                                }
//
//
//                            } else {
//                                SettingsActivity.addFree(0, toAdd);
//                                for (Free free : weekFrees.get(0)) {
//                                    frees += free.toString() + " ";
//                                }
//
//                            }
//                            viewHolder0.enteredFrees.setText(frees);
//                            calcPositions();
//
//                            notifyDataSetChanged();
//
//                        }
//                    }
//                }
//            }
//        });
//
//    }


//}

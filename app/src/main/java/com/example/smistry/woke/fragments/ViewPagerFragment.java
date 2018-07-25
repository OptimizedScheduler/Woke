package com.example.smistry.woke.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.R;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;
import com.example.smistry.woke.newTask;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ViewPagerFragment extends Fragment {

    ViewPager viewPager;
    ArrayList<Day> daysA;
    static String tabLabel="";
    final static Date today = new Date();
    final static int dofNum = today.getDay();
    ViewPagerAdapter viewPagerAdapter;
    final int REQ_CODE = 1;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     * @return A new instance of fragment ViewPagerFragment.
     */
    public static ViewPagerFragment newInstance() {
        ViewPagerFragment fragment = new ViewPagerFragment();
        return fragment;
    }

    //crear una instance que le permita recibir el arrayList de la BottomNav Activity
    public static ViewPagerFragment newInstance(ArrayList<Day> days) {
        Bundle args=new Bundle();
        ViewPagerFragment fragment = new ViewPagerFragment();
        args.putParcelable("days", Parcels.wrap(days));
        fragment.setArguments(args);
        return fragment;
    }


    public ArrayList<Free> getArray(int pos){
        return daysA.get(pos).getFreeBlocks();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daysA = new ArrayList<>();
        //retrieve the Array passed by the Activity (bottomNav)
        //TODO check if it's not nullPointer
        daysA= Parcels.unwrap(this.getArguments().getParcelable("days"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        viewPager = view.findViewById(R.id.vpPager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),newTask.class);
                intent.putExtra("dayArray", Parcels.wrap(daysA));
                startActivityForResult(intent,REQ_CODE);

                Snackbar.make(view, "Creating a new task", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE){
            int dayIndex = data.getIntExtra("dayIndex",0);
            int freeIndex = data.getIntExtra("freeIndex",0);
            ArrayList<Free> newFB = new ArrayList<>();
            newFB=Parcels.unwrap(data.getParcelableExtra("newFreeBlock"));
            Task task =newFB.get(freeIndex).getTasks().get(newFB.get(freeIndex).getTasks().size()-1);
            daysA.get(dayIndex).getFreeBlocks().get(freeIndex).getTasks().add(task);
            //viewPagerAdapter.notifyDataSetChanged();
            Log.d("ADD",  daysA.get(dayIndex).getFreeBlocks().get(freeIndex).getTasks().get(newFB.get(freeIndex).getTasks().size()-1).getTaskTitle());
        }
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_ITEMS = 7;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
               case 1:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
                case 2:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
                case 3:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
                case 4:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
                case 5:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);
                case 6:
                    return TaskFragment.newInstance((dofNum+position)%7, tabLabel);

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch((today.getDay()+position)%7){
                case 0:
                    tabLabel= "Sunday";
                    break;
                case 1:
                    tabLabel= "Monday";
                    break;
                case 2:
                    tabLabel= "Tuesday";
                    break;
                case 3:
                    tabLabel= "Wednesday";
                    break;
                case 4:
                    tabLabel= "Thursday";
                    break;
                case 5:
                    tabLabel= "Friday";
                    break;
                case 6:
                    tabLabel= "Saturday";
                    break;
            }
            return tabLabel;
        }
    }
}
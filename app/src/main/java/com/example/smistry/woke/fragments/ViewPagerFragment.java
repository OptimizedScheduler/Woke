package com.example.smistry.woke.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.R;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.newTask;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ViewPagerFragment extends Fragment {

    ViewPager viewPager;
    ArrayList<Day> daysA;
    final static Date today = new Date();
    final static int dofNum = today.getDay();  //int representation of day of the Week (Sunday, Monday... -> 0,1...)
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

    //create an instance that recieves the arrayList from BottomNav Activity
    public static ViewPagerFragment newInstance(ArrayList<Day> days) {
        Bundle args=new Bundle();
        ViewPagerFragment fragment = new ViewPagerFragment();
        args.putParcelable("days", Parcels.wrap(days));
        fragment.setArguments(args);
        return fragment;
    }

    //Used for ViewPager --> TaskFragment communication
    public ArrayList<Free> getArray(int pos){
        return daysA.get(pos).getFreeBlocks();
    }

    public void setDaysA(ArrayList<Day> daysA) {
        this.daysA = daysA;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daysA = new ArrayList<>();
        //retrieve the Array passed by the Activity (bottomNav)
        daysA= Parcels.unwrap(this.getArguments().getParcelable("days"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        viewPager = view.findViewById(R.id.vpPager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) viewPager.findViewById(R.id.pager_header);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.blue0));
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
            }
        });
    }

    //Recieves newTask's information
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE){
            int dayIndex = data.getIntExtra("dayIndex",0);
            int freeIndex = data.getIntExtra("freeIndex",0);
            ArrayList<Free> newFB;
            newFB=Parcels.unwrap(data.getParcelableExtra("newFreeBlock"));
            daysA.get(dayIndex).setFreeBlocks(newFB);
            viewPagerAdapter.notifyDataSetChanged();
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

        //Recieves an int 'position' which points which Tab from the viewPager is selected
        //ViewPager will always be filled with Tab 0 = Today (day of Week)
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: //Today
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 1:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 2:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 3:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 4:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 5:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));
                case 6:
                    return TaskFragment.newInstance((dofNum+position)%7, getLabel(position));

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getLabel(position);
        }

        public String getLabel(int pos){
            switch((dofNum+pos)%7){
                case 0:
                    return "Sunday";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Thursday";
                case 5:
                    return "Friday";
                case 6:
                    return "Saturday";
            }
            return "";
        }
    }
}
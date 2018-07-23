package com.example.smistry.woke;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.models.Free;

import java.util.ArrayList;

public class settingsFreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Free> frees;

    @Override
    public int getItemCount() {
        return frees.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {


        public ViewHolder0(View itemView) {

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        public ViewHolder2(View itemView) {

        }
    }

        @Override
        public int getItemViewType(int position) {
            // Just as an example, return 0 or 2 depending on position
            // Note that unlike in ListView adapters, types don't have to be contiguous
            return position % 2 * 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new ViewHolder0(...);
                case 2:
                    return new ViewHolder2(...);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    ViewHolder0 viewHolder0 = (ViewHolder0) holder;

                    break;

                case 2:
                    ViewHolder2 viewHolder2 = (ViewHolder2) holder;

                    break;
            }
        }
    }
}

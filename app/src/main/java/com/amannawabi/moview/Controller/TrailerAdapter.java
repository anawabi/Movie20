package com.amannawabi.moview.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amannawabi.moview.Model.Trailer;
import com.amannawabi.moview.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> mTrailerList;
    private static final String TAG = "MovieTrailerAdapter";

    public TrailerAdapter(List<Trailer> trailerList) {
        mTrailerList = trailerList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);

        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.mTrailerType.setText(mTrailerList.get(position).getsName() + " " + position);

    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView mTrailerType;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerType = itemView.findViewById(R.id.trailer_Type);
        }
    }
}

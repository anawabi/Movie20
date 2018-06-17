package com.amannawabi.moview.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amannawabi.moview.DetailedLayout;
import com.amannawabi.moview.Model.Trailer;
import com.amannawabi.moview.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> mTrailerList;
    private static final String TAG = "MovieTrailerAdapter";
    Context mContext;
    final private TrailerAdapter.ListItemClickListener mOnClickListener;
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public TrailerAdapter(List<Trailer> trailerList, ListItemClickListener mListItemListener) {
        mTrailerList = trailerList;

        mOnClickListener = mListItemListener;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);

        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, final int position) {
        holder.mTrailerType.setText(mTrailerList.get(position).getsName() + " " + position);
//        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri webpage = Uri.parse("https://www.youtube.com/watch?v="+mTrailerList.get(position).getsTrailerKey());
//                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//                mContext.startActivity(intent);
//                Toast.makeText(mContext, mTrailerList.get(getItemViewType(1)).getsTrailerKey(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTrailerType;
        LinearLayout mParentLayout;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerType = itemView.findViewById(R.id.trailer_Type);
            mParentLayout = itemView.findViewById(R.id.parent_layout);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        /**
         * Generates onClick event for the items in Recycler view by identifyind their position
         */
//
    }
}

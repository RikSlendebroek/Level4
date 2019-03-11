package com.example.bucketlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketViewHolder> {
    private List<Bucket> buckets;
    private Context context;

    public BucketAdapter(Context context, List<Bucket> buckets) {
        this.buckets = buckets;
        this.context = context;
    }

    @NonNull
    @Override
    public BucketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket, parent, false);

        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketViewHolder viewHolder, int i) {
        // Gets a single item in the list from its position
        final Bucket bucket = buckets.get(i);
        // The holder argument is used to reference the views inside the viewHolder
        // Populate the views with the data from the list
        viewHolder.crossed.setText(bucket.getTitle());
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }
}

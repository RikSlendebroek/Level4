package com.example.bucketlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

public class BucketViewHolder extends RecyclerView.ViewHolder {
    public CheckBox crossed;
    public View view;

    public BucketViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        crossed = itemView.findViewById( R.id.checkBox );
    }
}

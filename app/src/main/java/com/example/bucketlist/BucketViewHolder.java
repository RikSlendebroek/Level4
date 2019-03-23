package com.example.bucketlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class BucketViewHolder extends RecyclerView.ViewHolder {
    public CheckBox crossed;
    public View view;
    public TextView descriptionTxt;

    public BucketViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        crossed = itemView.findViewById( R.id.checkBox );
        descriptionTxt = itemView.findViewById( R.id.descriptionTxt );
    }
}

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
    public Bucket bucket;

    public BucketViewHolder(@NonNull View itemView, final Bucket bucket, final IBucketClickListener listener) {
        super( itemView );
        view = itemView;
        crossed = itemView.findViewById( R.id.checkBox );
        descriptionTxt = itemView.findViewById( R.id.descriptionView );
this.bucket = bucket;

        itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick( BucketViewHolder.this.bucket );
                return false;
            }
        } );

        crossed.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheck( BucketViewHolder.this.bucket );
            }
        } );
    }
}

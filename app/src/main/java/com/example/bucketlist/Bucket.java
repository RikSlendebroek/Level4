package com.example.bucketlist;

import android.os.Parcel;
import android.os.Parcelable;

public class Bucket implements Parcelable {
    private long id;
    private String title;
    private String description;
    private boolean crossed;

    //region Constructors
    public Bucket(String title, String description) {
        this.title = title;
        this.description = description;
        this.crossed  =false;
    }
    //endregion

    //region Getters setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCrossed() {
        return crossed;
    }

    public void setCrossed(boolean crossed) {
        this.crossed = crossed;
    }
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte(this.crossed ? (byte) 1 : (byte) 0);
    }

    protected Bucket(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.crossed = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Bucket> CREATOR = new Parcelable.Creator<Bucket>() {
        @Override
        public Bucket createFromParcel(Parcel source) {
            return new Bucket(source);
        }

        @Override
        public Bucket[] newArray(int size) {
            return new Bucket[size];
        }
    };
}

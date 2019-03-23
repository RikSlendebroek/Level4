package com.example.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BucketDao {
    @Query("SELECT * FROM bucket")
    List<Bucket> getAllBuckets();

    @Insert
    void insertBucket(Bucket bucket);

    @Delete
    void deleteBucket(Bucket bucket);

    @Update
    void updateBucket(Bucket bucket);
}

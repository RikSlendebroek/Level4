package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements IBucketClickListener {
    private List<Bucket> buckets;
    private RecyclerView recyclerView;

    private BucketAdapter adapter;
    private BucketRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    //Constants used when calling the update activity
    public static final String EXTRA_BUCKET = "bucket";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        db = BucketRoomDatabase.getDatabase(this);
        buckets = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        adapter = new BucketAdapter(this, buckets, this);
        recyclerView.setAdapter(adapter);

        getAllBuckets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Bucket addBucket = data.getParcelableExtra(MainActivity.EXTRA_BUCKET);
                insertBucket(addBucket);
                updateUI();
            }
        }
    }

    //region CRUD
    private void getAllBuckets() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                buckets = db.bucketDao().getAllBuckets();
                // In a background thread the user interface cannot be updated from this thread.
                // This method will perform statements on the main thread again.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }

    private void insertBucket(final Bucket bucket) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketDao().insertBucket(bucket);
                getAllBuckets(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }

    private void updateBucket(final Bucket bucket) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketDao().updateBucket(bucket);
                getAllBuckets(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }

    private void deleteBucket(final Bucket bucket) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketDao().deleteBucket(bucket);
                getAllBuckets(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }
    //endregion


    private void updateUI() {
        if (adapter == null) {
            adapter = new BucketAdapter(this, buckets, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapList(buckets);
        }

    }

    @Override
    public void onLongClick(Bucket bucket) {

        Toast.makeText(MainActivity.this, "Touchy?", Toast.LENGTH_SHORT);
    }

    @Override
    public void onCheck(Bucket bucket) {

    }
}

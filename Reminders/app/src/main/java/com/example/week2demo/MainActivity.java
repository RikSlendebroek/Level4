package com.example.week2demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    //Constants used when calling the update activity
    public static final String EXTRA_REMINDER = "Reminder";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;

    //instance variables
    private List<Reminder> mReminders;
    private ReminderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mNewReminderText;

    private GestureDetector mGestureDetector;

    private ReminderRoomDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        db = ReminderRoomDatabase.getDatabase( this );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mNewReminderText.getText().toString();

                //Check if some text has been added
                if (!(TextUtils.isEmpty( text ))) {
                    Reminder newReminder = new Reminder( text );

                    //Add the text to the list (datamodel)
                    mReminders.add( newReminder );
                    insertReminder( newReminder );

                    //Tell the adapter that the data set has been modified: the screen will be refreshed.
                    updateUI();

                    //Initialize the EditText for the next item
                    mNewReminderText.setText( "" );

                } else {
                    //Show a message to the user if the textfield is empty
                    Snackbar.make( view, "Please enter some text in the textfield", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

                }
            }
        } );

        mRecyclerView = findViewById( R.id.recyclerView );
        mNewReminderText = findViewById( R.id.editText_main );
        mReminders = new ArrayList<>();
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        /*
        Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
        An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
        and uses callbacks to signal when a user is performing these actions.
        */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
                    // The first integer parameter refers to the dragging directions. We ignore these here.
                    // The second integer parameter refers to the swiping directions.

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());

                        deleteReminder( mReminders.get( position ) );
                        updateUI();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( simpleItemTouchCallback );
        itemTouchHelper.attachToRecyclerView( mRecyclerView );

        mGestureDetector = new GestureDetector( this, new GestureDetector.SimpleOnGestureListener() {

            @Override

            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        } );
        mRecyclerView.addOnItemTouchListener( this );
        updateUI();
    }

    private void updateUI() {
        getAllReminders();
        if (mAdapter == null) {
            mAdapter = new ReminderAdapter( mReminders );
            mRecyclerView.setAdapter( mAdapter );
        } else {
            mAdapter.swapList( mReminders );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
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

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder( motionEvent.getX(), motionEvent.getY() );
        int mAdapterPosition = recyclerView.getChildAdapterPosition( child );
        if (child != null && mGestureDetector.onTouchEvent( motionEvent )) {
            Toast.makeText( this, mReminders.get( mAdapterPosition ).getReminderText(), Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent( MainActivity.this, UpdateReminderActivity.class );
            mModifyPosition = mAdapterPosition;
            intent.putExtra( EXTRA_REMINDER, mReminders.get( mAdapterPosition ) );
            startActivityForResult( intent, REQUESTCODE );
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Reminder updatedReminder = data.getParcelableExtra( MainActivity.EXTRA_REMINDER );
                // New timestamp: timestamp of update
                mReminders.set( mModifyPosition, updatedReminder );
                updateReminder(updatedReminder);
                updateUI();
            }
        }
    }


    //region Database methods
    private void getAllReminders() {
        executor.execute( new Runnable() {
            @Override
            public void run() {
                mReminders = db.reminderDao().getAllReminders();
                // In a background thread the user interface cannot be updated from this thread.
                // This method will perform statements on the main thread again.
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                } );
            }
        } );
    }

    private void insertReminder(final Reminder reminder) {
        executor.execute( new Runnable() {
            @Override
            public void run() {
                db.reminderDao().insertReminder( reminder );
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        } );
    }

    private void updateReminder(final Reminder reminder) {
        executor.execute( new Runnable() {
            @Override
            public void run() {
                db.reminderDao().updateReminder( reminder );
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        } );
    }

    private void deleteReminder(final Reminder reminder) {
        executor.execute( new Runnable() {
            @Override
            public void run() {
                db.reminderDao().deleteReminder( reminder );
                getAllReminders(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        } );
    }
    //endregion
}

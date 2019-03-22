package com.example.week2demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class UpdateReminderActivity extends AppCompatActivity {

    private EditText mReminderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_reminder );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        //Init local variables
        mReminderView = findViewById( R.id.editText );


        //Obtain the parameters provided by MainActivity
        final Reminder reminderUpdate = getIntent().getParcelableExtra( MainActivity.EXTRA_REMINDER );
        mReminderView.setText( reminderUpdate.getReminderText() );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = mReminderView.getText().toString();


                //(reminderUpdate.setmReminderText(updatedReminderText)));
                if (!TextUtils.isEmpty( text )) {
                    reminderUpdate.setReminderText( text );

                    //Prepare the return parameter and return
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra( MainActivity.EXTRA_REMINDER, reminderUpdate );
                    setResult( Activity.RESULT_OK, resultIntent );

                    finish();

                } else {
                    Snackbar.make( mReminderView, "Enter some data", Snackbar.LENGTH_LONG );
                }
            }
        } );
    }

}

package com.example.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "user";
    public static final String DIALOG_TAG = "dialog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String user = getIntent().getStringExtra(USER_KEY);

        findViewById(R.id.welcome_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();
                DialogFragment dialog = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        return new AlertDialog.Builder(getActivity())
                                .setMessage(String.format(getString(R.string.message), user))
                                .setPositiveButton(android.R.string.ok, null)
                                .create();
                    }
                };

                dialog.show(getFragmentManager(), DIALOG_TAG);
            }
        });
    }

    private void doSomething() {
        Log.d("MainActivity", "onClick");
    }

}

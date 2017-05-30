package com.example.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tananaev.fmelib.EasyFragment;

import java.security.SecureRandom;
import java.util.ArrayList;

import icepick.Icepick;
import icepick.State;

public class MainFragment extends EasyFragment {

    public enum Request { EXIT }
    public enum Data { USER }

    @State
    protected ArrayList<String> array;

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Icepick.restoreInstanceState(this, inState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public CharSequence getTitle() {
        return getArguments().getString(Data.USER.name());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (array == null) {
            array = new ArrayList<>();
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 10; i++) {
                array.add(Long.toString(Math.abs(random.nextLong()), 36));
            }
        }

        ((ListView) view.findViewById(android.R.id.list)).setAdapter(
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Request.EXIT.ordinal() && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }
    }

    public void exit() {
        Bundle arguments = new Bundle();
        arguments.putString(ExitDialogFragment.Data.USER.name(), getArguments().getString(Data.USER.name()));
        DialogFragment fragment = new ExitDialogFragment();
        fragment.setArguments(arguments);
        fragment.setTargetFragment(this, Request.EXIT.ordinal());
        fragment.show(getFragmentManager(), null);
    }

    public static class ExitDialogFragment extends DialogFragment {

        public enum Data { USER }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String user = getArguments().getString(Data.USER.name());
            return new AlertDialog.Builder(getActivity())
                    .setMessage(user + ", are you sure you want to exit?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                    })
                    .create();
        }

    }

}

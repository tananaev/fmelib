package com.example.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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

    public static final String KEY_USER = "user";

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
        return getArguments().getString(KEY_USER);
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

}

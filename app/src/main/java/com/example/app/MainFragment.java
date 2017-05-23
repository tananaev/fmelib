package com.example.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tananaev.fmelib.EasyFragment;
import com.tananaev.fmelib.EasySaveInstance;

import java.security.SecureRandom;
import java.util.ArrayList;

public class MainFragment extends EasyFragment {

    @EasySaveInstance
    private ArrayList<String> array;

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

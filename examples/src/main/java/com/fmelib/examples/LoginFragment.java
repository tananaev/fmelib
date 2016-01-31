package com.fmelib.examples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmelib.EasySupportFragment;
import com.fmelib.RunWhenResumed;

public class LoginFragment extends EasySupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return view;
    }

    public void login() {
        sendLoginRequest(new LoginRequestCallback() {
            @Override
            public void onSuccess(final String user) {
                startMainActivity(user);
            }
        });
    }

    public void sendLoginRequest(final LoginRequestCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.onSuccess("User");
            }
        }.execute();
    }

    @RunWhenResumed
    public void startMainActivity(String user) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY, user);
        startActivity(intent);
    }

    public interface LoginRequestCallback {
        void onSuccess(String user);
    }

}

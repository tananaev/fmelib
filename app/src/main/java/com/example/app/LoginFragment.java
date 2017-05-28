package com.example.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tananaev.fmelib.EasyFragment;
import com.tananaev.fmelib.EasyUtil;
import com.tananaev.fmelib.Task;

import java.io.Serializable;

public class LoginFragment extends EasyFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.findViewById(R.id.login_button).setOnClickListener(v -> loginLambda());
        return view;
    }

    public void loginLambda() {
        sendLoginRequest(user -> runWhenStarted(fragmentDestroyed -> {
            Intent intent = EasyUtil.createAliasIntent(getContext(), ".MainActivity");
            intent.putExtra(MainFragment.KEY_USER, user);
            startActivity(intent);
        }));
    }

    public void loginSerializableLambda() {
        sendLoginRequest(user -> runWhenStarted((Serializable & Task) fragmentDestroyed -> {
            Log.i(TAG, user);
        }));
    }

    public void loginClass() {
        sendLoginRequest(new LoginRequestCallback() {
            @Override
            public void onSuccess(String user) {
                runWhenStarted(new Task() {
                    @Override
                    public void run(boolean fragmentDestroyed) {
                        Intent intent = EasyUtil.createAliasIntent(getContext(), ".MainActivity");
                        intent.putExtra(MainFragment.KEY_USER, user);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public interface LoginRequestCallback {
        void onSuccess(String user);
    }

    public void sendLoginRequest(final LoginRequestCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.w(TAG, e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.onSuccess("User");
            }
        }.execute();
    }

}

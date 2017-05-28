/*
 * Copyright 2017 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tananaev.fmelib;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

public class EasyDialogActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String TAG = EasyDialogActivity.class.getSimpleName();

    public static final String KEY_CONTENT_FRAGMENT = "contentFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ViewGroup) findViewById(android.R.id.content).getParent()).removeAllViews();
        try {
            setTheme(getPackageManager().getApplicationInfo(getPackageName(), 0).theme);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        if (savedInstanceState == null) {
            Class fragmentClass = (Class) getIntent().getSerializableExtra(KEY_CONTENT_FRAGMENT);
            if (fragmentClass != null) {
                try {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                    DialogFragment fragment = (DialogFragment) fragmentClass.newInstance();
                    fragment.setArguments(getIntent().getExtras());
                    fragment.show(transaction, null);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

}

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

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class EasyActivity extends AppCompatActivity {

    public static final String KEY_DEFAULT_FRAGMENT = "defaultFragment";
    public static final String KEY_CONTENT_FRAGMENT = "contentFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Class fragmentClass = null;

            if (getIntent().hasExtra(KEY_CONTENT_FRAGMENT)) {
                fragmentClass = (Class) getIntent().getSerializableExtra(KEY_CONTENT_FRAGMENT);
            } else {
                try {
                    ActivityInfo activityInfo = getPackageManager().getActivityInfo(
                            getComponentName(), PackageManager.GET_META_DATA);
                    String className = activityInfo.metaData.getString(KEY_DEFAULT_FRAGMENT);
                    if (className != null) {
                        if (className.startsWith(".")) {
                            className = getApplicationContext().getPackageName() + className;
                        }
                        fragmentClass = Class.forName(className);
                    }
                } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fragmentClass != null) {
                try {
                    Fragment fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(getIntent().getExtras());
                    if (fragment instanceof EasyFragment) {
                        CharSequence title = ((EasyFragment) fragment).getTitle();
                        if (title != null) {
                            setTitle(title);
                        }
                    }
                    getSupportFragmentManager()
                            .beginTransaction().add(android.R.id.content, fragment).commit();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

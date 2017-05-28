/*
 * Copyright 2016 - 2017 Anton Tananaev (anton.tananaev@gmail.com)
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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class EasyFragment extends Fragment {

    private static final String FRAGMENT_TAG = "retainedFragment";

    private RetainedFragment retainedFragment;

    private boolean started;

    public void runWhenStarted(Task task) {
        if (started) {
            task.run(false);
        } else {
            retainedFragment.storeTask(this, task);
        }
    }

    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            retainedFragment = new RetainedFragment();
            getChildFragmentManager().beginTransaction().add(retainedFragment, FRAGMENT_TAG).commit();
        } else {
            retainedFragment = (RetainedFragment) getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }
        retainedFragment.onParentCreate(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        started = true;
        retainedFragment.onParentStart(this);
    }

    public void onRestoreInstanceState(Bundle inState) {
    }

    @Override
    public void onDestroy() {
        retainedFragment.onParentDestroy(this);
        started = false;
        super.onDestroy();
    }

}

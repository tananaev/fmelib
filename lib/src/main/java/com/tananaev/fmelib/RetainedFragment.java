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

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public final class RetainedFragment extends Fragment {

    private static final String KEY_STORE = "store";

    private EasyFragment parent;

    private final ArrayList<Bundle> coldStore = new ArrayList<>();
    private final ArrayList<Task> hotStore = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            ArrayList<Bundle> store = savedInstanceState.getParcelableArrayList(KEY_STORE);
            if (store != null) {
                coldStore.addAll(store);
            }
        }
    }

    public void onParentStart(EasyFragment parent) {
        for (Task task : hotStore) {
            coldStore.add(TaskSerializer.serializeTask(parent, task));
        }
        hotStore.clear();
        for (Bundle bundle : coldStore) {
            TaskSerializer.deserializeTask(parent, bundle).run(false);
        }
        coldStore.clear();
    }

    public void onParentCreate(EasyFragment parent) {
        this.parent = parent;
    }

    public void onParentDestroy(EasyFragment parent) {
        if (parent.isRemoving()) {
            for (Bundle bundle : coldStore) {
                TaskSerializer.deserializeTask(parent, bundle).run(true);
            }
            coldStore.clear();
            for (Task task : hotStore) {
                task.run(true);
            }
            hotStore.clear();
        } else {
            for (Task task : hotStore) {
                coldStore.add(TaskSerializer.serializeTask(parent, task));
            }
            hotStore.clear();
        }
        this.parent = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (Task task : hotStore) {
            coldStore.add(TaskSerializer.serializeTask(parent, task));
        }
        hotStore.clear();
        outState.putParcelableArrayList(KEY_STORE, coldStore);
    }

    public void storeTask(EasyFragment parent, Task task) {
        if (parent == this.parent) {
            hotStore.add(task);
        } else {
            coldStore.add(TaskSerializer.serializeTask(parent, task));
        }
    }

}

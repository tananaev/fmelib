/*
 * Copyright 2016 Anton Tananaev (anton.tananaev@gmail.com)
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

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Collection;
import java.util.LinkedList;
import java.util.WeakHashMap;

public final class FragmentUtil {

    private FragmentUtil() {
    }

    private static WeakHashMap<Object, Collection<Task>> pendingTasks = new WeakHashMap<>();

    private static Collection<Task> fragmentPendingTasks(Object fragment) {
        if (!pendingTasks.containsKey(fragment)) {
            pendingTasks.put(fragment, new LinkedList<Task>());
        }
        return pendingTasks.get(fragment);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void runWhenResumed(android.app.Fragment fragment, Task task) {
        if (fragment.isResumed()) {
            task.run(false);
        } else {
            fragmentPendingTasks(fragment).add(task);
        }
    }

    public static void runWhenResumed(android.support.v4.app.Fragment fragment, Task task) {
        if (fragment.isResumed()) {
            task.run(false);
        } else {
            fragmentPendingTasks(fragment).add(task);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void onResume(android.app.Fragment fragment) {
        for (Task task : fragmentPendingTasks(fragment)) {
            task.run(false);
        }
    }

    public static void onResume(android.support.v4.app.Fragment fragment) {
        for (Task task : fragmentPendingTasks(fragment)) {
            task.run(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void onDestroy(android.app.Fragment fragment) {
        for (Task task : fragmentPendingTasks(fragment)) {
            task.run(true);
        }
    }

    public static void onDestroy(android.support.v4.app.Fragment fragment) {
        for (Task task : fragmentPendingTasks(fragment)) {
            task.run(true);
        }
    }

}

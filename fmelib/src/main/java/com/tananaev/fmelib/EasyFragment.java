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
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EasyFragment extends Fragment {

    private static final String FRAGMENT_TAG = "retainedFragment";

    private RetainedFragment retainedFragment;

    private boolean started;

    public EasyFragment() {
        try {
            if ((getClass().getDeclaredConstructor().getModifiers() & Modifier.PUBLIC) == 0) {
                throw new IllegalArgumentException("Fragment has to have public default constructor");
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Fragment has to have default constructor");
        }
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EasySaveInstance.class)) {
                if (!field.getType().isPrimitive()
                        && !Serializable.class.isAssignableFrom(field.getType())
                        && !Parcelable.class.isAssignableFrom(field.getType())) {
                    throw new IllegalArgumentException("Fields marked as EasySaveInstance have to be Serializable or Parcelable");
                }
            }
        }
    }

    public void runWhenStarted(Task task) {
        if (started) {
            task.run(false);
        } else {
            retainedFragment.storeTask(this, task);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            retainedFragment = new RetainedFragment();
            getChildFragmentManager().beginTransaction().add(retainedFragment, FRAGMENT_TAG).commit();
        } else {
            onRestoreInstanceState(savedInstanceState);
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
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(EasySaveInstance.class)) {
                    String key = "$" + field.getName();
                    field.setAccessible(true);
                    if (byte.class.equals(field.getType())) {
                        field.set(this, inState.getByte(key));
                    } else if (short.class.equals(field.getType())) {
                        field.set(this, inState.getShort(key));
                    } else if (int.class.equals(field.getType())) {
                        field.set(this, inState.getInt(key));
                    } else if (long.class.equals(field.getType())) {
                        field.set(this, inState.getLong(key));
                    } else if (float.class.equals(field.getType())) {
                        field.set(this, inState.getFloat(key));
                    } else if (double.class.equals(field.getType())) {
                        field.set(this, inState.getDouble(key));
                    } else if (boolean.class.equals(field.getType())) {
                        field.set(this, inState.getBoolean(key));
                    } else if (char.class.equals(field.getType())) {
                        field.set(this, inState.getChar(key));
                    } else if (Serializable.class.isAssignableFrom(field.getType())) {
                        field.set(this, inState.getSerializable(key));
                    } else if (Parcelable.class.isAssignableFrom(field.getType())) {
                        field.set(this, inState.getParcelable(key));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(EasySaveInstance.class)) {
                    String key = "$" + field.getName();
                    field.setAccessible(true);
                    if (byte.class.equals(field.getType())) {
                        outState.putByte(key, field.getByte(this));
                    } else if (short.class.equals(field.getType())) {
                        outState.putShort(key, field.getShort(this));
                    } else if (int.class.equals(field.getType())) {
                        outState.putInt(key, field.getInt(this));
                    } else if (long.class.equals(field.getType())) {
                        outState.putLong(key, field.getLong(this));
                    } else if (float.class.equals(field.getType())) {
                        outState.putFloat(key, field.getFloat(this));
                    } else if (double.class.equals(field.getType())) {
                        outState.putDouble(key, field.getDouble(this));
                    } else if (boolean.class.equals(field.getType())) {
                        outState.putBoolean(key, field.getBoolean(this));
                    } else if (char.class.equals(field.getType())) {
                        outState.putChar(key, field.getChar(this));
                    } else if (Serializable.class.isAssignableFrom(field.getType())) {
                        outState.putSerializable(key, (Serializable) field.get(this));
                    } else if (Parcelable.class.isAssignableFrom(field.getType())) {
                        outState.putParcelable(key, (Parcelable) field.get(this));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        retainedFragment.onParentDestroy(this);
        started = false;
        super.onDestroy();
    }

}

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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

class TaskSerializer {

    private static final String KEY_CLASS = "class";
    private static final String KEY_FRAGMENT_PARENT = "fragmentParent";
    private static final String KEY_PARENT = "parent";
    private static final String KEY_ARGUMENTS = "arguments";
    private static final String KEY_SERIALIZABLE = "serializable";

    static Bundle serializeTask(Fragment parent, Task task) {
        return serializeObject(parent, task);
    }

    static Task deserializeTask(Fragment parent, Bundle bundle) {
        return (Task) deserializeObject(parent, bundle);
    }

    static Task updateTaskParent(Fragment parent, Task task) {
        return (Task) updateObjectParent(parent, task);
    }

    private static void validateConstructor(Fragment parent, Constructor constructor, Field[] fields) {
        if (fields.length != constructor.getParameterTypes().length) {
            throw new IllegalArgumentException("Only anonymous classes and lambdas are supported");
        }

        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getType().equals(constructor.getParameterTypes()[i])) {
                throw new IllegalArgumentException("Only anonymous classes and lambdas are supported");
            }

            if (!Serializable.class.isAssignableFrom(fields[i].getType())) {
                if (i == 0) {
                    if (!fields[i].getType().getName().startsWith(parent.getClass().getName())) {
                        throw new IllegalArgumentException("Captured variables have to be serializable");
                    }
                } else {
                    throw new IllegalArgumentException("Captured variables have to be serializable");
                }
            }
        }
    }

    private static Object updateObjectParent(Fragment parent, Object object) {
        try {
            if (object instanceof Serializable) {
                return object;
            } else {
                Constructor constructor = object.getClass().getDeclaredConstructors()[0];
                constructor.setAccessible(true);

                Field[] fields = object.getClass().getDeclaredFields();
                validateConstructor(parent, constructor, fields);

                for (Field field : fields) {
                    field.setAccessible(true);
                }

                if (fields.length > 0 && !Serializable.class.isAssignableFrom(fields[0].getType())) {
                    Object[] parameters = new Object[constructor.getParameterTypes().length];

                    if (parent.getClass().equals(fields[0].getType())) {
                        parameters[0] = parent;
                    } else {
                        parameters[0] = updateObjectParent(parent, fields[0].get(object));
                    }

                    for (int i = 1; i < constructor.getParameterTypes().length; i++) {
                        parameters[i] = fields[i].get(object);
                    }

                    return constructor.newInstance(parameters);
                }

                return object;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Bundle serializeObject(Fragment parent, Object object) {
        try {
            Bundle bundle = new Bundle();
            if (object instanceof Serializable) {
                bundle.putSerializable(KEY_SERIALIZABLE, (Serializable) object);
            } else {
                bundle.putString(KEY_CLASS, object.getClass().getName());

                Field[] fields = object.getClass().getDeclaredFields();
                validateConstructor(parent, object.getClass().getDeclaredConstructors()[0], fields);

                for (Field field : fields) {
                    field.setAccessible(true);
                }

                int fieldIndex = 0;

                if (fields.length > 0) {
                    if (parent.getClass().equals(fields[0].getType())) {
                        bundle.putBoolean(KEY_FRAGMENT_PARENT, true);
                        fieldIndex++;
                    } else if (!Serializable.class.isAssignableFrom(fields[0].getType())) {
                        bundle.putBundle(KEY_PARENT, serializeObject(parent, fields[0].get(object)));
                        fieldIndex++;
                    }
                }

                ArrayList<String> fieldNames = new ArrayList<>();

                for (int i = fieldIndex; i < fields.length; i++) {
                    if (Serializable.class.isAssignableFrom(fields[i].getType())) {
                        bundle.putSerializable("$" + fields[i].getName(), (Serializable) fields[i].get(object));
                    } else {
                        throw new IllegalArgumentException("Constructor arguments have to be serializable");
                    }
                    fieldNames.add(fields[i].getName());
                }

                bundle.putSerializable(KEY_ARGUMENTS, fieldNames);
            }
            return bundle;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object deserializeObject(Fragment parent, Bundle bundle) {
        try {
            if (bundle.containsKey(KEY_SERIALIZABLE)) {
                return bundle.getSerializable(KEY_SERIALIZABLE);
            } else {
                Class clazz = Class.forName(bundle.getString(KEY_CLASS));
                Constructor constructor = clazz.getDeclaredConstructors()[0];
                constructor.setAccessible(true);

                Object[] parameters = new Object[constructor.getParameterTypes().length];

                int parameterIndex = 0;

                if (bundle.containsKey(KEY_FRAGMENT_PARENT)) {
                    parameters[0] = parent;
                    parameterIndex++;
                } else if (bundle.containsKey(KEY_PARENT)) {
                    parameters[0] = deserializeObject(parent, bundle.getBundle(KEY_PARENT));
                    parameterIndex++;
                }

                Iterator<String> fieldIterator = ((ArrayList) bundle.getSerializable(KEY_ARGUMENTS)).iterator();

                for (int i = parameterIndex; i < constructor.getParameterTypes().length; i++) {
                    parameters[i] = bundle.getSerializable("$" + fieldIterator.next());
                }

                return constructor.newInstance(parameters);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}

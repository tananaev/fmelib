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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public final class EasyUtil {

    private EasyUtil() {
    }

    public static Intent createFragmentIntent(
            Context context, Class<? extends EasyActivity> activityClass, Class<? extends Fragment> fragmentClass) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(EasyActivity.KEY_CONTENT_FRAGMENT, fragmentClass);
        return intent;
    }

    public static Intent createFragmentIntent(
            Context context, Class<? extends Fragment> fragmentClass) {
        return createFragmentIntent(context, EasyActivity.class, fragmentClass);
    }

    public static Intent createAliasIntent(Context context, String aliasName) {
        Intent intent = new Intent();
        if (aliasName.startsWith(".")) {
            aliasName = context.getPackageName() + aliasName;
        }
        intent.setComponent(new ComponentName(context.getPackageName(), aliasName));
        return intent;
    }

    public static Intent createDialogIntent(Context context, Class<? extends DialogFragment> fragmentClass) {
        Intent intent = new Intent(context, EasyDialogActivity.class);
        intent.putExtra(EasyDialogActivity.KEY_CONTENT_FRAGMENT, fragmentClass);
        return intent;
    }

}

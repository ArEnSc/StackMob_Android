/**
 * Copyright 2011-2013 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.android.sdk.common;

import com.stackmob.sdk.util.StackMobLogger;
import android.util.Log;


public class StackMobAndroidLogger extends StackMobLogger {

    private static String TAG = "StackMob";

    private boolean enableLogging = false;

    public void setLogging(boolean logging) {
        enableLogging = logging;
    }

    public void logDebug(String format, Object... args) {
        if(enableLogging) Log.d(TAG, String.format(format, args));
    }

    public void logInfo(String format, Object... args) {
        if(enableLogging) Log.i(TAG, String.format(format, args));
    }

    public void logWarning(String format, Object... args) {
        if(enableLogging) Log.w(TAG, String.format(format, args));
    }

    public void logError(String format, Object... args) {
        if(enableLogging) Log.e(TAG, String.format(format, args));
    }


}

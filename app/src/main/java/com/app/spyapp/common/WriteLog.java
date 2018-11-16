package com.app.spyapp.common;

import android.util.Log;

public class WriteLog {
    public static final void V(final String tag, final String message) {
        Log.v(tag, message);
    }

    public static final void I(final String tag, final String message) {
        Log.i(tag, message);
    }

    public static final void E(final String tag, final String message) {
        Log.e(tag, message);
    }

    public static final void D(final String tag, final String message) {
        Log.d(tag, message);
    }

    public static final void W(final String tag, final String message) {
        Log.w(tag, message);
    }
}

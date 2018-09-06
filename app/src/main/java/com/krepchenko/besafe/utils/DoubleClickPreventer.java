package com.krepchenko.besafe.utils;

import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v4.util.LongSparseArray;

public class DoubleClickPreventer {

    public static final long MIN_CLICK_INTERVAL_100_MS = 100;
    public static final long MIN_CLICK_INTERVAL_200_MS = 200;
    public static final long MIN_CLICK_INTERVAL_500_MS = 500;

    private static LongSparseArray<Long> timeClick = new LongSparseArray<>();

    public static void onClick(@IdRes int resId, OnNextListener listener) {
        if (timeClick.get(resId) == null) {
            timeClick.put(resId, SystemClock.uptimeMillis());
            listener.onNext();
            return;
        }

        if (SystemClock.uptimeMillis() - timeClick.get(resId) < MIN_CLICK_INTERVAL_200_MS) {
            timeClick.put(resId, SystemClock.uptimeMillis());
        } else {
            timeClick.put(resId, SystemClock.uptimeMillis());
            listener.onNext();
        }
    }

    public static void onClickWithCustomInterval(long operationId, OnNextListener listener, long minClickInterval) {
        if (timeClick.get(operationId) == null) {
            timeClick.put(operationId, SystemClock.uptimeMillis());
            listener.onNext();
            return;
        }

        if (SystemClock.uptimeMillis() - timeClick.get(operationId) < minClickInterval) {
            timeClick.put(operationId, SystemClock.uptimeMillis());
        } else {
            timeClick.put(operationId, SystemClock.uptimeMillis());
            listener.onNext();
        }
    }

    public interface OnNextListener {
        void onNext();
    }
}

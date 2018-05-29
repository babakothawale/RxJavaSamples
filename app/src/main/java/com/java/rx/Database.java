package com.java.rx;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class Database {

    private static final String TAG = Database.class.getSimpleName();

    static List<Integer> readDatabaseValues() {
        SystemClock.sleep(500);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Log.i(TAG, String.format("Fetching data", i));
            SystemClock.sleep(50);
            list.add(i);
        }
        Log.i(TAG, "Fetch complete");
        return list;
    }
}


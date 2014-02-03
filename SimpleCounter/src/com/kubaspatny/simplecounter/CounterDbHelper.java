/*
 * Copyright 2014 Jakub Spatny
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

package com.kubaspatny.simplecounter;

import com.kubaspatny.simplecounter.CounterContract.CounterEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CounterDbHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Counter.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_COUNTERS =
        "CREATE TABLE " + CounterEntry.TABLE_NAME + " (" +
        		CounterEntry._ID + " INTEGER PRIMARY KEY," +
        		CounterEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        CounterEntry.COLUMN_NAME_COUNT + INT_TYPE + COMMA_SEP +
        CounterEntry.COLUMN_NAME_COLOR + INT_TYPE +
        " )";

    private static final String SQL_DELETE_COUNTERS =
        "DROP TABLE IF EXISTS " + CounterEntry.TABLE_NAME;


    public CounterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COUNTERS);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_COUNTERS);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

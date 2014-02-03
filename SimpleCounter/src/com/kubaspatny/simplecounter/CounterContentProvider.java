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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class CounterContentProvider extends ContentProvider {

	private CounterDbHelper counterDbHelper;

	private static final int ALL_COUNTERS = 1;
	private static final int SINGLE_COUNTER = 2;

	private static final String AUTHORITY = "com.kubaspatny.simplecounter.data.countercontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/counters");

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "counters", ALL_COUNTERS);
		uriMatcher.addURI(AUTHORITY, "counters/#", SINGLE_COUNTER);
	}

	@Override
	public boolean onCreate() {
		counterDbHelper = new CounterDbHelper(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {

		switch (uriMatcher.match(uri)) {
		case ALL_COUNTERS:
			return "vnd.android.cursor.dir/vnd.com.kubaspatny.simplecounter.data.countercontentprovider.counters";
		case SINGLE_COUNTER:
			return "vnd.android.cursor.item/vnd.com.kubaspatny.simplecounter.data.countercontentprovider.counters";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = counterDbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case ALL_COUNTERS:
			// do nothing
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		long id = db.insert(CounterContract.CounterEntry.TABLE_NAME, null,
				values);
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(CONTENT_URI + "/" + id);

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = counterDbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(CounterEntry.TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case ALL_COUNTERS:
			// do nothing
			break;
		case SINGLE_COUNTER:
			String id = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(CounterEntry._ID + "=" + id);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = counterDbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case ALL_COUNTERS:
			// do nothing
			break;
		case SINGLE_COUNTER:
			String id = uri.getPathSegments().get(1);
			selection = CounterEntry._ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int deleteCount = db.delete(CounterEntry.TABLE_NAME, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = counterDbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case ALL_COUNTERS:
			// do nothing
			break;
		case SINGLE_COUNTER:
			String id = uri.getPathSegments().get(1);
			selection = CounterEntry._ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int updateCount = db.update(CounterEntry.TABLE_NAME, values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

}

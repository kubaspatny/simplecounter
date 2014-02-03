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
import com.kubaspatny.simplecounter.widget.CounterWidgetProvider;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CounterActivity extends Activity implements
		LoaderCallbacks<Cursor> {

	private CounterCursorAdapter counterAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);

		displayListView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_counter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_counter:

			(new NewCounterDialog()).show(getFragmentManager(), "dialog");

			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { CounterEntry._ID,
				CounterEntry.COLUMN_NAME_TITLE, CounterEntry.COLUMN_NAME_COUNT,
				CounterEntry.COLUMN_NAME_COLOR };
		
		String sortOrder = CounterEntry._ID + " DESC";

		CursorLoader cursorLoader = new CursorLoader(this,
				CounterContentProvider.CONTENT_URI, projection, null, null,
				sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		counterAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		counterAdapter.swapCursor(null);
	}

	private void displayListView() {

		counterAdapter = new CounterCursorAdapter(this, null);

		ListView listView = (ListView) findViewById(R.id.counter_list);
		listView.setAdapter(counterAdapter);
		LinearLayout emptyView = (LinearLayout)findViewById(R.id.empty_state);
		listView.setEmptyView(emptyView);
		ImageButton i = ((ImageButton)emptyView.findViewById(R.id.empty_state_newcounter));
		i.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				(new NewCounterDialog()).show(getFragmentManager(), "dialog");
				
			}
		});
		
		getLoaderManager().initLoader(0, null, this);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView listView, View view,
					int position, long id) {

				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				int counter_id = cursor.getInt(cursor.getColumnIndexOrThrow(CounterEntry._ID));
				
				Intent i = new Intent(getBaseContext(), CounterEditActivity.class);
				i.putExtra(CounterEditActivity.COUNTER_ID, counter_id);
				startActivity(i);

			}

		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		
		// update other widgets as well (in case there are more than one widget of the same counter)
		// this is just a temporal bug fix - it will be resolved better
		Intent update_intent = new Intent(this, CounterWidgetProvider.class);
		update_intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");			
		int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, CounterWidgetProvider.class));
		update_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		this.sendBroadcast(update_intent);
					
		
	}
	
	

}

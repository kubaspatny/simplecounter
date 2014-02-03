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

package com.kubaspatny.simplecounter.widget;

import com.kubaspatny.simplecounter.CounterContentProvider;
import com.kubaspatny.simplecounter.R;
import com.kubaspatny.simplecounter.WidgetUtils;
import com.kubaspatny.simplecounter.CounterContract.CounterEntry;
import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.appwidget.AppWidgetManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class CounterWidgetConfigActivity extends Activity implements LoaderCallbacks<Cursor>{

	private SimpleCursorAdapter adapter;
	private Spinner counterSpinner;
	private Spinner colormodeSpinner;
	
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter_widget_config);
		
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
				new String[] { CounterEntry.COLUMN_NAME_TITLE, CounterEntry.COLUMN_NAME_COUNT }, 
		        new int[] { android.R.id.text1, android.R.id.text2 });
		
		counterSpinner = (Spinner) findViewById(R.id.spinner_counter);
		counterSpinner.setAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
		
		colormodeSpinner = (Spinner) findViewById(R.id.spinner_colormode);
		ArrayAdapter<CharSequence> adapter_color = ArrayAdapter.createFromResource(this,
		        R.array.colormodes_array, android.R.layout.simple_spinner_item);
		adapter_color.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colormodeSpinner.setAdapter(adapter_color);
		
		setResult(RESULT_CANCELED);

		Button confirm = (Button) findViewById(R.id.widget_config_ok);
		confirm.setOnClickListener(confirmListener);
		
		Button cancel = (Button) findViewById(R.id.widget_config_cancel);
		cancel.setOnClickListener(cancelListener);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { CounterEntry._ID,
				CounterEntry.COLUMN_NAME_TITLE, CounterEntry.COLUMN_NAME_COUNT,  CounterEntry.COLUMN_NAME_COLOR};
		
		String sortOrder = CounterEntry._ID + " DESC";

		CursorLoader cursorLoader = new CursorLoader(this,
				CounterContentProvider.CONTENT_URI, projection, null, null,
				sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
		
		if(!cursor.moveToFirst()){
			
			setContentView(R.layout.activity_counter_widget_config_empty);
			Button cancel = (Button) findViewById(R.id.widget_config_empty_cancel);
			cancel.setOnClickListener(cancelListener);
			
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
	
	private Button.OnClickListener confirmListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

			SharedPreferences prefs = getSharedPreferences(WidgetUtils.SHARED_PREFERENCES_NAME, 0);
			SharedPreferences.Editor editor = prefs.edit();
			
			Cursor cursor = (Cursor) counterSpinner.getItemAtPosition(counterSpinner.getSelectedItemPosition());
			int counter_id = cursor.getInt(cursor.getColumnIndexOrThrow(CounterEntry._ID));
			
			editor.putInt(WidgetUtils.SHARED_PREFERENCES_NAME + mAppWidgetId + WidgetUtils.SHARED_PREFERENCES_ROW,
					counter_id);
			editor.commit();
			editor = prefs.edit();
			
			editor.putInt(WidgetUtils.SHARED_PREFERENCES_NAME + mAppWidgetId + WidgetUtils.SHARED_PREFERENCES_COLORFUL,
					colormodeSpinner.getSelectedItemPosition());
			
			editor.commit();
			
			
																					// context
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CounterWidgetConfigActivity.this);
			
												// context
			CounterWidgetProvider.updateAppWidget(CounterWidgetConfigActivity.this, appWidgetManager, mAppWidgetId);

			setResult(RESULT_OK, resultValue);
			finish();
			
		}
	};
	
	private Button.OnClickListener cancelListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

}

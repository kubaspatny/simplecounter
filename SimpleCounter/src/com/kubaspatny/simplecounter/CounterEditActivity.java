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

import java.util.ArrayList;

import com.kubaspatny.simplecounter.CounterContract.CounterEntry;
import com.kubaspatny.simplecounter.widget.CounterWidgetProvider;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CounterEditActivity extends Activity implements OnClickListener,
		OnDeleteListener, OnChangeColorListener {

	public static final String COUNTER_ID = "com.kubaspatny.simplecounter.COUNTER_ID";
	private int id;
	private TextView title_tv;
	private TextView count_tv;
	private ImageButton inc;
	private ImageButton dec;
	private Button reset;
	private int count;
	private LinearLayout buttons;
	private int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter_edit);

		title_tv = (TextView) findViewById(R.id.CounterEdit_title);
		count_tv = (TextView) findViewById(R.id.CounterEdit_count_textview);
		inc = (ImageButton) findViewById(R.id.CounterEdit_increment);
		dec = (ImageButton) findViewById(R.id.CounterEdit_decrement);
		reset = (Button) findViewById(R.id.CounterEdit_reset);
		buttons = (LinearLayout) findViewById(R.id.CounterEdit_buttons_container);

		Bundle extras = this.getIntent().getExtras();
		if (extras != null) {
			id = extras.getInt(COUNTER_ID);
		} else {
			finish();
		}

		inc.setOnClickListener(this);
		reset.setOnClickListener(this);

		if (count <= 0) {
			buttons.removeView(dec);
			dec = null;
		} else {
			dec.setOnClickListener(this);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		load();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_counter_edit, menu);
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_counter_color:
			ColorDialog.newInstance(color).show(getFragmentManager(), "dialog");
			break;
		case R.id.edit_counter_delete:
			DeleteDialog.newInstance(title_tv.getText().toString()).show(
					getFragmentManager(), "dialog");
			break;

		case android.R.id.home:
			finish();
			break;
		default:
			super.onOptionsItemSelected(item);
			break;
		}

		return true;
	}

	@SuppressLint("NewApi")
	private void load() {

		String[] projection = { CounterEntry._ID,
				CounterEntry.COLUMN_NAME_TITLE, CounterEntry.COLUMN_NAME_COUNT,
				CounterEntry.COLUMN_NAME_COLOR };

		Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/" + id);
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			String title = cursor.getString(cursor
					.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_TITLE));
			count = cursor.getInt(cursor
					.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COUNT));
			color = cursor.getInt(cursor
					.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COLOR));

			title_tv.setText(title);
			count_tv.setText(count + "");
			cursor.close();

			switch (color) {
			case 2:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_2);
				break;
			case 3:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_3);
				break;
			case 4:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_4);
				break;
			case 5:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_5);
				break;
			case 6:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_6);
				break;
			case 7:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_7);
				break;
			case 8:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg_8);
				break;
			default:
				((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
						.setBackgroundResource(R.color.card_bg);
				break;
			}
		}

		if (count <= 0 && dec != null) {
			LinearLayout buttons = (LinearLayout) findViewById(R.id.CounterEdit_buttons_container);
			buttons.removeView(dec);
			dec = null;
		} else if (count > 0 && dec == null) {

			View viewForButton = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.activity_counter_edit, null, false);
			dec = (ImageButton) viewForButton
					.findViewById(R.id.CounterEdit_decrement);
			LinearLayout viewButtons = (LinearLayout) viewForButton
					.findViewById(R.id.CounterEdit_buttons_container);
			viewButtons.removeView(dec);
			((LinearLayout) findViewById(R.id.CounterEdit_buttons_container))
					.addView(dec, 1);
			dec.setOnClickListener(this);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			LayoutTransition tr = ((RelativeLayout) findViewById(R.id.CounterEditRelativeLayout))
					.getLayoutTransition();
			tr.enableTransitionType(LayoutTransition.CHANGING);
			buttons.getLayoutTransition().disableTransitionType(
					LayoutTransition.CHANGE_DISAPPEARING);
		} else {
			buttons.setLayoutTransition(null);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.CounterEdit_increment:
			Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/" + id);
			ContentValues values = new ContentValues();
			values.put(CounterEntry.COLUMN_NAME_COUNT, count + 1);
			getContentResolver().update(uri, values, null, null);
			break;
		case R.id.CounterEdit_decrement:
			Uri uri_dec = Uri.parse(CounterContentProvider.CONTENT_URI + "/"
					+ id);
			ContentValues values_dec = new ContentValues();
			values_dec.put(CounterEntry.COLUMN_NAME_COUNT, count - 1);
			getContentResolver().update(uri_dec, values_dec, null, null);
			break;
		case R.id.CounterEdit_reset:
			Uri uri_res = Uri.parse(CounterContentProvider.CONTENT_URI + "/"
					+ id);
			ContentValues values_res = new ContentValues();
			values_res.put(CounterEntry.COLUMN_NAME_COUNT, 0);
			getContentResolver().update(uri_res, values_res, null, null);
			break;
		}

		load();

	}

	public void delete() {
		Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/" + id);
		getContentResolver().delete(uri, null, null);

		// set counter_id to -2 for widgets of this counter
		int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(
				new ComponentName(this, CounterWidgetProvider.class));
		SharedPreferences prefs = getSharedPreferences(
				WidgetUtils.SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		
		ArrayList<Integer> toBeUpdated = new ArrayList<Integer>();
		
		for (int widget_id : ids) {

			int counter_id = prefs.getInt(WidgetUtils.SHARED_PREFERENCES_NAME + widget_id + WidgetUtils.SHARED_PREFERENCES_ROW, 0);
			if(counter_id == id){
				
				editor.putInt(WidgetUtils.SHARED_PREFERENCES_NAME + widget_id + WidgetUtils.SHARED_PREFERENCES_ROW, -2);
				editor.remove(WidgetUtils.SHARED_PREFERENCES_NAME + widget_id + WidgetUtils.SHARED_PREFERENCES_COLORFUL);
				editor.commit();
				
				toBeUpdated.add(widget_id);
				
			}
			
			Intent update_intent = new Intent(this, CounterWidgetProvider.class);
			update_intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			int[] ids2 = new int[toBeUpdated.size()];
			for(int i = 0; i < toBeUpdated.size(); ++i){
				ids[i] = toBeUpdated.get(i);
			}
			update_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids2);
			this.sendBroadcast(update_intent);
			
		}

		// ------------------------------------------------

		finish();
	}

	public void changeColor(int color) {
		if (color == -1)
			return; // WRONG color code

		Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/" + id);
		ContentValues values = new ContentValues();
		values.put(CounterEntry.COLUMN_NAME_COLOR, color);
		getContentResolver().update(uri, values, null, null);

		load();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// update other widgets as well (in case there are more than one widget
		// of the same counter)
		// this is just a temporal bug fix - it will be resolved better
		Intent update_intent = new Intent(this, CounterWidgetProvider.class);
		update_intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(
				new ComponentName(this, CounterWidgetProvider.class));
		update_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		this.sendBroadcast(update_intent);

	}

}

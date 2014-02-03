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
import com.kubaspatny.simplecounter.CounterEditActivity;
import com.kubaspatny.simplecounter.R;
import com.kubaspatny.simplecounter.WidgetUtils;
import com.kubaspatny.simplecounter.CounterContract.CounterEntry;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

public class CounterWidgetIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		int widget_id = -1;
		Bundle extras = intent.getExtras();

		if (extras != null) {
			widget_id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		}

		SharedPreferences prefs = context.getSharedPreferences(
				WidgetUtils.SHARED_PREFERENCES_NAME, 0);
		int counter_id = prefs.getInt(WidgetUtils.SHARED_PREFERENCES_NAME
				+ widget_id + WidgetUtils.SHARED_PREFERENCES_ROW, 0);
		int color_mode = prefs.getInt(WidgetUtils.SHARED_PREFERENCES_NAME
				+ widget_id + WidgetUtils.SHARED_PREFERENCES_COLORFUL, 0);

		RemoteViews rv;

		if (counter_id == WidgetUtils.WIDGETS_COUNTER_DELETED) {

			rv = new RemoteViews(context.getPackageName(),
					R.layout.counter_widget_deleted);

		} else {

			String[] projection = { CounterEntry._ID,
					CounterEntry.COLUMN_NAME_TITLE,
					CounterEntry.COLUMN_NAME_COUNT,
					CounterEntry.COLUMN_NAME_COLOR };

			Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/"
					+ counter_id);
			Cursor cursor = context.getContentResolver().query(uri, projection,
					null, null, null);

			String title = null;
			int count = -1;
			int color = -1;

			if (cursor != null) {
				cursor.moveToFirst();
				title = cursor.getString(cursor
						.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_TITLE));
				count = cursor.getInt(cursor
						.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COUNT));
				color = cursor.getInt(cursor
						.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COLOR));

				cursor.close();

			}

			switch (color_mode) {
			case 1:
				rv = new RemoteViews(context.getPackageName(),
						R.layout.counter_widget_layout_dark);
				break;
			case 2:
				switch (color) {
				case 2:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_2);
					break;
				case 3:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_3);
					break;
				case 4:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_4);
					break;
				case 5:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_5);
					break;
				case 6:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_6);
					break;
				case 7:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_7);
					break;
				case 8:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_8);
					break;
				default:
					rv = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_1);
					break;
				}

				break;
			default:
				rv = new RemoteViews(context.getPackageName(),
						R.layout.counter_widget_layout);
				break;
			}
			
			rv.setTextViewText(R.id.widget_title, title);

			if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {

				rv.setTextViewText(R.id.widget_count, count + "");

			} else if (intent.getAction().equals(WidgetUtils.WIDGET_INCREMENT_ACTION)) {

				ContentValues values = new ContentValues();
				values.put(CounterEntry.COLUMN_NAME_COUNT, count + 1);
				context.getContentResolver().update(uri, values, null, null);

				rv.setTextViewText(R.id.widget_count, (count + 1) + "");

				// update other widgets as well (in case there are more than one
				// widget of the same counter)
				// this is just a temporal bug fix - it will be resolved better
				Intent update_intent = new Intent(context,
						CounterWidgetProvider.class);
				update_intent
						.setAction("android.appwidget.action.APPWIDGET_UPDATE");
				int ids[] = AppWidgetManager.getInstance(context)
						.getAppWidgetIds(
								new ComponentName(context,
										CounterWidgetProvider.class));
				update_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
						ids);
				context.sendBroadcast(update_intent);

			} else if (intent.getAction().equals(WidgetUtils.WIDGET_DECREMENT_ACTION)) {

				ContentValues values = new ContentValues();
				values.put(CounterEntry.COLUMN_NAME_COUNT, count - 1);
				context.getContentResolver().update(uri, values, null, null);

				rv.setTextViewText(R.id.widget_count, (count - 1) + "");

				// update other widgets as well (in case there are more than one
				// widget of the same counter)
				// this is just a temporal bug fix - it will be resolved better
				Intent update_intent = new Intent(context,
						CounterWidgetProvider.class);
				update_intent
						.setAction("android.appwidget.action.APPWIDGET_UPDATE");
				int ids[] = AppWidgetManager.getInstance(context)
						.getAppWidgetIds(
								new ComponentName(context,
										CounterWidgetProvider.class));
				update_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
						ids);
				context.sendBroadcast(update_intent);
				
			} else if (intent.getAction().equals(WidgetUtils.WIDGET_OPENCOUNTER_ACTION)) {
			
				Intent openCounter = new Intent(context, CounterEditActivity.class);
				openCounter.putExtra(CounterEditActivity.COUNTER_ID, counter_id);
				openCounter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				openCounter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(openCounter);
				
			}

		}

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(widget_id, rv);
	}

}

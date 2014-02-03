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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;



public class CounterWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, i);
		}

	}

	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		SharedPreferences prefs = context.getSharedPreferences(
				WidgetUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		int counter_id = prefs.getInt(WidgetUtils.SHARED_PREFERENCES_NAME
				+ appWidgetId + WidgetUtils.SHARED_PREFERENCES_ROW, 0);
		int color_mode = prefs.getInt(WidgetUtils.SHARED_PREFERENCES_NAME
				+ appWidgetId + WidgetUtils.SHARED_PREFERENCES_COLORFUL, 0);

		// choose theme
		RemoteViews updateViews;

		if (counter_id == WidgetUtils.WIDGETS_COUNTER_DELETED) {
			updateViews = new RemoteViews(context.getPackageName(),	R.layout.counter_widget_deleted);
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
				if (!cursor.isAfterLast()) {
					title = cursor
							.getString(cursor
									.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_TITLE));
					count = cursor
							.getInt(cursor
									.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COUNT));
					color = cursor
							.getInt(cursor
									.getColumnIndexOrThrow(CounterEntry.COLUMN_NAME_COLOR));
				}
				cursor.close();
			}

			switch (color_mode) {
			case 1:
				updateViews = new RemoteViews(context.getPackageName(),
						R.layout.counter_widget_layout_dark);
				break;
			case 2:
				switch (color) {
				case 2:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_2);
					break;
				case 3:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_3);
					break;
				case 4:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_4);
					break;
				case 5:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_5);
					break;
				case 6:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_6);
					break;
				case 7:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_7);
					break;
				case 8:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_8);
					break;
				default:
					updateViews = new RemoteViews(context.getPackageName(),
							R.layout.counter_widget_1);
					break;
				}
				break;
			default:
				updateViews = new RemoteViews(context.getPackageName(),
						R.layout.counter_widget_layout);
				break;
			}

			updateViews.setTextViewText(R.id.widget_count, count + "");
			updateViews.setTextViewText(R.id.widget_title, title);

			// set intent for button +
			Intent intent = new Intent(context,
					CounterWidgetIntentReceiver.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			intent.setAction(WidgetUtils.WIDGET_INCREMENT_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					appWidgetId, intent, 0);
			updateViews.setOnClickPendingIntent(R.id.widget_increment,
					pendingIntent);

			// set intent for button -
			Intent intent2 = new Intent(context,
					CounterWidgetIntentReceiver.class);
			intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			intent2.setAction(WidgetUtils.WIDGET_DECREMENT_ACTION);
			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
					appWidgetId, intent2, 0);
			updateViews.setOnClickPendingIntent(R.id.widget_decrement,
					pendingIntent2);
			
			// set intent for button
			Intent openCounter = new Intent(context, CounterWidgetIntentReceiver.class);
			openCounter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			openCounter.setAction(WidgetUtils.WIDGET_OPENCOUNTER_ACTION);
			PendingIntent openCounterPI = PendingIntent.getBroadcast(context,appWidgetId, openCounter, 0);
			updateViews.setOnClickPendingIntent(R.id.widget_container, openCounterPI);
			
			if(count <= 0){
				updateViews.setViewVisibility(R.id.widget_decrement, View.GONE);
			} else {
				updateViews.setViewVisibility(R.id.widget_decrement, View.VISIBLE);
			}

		}

		appWidgetManager.updateAppWidget(appWidgetId, updateViews);

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);

		for (int i : appWidgetIds) {

			SharedPreferences prefs = context.getSharedPreferences(
					WidgetUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
			prefs.edit().remove(
					WidgetUtils.SHARED_PREFERENCES_NAME + i
							+ WidgetUtils.SHARED_PREFERENCES_ROW).commit();
			prefs.edit().remove(
					WidgetUtils.SHARED_PREFERENCES_NAME + i
							+ WidgetUtils.SHARED_PREFERENCES_COLORFUL).commit();

		}

	}

}

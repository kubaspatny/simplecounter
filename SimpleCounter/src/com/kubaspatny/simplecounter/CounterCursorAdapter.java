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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CounterCursorAdapter extends CursorAdapter {

	private final Context context;
	private final LayoutInflater inflater;

	public CounterCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public void bindView(View view, Context context, Cursor c) {

		TextView mTitle = (TextView) view.findViewById(R.id.card_title);
		TextView mCount = (TextView) view.findViewById(R.id.card_count);

		final int id = c.getInt(c.getColumnIndex(CounterEntry._ID));
		final String title = c.getString(c
				.getColumnIndex(CounterEntry.COLUMN_NAME_TITLE));
		final int count = c.getInt(c
				.getColumnIndex(CounterEntry.COLUMN_NAME_COUNT));
		final int color = c.getInt(c
				.getColumnIndex(CounterEntry.COLUMN_NAME_COLOR));

		mTitle.setText(title);
		mCount.setText(count + "");
		
		switch(color){
			case 2:
				view.setBackgroundResource(R.drawable.card_background_2);
				break;
			case 3:
				view.setBackgroundResource(R.drawable.card_background_3);
				break;
			case 4:
				view.setBackgroundResource(R.drawable.card_background_4);
				break;
			case 5:
				view.setBackgroundResource(R.drawable.card_background_5);
				break;
			case 6:
				view.setBackgroundResource(R.drawable.card_background_6);
				break;
			case 7:
				view.setBackgroundResource(R.drawable.card_background_7);
				break;
			case 8:
				view.setBackgroundResource(R.drawable.card_background_8);
				break;
			default:
				view.setBackgroundResource(R.drawable.card_background);
				break;
		}
		
		ImageButton dec = (ImageButton) view.findViewById(R.id.decrement);
		ImageButton inc = (ImageButton) view.findViewById(R.id.increment);

		LinearLayout buttons = (LinearLayout) view.findViewById(R.id.buttons);

		if (count <= 0 && dec != null) {
			buttons.removeView(dec);
		} else if (count > 0 && dec == null) {

			View viewForButton = inflater.inflate(R.layout.card_layout, null,
					false);
			dec = (ImageButton) viewForButton.findViewById(R.id.decrement);
			LinearLayout viewButtons = (LinearLayout) viewForButton
					.findViewById(R.id.buttons);
			viewButtons.removeView(dec);
			buttons.addView(dec, 0);

		}

		if (dec != null) {
			dec.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI
							+ "/" + id);
					ContentValues values = new ContentValues();
					values.put(CounterEntry.COLUMN_NAME_COUNT, count - 1);
					CounterCursorAdapter.this.context.getContentResolver()
							.update(uri, values, null, null);
				}
			});
		}

		inc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(CounterContentProvider.CONTENT_URI + "/"
						+ id);
				ContentValues values = new ContentValues();
				values.put(CounterEntry.COLUMN_NAME_COUNT, count + 1);
				CounterCursorAdapter.this.context.getContentResolver().update(
						uri, values, null, null);
			}
		});

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(R.layout.card_layout, parent, false);
		return view;
	}

}

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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NewCounterDialog extends DialogFragment {
	
	Activity mActivity;
	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2;
	private EditText title;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("New Counter");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final View view = inflater.inflate(R.layout.newcounter_dialog_layout, null);

		title = (EditText) view.findViewById(R.id.newcounter_edittext_title);
		radioGroup1 = (RadioGroup) view.findViewById(R.id.newcounter_line_1);
		radioGroup2 = (RadioGroup) view.findViewById(R.id.newcounter_line_2);


				((RadioButton) radioGroup1.findViewById(R.id.newcounter_radio_1)).setChecked(true);
		radioGroup1
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId != -1) {

							if (((RadioButton) NewCounterDialog.this.radioGroup1
									.findViewById(checkedId)).isChecked()) {

								NewCounterDialog.this.radioGroup2.clearCheck();
							}
						}
					}
				});

		radioGroup2
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId != -1) {
							if (((RadioButton) NewCounterDialog.this.radioGroup2
									.findViewById(checkedId)).isChecked()) {

								NewCounterDialog.this.radioGroup1.clearCheck();
							}
						}
					}
				});

		builder.setView(view);

		builder.setCancelable(true);
		builder.setPositiveButton("Create", new OkOnClickListener());
		builder.setNegativeButton("Cancel", new CancelOnClickListener());
		final Dialog dialog = builder.create();
		
		title.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				final Button saveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
		        if(title.getText().length() == 0) {
		            saveButton.setEnabled(false);
		        } else {
		            saveButton.setEnabled(true);
		        }
				
			}
		});
		
		dialog.setOnShowListener(new Dialog.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			}
		});
		
		return dialog;
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			NewCounterDialog.this.dismiss();

		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
			
			int i;
			switch (NewCounterDialog.this.radioGroup1.getCheckedRadioButtonId()) {
			case R.id.newcounter_radio_1:
				i = 1;
				break;
			case R.id.newcounter_radio_2:
				i = 2;
				break;
			case R.id.newcounter_radio_3:
				i = 3;
				break;
			case R.id.newcounter_radio_4:
				i = 4;
				break;
			default:
				i = -1;
				break;
			}

			if (i == -1) {

				switch (NewCounterDialog.this.radioGroup2.getCheckedRadioButtonId()) {
				case R.id.newcounter_radio_5:
					i = 5;
					break;
				case R.id.newcounter_radio_6:
					i = 6;
					break;
				case R.id.newcounter_radio_7:
					i = 7;
					break;
				case R.id.newcounter_radio_8:
					i = 8;
					break;
				default:
					i = 1;
					break;
				}

			}

			ContentValues values = new ContentValues();
			values.put(CounterEntry.COLUMN_NAME_TITLE, NewCounterDialog.this.title.getText().toString());
			values.put(CounterEntry.COLUMN_NAME_COUNT, 0);
			values.put(CounterEntry.COLUMN_NAME_COLOR, i);			
			NewCounterDialog.this.mActivity.getContentResolver().insert(CounterContentProvider.CONTENT_URI,
					values);
			
			NewCounterDialog.this.dismiss();
			
			
		}
	}

}

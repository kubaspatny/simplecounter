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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ColorDialog extends DialogFragment {

	OnChangeColorListener mChangeColorListener;
	private int color;
	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mChangeColorListener = (OnChangeColorListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnChangeColorListener");
		}
	}

	static ColorDialog newInstance(int color) {
		ColorDialog f = new ColorDialog();

		Bundle args = new Bundle();
		args.putInt("COLOR", color);
		f.setArguments(args);

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		color = getArguments().getInt("COLOR");

		builder.setTitle("Counter Color");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.color_dialog_layout, null);

		radioGroup1 = (RadioGroup) view.findViewById(R.id.line_1);
		radioGroup2 = (RadioGroup) view.findViewById(R.id.line_2);

		switch (color) {
		case 1:
			((RadioButton) radioGroup1.findViewById(R.id.radio_1))
					.setChecked(true);
			break;
		case 2:
			((RadioButton) radioGroup1.findViewById(R.id.radio_2))
					.setChecked(true);
			break;
		case 3:
			((RadioButton) radioGroup1.findViewById(R.id.radio_3))
					.setChecked(true);
			break;
		case 4:
			((RadioButton) radioGroup1.findViewById(R.id.radio_4))
					.setChecked(true);
			break;
		case 5:
			((RadioButton) radioGroup2.findViewById(R.id.radio_5))
					.setChecked(true);
			break;
		case 6:
			((RadioButton) radioGroup2.findViewById(R.id.radio_6))
					.setChecked(true);
			break;
		case 7:
			((RadioButton) radioGroup2.findViewById(R.id.radio_7))
					.setChecked(true);
			break;
		case 8:
			((RadioButton) radioGroup2.findViewById(R.id.radio_8))
					.setChecked(true);
			break;

		}

		radioGroup1
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId != -1) {

							if (((RadioButton) ColorDialog.this.radioGroup1
									.findViewById(checkedId)).isChecked()) {

								ColorDialog.this.radioGroup2.clearCheck();
							}
						}
					}
				});

		radioGroup2
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId != -1) {
							if (((RadioButton) ColorDialog.this.radioGroup2
									.findViewById(checkedId)).isChecked()) {

								ColorDialog.this.radioGroup1.clearCheck();
							}
						}
					}
				});

		builder.setView(view);

		builder.setCancelable(true);
		builder.setPositiveButton("OK", new OkOnClickListener());
		builder.setNegativeButton("Cancel", new CancelOnClickListener());

		return builder.create();
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			ColorDialog.this.dismiss();

		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			ColorDialog.this.dismiss();
			int i;
			switch (ColorDialog.this.radioGroup1.getCheckedRadioButtonId()) {
			case R.id.radio_1:
				i = 1;
				break;
			case R.id.radio_2:
				i = 2;
				break;
			case R.id.radio_3:
				i = 3;
				break;
			case R.id.radio_4:
				i = 4;
				break;
			default:
				i = -1;
				break;
			}

			if (i == -1) {

				switch (ColorDialog.this.radioGroup2.getCheckedRadioButtonId()) {
				case R.id.radio_5:
					i = 5;
					break;
				case R.id.radio_6:
					i = 6;
					break;
				case R.id.radio_7:
					i = 7;
					break;
				case R.id.radio_8:
					i = 8;
					break;
				default:
					i = -1;
					break;
				}

			}

			ColorDialog.this.mChangeColorListener.changeColor(i);
		}
	}

}

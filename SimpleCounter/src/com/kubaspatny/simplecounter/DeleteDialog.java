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

public class DeleteDialog extends DialogFragment {

	OnDeleteListener mDeleteListener;
	private String title;
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mDeleteListener = (OnDeleteListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDeleteListener");
		}
	}
	
	static DeleteDialog newInstance(String title) {
		DeleteDialog f = new DeleteDialog();

	    Bundle args = new Bundle();
	    args.putString("TITLE", title);
	    f.setArguments(args);

	    return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		title = getArguments().getString("TITLE");
		
		builder.setTitle(title);
		builder.setMessage("Do you want to delete this counter?");
		builder.setCancelable(true);
		builder.setPositiveButton("Yes", new OkOnClickListener());
		builder.setNegativeButton("Cancel", new CancelOnClickListener());

		return builder.create();
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			DeleteDialog.this.dismiss();

		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			DeleteDialog.this.dismiss();
			DeleteDialog.this.mDeleteListener.delete();
		}
	}

}

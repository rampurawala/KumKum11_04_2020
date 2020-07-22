package com.expedite.apps.kumkum.common;

import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Jaydeep patel on 13-April-17.
 */
public class MyTextWatcher implements TextWatcher {

	private TextInputLayout view;

	public MyTextWatcher(TextInputLayout view) {
		this.view = view;
	}

	public void beforeTextChanged (CharSequence s, int start, int count, int after) {

	}

	public void onTextChanged (CharSequence s, int start, int before, int count) {

	}

	public void afterTextChanged (Editable editable) {
		view.setError (null);
	}
}

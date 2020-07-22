package com.expedite.apps.kumkum.common;

 

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.expedite.apps.kumkum.R;

public class AlertDialogManager extends Activity {
	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * 				 - pass null if you don't want icon
	 * */
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	public void showAppcontrolDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	public void ShowInformationDialougue(Context context, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle("Update information");

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
			// Setting alert dialog icon
	   alertDialog.setIcon(R.drawable.information);

		// Setting OK Button 	    
		alertDialog.setButton("Download", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			
				
			}
		});
		
		alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			
				
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	public void ShowInternetConnectionError(Context context, String title, String message) {

		try {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle(title);
			alertDialog
					.setMessage(message);
			alertDialog.setIcon(R.drawable.alert);
			alertDialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						}
					});
			alertDialog.show();
		} catch (Exception err) {

		}
	}
}

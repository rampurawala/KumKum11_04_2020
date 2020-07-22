package com.expedite.apps.kumkum.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.expedite.apps.kumkum.R;

public class ShowAlertMessage {

	Context context;
	String msgtitle ="";
	String msg ="";
	public ShowAlertMessage(Context c,String msgtitle,String msg) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.msgtitle = msgtitle;
		this.msg = msg;
	}
	
	public void ShowAlertDiolougue() {
		
		AlertDialog alertDialog = new AlertDialog.Builder(
				context).create();
		
		       alertDialog.setTitle(msgtitle);
		    // Setting Dialog Message
		        alertDialog.setMessage(msg);
		    // Setting Icon to Dialog
		       alertDialog.setIcon(R.drawable.alert);

		       alertDialog.setButton("Ok",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}); 
		       alertDialog.show();
		      
	}
	
    public void ShowInformationDiolougue() {
		
		AlertDialog alertDialog = new AlertDialog.Builder(
				context).create();
		
		       alertDialog.setTitle(msgtitle);
		    // Setting Dialog Message
		        alertDialog.setMessage(msg);
		    // Setting Icon to Dialog
		       alertDialog.setIcon(R.drawable.information);

		       alertDialog.setButton("Ok",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}); 
		       alertDialog.show();
		      
	}
    
public void ShowProgressBar(ProgressDialog dialog) {
		
	        dialog = new ProgressDialog(context);
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.setMessage(msg);
	    	dialog.show();
	    			      
	}
	
	
}

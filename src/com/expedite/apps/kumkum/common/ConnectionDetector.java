package com.expedite.apps.kumkum.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.expedite.apps.kumkum.constants.Constants;

public class ConnectionDetector {

	private Context _context;
	public ConnectionDetector(Context context) {
		this._context = context;
	}
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			try {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							if (info[i].getTypeName().equalsIgnoreCase("MOBILE")) {
								if (!info[i].getExtraInfo().equalsIgnoreCase("IMS")) {
									return true;
								}
							} else {
								return true;
							}
						}
					}
			}catch (Exception ex)
			{
				Constants.writelog("ConnectionDetector","MSG 36:"+ex.getMessage());
			}
		}
		return false;
	}

}

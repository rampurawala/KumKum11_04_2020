package com.expedite.apps.kumkum;


import android.content.Intent;
import android.util.Log;

import com.expedite.apps.kumkum.common.Datastorage;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
        @Override
        public void onTokenRefresh()
        {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG1111",refreshedToken);
            Datastorage.SetGetFcmRegId(this, refreshedToken);
         }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent(this, MyFirebaseInstanceIdService.class);
        startService(intent);
    }
}



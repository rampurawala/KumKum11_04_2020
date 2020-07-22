package com.expedite.apps.kumkum;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.expedite.apps.kumkum.activity.BtmNavigationActivity;
import com.expedite.apps.kumkum.activity.CurriculumClassroomActivity;
import com.expedite.apps.kumkum.activity.CurriculumImageViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumMediaPlayerActivity;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumResponseClassroomActivity;
import com.expedite.apps.kumkum.activity.CurriculumTextFileActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.expedite.apps.kumkum.activity.CalendarDataDetailActivity;
import com.expedite.apps.kumkum.activity.FeedbackDetailActivity;
import com.expedite.apps.kumkum.activity.LeaveListActivity;
import com.expedite.apps.kumkum.activity.RedirectActivity;
import com.expedite.apps.kumkum.activity.SingleMessageActivity;
import com.expedite.apps.kumkum.activity.StudentCircularActivity_New;
import com.expedite.apps.kumkum.activity.SubjectWiseHWNTDNNotificationActivity;
import com.expedite.apps.kumkum.activity.SubjectWiseHWNotificationActivity;
import com.expedite.apps.kumkum.activity.TrackVehicle;
import com.expedite.apps.kumkum.activity.VoiceMessageListActivity;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.expedite.apps.kumkum.constants.Constants.displayMessage;


public class MyFirebaseMessagingService extends
        com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    public static String circulardetails;
    public static String filename;
    /*   private static String CHANNEL_ID = "General_channel_id";
       private static String CHANNEL_NAME = "General";
       private static String CHANNEL_DESCRIPTION = "General Notification";*/
    static String CHANNEL_ID = "my_channel_01";
    private static String subjectwisemsg = "";
    Context mContext;
    DatabaseHandler db = new DatabaseHandler(this);
    CharSequence name = "my_channel";
    String Description = "This is my channel",fileId="",filenameClassroom="",fileurl="",parentId="",fileType="",CurriculamDueDate="",Curriculam="",CurriculamId="",testId="",subId="";

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        try {
            if (s != null && !s.equals("")) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("TAG1111",refreshedToken);
                Datastorage.SetGetFcmRegId(this, refreshedToken);
            } else {
                Constants.writelog(TAG, "onNewToken() 63 Token:Null");
            }
            Constants.Logwrite("Refreshed token: 65 ", s);
        } catch (Exception ex) {
            Constants.writelog(TAG, "onNewToken() 67 Ex:" + ex.getMessage());
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Constants.Logwrite("Firebase", "onTaskRemoved()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.Logwrite("Firebase", "OnCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constants.Logwrite("Firebase", "OnDestroy()");
    }

    private void generateNotificationold(MyFirebaseMessagingService context,
                                                String message, String studid, String moduleid, String examid,
                                                String typeid, String name, String schoolid) {
        try {
            String msg = message;
            Constants.writelog("Notification: ",
                    "generateNotificationold:663 msg: " + message + " Studid: "
                            + studid + " moduleid: " + moduleid + "  examid: "
                            + examid + "  typeid:" + typeid + "  name: " + name
                            + "  schoolid: " + schoolid);
            Intent notificationIntent=null;
            msg = studid + "," + moduleid + "," + examid + "," + typeid + "," + schoolid + "," + name;
            if (!typeid.equals("5")) {
                if (moduleid.equals("9") || moduleid.equals("12") || moduleid.equals("15") || moduleid.equals("16") || moduleid.equals("17")) {
                    // msg = studid + "," + schoolid + ","+message;
                    //  msg = message;
                    notificationIntent = new Intent(context,
                            SubjectWiseHWNotificationActivity.class);
                    notificationIntent.putExtra("MSG", subjectwisemsg);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                    notificationIntent.putExtra("ModuleId", moduleid);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (moduleid.equals("18")) {
                    notificationIntent = new Intent(context,
                            CalendarDataDetailActivity.class);
                    if (subjectwisemsg != null && !subjectwisemsg.isEmpty()) {
                        String[] spltr = subjectwisemsg.split("@#@#@#");
                        if (spltr.length > 0) {
                            String[] strinrsplt = spltr[0].split("@@##HW@@##");
                            notificationIntent.putExtra("Title", strinrsplt[0]);
                            notificationIntent.putExtra("Date", strinrsplt[1]);
                            notificationIntent.putExtra("Description", strinrsplt[2]);
                            notificationIntent.putExtra("ImageUrl", strinrsplt[3]);
                            notificationIntent.putExtra("IsNotification", "1");
                        }
                    }
                } else if (moduleid.equals("19")) {
                    notificationIntent = new Intent(context, VoiceMessageListActivity.class);
                } else if (moduleid.equals("10")) {
                    msg = studid + "," + schoolid + "," + message;
                    notificationIntent = new Intent(context, SubjectWiseHWNTDNNotificationActivity.class);
                    notificationIntent.putExtra("MSG", subjectwisemsg);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (moduleid.equals("11")) {
                    notificationIntent = new Intent(context, TrackVehicle.class);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                } else if (circulardetails != null && circulardetails != "") {
                    //chnge by vishwa StudentCircularActivity-->StudentCircularActivity_New,"filename-->CircularPath"
                    notificationIntent = new Intent(context, StudentCircularActivity_New.class);
                    notificationIntent.putExtra("CircularPath", filename);
                    notificationIntent.putExtra("Name", subjectwisemsg);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (moduleid.equals("901")) {
                    notificationIntent = new Intent(context, FeedbackDetailActivity.class);
                    notificationIntent.putExtra("value", subjectwisemsg);
                    notificationIntent.putExtra("isNotification", "1");

                } else if (moduleid.equals("902")) {
                    notificationIntent = new Intent(context, LeaveListActivity.class);
                    notificationIntent.putExtra("value", subjectwisemsg);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (moduleid.equals("1050") || moduleid.equals("1051")) {
                    if(fileType.equalsIgnoreCase("1")){
                        notificationIntent = new Intent(context, CurriculumPdfViewActivity.class);
                        notificationIntent.putExtra("isFrom", "notification");
                        notificationIntent.putExtra("Name", filenameClassroom);
                        notificationIntent.putExtra("Url", fileurl);
                        notificationIntent.putExtra("parentId", parentId);
                    }else if(fileType.equalsIgnoreCase("2")){
                        notificationIntent = new Intent(context, CurriculumImageViewActivity.class);
                        notificationIntent.putExtra("isFrom", "notification");
                        notificationIntent.putExtra("Name", filenameClassroom);
                        notificationIntent.putExtra("Url", fileurl);
                        notificationIntent.putExtra("parentId", parentId);
                    }else if(fileType.equalsIgnoreCase("3")){
                        notificationIntent = new Intent(context, CurriculumMediaPlayerActivity.class);
                        notificationIntent.putExtra("isFrom", "notification");
                        notificationIntent.putExtra("Name", filenameClassroom);
                        notificationIntent.putExtra("Url", fileurl);
                        notificationIntent.putExtra("parentId", parentId);
                    }else if(fileType.equalsIgnoreCase("4")){
                        notificationIntent = new Intent(context, CurriculumImageViewActivity.class);
                        notificationIntent.putExtra("isFrom", "notification");
                        notificationIntent.putExtra("Name", filenameClassroom);
                        notificationIntent.putExtra("Url", fileurl);
                        notificationIntent.putExtra("isVideo", "1");
                        notificationIntent.putExtra("parentId", parentId);

                    }else if(fileType.equalsIgnoreCase("5")){
                        notificationIntent = new Intent(context, CurriculumTextFileActivity.class);
                        notificationIntent.putExtra("isFrom", "notification");
                        notificationIntent.putExtra("Name", filenameClassroom);
                        notificationIntent.putExtra("Url", fileurl);
                        notificationIntent.putExtra("parentId", parentId);

                    }

                } else if (moduleid.equals("1052")){
                    notificationIntent = new Intent(context, CurriculumResponseClassroomActivity.class);
                    notificationIntent.putExtra("isFrom", "notification");
                    notificationIntent.putExtra("CurriculamId", CurriculamId);
                    notificationIntent.putExtra("Curriculam", Curriculam);
                    notificationIntent.putExtra("CurriculamDueDate", CurriculamDueDate);

                }else if (moduleid.equals("1053")){
                    notificationIntent = new Intent(context, BtmNavigationActivity.class);
                    notificationIntent.putExtra("isFrom", "notification");
                    notificationIntent.putExtra("selectedId", subId);
                }
                else {
                    notificationIntent = new Intent(context, RedirectActivity.class);
                    notificationIntent.putExtra("MSG", msg);
                }
            } else {
                    notificationIntent = new Intent(context,
                            SingleMessageActivity.class);
                    notificationIntent.putExtra("MSG", message);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
            }

            Random rnd = new Random();
            //int notificationID = new Random().nextInt(3000);
            int randnoint = rnd.nextInt(999999);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, randnoint, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            // int notificationIcon = R.mipmap.notification_big;
            int notificationIcon = R.mipmap.notification_small;
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }

            Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.notification_small);
            //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext,ad)
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setLargeIcon(notificationLargeIconBitmap)
                    .setColor(context.getResources().getColor(R.color.colorPrimaryDark))
                    .setContentTitle(name)
                    .setSmallIcon(notificationIcon)
                    .setContentText(Html.fromHtml(message))
                    .setAutoCancel(true)
                    .setLights(Color.RED, 1000, 1000)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    //.setNumber(10)
                    //.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message)))
                    .setContentIntent(pendingIntent);
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            notificationManager.notify(randnoint, notificationBuilder.build());
            /*
             * String tm; tm = "";
             */
            Constants.Logwrite("NotDetails", "Details:" + msg);
           /* if (!typeid.equals("5")) {
                if (moduleid.equals("9") || moduleid.equals("12") || moduleid.equals("15") || moduleid.equals("16") || moduleid.equals("17")) {
                    // msg = studid + "," + schoolid + ","+message;
                    msg = message;
                    notificationIntent = new Intent(context,
                            SubjectWiseHWNotificationActivity_Food_Infraction.class);
                    notificationIntent.putExtra("MSG", subjectwisemsg);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                    notificationIntent.putExtra("ModuleId", moduleid);
                } else if (moduleid.equals("18")) {
                    notificationIntent = new Intent(context,
                            CalendarDataDetailActivity.class);
                    if (subjectwisemsg != null && !subjectwisemsg.isEmpty()) {
                        String[] spltr = subjectwisemsg.split("@#@#@#");
                        if (spltr.length > 0) {
                            String[] strinrsplt = spltr[0].split("@@##HW@@##");
                            notificationIntent.putExtra("Title", strinrsplt[0]);
                            notificationIntent.putExtra("Date", strinrsplt[1]);
                            notificationIntent.putExtra("Description", strinrsplt[2]);
                            notificationIntent.putExtra("ImageUrl", strinrsplt[3]);
                            notificationIntent.putExtra("IsNotification", "1");
                        }
                    }
                } else if (moduleid.equals("10")) {
                    msg = studid + "," + schoolid + "," + message;
                    notificationIntent = new Intent(context, SubjectWiseHWNTDNNotificationActivity.class);
                    notificationIntent.putExtra("MSG", subjectwisemsg);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                } else if (moduleid.equals("11")) {
                    notificationIntent = new Intent(context, TrackVehicle.class);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                } else if (circulardetails != null && circulardetails != "") {
                    //chnge by vishwa StudentCircularActivity-->StudentCircularActivity_New,"filename-->CircularPath"
                    notificationIntent = new Intent(context, StudentCircularActivity_New.class);
                    notificationIntent.putExtra("CircularPath", filename);
                    notificationIntent.putExtra("Studid", studid);
                    notificationIntent.putExtra("Schoolid", schoolid);
                } else if (moduleid.equals("901")) {
                    notificationIntent = new Intent(context, FeedbackDetailActivity.class);
                    notificationIntent.putExtra("value", subjectwisemsg);
                } else if (moduleid.equals("902")) {
                    notificationIntent = new Intent(context, LeaveListActivity.class);
                    notificationIntent.putExtra("value", subjectwisemsg);
                } else {
                    notificationIntent = new Intent(context, RedirectActivity.class);
                    notificationIntent.putExtra("MSG", msg);
                }
            } else {
                notificationIntent = new Intent(context,
                        SingleMessageActivity.class);
                notificationIntent.putExtra("MSG", message);
                notificationIntent.putExtra("Studid", studid);
                notificationIntent.putExtra("Schoolid", schoolid);
            }*/
          /*  notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
           /* Random rnd1 = new Random();
            int randnoint = rnd1.nextInt(999999);
            PendingIntent intent = PendingIntent.getActivity(context,
                    randnoint, notificationIntent, 0);
*/
            notificationBuilder.setContentIntent(pendingIntent);

            // notification.setLatestEventInfo(context, title, message, intent);
            // notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // Play default notification sound
            // notification.defaults |= Notification.DEFAULT_SOUND;
            // notification.sound = Uri.parse("android.resource://" +
            // context.getPackageName() + "your_sound_file_name.mp3");

            // Vibrate if vibrate is enabled
            // notification.defaults |= Notification.DEFAULT_VIBRATE;

            if (Constants.NOTIFICATION_ID > 1073741824) {
                Constants.NOTIFICATION_ID = 0;
            }
            /*Random rnd = new Random();
            int randno = rnd.nextInt(999999);*/
            Constants.Logwrite("Random:", "RandomNum:" + randnoint);

            // notificationManager.notify(Constants.NOTIFICATION_ID++,notification);
            notificationManager.notify(randnoint, notificationBuilder.build());

            // notificationManager.notify(0, notification);
        } catch (Exception err) {
            Constants.Logwrite("Notification:", err.getMessage() + ":::"
                    + err.getStackTrace().toString());
            Constants.writelog("Notification:",
                    "Exception: generateNotificationold 736" + err.getMessage()
                            + ":::" + err.getStackTrace().toString());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            String studid = remoteMessage.getData().get("studid");
            int schoolid = Integer.parseInt(remoteMessage.getData().get("schoolid"));
            String Notidismsg = "";
            DatabaseHandler jj = new DatabaseHandler(this);
            int is_stud_exist = 0;
            String moduleid = remoteMessage.getData().get("moduleid");
            String mStudentNameLocal = "";
            try {
                Cursor cn = jj.getStudentDetails(Integer.parseInt(studid),
                        schoolid);
                cn.moveToFirst();
                mStudentNameLocal = cn.getString(cn.getColumnIndex(DatabaseHandler.KEY_NAME));
                cn.close();
            } catch (Exception ex) {
                Constants.Logwrite("FCM Intent Service", "Exception 100:" + ex.getMessage());
            }
            if (moduleid.equalsIgnoreCase("1001")) {
                try {
                    Constants.writelog("Notificaion",
                            " setCurrentYearAsDefaultYear 88 Start");
                    try {
                        Datastorage.SetCurrentYearisDefYear(getApplicationContext(), 0);
                        List<Contact> contactList = new ArrayList<Contact>();
                        contactList = db.getAllContacts();
                        Contact c;
                        String schoolidTemp = "";
                        String studidTemp = "";
                        int flag = 0;
                        for (int i = 0; i < contactList.size(); i++) {
                            c = contactList.get(i);
                            schoolidTemp = String.valueOf(c.getSchoolId());
                            studidTemp = String.valueOf(c.getStudentId());
                            int result = Constants
                                    .SetCurrentYearAsDefYear(this,
                                            schoolidTemp, studidTemp, db);
                            if (result == 1) {
                                flag = 1;
                            } else {
                                flag = 0;
                                break;
                            }
                        }
                        if (flag == 1) {
                            PackageInfo pinfo = getPackageManager()
                                    .getPackageInfo(getPackageName(), 0);
                            int versionNumber = 0;
                            versionNumber = pinfo.versionCode;
                            Datastorage.SetCurrentYearisDefYear(
                                    getApplicationContext(), versionNumber);
                        }
                    } catch (Exception ex) {
                        Constants.writelog(
                                "Notification",
                                "Exception SetCurrentYearAsDefaultYear() 133 MSG:"
                                        + ex.getMessage() + ":::::"
                                        + ex.getStackTrace());
                    }
                    Constants.writelog("Notificaion",
                            " setCurrentYearAsDefaultYear 91 End");
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:94 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1002")) {
                try {
                    Constants.writelog("Notification",
                            "L147 Module:1002.");
                    Datastorage.SetLastAutoUpdateProfile(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:147 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1003")) {
                try {
                    Constants.writelog("Notification",
                            "L156 Module:1003.");
                    Datastorage.SetLastAutoUpdateAttendance(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:157 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1004")) {
                try {
                    Constants.writelog("Notification",
                            "L165 Module:1004.");
                    Datastorage.SetLastAutoUpdateNoticeDay(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:166 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1005")) {
                try {
                    Constants.writelog("Notification",
                            "L174 Module:1005.");
                    Datastorage.SetLastAutoUpdateMessageDay(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:175 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1006")) {
                try {
                    Constants.writelog("Notification",
                            "L183 Module:1006.");
                    Datastorage.SetLastAutoUpdateExamDay(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:184 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1007")) {
                try {
                    Constants.writelog("Notification",
                            "L192 Module:1007.");
                    Datastorage.SetLastAutoUpdateExamMarksheetDay(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:193 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1008")) {
                try {
                    Constants.writelog("Notification",
                            "L201 Module:1008.");
                    Datastorage.SetLastAutoUpdatePhotoDay(this, 0);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:212 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            }

            // For delete tables
            else if (moduleid.equalsIgnoreCase("1009")) {
                Constants.writelog("Notification", "L218 Module:1009");
                String msg = remoteMessage.getData().get("message");
                Constants.writelog("Notification", "L221 Msg Type:" + msg);
                try {
                    if (msg != null && msg.equals("")) {
                        String[] parts = msg.split(",");
                        for (int i = 0; i < parts.length; i++) {
                            try {
                                int res = db.DeleteTableRecords(parts[i]);
                                Constants.writelog("Notification",
                                        "Type=" + parts[i] + " Result:" + res);
                            } catch (Exception ex) {
                                Constants.writelog("Notification",
                                        "Type: " + parts[i] + " Exceptoin:225"
                                                + ex.getMessage() + " :::: "
                                                + ex.getStackTrace());
                            }
                        }
                    } else {
                        Constants.writelog("Notification", "MSG is null 239");
                    }
                } catch (Exception ex) {
                    Constants.writelog("Notification", "MSG Type:" + msg
                            + " Exceptoin:246" + ex.getMessage() + " :::: "
                            + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("1010")) {
                Constants.writelog("Notification", "L242 Module:1010");
                String msg = remoteMessage.getData().get("message");
                Constants
                        .writelog("Notification", "L221 Msg Type:" + msg);
                try {
                    if (msg != null && msg.equals("")) {
                        if (msg.equals("1")) {
                            Datastorage.SetPhotoGalleryType(this, 1);
                        } else {
                            Datastorage.SetPhotoGalleryType(this, 0);
                        }
                    } else {
                        Constants.writelog("Notification", "MSG is null 252");
                    }
                } catch (Exception ex) {
                    Constants.writelog("Notification", "MSG Type:" + msg
                            + " Exceptoin:256" + ex.getMessage() + " :::: "
                            + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("2001")) {
                try {
                    Constants.writelog("Notification", "218 Module:2001.");
                    String msg = remoteMessage.getData().get("message");
                    int lines = 1000;
                    try {
                        lines = Integer.parseInt(msg);
                    } catch (Exception ex) {
                    }
                    Datastorage.SetLogSendComplete(getApplicationContext(),
                            lines);
                    String deviceId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String DeviceDetails = "Details: Mobile:"
                            + Datastorage
                            .GetPhoneNumber(getApplicationContext())
                            + "||| Name:" + Datastorage
                            .GetStudentName(getApplicationContext()) + "|||"
                            + Datastorage.GetFcmRegId(getApplicationContext())
                            + "||||" + Build.DEVICE + "|||" + Build.MODEL
                            + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                            + "|||" + Build.VERSION.RELEASE;
                    Constants.MyTaskSendLog SendLog = new Constants.MyTaskSendLog();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        SendLog.execute(getApplicationContext(), DeviceDetails, deviceId, lines);
                    } else {
                        SendLog.execute(getApplicationContext(), DeviceDetails, lines);
                    }
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:225 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            } else if (moduleid.equalsIgnoreCase("2002")) {
                try {
                    Constants.writelog("Notification", "254 Module:2002.");
                    String msg = remoteMessage.getData().get("message");
                    int lines = 0;
                    try {
                        lines = Integer.parseInt(msg);
                    } catch (Exception ex) {
                    }
                    Datastorage.SetlineForlog(getApplicationContext(), lines);
                    return;
                } catch (Exception ex) {
                    Constants.writelog("Notification", "Exceptoin:225 "
                            + ex.getMessage() + " :::: " + ex.getStackTrace());
                }
            }
            try {
                is_stud_exist = jj.getContactsCountUsingStud_ID_and_School_Id(
                        Integer.parseInt(studid), schoolid);
                Constants.Logwrite("Notification:", "Count:" + is_stud_exist);
                Constants.writelog("Notification:", "Count:"
                        + is_stud_exist);
            } catch (Exception err) {
                Constants.writelog("Notificatoin",
                        "Exception_DB:" + err.getMessage() + "StackStrace"
                                + err.getStackTrace());
            }
            if (is_stud_exist > 0) {
                Constants.Logwrite(TAG, "Received message");
                circulardetails = remoteMessage.getData().get("circulardetails");
                String message = remoteMessage.getData().get("message");
                Constants.Logwrite("Notification:", "message:" + message);
                Constants.Logwrite("Notification:", "studid :" + studid);
                Constants.writelog("Notification:", "message:" + message);
                Constants.writelog("Notification:", "studid :" + studid);

                int modid = Integer.parseInt(moduleid);
                Constants.Logwrite("Notification:", "moduleid:" + moduleid);

                String examid = remoteMessage.getData().get("examid");
                Constants.Logwrite("Notification:", "examid:" + examid);

                int messageid = Integer.parseInt(remoteMessage.getData().get(
                        "messageid"));
                Constants.Logwrite("Notification:", "messageid:" + messageid);

                String typeid = remoteMessage.getData().get("typeid");
                int tpid = Integer.parseInt(typeid);
                Constants.Logwrite("Notification:", "typeid:" + typeid);

               /* String name = intent.getExtras().getString("name");
                Constants.Logwrite("Notification:", "name:" + name);*/

                String SchoolId = remoteMessage.getData().get("schoolid");
                Constants.Logwrite("Notification:", "SchoolId:" + SchoolId);

                Constants.writelog("Notification Details 119 :",
                        "moduleid:" + moduleid + "  examid:" + examid
                                + "  messageid:" + messageid + "   Typeid:"
                                + typeid + "   name:" + mStudentNameLocal + "  schoolid:"
                                + SchoolId);
                //***************************************************************************
                //String typeid = intent.getExtras().getString("typeid");
                //String[] Splits = message.split("##@@");
                //int msgid = Integer.parseInt(Splits[5]);
                Constants.Logwrite("Notification:", "modid:" + modid);
                try {
                    if (modid == 1) {
                        NotifyExamMessage(this, studid, examid, tpid,
                                SchoolId, jj);
                    } else if (modid == 8) {
                        message = NotifyPhotoGallery(this, message, studid,
                                schoolid, jj);
                    } else if (modid == 9 || modid == 10 || modid == 12 || modid == 15 || modid == 16 || modid == 17 || modid == 18) {
                        Constants.writelog("Notification: 137:",
                                "Message: " + message);
                        String[] msgitem1 = message.split("@@##HWO@@##");
                        String[] msgitem = msgitem1[0].split(",");
                        int HMSGID = Integer.parseInt(msgitem[0]);
                        int HMSG_DAY = Integer.parseInt(msgitem[1]);
                        int HMSG_MONTH = Integer
                                .parseInt(msgitem[2]);
                        int HMSG_YEAR = Integer.parseInt(msgitem[3]);
                        int HMSG_YEARID = Integer.parseInt(msgitem[4]);
                        message = msgitem1[1];
                        String[] spltr = message.split("@#@#@#");
                        if (spltr.length > 0) {
                            for (String hwstr : spltr) {
                                String[] strinrsplt = hwstr.split("@@##HW@@##");
                                if (modid == 15 || modid == 17) {
                                    Notidismsg = Notidismsg
                                            + strinrsplt[0] + " ";
                                } else if (modid == 18) {
                                    Notidismsg = "Event:" + strinrsplt[0] + ".\nDate:" + strinrsplt[1];
                                } else {
                                    Notidismsg = Notidismsg
                                            + strinrsplt[1] + ":";
                                    if (modid == 9) {
                                        Notidismsg = Notidismsg
                                                + strinrsplt[2].split("@PDF@")[0] + " ";
                                    } else {
                                        Notidismsg = Notidismsg
                                                + strinrsplt[2] + " ";
                                    }
                                }
                            }
                            if (modid == 18) {
                                subjectwisemsg = message;
                                message = Notidismsg;
                                generateNotificationold(this, message, studid, moduleid, examid, typeid, mStudentNameLocal, SchoolId);
                                return;
                            }
                        }
                        String HW_MSG = msgitem1[1];
                        subjectwisemsg = HW_MSG;
                        Boolean Ismessgaeinsert = jj.CheckMessageInsertorNot(
                                Integer.parseInt(studid),
                                Integer.parseInt(SchoolId), HMSGID);
                        if (!Ismessgaeinsert) {
                            jj.AddSMS(new Contact(HMSGID, HW_MSG, Integer
                                    .parseInt(studid), Integer
                                    .parseInt(SchoolId), HMSG_DAY, HMSG_MONTH,
                                    HMSG_YEAR, modid, HMSG_YEARID));
                        }
                        Constants.setDefaultYear(this, studid,
                                SchoolId, HMSG_YEARID + "", db);
                    } else if (modid == 11) {

                    } else if (modid == 901) {
                        String[] spltr = message.split("@#@#@#");
                        if (spltr != null && spltr.length > 1) {
                            message = spltr[0];
                            subjectwisemsg = spltr[1];
                            generateNotificationold(this, message, studid, moduleid, examid, typeid, mStudentNameLocal, SchoolId);
                            return;
                        }
                    } else if (modid == 902) {
                        String[] spltr = message.split("@#@#@#");
                        if (spltr != null && spltr.length > 1) {
                            message = spltr[0];
                            subjectwisemsg = spltr[1];
                            generateNotificationold(this, message, studid, moduleid, examid, typeid, mStudentNameLocal, SchoolId);
                            return;
                        }
                    }else if (modid == 1050 || modid ==1051) {
                        String[] spltr = message.split("@#@#@#");
                        if (spltr != null && spltr.length > 1) {
                            mStudentNameLocal=spltr[0];
                            message = spltr[1];
                            filenameClassroom=spltr[1];
                            // value = "New Classroom file received" + "@#@#@#" + filename  + "@#@#@#";//0
                            //                        value += fileId + "@@##";//1
                            //                        value += url + "@@##";//2
                            //                        value += parentFolderId + "@@##";//3
                            //                        value += fileType + "@@##";//4
                            String[] spltrnested = spltr[2].split("@@##");
                            fileId=spltrnested[0];
                            fileurl=spltrnested[1];
                            parentId=spltrnested[2];
                            fileType=spltrnested[3];
                            generateNotificationold(this, message, studid,String.valueOf(modid) , examid, typeid, mStudentNameLocal, SchoolId);
                            return;
                        }
                    } else if (modid == 1053) {
                        String[] spltr = message.split("@#@#@#");
                        if (spltr != null && spltr.length > 1) {
                            mStudentNameLocal=spltr[0];
                            message = spltr[1];

                            // value = "New Classroom file received" + "@#@#@#" + filename  + "@#@#@#";//0
                            //                        value += fileId + "@@##";//1
                            //                        value += url + "@@##";//2
                            //                        value += parentFolderId + "@@##";//3
                            //                        value += fileType + "@@##";//4
                            String[] spltrnested = spltr[2].split("@@##");
                            testId=spltrnested[0];
                            subId=spltrnested[1];
                            generateNotificationold(this, message, studid,String.valueOf(modid) , examid, typeid, mStudentNameLocal, SchoolId);
                            return;
                        }
                    } else if (modid == 1052) {
                        String[] spltr = message.split("@#@#@#");
                        if (spltr != null && spltr.length > 1) {
                            message = spltr[0];
                            // value = "New Classroom file received" + "@#@#@#" + filename  + "@#@#@#";//0
                            //                        value += fileId + "@@##";//1
                            //                        value += url + "@@##";//2
                            //                        value += parentFolderId + "@@##";//3
                            //                        value += fileType + "@@##";//4
                            String[] spltrnested = spltr[1].split("@@##");
                            CurriculamId=spltrnested[0];
                            Curriculam=spltrnested[1];
                            CurriculamDueDate=spltrnested[2];
                            generateNotificationold(this, message, studid,String.valueOf(modid) , examid, typeid, mStudentNameLocal, SchoolId);
                            return;
                        }
                    }else {
                        int IsProcessed = 0;
                        if (modid == 7) {
                            IsProcessed = 1;
                            if (circulardetails != null
                                    && circulardetails != "") {
                                String[] parts = circulardetails
                                        .split("@@@ci###");
                                //15252  -0
                                //@@@ci###
                                //12:36 testing---1
                                //@@@ci###
                                //410---2
                                //@@@ci###
                                //637066533780390599---3
                                //@@@ci###
                                //1_8414_1571036777.pdf---4
                                //@@@ci###
                                //0---5
                                //@@@ci###
                                //5----6
                                //@@@ci###
                                //14/10/2019-----7
                                if (parts.length > 1) {
                                    String circularid = parts[0];
                                    String circularname = parts[1];
                                    String groupid = parts[2];
                                    String groupname = parts[3];
                                    String yearid = parts[7];
                                    String isdeleted = parts[6];
                                    String ticks = parts[4];
                                    filename = parts[5];
                                    String datetext = parts[8];
                                    Boolean isinserted = db
                                            .CheckCircularInserted(
                                                    Integer.parseInt(studid),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(circularid));
                                    if (!isinserted) {
                                        int res = db.AddCircular(new Contact(
                                                Integer.parseInt(studid),
                                                Integer.parseInt(SchoolId),
                                                Integer.parseInt(yearid),
                                                Integer.parseInt(groupid),
                                                groupname, Integer
                                                .parseInt(circularid),
                                                circularname, datetext,
                                                filename, ticks, Integer
                                                .parseInt(isdeleted)));
                                        if (res != 1) {
                                            Constants.Logwrite("FcmIntentservice",
                                                    "221 AddCircular fail.");
                                            Constants.writelog(
                                                    "FcmIntentservice",
                                                    "221 AddCircular fail.");
                                        }
                                    } else {
                                        int res = db
                                                .UpdateCircular(new Contact(
                                                        Integer.parseInt(studid),
                                                        Integer.parseInt(SchoolId),
                                                        Integer.parseInt(yearid),
                                                        Integer.parseInt(groupid),
                                                        groupname, Integer.parseInt(circularid), circularname,
                                                        datetext, filename, ticks, Integer.parseInt(isdeleted)));
                                        if (res != 1) {
                                            Constants.writelog("FcmIntentservice", "250 UpdateCircular fail.");
                                        }
                                    }
                                    String fileURL = Constants.circularUrl + filename;
                                    File myDir = Constants.CreatePhotoGalleryFolder();
                                    File file = new File(myDir, filename);
                                    if (!file.exists()) {
                                        Constants.SavePdf(this, fileURL, filename);
                                    }
                                    Constants.setDefaultYear(this, studid, SchoolId, yearid, db);
                                    filename = Constants.circularUrl + filename;
                                    subjectwisemsg = circularname;
                                }
                            }
                        }
                        if (IsProcessed == 0) {
                            Constants.setDefaultYear(this, studid,
                                    SchoolId, "", db);
                            int SMS_Record_Count = 0;
                            //String SMS_Id = "";
                            String[] messages = null;
                            int stdid = Integer.parseInt(studid);
                            // Gets Count in SMS Table
                            SMS_Record_Count = jj.getSMSCount(stdid, schoolid);
                            // Delete Previous Table Duplicate Entry
                            DeleteDuplicateRecords(studid, SchoolId, jj);
                            if (SMS_Record_Count > 0) {
                                // Get Last Message Id From Local Database
                                String yearidParts = db
                                        .GetDefaultAcademicYearAccount(
                                                schoolid, stdid);
                                String[] parts = yearidParts.split(",");
                                int SMSId = 0;
                                try {
                                    SMSId = jj.GetLatestSMSID(stdid, schoolid,
                                            Integer.parseInt(parts[1]
                                            ));
                                } catch (Exception ex) {
                                    Constants.writelog("FcmIntenetService", "MSg 477:" + ex.getMessage());
                                }
                                //SMS_Id = Integer.toString(SMSId);
                                Constants.GetNewMessageDetail(this, studid, schoolid + "", SMSId);
                                /*messages = Constants
                                        .GetMessageDetailsForNotificationNew(
                                                this, studid, SchoolId,
                                                SMS_Id);*/
                            } else {
                                //SMS_Id = "0";
                                Constants.GetNewMessageDetail(this, studid, schoolid + "", 0);
                                /*messages = Constants
                                        .GetMessageDetailsForNotificationNew(
                                                this, studid, SchoolId,
                                                SMS_Id);*/
                            }
                            // Store this Messages is Local Database.
                            if (messages != null && messages.length > 0) {
                                for (int i = 0; i < messages.length; i++) {
                                    int day = 0;
                                    int month = 0;
                                    int year = 0;
                                    int Mod_Id = 0;
                                    int YearId = 0;
                                    String[] msgitem = messages[i]
                                            .split("##@@##@@");
                                    int msgid = 0;
                                    if (msgitem != null && msgitem.length >= 1)
                                        msgid = Integer.parseInt(msgitem[4]
                                        );
                                    else
                                        Constants.Logwrite("MessageLog", "MsgId:" + i + ""
                                                + msgitem[4]);
                                    String msgtext = "";
                                    if (msgitem != null && msgitem.length >= 2)
                                        msgtext = msgitem[6];
                                    else
                                        Constants.Logwrite("GenListInMonth", "MessageText:+"
                                                + i + ":" + msgitem[6]);
                                    try {
                                        day = Integer.parseInt(msgitem[0]
                                        );
                                        month = Integer.parseInt(msgitem[1]
                                        );
                                        year = Integer.parseInt(msgitem[2]
                                        );
                                        YearId = Integer.parseInt(msgitem[5]
                                        );
                                        if (!msgitem[3].trim().equals("")) {
                                            Mod_Id = Integer.parseInt(msgitem[3]
                                            );
                                        }
                                    } catch (Exception err) {
                                        Constants.Logwrite("FCMInetentService",
                                                "Exception:308 "
                                                        + err.getMessage()
                                                        + ":::::"
                                                        + err.getStackTrace());
                                    }
                                    if (Mod_Id == 5) {
                                        Boolean Ismessgaeinsert = false;
                                        Ismessgaeinsert = jj
                                                .CheckAbsentMessageInsertorNot(
                                                        Integer.parseInt(studid),
                                                        Integer.parseInt(SchoolId),
                                                        day, month, year,
                                                        Mod_Id);
                                        if (Ismessgaeinsert) {
                                        } else {
                                            jj.AddSMS(new Contact(msgid,
                                                    msgtext, stdid, schoolid,
                                                    day, month, year, Mod_Id,
                                                    YearId));
                                        }
                                    } else {
                                        jj.AddSMS(new Contact(msgid, msgtext,
                                                stdid, schoolid, day, month,
                                                year, Mod_Id, YearId));
                                    }
                                }
                            }
                            if (modid == 5) {
                                String[] messagesabsent = null;
                                int Year_Id = Integer.parseInt(jj.GetYearId(
                                        stdid, schoolid));
                                messagesabsent = Constants
                                        .GetLessonDairyMessageForAbsent(
                                                this, stdid, schoolid,
                                                Year_Id);
                                // Get Student Year Id
                                int count = 0;
                                if (messagesabsent != null
                                        && messagesabsent.length > 0) {
                                    count = messagesabsent.length;
                                    for (int i = 0; i < count; i++) {
                                        String[] msgitem = messagesabsent[i]
                                                .split("##@@##@@");
                                        Constants.writelog(
                                                "Notification:274 ",
                                                "message in modid:5 "
                                                        + messagesabsent[i]
                                        );
                                        int AbsemtModId = Integer
                                                .parseInt(msgitem[3]);
                                        int SMS_DAY = Integer
                                                .parseInt(msgitem[0]);
                                        int SMS_MONTH = Integer
                                                .parseInt(msgitem[1]);
                                        int SMS_YEAR = Integer
                                                .parseInt(msgitem[2]);
                                        //int SMS_MSG_ID = Integer.parseInt(msgitem[4]);
                                        // Check Message Already Insert or Not
                                        Boolean Ismessgaeinsert = false;
                                        Ismessgaeinsert = jj.CheckAbsentMessageInsertorNot(
                                                Integer.parseInt(studid),
                                                Integer.parseInt(SchoolId),
                                                SMS_DAY, SMS_MONTH,
                                                SMS_YEAR, AbsemtModId);
                                        if (Ismessgaeinsert) {
                                        } else {
                                            jj.AddSMS(new Contact(Integer
                                                    .parseInt(msgitem[4]
                                                    ),
                                                    msgitem[6],
                                                    Integer.parseInt(studid),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(msgitem[0]
                                                    ),
                                                    Integer.parseInt(msgitem[1]
                                                    ),
                                                    Integer.parseInt(msgitem[2]
                                                    ),
                                                    Integer.parseInt(msgitem[3]
                                                    ),
                                                    Integer.parseInt(msgitem[5]
                                                    )));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception err) {
                    Constants.writelog("Notification",
                            "onMessage Exception:337 " + err.getMessage()
                                    + ":::" + err.getStackTrace());
                }
                // *****************************************************************************
                if (is_stud_exist > 0) {

                    String Is_Msg_Update = Constants
                            .UpdateNotifcationTable(this, messageid);
                    if (Is_Msg_Update.equals("true")) {
                        //
                        Constants.Logwrite("Notification:", "UpdateMessage:" + Is_Msg_Update);
                    } else {
                        //
                        Constants.Logwrite("Notification:", "UpdateMessage:" + Is_Msg_Update);
                    }
                    displayMessage(this, message);
                    Constants.writelog("Notification:338",
                            "msg for display:" + message);
                    // notifies user
                    if (modid == 9 || modid == 10 || modid == 12 || modid == 15 || modid == 16 || modid == 17) {
                        message = Notidismsg;
                    }
                    Constants.writelog("Notification:343",
                            "msg for generate notifi:" + message);
                    generateNotificationold(this, message, studid, moduleid,
                            examid, typeid, mStudentNameLocal, SchoolId);
                } else {
                    /*
                     * Delete account from fcm service if user not exist in
                     * mobile app
                     */
                    String User_Exist = Constants.CheckUserRegistered(
                            Integer.parseInt(studid), schoolid,
                            getApplicationContext());
                    if (User_Exist.equals("true")) {
                        Constants.DeleteAccountFromGCMData(
                                Integer.parseInt(studid), schoolid,
                                getApplicationContext());
                    }
                }
            } else {
                // Remove Student Data
                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                // LoginDetail.setDeivce_ID(m_androidId);
                Datastorage.SetDeviceId(this, m_androidId);
                Constants.DeleteAccountFromGCMData(
                        Integer.parseInt(studid), schoolid, this);
            }


        } catch (Exception ex) {
            Log.e("msg 110", ex.getMessage());
            //  Common.writelog("OnMessage 43:", "Error:" + ex.getMessage());
        }
    }

    public void DeleteDuplicateRecords(String studid, String SchoolId,
                                       DatabaseHandler jj) {
        try {
            List<Contact> contacts = jj.GetAllSMSDetails(
                    Integer.parseInt(studid), Integer.parseInt(SchoolId));
            int cntr = 0;
            String[] messagesdelete = new String[contacts.size()];
            if (messagesdelete != null && messagesdelete.length > 0)
                for (Contact cn : contacts) {

                    int MsgId = cn.getSMSID();

                    // Check Entry of This Message Id Once or Not

                    List<Contact> contactdelete = jj
                            .GetMessageDetailsFromMessageId(
                                    Integer.parseInt(studid),
                                    Integer.parseInt(SchoolId), MsgId);

                    String[] selmsgdetails = new String[contactdelete.size()];
                    if (selmsgdetails != null && selmsgdetails.length > 0) {
                        int incr = 0;
                        for (Contact cn1 : contactdelete) {
                            String msg = cn1.getGlobalText();
                            if (incr == 0) {
                            } else {
                                String[] spltrstring = msg.split("##@@");
                                String SMS_ID = spltrstring[0];
                                String MSG_ID = spltrstring[1];
                                int Cnt = Integer.parseInt(spltrstring[2]
                                );
                                if (Cnt > 1) {
                                    //Delete Record
                                    int IsRecordDeleted = jj
                                            .DeleteMessageRecord(SMS_ID, MSG_ID);
                                }
                            }
                            incr++;
                        }
                    }
                }
        } catch (Exception err) {
            Constants.Logwrite("Notification:",
                    "Exception:DeleteDuplicateRecords" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private String NotifyPhotoGallery(MyFirebaseMessagingService cntxt, String message,
                                      String studid, int schoolid, DatabaseHandler jj) {
        try {
            int IsAutoDownload = 1;
            File file = Constants.CreatePhotoGalleryFolder();
            Constants.Logwrite("AlbumNotification", "Paht:"
                    + file.getAbsolutePath().toString());
            Constants.Logwrite("AlbumNotification", "Name" + file.getName());
            int ClassSecId = 0;
            int AlbumId = 0;
            String[] msg = message.split("@@@@####");
            Constants.Logwrite("AlbumNotification", "MessageLength" + msg.length);
            message = msg[1] + " " + msg[0];
            Constants.Logwrite("AlbumNotification", "Message" + message);
            ClassSecId = Integer.parseInt(msg[2]);
            Constants.Logwrite("AlbumNotification", "ClassSecId" + ClassSecId);
            AlbumId = Integer.parseInt(msg[3]);
            Constants.Logwrite("AlbumNotification", "AlbumId" + AlbumId);
            String[] StrPhotoAlbumDetails = Constants
                    .AlbumDetailsWithClassSecId(cntxt, schoolid, ClassSecId,
                            AlbumId, IsAutoDownload);

            if (StrPhotoAlbumDetails.length > 0) {
                for (int i = 0; i < StrPhotoAlbumDetails.length; i++) {
                    //
                    String[] msgitem = StrPhotoAlbumDetails[i].split("##@@");
                    String filename = msgitem[2];
                    File fileexist = new File(file + "/" + filename + "");
                    int albumid = Integer.parseInt(msgitem[0]);
                    // Check That Album File is Inserted or Not

                    boolean isinserted = jj.CheckAlbumDetailsInserted(
                            Integer.parseInt(studid), schoolid,
                            Integer.parseInt(msgitem[4]), albumid,
                            filename);

                    if (!isinserted) {
                        jj.AddAlbumDetails(new Contact(
                                Integer.parseInt(studid), albumid, msgitem[1]
                                , msgitem[2], msgitem[5],
                                schoolid, Integer.parseInt(msgitem[4]
                        ), Long.parseLong(msgitem[8]
                        ), msgitem[6]));
                    }
                    if (!fileexist.exists()) {
                        Bitmap bmp = Constants.getBitmap(
                                msgitem[5], fileexist); // getImage(msgitem[5]);
                        Constants.SaveImage(bmp, filename);
                    }
                }
            } else {
                Constants.Logwrite("AlbumNotification", "No Album Details Found");
            }
        } catch (Exception err) {
            Constants.Logwrite("AlbumNotification", "Error:" + err.getMessage()
                    + "StackTrace:" + err.getStackTrace());
        }
        return message;
    }

    private void NotifyExamMessage(MyFirebaseMessagingService cntxt, String studid, String examid,
                                   int tpid, String SchoolId, DatabaseHandler jj) {
        int ExamId = Integer.parseInt(examid);
        Constants.Logwrite("Notification:", "ExamId:" + ExamId);
        int StudId = Integer.parseInt(studid);
        Constants.Logwrite("Notification:", "StudId:" + StudId);
        int SchlId = Integer.parseInt(SchoolId);
        Constants.Logwrite("Notification:", "SchoolId:" + SchlId);
        int Yearid = 0;

        //Get Exam Data and Insert in Database
        //if Single Exams
        String aa = Constants.GetSingleExams(cntxt, SchlId, StudId,
                ExamId, jj);
        Constants.Logwrite("Notification:", "ExamString:" + aa);
        String[] spltrstr = aa.split(",");
        Constants.Logwrite("Notification:", "ExamStringLength:" + spltrstr.length);
        String ExamName = spltrstr[1];
        Constants.Logwrite("Notification:", "ExamName:" + ExamName);
        Yearid = Integer.parseInt(spltrstr[2]);
        Constants.Logwrite("Notification:", "ExamYearId:" + Yearid);
        // change by tejas Patel 24-07-2018 Add to parameter CategoryId and Name
        String categoryId = spltrstr[3];
        Constants.Logwrite("Notification:", "CategoryId:" + categoryId);
        String categoryName = spltrstr[4];
        Constants.Logwrite("Notification:", "CategoryName:" + categoryName);

        Boolean IsExamInserted = jj.CheckExamInserted(StudId, SchlId, Yearid,
                ExamId);
        Constants.Logwrite("Notification:", "ExamStatus:" + IsExamInserted);
        if (!IsExamInserted) {
            List<Contact> data = db.GetAllExamRecords(SchlId, StudId, Yearid, -2);
            int srno = data.size();
            if (tpid == 1 || tpid == 2) {
                jj.AddExams(new Contact(Yearid, StudId, SchlId, Integer
                        .parseInt(examid), ExamName, "", 0,
                        "", categoryId, categoryName, srno + 1));
            } else {
                jj.AddExams(new Contact(Yearid, StudId, SchlId, Integer
                        .parseInt(examid), ExamName, "", 1,
                        "", categoryId, categoryName, srno + 1));
            }
        }
        Constants.setDefaultYear(cntxt, studid, SchoolId, Yearid + "", db);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}



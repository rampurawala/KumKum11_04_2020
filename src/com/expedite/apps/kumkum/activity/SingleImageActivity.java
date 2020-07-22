package com.expedite.apps.kumkum.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;

public class SingleImageActivity extends BaseActivity implements OnTouchListener {


    private ImageView imageview;
    private String imagepath, imageurl;
    private File file, filepth;
    private Matrix matrix = new Matrix(), savedMatrix = new Matrix();
    // The 3 states (events) which the user is trying to perform
    private static final int NONE = 0, DRAG = 1, ZOOM = 2;
    private int mode = NONE;
    // these PointF objects are used to record the point(s) the user is touching
    private PointF start = new PointF(), mid = new PointF();
    private float oldDist = 1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinle_image);

        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), SingleImageActivity.this,
                SingleImageActivity.this, "Full Image", "Full Image", ActivityNames.SingleImageActivity);
        try {
            file = Constants.CreatePhotoGalleryFolder();
            imagepath = getIntent().getStringExtra("FileName");
            String flag1 = getIntent().getStringExtra("flag");
            imageurl = getIntent().getStringExtra("albumurl");
            imageview = (ImageView) findViewById(R.id.full_image_view);
            if (flag1.equals("1")) {
                // Decode the filepath with BitmapFactory followed by the position
                filepth = new File(imagepath);
                ;
                if (!filepth.exists()) {
                    new saveImagetask().execute();
                } else {
                    Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(filepth));
                    // Set the decoded bitmap into ImageView
                    imageview.setImageBitmap(bmp);
                }
            } else if (flag1.equals("0")) {
                filepth = new File(file + "/" + imagepath + "");
                if (!filepth.exists()) {
                    new saveImagetask().execute();
                } else {
                    Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(filepth));
                    // Set the decoded bitmap into ImageView
                    imageview.setImageBitmap(bmp);
                }
            }
            imageview.setOnTouchListener(this);
        } catch (Exception err) {
            Constants.writelog("Singleimage", "oncreate()264 MSG:" + err.getMessage());
            err.printStackTrace();
        }
    }

    public void SaveImage(Bitmap finalBitmap, String fileneme) {

        File myDir = Constants.CreatePhotoGalleryFolder();
        String fname = fileneme;
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        String extension = "";
        int i = fname.lastIndexOf('.');
        int p = Math.max(fname.lastIndexOf('/'), fname.lastIndexOf('\\'));

        if (i > p) {
            extension = fname.substring(i + 1);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("jpeg")) {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } else if (extension.equalsIgnoreCase("png")) {
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            Constants.writelog("PhotoGalleryActivity", "SaveImage()152 MSG:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                hideKeyboard(SingleImageActivity.this);
                SingleImageActivity.this.finish();
                onBackClickAnimation();
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingleImageActivity.this.finish();
        onBackClickAnimation();
    }

    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                // Constants.Logwrite(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                // Constants.Logwrite(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                // Constants.Logwrite(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    // Constants.Logwrite(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); 
                /* create the transformation in the matrix
                 * of points
				 */
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist = spacing(event);
                    // Constants.Logwrite(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                    /*
                     * setting the scaling of the matrix...if scale > 1 means
					 * zoom in...if scale < 1 means zoom out
					 */
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Float.parseFloat(String.valueOf(Math.sqrt(x * x + y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");

        // Constants.Logwrite("Touch Event", sb.toString());
    }

    public class saveImagetask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(
                SingleImageActivity.this);

        @Override
        protected void onPreExecute() {
            try {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Fetching Details...");
                dialog.show();
                super.onPreExecute();
            } catch (Exception err) {
                Constants.writelog("PhotoGalleryActivity", "onPreExecute()163 MSG:" + err.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (!filepth.exists()) {
                    Bitmap bm = Constants.getBitmap(imageurl, filepth); // getImageurl for generate Bitmap
                    SaveImage(bm, imagepath);
                }
            } catch (Exception ex) {
                Constants.writelog("Singleimage_saveImageTask", "DoInBackgrnd()264 MSG:" + ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            try {
                Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(filepth));
                // Set the decoded bitmap into ImageView
                imageview.setImageBitmap(bmp);
                //Picasso.with(SingleImageActivity.this).load(imageurl).into(imageview);
            } catch (Exception ex) {
                Constants.writelog("Singleimage_saveImageTask", "DoInBackgrnd()264 MSG:" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}



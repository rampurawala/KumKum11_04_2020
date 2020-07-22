package com.expedite.apps.kumkum.activity;


import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.ImageZoom;
import com.expedite.apps.kumkum.constants.Constants;
import com.squareup.picasso.Picasso;



public class ImageZoomActivity extends BaseActivity {

    ImageZoom imz;
    Bitmap img;
    static final String tag = "ImageZoomActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorshadow)));

        imz = (ImageZoom) findViewById(R.id.imzview);
        try {
            if (getIntent() != null) {
                String imgurl = getIntent().getExtras().getString("imgURL");
             /*   if(pname.equalsIgnoreCase(null)||pname.equalsIgnoreCase("")) {
                    pname="Taaza Food";
                }
                getSupportActionBar().setTitle(pname);*/
                Picasso.with(getApplicationContext()).load(imgurl).into(imz);
            }
        } catch (NullPointerException ex) {
            Constants.writelog(tag, "onCreate():NullPointerException:Error:47:" + ex.getMessage());
        } catch (Exception ex) {
            Constants.writelog(tag, "onCreate():Exception:Error:49:" + ex.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                onBackClickAnimation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
        finish();
    }


}

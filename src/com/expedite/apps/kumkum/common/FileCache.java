package com.expedite.apps.kumkum.common;

import java.io.File;
import android.content.Context;

import com.expedite.apps.kumkum.constants.SchoolDetails;

public class FileCache {
 
    private File cacheDir;
 
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
        	//String Name = "KumKum";
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(), SchoolDetails.PhotoGalleryFolder);
        }
            else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
 
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir,filename);
        return f;
    }
 
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
 
}
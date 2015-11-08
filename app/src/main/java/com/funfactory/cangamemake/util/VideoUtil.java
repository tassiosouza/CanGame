package com.funfactory.cangamemake.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.funfactory.cangamemake.R;

public class VideoUtil {

    public static final int    CAPTURE_VIDEO = 200;
    public static final String PREFIX        = "VIDEO";

    private static VideoUtil   sInstance;

    public static VideoUtil getInstance() {
        if (sInstance == null) {
            sInstance = new VideoUtil();
        }
        return sInstance;
    }

    public String gravar(Activity activity){
        
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri uri = Uri.fromFile(getOutputVideosFile(activity));
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(i, CAPTURE_VIDEO);
        
        return uri.toString();
    }
    
    private File getOutputVideosFile(Activity activity) {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                activity.getPackageName());

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        File ff = new File(directory.getPath() + File.separator + "VIDEO_" + timeStamp + ".jpg");
        return ff;
    }
    
    public void executar(Activity activity, String uri) {
        
        if (uri != null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
           
            Uri videoUri = Uri.parse(uri);
            intent.setDataAndType(videoUri, "video/*");
            
            String messsage = activity.getString(R.string.completar_acao_usando);
            activity.startActivity(Intent.createChooser(intent, messsage));
        }
    }
}

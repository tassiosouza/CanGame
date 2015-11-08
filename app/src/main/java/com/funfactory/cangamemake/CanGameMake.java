package com.funfactory.cangamemake;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CanGameMake extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        loadResources();
    }

    public static Context getContext() {
        return mContext;
    }
    
    private void loadResources(){
        initUniversalImageLoader();
    }
    
    private void initUniversalImageLoader(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_launcher)
            .cacheInMemory(true)
            .build();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
            .defaultDisplayImageOptions(defaultOptions)
            .build();
    
        ImageLoader.getInstance().init(config);
    }

}

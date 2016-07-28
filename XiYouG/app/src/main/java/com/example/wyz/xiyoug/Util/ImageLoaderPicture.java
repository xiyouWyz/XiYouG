package com.example.wyz.xiyoug.Util;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Wyz on 2016/7/26.
 */

public  class ImageLoaderPicture {
    private DisplayImageOptions options;
    public ImageLoaderPicture(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .build();
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .showStubImage(0)
                .showImageForEmptyUri(0)
                .showImageOnFail(0)
                .cacheInMemory().cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
                .build();
    }

    public DisplayImageOptions getOptions() {
        return options;
    }

    public void setOptions(DisplayImageOptions options) {
        this.options = options;
    }

}





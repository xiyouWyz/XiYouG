package com.example.wyz.xiyoug.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

/**
 * * 缓存图片，，获取缓存图片对图片进行压缩，读取压缩图片
 * Created by Wyz on 2016/8/11.
 */
public class ImageLoaderCompress {
    private  static LruCache<String,Bitmap> mMemoryCache;

    private  static  ImageLoaderCompress imageLoaderCompress=new ImageLoaderCompress();

    private ImageLoaderCompress()
    {
        //获取应用程序最大可用内存
        int maxMemory= (int)Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/8;
        //设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache=new LruCache<String, Bitmap>(cacheSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    public  static  ImageLoaderCompress getInstance()
    {
        return  imageLoaderCompress;
    }
    public  void addBitmapToMEmoryCache(String key,Bitmap bitmap)
    {
        if(getBitmapFromMemoryCache(key)==null)
        {
            mMemoryCache.put(key,bitmap);
        }
    }
    public  Bitmap getBitmapFromMemoryCache(String key)
    {
        return  mMemoryCache.get(key);
    }
    //计算压缩比例值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth) {
        // 源图片的宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            // 计算出实际宽度和目标宽度的比率
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }
    //从pathName解码文件成Bitmap
    public static Bitmap decodeSampledBitmapFromResource(String pathName,
                                                         int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }
}

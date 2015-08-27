package com.wfj.jaydenarchitecture.model.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.wfj.jaydenarchitecture.app.DirContext;
import com.wfj.jaydenarchitecture.utils.Util;

import java.io.File;
import java.io.IOException;

import fbcore.cache.DiskCache;
import fbcore.cache.MemoryCache;
import fbcore.cache.image.ImageLoader;
import fbcore.cache.image.impl.ImageDiskCache;
import fbcore.cache.image.impl.LruMemoryCache;
import fbcore.log.LogUtil;
import fbcore.security.Md5;

/**
 * 初始化数据缓存的目录
 * 初始化图片缓存的目录
 *
 * fbCore 和 Picasso 两个图片加载库的初始化
 *
 * Created by Jayden on 2015/8/25.
 */
public class DataProvider {

    private static DataProvider sDataProvider;
    private static Context context;

    private ImageLoader imageLoader;
    private Disker disker;

    public static void init(Context context) {
        DataProvider.context = context;
    }

    private DataProvider() {
        disker = new Disker(context, DirContext.ROOT_DIR);
        initImageCache(context);
    }

    public static DataProvider getInstance() {
        if (sDataProvider == null) {
            synchronized (DataProvider.class) {
                if (sDataProvider == null) {
                    sDataProvider = new DataProvider();
                }
            }
        }
        return sDataProvider;
    }

    private void initImageCache(Context context) {
        MemoryCache<String, Bitmap> memoryCache = new LruMemoryCache(1024 * 1024 * 4);
        final DiskCache<String, byte[]> diskCache = new ImageDiskCache(disker.imageDir.getAbsolutePath(),
                1024 * 1024 * 10);
        imageLoader = new ImageLoader(context.getResources(), memoryCache, diskCache);
        loadImageCache(context);
    }

    private void loadImageCache(Context context) {
        final String imageCacheDir = disker.imageDir.getAbsolutePath();
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(new File(imageCacheDir)))
                .memoryCache(new LruCache(1024 * 1024 * 4)).build();
        try {
            Picasso.setSingletonInstance(picasso);
        }catch (Exception e){

        }

        /**
         * 调用函数 Picasso.setDebug(true)
         * 可以在加载的图片左上角显示一个 三角形 ，
         * 不同的颜色代表加载的来源
         * 红色：代表从网络下载的图片
         * 蓝色：代表从磁盘缓存加载的图片
         * 绿色：代表从内存中加载的图片
         * 如果项目中使用了OkHttp库的话，
         * 默认会使用OkHttp来下载图片。
         * 否则使用HttpUrlConnection来下载图片。
         */
        picasso.setIndicatorsEnabled(false);
    }

    public void putStringToDisker(String content, String url) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(url)) {
            return;
        }
        int startIndex = url.indexOf("srv");
        if (startIndex == -1) {
            return;
        }
        String key = Md5.digest32(url.substring(startIndex));
        LogUtil.i("Data", "put key : " + key + "startIndex : " + startIndex);

        disker.putStringToDisker(content, key);
    }

    public String getCacheFromDisker(String url) {
        try {
            int startIndex = url.indexOf("srv");
            if (startIndex == -1) {
                return null;
            }
            String key = Md5.digest32(url.substring(startIndex));
            return disker.getStringFromDisk(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearAllCache() {
        disker.deleteCache();
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public String getDiskSize() {
        return Util.bytes2Convert(disker.getSize());
    }
}

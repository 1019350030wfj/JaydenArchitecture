package com.wfj.jaydenarchitecture.app;

import android.content.Context;

import java.io.File;

import fbcore.utils.Utils;

/**
 * 应用文件系统上下文.
 * 负责应用中文件目录的初始化等工作.
 *
 * Created by Jayden on 2015/8/25.
 */
public class DirContext {

    public static final String ROOT_DIR = "jayden";
    public static final String IMAGE = "image";
    public static final String CACHE = "cache";
    public static final String DOWNLOAD = "download";

    private static DirContext sDirContext;

    private String mCacheDir;

    private DirContext() {
        initDirContext();
    }

    public static DirContext getInstance() {
        if (sDirContext == null) {
            synchronized (DirContext.class) {
                if (sDirContext == null) {
                    sDirContext = new DirContext();
                }
            }
        }
        return sDirContext;
    }

    private void initDirContext() {

    }

    public void initCacheDir(Context context) {
        this.mCacheDir = Utils.getDiskCacheDir(context,"").getAbsolutePath();
    }

    public File getRootDir() {
        File file = new File(
                android.os.Environment.getExternalStorageDirectory(),
                ROOT_DIR);

        if(!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        return file;
    }

    public File getDir(String path) {
        File file = new File(getRootDir(), path);
        if(!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }
}

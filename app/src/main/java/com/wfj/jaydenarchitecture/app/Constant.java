package com.wfj.jaydenarchitecture.app;

/**
 * Created by Jayden on 2015/8/26.
 */
public interface Constant {

    //当前是否为调试模式
    boolean DEBUG = true;

    boolean TRACE = true;

    //是否使用外网地址
    boolean USE_REAL_SEVER = true;

    //屏蔽用户频繁点击的最短时间间隔 ms
    long CLICK_INTERVAL = 600;

    /** 平台类型 3：Android */
    String OS_TYPE = "3";

    /** 服务器地址 */
    String REAL_SERVER = "http://api.app.yiwaiart.com:8080/api.php";

    String TEST_SERVER = "http://192.168.45.4:8090/api.php";

    //LoadingHelper用到
    int LOADING_CIRCLE_VIEW_MAX_SIZE = 35;

    /** 检查更新，本地有安装包下提示是否安装的倒计时 */
    int CHECK_NEW_VERSION = 8 * 1000;
}

package com.wfj.jaydenarchitecture.utils;

import java.math.BigDecimal;

/**
 * Created by Jayden on 2015/8/25.
 */
public class Util {

    /**
     * 字节转换成相应大小的MB,KB
     *
     * @param bytes
     * @return
     */
    public static String bytes2Convert(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal gbyte = new BigDecimal(1024 * 1024 * 1024);
        float returnValue = filesize.divide(gbyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue >= 1) {
            return (returnValue + "GB");
        }
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue >= 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte).intValue();
        return (returnValue + "KB");
    }
}

package com.wfj.jaydenarchitecture.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    private static final int S = 1000;
    private static final int M = 60 * S;
    private static final int H = 60 * M;
    private static final int D = 24 * H;
    
    /**
     * 生成时间戳
     * 
     * @return
     */
    public static String generateTimestamp() {
        Date date = new Date(new Date().getTime());

        return getTimestampSDF().format(date);
    }

    /**
     * 取得时间戳的 SimpleDateFormat
     * 
     * @return
     */
    private static SimpleDateFormat getTimestampSDF() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    // 生成提交时间
    public static long getPublishTime() {
        return (System.currentTimeMillis() / 1000);
    }
    
    //离现在过了多久时间，返回秒
    public static long intervalTime(long oldTime){
        return getPublishTime()-oldTime;
    }
    
    public static String transformTime(long oldTime){
        return transformTime(oldTime * 1000, System.currentTimeMillis());
    }

    public static boolean overOneDay(long oldTime, long newTime) {
        long diffTime = newTime - oldTime;
        
        Calendar oldDate = Calendar.getInstance();
        oldDate.setTimeInMillis(oldTime);
        extractDate(oldDate);
        Calendar newDate = Calendar.getInstance();
        newDate.setTimeInMillis(newTime);

        int diffDate = (int) ((newDate.getTimeInMillis() - oldDate.getTimeInMillis()) / D);
        
        if(diffDate==0 || diffTime < 24 * H){
            return false;
        } else {
            return true;
        }
    }
    
    public static String transformTime(long oldTime, long newTime) {
        long diffTime = newTime - oldTime;
        if(diffTime < M){
            return "刚刚";
        } else if(diffTime < H){
            return diffTime / M + "分钟前";
        } else{
            Calendar oldDate = Calendar.getInstance();
            oldDate.setTimeInMillis(oldTime);
            extractDate(oldDate);
            Calendar newDate = Calendar.getInstance();
            newDate.setTimeInMillis(newTime);
            int diffDate = (int) ((newDate.getTimeInMillis() - oldDate.getTimeInMillis()) / D);
            if(diffDate==0 || diffTime < 6 * H){
                return diffTime / H + "小时前";
            } /*else if(diffDate == 1){
                return "昨天";
            } else if(diffDate < 6){
                return diffDate + "天前";
            }*/ else if(oldDate.get(Calendar.YEAR) == newDate.get(Calendar.YEAR)){
                SimpleDateFormat format = new SimpleDateFormat("MM-dd");
                return format.format(oldDate.getTime());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                return format.format(oldDate.getTime());
            }
        }
    }
    
    private static void extractDate(Calendar calendar){
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }
    
    public static String time(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        if(calendar.get(Calendar.YEAR) == curr.get(Calendar.YEAR)){
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            return format.format(calendar.getTime());
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(calendar.getTime());
        }
    }

    public static String fullTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    /**
     * 根据当前时间生成的目录
     * @return 如: /2015/07/13/
     */
    public static String dateDir(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
        return format.format(calendar.getTime());
    }
}

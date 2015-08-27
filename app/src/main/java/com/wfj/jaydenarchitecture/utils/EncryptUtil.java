package com.wfj.jaydenarchitecture.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import com.wfj.jaydenarchitecture.app.AppContext;

import java.security.MessageDigest;

public class EncryptUtil {
    private static final String RELEASE_SIGNATURE = "37241476ff5d02a206971f87be86e0eb";

    public static boolean checkReleaseSignature(){
        try {
            PackageInfo packageInfo = AppContext.getContext().getPackageManager().getPackageInfo(AppContext.getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] sigs = packageInfo.signatures;
            return RELEASE_SIGNATURE.equalsIgnoreCase(md5(sigs[0].toByteArray()));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String md5(byte[] data){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = md5.digest(data);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            int val = byteArray[i] & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    //MD5加密，32位
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = md5Bytes[i] & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String auth_key() {
        return "FEiafda^#&09342sdfjsdfKJFfiajfako345254F435";
    }
    
    public static String getAccessToken(String uid, int id){
        return md5(md5(uid + id + auth_key()));
    }
}

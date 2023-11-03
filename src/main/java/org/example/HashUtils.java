package org.example;

import java.math.BigInteger;
import java.security.MessageDigest;

/* loaded from: C:\Users\Administrator\Desktop\com.stjy360.neweducation\cookie_10382228.dex */
public class HashUtils {
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            String bigInteger = new BigInteger(1, messageDigest.digest()).toString(16);
            while (bigInteger.length() < 32) {
                bigInteger = "0" + bigInteger;
            }
            return bigInteger;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
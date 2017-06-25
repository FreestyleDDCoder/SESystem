package com.helloncu.sesystem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    // 进行MD5加密设置
    public static String MD5Encryption(String password) {
        try {
            // 设置加密方式为MD5
            MessageDigest digester = MessageDigest.getInstance("MD5");
            // 对字符串加密,返回字节数组
            byte[] digest = digester.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            // 对加密过后的字节数组进行for each遍历
            for (byte b : digest) {
                // 获取字节的低八位有效值
                // 要获取某位字节，则将其他位与0，获取为与1
                int a = b & 0Xff;
                // 将整数转为16进制数
                String hexString = Integer.toHexString(a);
                // 如果hexString的位数为一位的话在前面补0
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 当找不到MD5的方式是走此异常
            e.printStackTrace();
        }

        return null;

    }

    //获取文件的MD5值
    public static String getFileMd5(String sourceDir) {

        File file = new File(sourceDir);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int len = 0;
            byte[] bytes = new byte[1024];

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            while ((len = fileInputStream.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }
            byte[] result = messageDigest.digest();

            StringBuffer sb = new StringBuffer();

            for (byte bt : result) {
                // 获取字节的低八位有效值
                // 要获取某位字节，则将其他位与0，获取为与1
                int number = bt & 0xff;
                // 将整数转为16进制数
                String toHexString = Integer.toHexString(number);
                if (toHexString.length() == 1) {
                    sb.append("0" + toHexString);
                } else {
                    sb.append(toHexString);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

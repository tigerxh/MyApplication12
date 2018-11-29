package vpn.tydic.com.myapplication.security;


import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密
 *
 * @author leo
 */
public final class MD5 {

    /**
     * 数据编码方式
     */
    private static final String ENCODE = "UTF-8";

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "MD5";

    /**
     * 返回明文的md5摘要
     *
     * @param text 明文
     * @return 返回text的md5摘要
     */
    private static final char[] md5(String text) {
        char[] value = null;
        try {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            byte[] strTemp = text.getBytes(ENCODE);
            MessageDigest mdTemp = MessageDigest.getInstance(ALGORITHM);
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            value = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                value[k++] = hexDigits[byte0 >>> 4 & 0xf];
                value[k++] = hexDigits[byte0 & 0xf];
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "执行加密算[" + ALGORITHM + "]法异常!");
        } catch (Exception e) {
            Log.e("MD5", "执行加密算[" + ALGORITHM + "]法异常!");
        }

        return value;
    }

    /**
     * 32位的md5
     *
     * @param text 明文
     * @return 返回密文
     */
    public static final String md5for32(String text) {
        String ptext = new String(md5(text));
        Log.e("MD5", "密文：" + ptext + " 明文：" + text);

        return ptext;
    }

    /**
     * 有的系统使用的是，该方法返回16位的md5。
     *
     * @param text 明文
     * @return 返回密文
     */
    public static final String md5for16(String text) {
        String ptext = new String(md5(text)).substring(8, 24);

        Log.e("MD5", "密文：" + ptext + " 明文：" + text);

        return ptext;
    }
}
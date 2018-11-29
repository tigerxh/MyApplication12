package vpn.tydic.com.myapplication.security;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AESHelper {
	/** 算法/模式/填充 **/
	private static final String CipherMode = "AES/ECB/PKCS5Padding";

	/** 创建密钥 **/
	private static SecretKeySpec createKey(String password) {
		byte[] data = null;
		if (password == null) {
			password = "";
		}
		StringBuffer sb = new StringBuffer(16);
		sb.append(password);
		while (sb.length() < 16) {
			sb.append("0");
		}
		if (sb.length() > 16) {
			sb.setLength(16);
		}

		try {
			data = sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new SecretKeySpec(data, "AES");
	}

	/** 加密字节数据 **/
	public static byte[] encrypt(byte[] content, String password) {
		try {
			SecretKeySpec key = createKey(password);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 加密(结果为16进制字符串) **/
	public static String encrypt(String content, String password) {
		byte[] data = null;
		try {
			data = content.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = encrypt(data, password);
		// base64的
		String result = Base64.encode(data);
//		 十六进制的
//		 String result = byte2hex(data);
		return result;
	}

	/** 解密字节数组 **/
	public static byte[] decrypt(byte[] content, String password) {
		try {
			SecretKeySpec key = createKey(password);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 解密成base64的字符串为字符串 **/
	/** 解密16进制的字符串为字符串 **/
	public static String decrypt(String content, String password) {
		if (TextUtils.isEmpty(password)) {
			return content;
		}
		byte[] data = null;
		try {
			// 十六进制的
//			 data = hex2byte(content);
			// modify by yang
//			 base64
			data = base64byte(content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		data = decrypt(data, password);
		if (data == null)
			return null;
		String result = null;
		try {
			result = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 字节数组转成16进制字符串 **/
	public static String byte2hex(byte[] b) { // 一个字节的数，
		StringBuffer sb = new StringBuffer(b.length * 2);
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			tmp = (Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
		}
		return sb.toString().toUpperCase(); // 转成大写
	}

	/**
	 * 将base64变成byte数组
	 * 
	 * @param inputString
	 * @return
	 */
	private static byte[] base64byte(String inputString) {
		return Base64.decode(inputString);
	}

	/** 将hex字符串转换成字节数组 **/
	@SuppressWarnings("unused")
	private static byte[] hex2byte(String inputString) {
		if (inputString == null || inputString.length() < 2) {
			return new byte[0];
		}
		inputString = inputString.toLowerCase();
		int l = inputString.length() / 2;
		byte[] result = new byte[l];
		for (int i = 0; i < l; ++i) {
			String tmp = inputString.substring(2 * i, 2 * i + 2);
			result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
		}
		return result;
	}
}

package com.vee.shop.alipay.utils;

import java.security.MessageDigest;

/**
 * @author LY MD5加密
 */
public class MD5 {

	// MD5加密，32位
	public static String getMD5(String str) {

		try {
			byte[] source = str.getBytes();
			// 用来将字节转换成 16 进制表示的字符
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'a', 'b', 'c', 'd', 'e', 'f' };
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char s[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				s[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
														// >>> 为逻辑右移，将符号位一起右移
				s[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			String data = new String(s); // 换后的结果转换为字符串
			return data;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * // MD5加密，32位 public static String getMD52(String str) { try {
	 * MessageDigest md5 = MessageDigest.getInstance("MD5"); char[] charArray =
	 * str.toCharArray(); byte[] byteArray = new byte[charArray.length]; for
	 * (int i = 0; i < charArray.length; i++) { byteArray[i] = (byte)
	 * charArray[i]; } byte[] md5Bytes = md5.digest(byteArray); StringBuffer
	 * hexValue = new StringBuffer(); for (int i = 0; i < md5Bytes.length; i++)
	 * { int val = ((int) md5Bytes[i]) & 0xff; if (val < 16) {
	 * hexValue.append("0"); } hexValue.append(Integer.toHexString(val)); }
	 * return hexValue.toString(); } catch (Exception e) { e.printStackTrace();
	 * } return null; }
	 */
}

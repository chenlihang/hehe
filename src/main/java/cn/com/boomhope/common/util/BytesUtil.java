package cn.com.boomhope.common.util;

public class BytesUtil {

	public static String bytes2hex(byte[] bytes){
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes){
			sb.append(Character.forDigit((b & 240) >> 4, 16));
			sb.append(Character.forDigit(b & 15, 16));
		}
		return sb.toString();
	}
}

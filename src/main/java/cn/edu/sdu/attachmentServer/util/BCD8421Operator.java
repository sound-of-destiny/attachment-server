package cn.edu.sdu.attachmentServer.util;

public class BCD8421Operator {

	/**
	 * BCD字节数组===>String
	 * 
	 * @param bytes byte[]
	 * @return 十进制字符串
	 */
	public static String bcd2String(byte[] bytes) {
		StringBuilder temp = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			// 高四位
			temp.append((b & 0xf0) >>> 4);
			// 低四位
			temp.append(b & 0x0f);
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * 字符串==>BCD字节数组
	 * 
	 * @param str String
	 * @return BCD字节数组
	 */
	public static byte[] string2Bcd(String str) {
		// 奇数,前补零
		if ((str.length() & 0x1) == 1) {
			str = "0" + str;
		}

		byte[] ret = new byte[str.length() / 2];
		byte[] bs = str.getBytes();
		for (int i = 0; i < ret.length; i++) {

			byte high = ascII2Bcd(bs[2 * i]);
			byte low = ascII2Bcd(bs[2 * i + 1]);

			// TODO 只遮罩BCD低四位?
			ret[i] = (byte) ((high << 4) | low);
		}
		return ret;
	}

	private static byte ascII2Bcd(byte asc) {
		if ((asc >= '0') && (asc <= '9'))
			return (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			return (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			return (byte) (asc - 'a' + 10);
		else
			return (byte) (asc - 48);
	}

	/**
	 * 左侧补齐
	 */
	public static byte[] leftPad(byte[] bytes, int length, byte padByte) {
		int i = length - bytes.length;
		if (i <= 0)
			return bytes;
		byte[] result = new byte[length];
		System.arraycopy(bytes, 0, result, i, bytes.length);
		for (int j = 0; j < i; j++)
			result[j] = padByte;
		return result;
	}
}

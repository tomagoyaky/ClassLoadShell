package com.ClassLoaderShell.utils;

import java.nio.ByteBuffer;

public class HexUtil {
	public static char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static byte[] emptybytes = new byte[0];

	public HexUtil() {
		super();
	}

	public static String byte2HexStr(byte b) {
		char[] v0 = new char[2];
		v0[1] = HexUtil.digits[b & 15];
		v0[0] = HexUtil.digits[(((byte) (b >>> 4))) & 15];
		return new String(v0);
	}

	public static String bytes2HexStr(ByteBuffer bs) {
		ByteBuffer v0 = bs.duplicate();
		v0.flip();
		byte[] v1 = new byte[v0.limit()];
		v0.get(v1);
		return HexUtil.bytes2HexStr(v1);
	}

	public static String bytes2HexStr(byte[] bytes) {
		String v3;
		if (bytes == null || bytes.length == 0) {
			v3 = null;
		} else {
			char[] v1 = new char[bytes.length * 2];
			int v2;
			for (v2 = 0; v2 < bytes.length; ++v2) {
				int v0 = bytes[v2];
				v1[v2 * 2 + 1] = HexUtil.digits[v0 & 15];
				v1[v2 * 2] = HexUtil.digits[(((byte) (v0 >>> 4))) & 15];
			}

			v3 = new String(v1);
		}

		return v3;
	}

	public static byte char2Byte(char ch) {
		byte v0;
		if (ch >= 48 && ch <= 57) {
			v0 = ((byte) (ch - 48));
			return v0;
		}

		if (ch >= 97 && ch <= 102) {
			return ((byte) (ch - 87));
		}

		if (ch >= 65) {
			if (ch > 70) {
				// goto label_23;
				return 0;
			}

			v0 = ((byte) (ch - 55));
		} else {
			// label_23:
			v0 = 0;
		}

		return v0;
	}

	public static byte hexStr2Byte(String str) {
		byte v0 = 0;
		if (str != null && str.length() == 1) {
			v0 = HexUtil.char2Byte(str.charAt(0));
		}

		return v0;
	}

	public static byte[] hexStr2Bytes(String str) {
		byte[] v0;
		if (str == null || (str.equals(""))) {
			v0 = HexUtil.emptybytes;
		} else {
			v0 = new byte[str.length() / 2];
			int v2;
			for (v2 = 0; v2 < v0.length; ++v2) {
				v0[v2] = ((byte) (HexUtil.char2Byte(str.charAt(v2 * 2)) * 16 + HexUtil
						.char2Byte(str.charAt(v2 * 2 + 1))));
			}
		}

		return v0;
	}

	public static void main(String[] args) {
		long v0 = System.currentTimeMillis();
		int v4;
		for (v4 = 0; v4 < 1000000; ++v4) {
			String v5 = "234" + v4;
			if (!new String(HexUtil.hexStr2Bytes(HexUtil.bytes2HexStr(v5
					.getBytes()))).equals(v5)) {
				System.out.println("error:" + v5);
			}
		}

		System.out.println("use:" + (System.currentTimeMillis() - v0));
	}
}
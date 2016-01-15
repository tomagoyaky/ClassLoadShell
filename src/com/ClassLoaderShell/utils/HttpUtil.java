package com.ClassLoaderShell.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpUtil {

	/**
	 * ����GET����
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static URLConnection sendGetRequest(String url,
			Map<String, String> params, Map<String, String> headers)
			throws Exception {
		StringBuilder buf = new StringBuilder(url);
		Set<Entry<String, String>> entrys = null;
		// �����GET���������������URL��
		if (params != null && !params.isEmpty()) {
			buf.append("?");
			entrys = params.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				buf.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append("&");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		URL url1 = new URL(buf.toString());
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setRequestMethod("GET");
		// ��������ͷ
		if (headers != null && !headers.isEmpty()) {
			entrys = headers.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		conn.getResponseCode();
		return conn;
	}

	/**
	 * ����POST����
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static URLConnection sendPostRequest(String url,
			Map<String, String> params, Map<String, String> headers)
			throws Exception {
		StringBuilder buf = new StringBuilder();
		Set<Entry<String, String>> entrys = null;
		// ������ڲ����������HTTP�����壬����name=aaa&age=10
		if (params != null && !params.isEmpty()) {
			entrys = params.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				buf.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append("&");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		URL url1 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		out.write(buf.toString().getBytes("UTF-8"));
		if (headers != null && !headers.isEmpty()) {
			entrys = headers.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		conn.getResponseCode(); // Ϊ�˷��ͳɹ�
		return conn;
	}
	/**
	 * ��������תΪ�ֽ�����
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] read2Byte(InputStream inStream)throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) !=-1 ){
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
}

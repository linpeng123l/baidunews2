package com.linpeng.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

	/**
	 * ¸Ä±ä×Ö·û±àÂë
	 * @param str Ä¿±ê×Ö·û
	 * @return ×ª»»×Ö·û
	 */
	public static String changeEncoding(String str,String encoding){
		try {
			String returnStr = new String(str.getBytes(), encoding);
			return returnStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

}

package com.jesse.pushdemo.util;


import java.util.*;
import java.util.Map.Entry;

/**
 * @Description: 资源配置文件
 */
public class HttpProperties {
	
	private static Map<String,String> paramsMap = new HashMap<String,String>();

	private static String resource[] = {
            "resource-push-new.properties"
	};

//	private static String resource[] = {
//		   "property/url.property"
//	};
	
	static {
		for (String res : resource) {
			try {
				Properties prop = PropUtil.getUrlProperties(res);
				if(null==prop){continue;}
				// 返回Properties中包含的key-value的Set视图
				Set<Entry<Object, Object>> set = prop.entrySet();
				// 返回在此Set中的元素上进行迭代的迭代器
				Iterator<Entry<Object, Object>> it = set.iterator();
				String key = null, value = null;
				// 循环取出key-value
				while (it.hasNext()) {
					Entry<Object, Object> entry = it.next();
					key = String.valueOf(entry.getKey());
					value = String.valueOf(entry.getValue());
					key = key == null ? key : key.trim().toUpperCase();
					value = value == null ? value : value.trim();
					// 将key-value放入map中
					paramsMap.put(key, value);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static String getVal(String key) {
		key = key == null ? "" : key.trim().toUpperCase();
		String value = paramsMap.get(key);
		return value == null ? "" : value.trim();
	}

	public static String getPrefixVal(String appType, String param) {
		return getVal(appType+"_"+param);
	}

	public static String getPrefix(String appType, String param) {
		return getVal(appType+param);
	}
	
	
	public static String getVal(String key, String defaul) {
		key = key == null ? "" : key.trim().toUpperCase();
		String value = paramsMap.get(key);
		value = value == null || "".equals(value) ? defaul : value.trim();
		return value;
	}
	
}
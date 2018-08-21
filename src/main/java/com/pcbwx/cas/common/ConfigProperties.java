package com.pcbwx.cas.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcbwx.cas.SystemStart;
import com.pcbwx.cas.enums.ConfigEnum;
import com.pcbwx.cas.exception.BusinessException;
import com.pcbwx.cas.exception.ExceptionType;

/**
 * 初始化静态变量
 * 
 * @author 孙贺宇
 * @version 1.0
 * 
 */
public class ConfigProperties {

	private static Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

	private static final String FILENAME = "config.properties";

	public static final Properties props = new Properties();

	static {
		String configFile = System.getenv("CONFIG_SPACE") + "/" + SystemStart.MYSYSTEMCODE + "/" + FILENAME;
		InputStream in = null;
		try {
			in = new FileInputStream(new File(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (in != null) {
			try {
				props.load(in);
			} catch (IOException e) {
				throw new BusinessException(ExceptionType.EXCEPTION_400, "未找到" + FILENAME + "文件");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	public static String getProperty(ConfigEnum config) {
		return (String)props.get(config.getCode());
	}

	public static Integer getPropertyInt(ConfigEnum config) {
		return (Integer)props.get(config.getCode());
	}
	
	public static Boolean getPropertyBoolean(ConfigEnum config) {
		return (Boolean) props.get(config.getCode());
	}
	
	public static String getProperty(ConfigEnum config, String defValue) {
		String value = props.getProperty(config.getCode());
		if (value == null) {
			logger.error("找不到该配置:" + config.getCode());
			return defValue;
		}
		return value;
	}

	public static Integer getPropertyInt(ConfigEnum config, Integer defValue) {
		String value = props.getProperty(config.getCode());
		if (value == null) {
			logger.error("找不到该配置:" + config.getCode());
			return defValue;
		}
		return Integer.valueOf(value);
	}
}

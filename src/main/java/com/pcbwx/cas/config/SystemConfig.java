package com.pcbwx.cas.config;

import com.pcbwx.cas.SystemStart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by liuma on 2017/9/7.
 */
public class SystemConfig {
    private static Properties props = new Properties();

    static {

        try {
            String configFile = System.getenv("CONFIG_SPACE") + "/" + SystemStart.MYSYSTEMCODE + "/" + SystemStart.FILENAME;
            InputStream in = new FileInputStream(new File(configFile));
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (props == null) new SystemConfig();
        return props == null ? null : props.getProperty(key);
    }

    /**
     * 获取属性
     *
     * @param key          属性key
     * @param defaultValue 属性value
     * @return
     */
    public static String getProperty(String key, String defaultValue) {

        return props == null ? null : props.getProperty(key, defaultValue);

    }

    /**
     * 获取properyies属性
     *
     * @return
     */
    public static Properties getProperties() {
        return props;
    }


}

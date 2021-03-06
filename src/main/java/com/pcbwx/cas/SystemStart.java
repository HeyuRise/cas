
package com.pcbwx.cas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * 程序启动类
 * 
 * @author 孙贺宇
 *
 */
@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.pcbwx.cas.dao") // mybatis包路径
public class SystemStart extends SpringBootServletInitializer {
	// springBoot配置文件名字
	public static final String FILENAME = "spring.properties";
	// 系统英文简写
	public final static String MYSYSTEMCODE = "cas";

	public static void main(String[] args) throws Exception {
		// 指定配置文件
		Properties properties = new Properties();
		String configFile = System.getenv("CONFIG_SPACE") + "/" + MYSYSTEMCODE + "/" + FILENAME;
		InputStream in = new FileInputStream(new File(configFile));
		properties.load(in);
		SpringApplication springApplication = new SpringApplication(SystemStart.class);
		springApplication.setDefaultProperties(properties);
		springApplication.run(args);
		log.info("系统已经启动");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SystemStart.class);
	}

}

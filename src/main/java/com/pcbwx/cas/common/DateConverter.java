package com.pcbwx.cas.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.pcbwx.cas.util.StringUtil;

public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		if (StringUtil.isBlank(source)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(source);
		} catch (ParseException e) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = sdf.parse(source);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return date;
	}
	
	
}

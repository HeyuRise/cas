package com.pcbwx.cas.control;

import org.springframework.beans.factory.annotation.Autowired;

import com.pcbwx.cas.bean.MyResponse;

/**
 * controller基类
 * @author 孙贺宇
 * @version 1.0
 *
 */
public class BaseController {

	@Autowired
	protected MyResponse<Object> response;
	
}

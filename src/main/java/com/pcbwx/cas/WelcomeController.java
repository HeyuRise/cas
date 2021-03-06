/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcbwx.cas;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcbwx.cas.bean.MyResponse;
import com.pcbwx.cas.common.ConfigProperties;
import com.pcbwx.cas.dao.WxtbUserMapper;
import com.pcbwx.cas.enums.ConfigEnum;
import com.pcbwx.cas.enums.DictionaryEnum;
import com.pcbwx.cas.model.WxtbUser;
import com.pcbwx.cas.service.RedisService;

@Controller
public class WelcomeController {
	
	private Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	
	@Autowired
	private MyResponse<Object> response;
	
	@Autowired
	public WxtbUserMapper wxtbUserMapper;
	
	@Resource
	private StringRedisTemplate stringTemplate;
	@Resource
	private RedisTemplate<String, WxtbUser> template;
	
	@Autowired
	private RedisService redisService;

	@GetMapping("/redis")
	@ResponseBody
	public Object welcome() {
		return redisService.getDictionarys(DictionaryEnum.PAY_METHOD);
	}
	
	// 登录页
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/test")
	@ResponseBody
	public MyResponse<Object> test(){
		List<WxtbUser> wxtbUsers = wxtbUserMapper.load();
		for (WxtbUser wxtbUser : wxtbUsers) {
			wxtbUser.setUsername(null);
		}
		logger.info(ConfigProperties.getProperty(ConfigEnum.SERVICE_MAIN_URL));
		response.setSuccess(wxtbUsers);
		return response;
	}
	
}

package com.pcbwx.cas.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcbwx.cas.bean.MyResponse;
import com.pcbwx.cas.bean.User;
import com.pcbwx.cas.enums.ErrorCodeEnum;
import com.pcbwx.cas.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auth")
@Api(tags = "权限api")
public class AuthController extends BaseController {
	
	@Autowired
	private AccountService accountService;

	@ApiOperation("获取用户详情")
	@GetMapping("/user")
	public MyResponse<Object> getUserAuths(HttpServletRequest request) {
		User wxtbUser = accountService.getWxtbUser();
		if (wxtbUser == null) {
			response.setCodeAndMsg(ErrorCodeEnum.SYSTEM_ERROR);
			return response;
		}
		Map<String, Object> userDetail = accountService.getUserDetail(wxtbUser);
		response.setSuccess(userDetail);
		return response;
	}

	@ApiOperation("获取用户详情")
	@GetMapping("/test")
	public MyResponse<Object> test(HttpServletRequest request) {
		User user = (User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		response.setSuccess(user);
		return response;
	}

	@ApiOperation("查看按钮是否显示")
	@GetMapping("/button")
	public MyResponse<Object> buttonAppear(HttpServletRequest request,
			@RequestParam("buttonId") Integer buttonId) {
		User wxtbUser = accountService.getWxtbUser();
		boolean idAppear = accountService.getButtonAppear(
				wxtbUser.getAccount(), buttonId);
		if (idAppear) {
			response.setCodeAndMsg(ErrorCodeEnum.SUCCESS);
			return response;
		} 
		response.setCodeAndMsg(ErrorCodeEnum.SYSTEM_ERROR);
		return response;
	}
	
}

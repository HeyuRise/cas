package com.pcbwx.cas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcbwx.cas.dao.ConfigMapper;
import com.pcbwx.cas.dao.DictionaryMapper;
import com.pcbwx.cas.dao.MenuMapper;
import com.pcbwx.cas.dao.RoleAuthMapper;
import com.pcbwx.cas.dao.UserAuthMapper;
import com.pcbwx.cas.dao.UserRoleMapper;
import com.pcbwx.cas.dao.UserRoleRelationMapper;
import com.pcbwx.cas.enums.ClickEnum;
import com.pcbwx.cas.enums.ConfigEnum;
import com.pcbwx.cas.enums.DictionaryEnum;
import com.pcbwx.cas.enums.ErrorCodeEnum;
import com.pcbwx.cas.model.Dictionary;
import com.pcbwx.cas.model.Menu;
import com.pcbwx.cas.model.RoleAuth;
import com.pcbwx.cas.model.UserAuth;
import com.pcbwx.cas.model.UserRole;
import com.pcbwx.cas.model.UserRoleRelation;
import com.pcbwx.cas.service.CacheService;
import com.pcbwx.cas.service.ConfigService;
import com.pcbwx.cas.service.RedisService;
import com.pcbwx.cas.service.SupportService;

/**
 * 日志接口实现类
 * 
 * @author 孙贺宇
 *
 */
@Service("supportService")
public class SupportServiceImpl implements SupportService {

	private static Logger logger = LoggerFactory.getLogger(SupportServiceImpl.class);

	@Autowired
	private CacheService cacheService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private RedisService redisService;

	@Autowired
	private DictionaryMapper dictionaryMapper;
	@Autowired
	private ConfigMapper configMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private UserRoleRelationMapper userRoleRelationMapper;
	@Autowired
	private RoleAuthMapper roleAuthMapper;
	@Autowired
	private UserAuthMapper userAuthMapper;
	@Autowired
	private MenuMapper menuMapper;
	
	@PostConstruct
	public void reloadCacheInfo() {
		logger.info("启动载入缓存...");
		
		configService.setUtilRecord(ConfigEnum.LAST_RELOAD_TIME, new Date(), "");
		// 配置缓存
		reloadDictionary();
		// 权限缓存
		reloadUserRole();
		reloadUserRoleRelation();
		reloadUserAuth();
		reloadRoleAuth();
		reloadMenu();
	}

	private void reloadDictionary() {
		List<Dictionary> dictionarys = dictionaryMapper.load();
		redisService.reloadDictionary(dictionarys);
	}
	
	@Override
	public void reloadUserRole(){
		List<UserRole> records = userRoleMapper.load();
		cacheService.reloadUserRole(records);
	}
	
	public void reloadUserRoleRelation(){
		List<UserRoleRelation> records = userRoleRelationMapper.load();
		cacheService.reloadUserRoleRelation(records);
	}

	public void reloadUserAuth(){
		List<UserAuth> userAuths = userAuthMapper.load();
		cacheService.reloadUserAuth(userAuths);
	}
	
	@Override
	public void reloadRoleAuth(){
		List<RoleAuth> roleAuths = roleAuthMapper.load();
		cacheService.reloadRoleAuth(roleAuths);
	}
	
	private void reloadMenu(){
		List<Menu> menus = menuMapper.load();
		cacheService.reloadMenu(menus);
	}

	@Override
	public synchronized boolean doReloadCache() {
		logger.info("载入...");
		Date lastDate = configService.getUtilDate(ConfigEnum.LAST_RELOAD_TIME);
		Date now = new Date();
		// 重载用户角色关系
		reloadUserRoleRelation();
		// 重载角色权限关系
		reloadRoleAuth();
		
		Date lastRecordTime = null;
		boolean haveReload = false;
		lastRecordTime = dictionaryMapper.selectLastRecordTime();
		if (lastDate == null || (lastRecordTime != null && lastDate.before(lastRecordTime))) {
			reloadDictionary();
			haveReload = true;
		}
		
		lastRecordTime = userRoleMapper.selectLastRecordTime();
		if (lastDate == null || (lastRecordTime != null && lastDate.before(lastRecordTime))) {
			reloadUserRole();
			haveReload = true;
		}
		
		lastRecordTime = userAuthMapper.selectLastRecordTime();
		if (lastDate == null || (lastRecordTime != null && lastDate.before(lastRecordTime))) {
			reloadUserAuth();
			haveReload = true;
		}
		
		lastRecordTime = menuMapper.selectLastRecordTime();
		if (lastDate == null || (lastRecordTime != null && lastDate.before(lastRecordTime))) {
			reloadMenu();
			haveReload = true;
		}

		//--------------config----------------
		lastRecordTime = configMapper.selectLastRecordTime();
		if (lastDate == null || (lastRecordTime != null && lastDate.before(lastRecordTime))) {
			configService.reloadConfig();
			haveReload = true;
		}
		//-------------记录时间
		if (haveReload) {
			configService.setUtilRecord(ConfigEnum.LAST_RELOAD_TIME, now, "");			
		}
		
		return haveReload;
	}

	
	@Override
	public Set<Integer> getUserAuths(String account){
		List<UserRole> userRoles = cacheService.getUserRole(account);
		if (userRoles == null || userRoles.size() == 0) {
			return null;
		}
		Set<Integer> allAuthId = new HashSet<Integer>();
		List<RoleAuth> roleAuths = new ArrayList<RoleAuth>();
		Map<Integer, RoleAuth> role = cacheService.getRoleAuth();
		for (Integer roleAuthId : role.keySet()) {
			roleAuths.add(role.get(roleAuthId));
		}
		for (UserRole userRole : userRoles) {
			for (RoleAuth roleAuth : roleAuths) {
				if (userRole.getId() == roleAuth.getRoleId()) {
					allAuthId.add(roleAuth.getAuthId());
				}
			}
		}
		return allAuthId;
	}

	@Override
	public Integer ebableClick(String account, ClickEnum clickEnum) {
		Set<Integer> authIds = this.getUserAuths(account);
		Integer click = ErrorCodeEnum.SUCCESS.getCode();
		// 查看用户是否有操作权限
		Dictionary dictionary = redisService.getDictionary(DictionaryEnum.BUTTON, clickEnum.getCode());
		if (dictionary != null) {
			Integer authId = dictionary.getParamInt1();
			if (authId != null) {
				if (!authIds.contains(authId)) {
					click = ErrorCodeEnum.SYSTEM_ERROR.getCode();
				}
			}
		}
		return click;
	}

}


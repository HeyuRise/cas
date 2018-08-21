package com.pcbwx.cas.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pcbwx.cas.enums.DictionaryEnum;
import com.pcbwx.cas.model.Dictionary;
import com.pcbwx.cas.model.Menu;
import com.pcbwx.cas.model.RoleAuth;
import com.pcbwx.cas.model.UserAuth;
import com.pcbwx.cas.model.UserRole;
import com.pcbwx.cas.model.UserRoleRelation;


public interface CacheService {
	void reloadDictionary(List<Dictionary> dictionarys);

	void reloadUserRole(List<UserRole> records);
	void reloadUserRoleRelation(List<UserRoleRelation> records);
	void reloadUserAuth(List<UserAuth> records);
	void reloadRoleAuth(List<RoleAuth> records);
	void reloadMenu(List<Menu> records);
	//--------------------------------------------------
	Dictionary getDictionary(DictionaryEnum type, Integer innerId);
	Dictionary getDictionary(DictionaryEnum type, String innerCode);
	
	List<Dictionary> getDictionarys(DictionaryEnum type);
	
	List<UserRole> getUserRole(String account);
	Set<Integer> getRoleIds(String account);
	Map<Integer, UserRole> getUserRoles();
	Map<Integer, RoleAuth> getRoleAuth();
	Map<Integer, Menu> getMenu();
} 

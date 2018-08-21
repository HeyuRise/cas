package com.pcbwx.cas.security;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcbwx.cas.bean.User;
import com.pcbwx.cas.dao.WxtbUserMapper;
import com.pcbwx.cas.model.UserRole;
import com.pcbwx.cas.model.WxtbUser;
import com.pcbwx.cas.service.CacheService;
import com.pcbwx.cas.service.SupportService;
import com.pcbwx.cas.util.DataUtil;
import com.pcbwx.cas.util.StringUtil;

@Service("userDetailService")
@Transactional
public class MyUserDetailServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
	
	Logger logger = LoggerFactory.getLogger(MyUserDetailServiceImpl.class);
	
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SupportService supportService;
	
	@Autowired
	private WxtbUserMapper wxtbUserMapper;
	
	private Set<GrantedAuthority> obtionGrantedAuthority(User user){
		Set<GrantedAuthority> roleSet = new HashSet<GrantedAuthority>();
		final List<UserRole> userRoles = cacheService.getUserRole(user.getAccount());
	    for(UserRole role : userRoles) {
	        roleSet.add(new SimpleGrantedAuthority(role.getRoleName()));
	    }	
        return roleSet;
	}

	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken casAssertionAuthenticationToken) throws UsernameNotFoundException {
		String account = casAssertionAuthenticationToken.getName();
		WxtbUser wxtbUser = null;
		try {
			wxtbUser = wxtbUserMapper.selectByAccount(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(wxtbUser == null){
			throw new UsernameNotFoundException("不存在该用户！ ");
		}else if (!Objects.equals(1, wxtbUser.getEnable())) {
			throw new UsernameNotFoundException("您已经被禁用！ ");
		}
		User user = (User) DataUtil.fatherToChild(wxtbUser, User.class);
		Set<GrantedAuthority> authorities = obtionGrantedAuthority(user);
		user.setAuthorities(authorities);
		return user;
	}
}




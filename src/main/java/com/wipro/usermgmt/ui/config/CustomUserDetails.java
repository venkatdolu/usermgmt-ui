package com.wipro.usermgmt.ui.config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wipro.usermgmt.ui.model.Role;
import com.wipro.usermgmt.ui.model.User;

/**
 * @author Venkat

 * Description: used to get user details for authentication and authorization
 *
 */
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	/**
	 * Description: used to get authorities for the requested user
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Role role = user.getRole();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getName()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isStatus();
	}

	public String getFullName() {
		return user.getFullName();
	}

}
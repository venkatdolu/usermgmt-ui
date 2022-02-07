package com.wipro.usermgmt.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wipro.usermgmt.ui.microservice.client.UserManagementFeignClient;
import com.wipro.usermgmt.ui.model.User;


/**
 * @author Venkat
 * 
 * Description: used to load the user from db for requested username for authentication and authorization
 * 				used by spring security
 *
 */
public class CustomUserDetailsService implements UserDetailsService {

	/*@Autowired
	private UserRepository userRepo; */
	
	private UserManagementFeignClient feignClient;

	@Autowired
	public void setFeignClient(UserManagementFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	/**
	 * Description: loading the requested user from the DB for authentication
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//User user = userRepo.findByUserName(username);
		User user = feignClient.getUserByName(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomUserDetails(user);
	}

}
package com.wipro.usermgmt.ui.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.usermgmt.ui.microservice.client.UserManagementFeignClient;
import com.wipro.usermgmt.ui.model.ResponseVo;
import com.wipro.usermgmt.ui.model.Role;
import com.wipro.usermgmt.ui.model.User;
import com.wipro.usermgmt.ui.service.AppService;
import com.wipro.usermgmt.ui.service.MailSender;

/**
 * @author Venkat 
 * 
 * Description: Service used to connect UserManagement micro
 *         service
 */
@Service
public class AppServiceImpl implements AppService {
	
	private static final Logger Log = LoggerFactory.getLogger(AppServiceImpl.class);

	private PasswordEncoder passwordEncoder;

	private MailSender mailSender;

	private UserManagementFeignClient feignClient;

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Autowired
	public void setFeignClient(UserManagementFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	/**
	 * Description: connect to createUser API using fiegnClient
	 */
	@Override
	public ResponseVo createUser(User user, String siteURL) {
		Log.debug("createUser method invoked for useName: {}", user.getUserName());
		try {
			User existanceUser = feignClient.getUserByName(user.getUserName());
			if (!Objects.isNull(existanceUser)) {
				Log.info("username {} already exists in the system", user.getUserName());
				return new ResponseVo(402,
						"User already exist with username:" + user.getUserName() + ". Please try with different user.");
			}
			Role roleUser = feignClient.getRoleById(user.getRole().getId());
			user.setRole(roleUser);
			String password = UUID.randomUUID().toString().replace("-", "");
			user.setPassword(password);
			encodePassword(user);
			User updatedUser = feignClient.createUser(user);
			mailSender.sendVerificationEmail(updatedUser, siteURL);
			Log.debug("createUser method end for useName: {}", user.getUserName());
			return new ResponseVo(200, "success");
		} catch (Exception e) {
			return new ResponseVo(402, e.getMessage());
		}
	}

	/**
	 * Description: connect to listAll API using fiegnClient
	 */
	@Override
	public List<User> listAll() {
		Log.debug("listAll method invoked.");
		List<User> users = feignClient.getUsers();
		Log.debug("listAll method end.");
		return users;
	}

	/**
	 * Description: connect to getUserById API using fiegnClient
	 */
	@Override
	public User get(Long id) {
		Log.debug("get method invoked for id {} ." + id);
		User user = feignClient.getUserById(id);
		Log.debug("get method end for id {} ." + id);
		return user;
	}

	/**
	 * Description: connect to updateUser API using fiegnClient
	 */
	@Override
	public void updateUser(User user) {
		Log.debug("updateUser method invoked for id {} ." + user.getUserName());
		encodePassword(user);
		feignClient.updateUser(user);
		Log.debug("updateUser method end for id {} ." + user.getUserName());
	}

	/**
	 * Description: connect to deleteUser API using fiegnClient
	 */
	@Override
	public boolean deleteUser(Long id) {
		Log.debug("deleteUser method end for id {} ." + id);
		feignClient.deleteUser(id);
		Log.debug("deleteUser method end for id {} ." + id);
		return true;
	}

	/**
	 * Description: connect to verifyActivation API using fiegnClient
	 */
	@Override
	public boolean verifyActivationLink(Long id, String verificationCode) {
		Log.debug("verifyActivationLink method invoked for id {} ." + id);
		boolean isActivated = feignClient.verifyActivationToken(verificationCode, id);
		Log.debug("verifyActivationLink method end for id {} ." + id);
		return isActivated;
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	/**
	 * Description: connect to getRoles API using fiegnClient
	 */
	@Override
	public List<Role> getRoles() {
		Log.debug("getRoles method invoked.");
		List<Role> roles = feignClient.getAllRoles();
		Log.debug("getRoles method end.");
		return roles;
	}

}

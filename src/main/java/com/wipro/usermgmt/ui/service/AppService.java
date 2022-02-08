package com.wipro.usermgmt.ui.service;

import java.util.List;

import com.wipro.usermgmt.ui.model.ResponseVo;
import com.wipro.usermgmt.ui.model.User;

public interface AppService {

	ResponseVo createUser(User user, String siteURL);

	List<User> listAll();

	void updateUser(User user);

	boolean deleteUser(Long id);

	boolean verifyActivationLink(Long id, String verificationCode);

	User getExistanceUser(String field, String value);

}

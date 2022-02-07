package com.wipro.usermgmt.ui.service;

import com.wipro.usermgmt.ui.model.User;

public interface MailSender {
	
	void sendVerificationEmail(User user, String siteURL);

}

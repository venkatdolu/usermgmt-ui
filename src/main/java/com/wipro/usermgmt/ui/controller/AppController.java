package com.wipro.usermgmt.ui.controller;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wipro.usermgmt.ui.model.ResponseVo;
import com.wipro.usermgmt.ui.model.Role;
import com.wipro.usermgmt.ui.model.User;
import com.wipro.usermgmt.ui.service.AppService;


/**
 * @author Venkat
 * 
 * Description: Handles User interface requests and resposnes
 */
@Controller
public class AppController {

	private static final Logger Log = LoggerFactory.getLogger(AppController.class);
	
	public final static List<Role> LEVEL2_ROLES = Arrays.asList(new Role(1L, "Level 1"), new Role(2L, "Level 2"));
	
	public final static List<Role> LEVEL3_ROLES = Arrays.asList(new Role(1L, "Level 1"), new Role(2L, "Level 2"),
			new Role(3L, "Level 3"));

	private AppService service;

	@Autowired
	public void setService(AppService service) {
		this.service = service;
	}

	/**
	 * Description: to get login screen
	 * 
	 * @return
	 */
	@GetMapping("")
	public String getLogin() {
		return "redirect:/login";
	}

	/**
	 * Description: redirect to create new user interface
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/register")
	public String showRegistrationForm(Model model, HttpServletRequest request,
			Authentication authentication ) {
		Log.info("showRegistrationForm request invoked");
		model.addAttribute("user", new User());
		List<Role> rolesList = LEVEL3_ROLES;
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> role = (Collection<GrantedAuthority>) authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : role) {
			if (grantedAuthority.getAuthority().equals("Level 2")) {
				rolesList = LEVEL2_ROLES;
			}
		}
		model.addAttribute("rolesList", rolesList);
		Log.info("showRegistrationForm request end");
		return "signup_form";
	}

	/**
	 * Description: Process the newly created registration
	 * 
	 * @param user
	 * @param model
	 * @param request
	 * @return
	 */
	@PostMapping("/process_register")
	public String processRegister(User user, Model model, HttpServletRequest request,
			Authentication authentication) {
		Log.info("processRegister request invoked for userName : {}", user.getUserName());
		ResponseVo resposne = service.createUser(user, getSiteURL(request));
		if (resposne.getResponseCode() != 200) {
			model.addAttribute("user", user);
			List<Role> rolesList = LEVEL3_ROLES;
			@SuppressWarnings("unchecked")
			Collection<GrantedAuthority> role = (Collection<GrantedAuthority>) authentication.getAuthorities();
			for (GrantedAuthority grantedAuthority : role) {
				if (grantedAuthority.getAuthority().equals("Level 2")) {
					rolesList = LEVEL2_ROLES;
				}
			}
			model.addAttribute("rolesList", rolesList);
			model.addAttribute("msg", resposne.getMessage());
			Log.info("processRegister request end for userName : {}", user.getUserName());
			return "signup_form";
		}
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("msg", user.getUserName() + " is created. verify link sent to : " + user.getEmail());
		Log.info("processRegister request end for userName : {}", user.getUserName());
		return "users";

	}

	/**
	 * Description: listing all users
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/users")
	public String listUsers(Model model, HttpServletRequest request) {
		Log.info("listUsers request invoked");
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		Log.info("listUsers request end");
		return "users";
	}

	/**
	 * Description: uses to delete the user
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/users/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam Long id) {
		Log.info("deleteUser request invoked for id: {}", id);
		service.deleteUser(id);
		Log.info("deleteUser request end for id: {}", id);
		return "redirect:/users";
	}

	/**
	 * Description: use to verifies the activation link
	 * 
	 * @param userId
	 * @param code
	 * @param model
	 * @return
	 */
	@GetMapping("/verify")
	public String verifyUser(@RequestParam("id") Long userId, @RequestParam("code") String code, Model model) {
		Log.info("verifyUser request invoked for id: {}", userId);
		if (service.verifyActivationLink(userId, code)) {
			model.addAttribute("userId", userId);
			Log.info("verifyUser request end for id: {}", userId);
			return "password_form";
		} else {
			Log.info("verifyUser request failed for id: {}", userId);
			return "verify_fail";
		}
	}

	/**
	 * Description: create password for the newly register users
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		Log.info("processResetPassword request invoked for id: {}", userId);
		User user = service.getExistanceUser("id", userId);
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			Log.info("processResetPassword request failed for id: {}", userId);
			model.addAttribute("message", "Invalid Token");
			return "verify_fail";
		} else {
			user.setPassword(password);
			service.updateUser(user);
			Log.info("processResetPassword request end for id: {}", userId);
			return "verify_success";
		}

	}

	/**
	 * Description: redircets to for error scenarios
	 * 
	 * @return
	 */
	@PostMapping("/error")
	public String redirectLogin() {
		Log.info("redirectLogin request invoked .");
		return "redirect:/login";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

}
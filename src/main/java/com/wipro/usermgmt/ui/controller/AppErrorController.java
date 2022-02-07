package com.wipro.usermgmt.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Venkat
 * 
 * Description: handles the errors requests
 *
 */
@Controller
public class AppErrorController implements ErrorController {

	/**
	 * Description: to handleError
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {

		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
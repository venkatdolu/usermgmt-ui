package com.wipro.usermgmt.ui.microservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.wipro.usermgmt.ui.config.AppFeignConfiguration;
import com.wipro.usermgmt.ui.model.User;


/**
 * @author Venkat
 * 
 * Description: To connect with usermgmt-service micro service
 */
@FeignClient ( url = "${user.management.url}", name = "usermgmt", configuration = AppFeignConfiguration.class)
public interface UserManagementFeignClient
{

	@PostMapping("/createuser")
	public User createUser(@RequestBody User user);

	@PutMapping("/updateuser")
	public User updateUser(@RequestBody User user);

	@GetMapping("/getusers")
	public List<User> getUsers();

	@DeleteMapping("/deleteuser")
	public String deleteUser(@RequestParam Long id);

	@GetMapping("/verifyactivation")
	public Boolean verifyActivationToken(@RequestParam String code, @RequestParam Long id);

	@GetMapping("/getExistanceUser")
	public User getExistanceUser(@RequestParam String field, @RequestParam String value);

}

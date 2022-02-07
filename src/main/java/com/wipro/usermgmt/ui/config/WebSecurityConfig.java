package com.wipro.usermgmt.ui.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Venkat
 * 
 * Description : configuring the details for authentication and authorization
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	/**
	 * Description: configure authorization roles for the requested urls
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/verify").permitAll()
			.antMatchers("/reset_password").permitAll()
			.antMatchers("/users").authenticated()
			//.antMatchers("/users/edit/**").hasAnyAuthority("ADMIN")
			.antMatchers("/register").hasAnyAuthority("Level 2, Level 3")
			.antMatchers("//process_register").hasAnyAuthority("Level 2, Level 3")
			.antMatchers("/users/delete").hasAnyAuthority("Level 3")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.usernameParameter("username")
				.defaultSuccessUrl("/users")
				.permitAll()
			.and()
			.logout().logoutSuccessUrl("/").permitAll()
			 .and()
	          .exceptionHandling().accessDeniedPage("/error");
	} 

}
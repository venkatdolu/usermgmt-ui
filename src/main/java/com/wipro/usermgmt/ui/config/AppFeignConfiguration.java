package com.wipro.usermgmt.ui.config;
import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;


/**
 * @author Venkat
 * 
 * Description: configuration for feign client
 *
 */
public class AppFeignConfiguration
{

	@Bean
	public ErrorDecoder errorDecoder() {
		return new FeignErrorDecoder();
	}

	@Bean
	public Logger.Level logger() {
		return Logger.Level.FULL;
	}

	@SuppressWarnings("deprecation")
	@Bean
	public Request.Options options() {
		return new Request.Options(200000, 200000);
	}

}

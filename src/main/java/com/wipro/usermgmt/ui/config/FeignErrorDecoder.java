package com.wipro.usermgmt.ui.config;
import javax.naming.ServiceUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import feign.Response;
import feign.codec.ErrorDecoder;


/**
 * @author Venkat
 * Description: config decoder for feignclient
 */
public class FeignErrorDecoder implements ErrorDecoder
{
	private static final Logger LOG = LoggerFactory.getLogger(FeignErrorDecoder.class);

	@Override
	public Exception decode(String methodKey, Response response) {
		LOG.warn("Got response code {} from method key {}", response.status(), methodKey);
		LOG.info("{}", response);
		if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
			return new ServiceUnavailableException(
					"Could not connect to underlying service. Please try after some time.");
		} else if (response.status() == HttpStatus.BAD_REQUEST.value()) {
			return new Exception("Could not process the request.");
		} else if (response.status() == HttpStatus.NOT_FOUND.value()) {
			return new RuntimeException("Could not process the request.");
		} else {
			LOG.warn("Could not find mapping for response code {}. Throwing generic error.", response.status());
			return new Exception("Error while processing request.");
		}
	}
}
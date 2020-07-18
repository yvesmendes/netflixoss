package com.thoughtmechanix.licenses.utils;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class UserContextRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
		requestTemplate.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
		requestTemplate.header("Authorization", UserContextHolder.getContext().getAuthToken());
	}
}
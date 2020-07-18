package com.thoughtmechanix.licenses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

	@Value("${example.property}")
	private String exampleProperty = "";

	public String getExampleProperty() {
		return exampleProperty;
	}

	@Value("${redis.host}")
	private String redisServer = "";

	@Value("${redis.port}")
	private String redisPort = "";

	public String getRedisServer() {
		return redisServer;
	}

	public Integer getRedisPort() {
		return new Integer(redisPort).intValue();
	}
}

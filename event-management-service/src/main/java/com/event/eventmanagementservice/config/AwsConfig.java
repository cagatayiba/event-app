package com.event.eventmanagementservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class AwsConfig {
	@Value("${server.port}")
	private int port;
	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils){
		EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		String ip = null;

		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		config.setIpAddress(ip);
		config.setPreferIpAddress(true);
		config.setNonSecurePort(port);

		return config;
	}
}

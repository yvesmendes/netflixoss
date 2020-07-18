package com.thoughtmechanix.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thoughtmechanix.authentication.services.CustomUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		if (passwordEncoder == null) {
			passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			// passwordEncoder = new BCryptPasswordEncoder();
		}
		return passwordEncoder;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return customUserDetailsService;
	}
}

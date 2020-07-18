package com.thoughtmechanix.authentication.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

	private static final long serialVersionUID = -2633659220734280260L;
	
	private Set<Role> roles = new HashSet<Role>();

	@Override
	public String getAuthority() {
		return "USER";
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}

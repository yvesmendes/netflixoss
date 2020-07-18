package com.thoughtmechanix.authentication.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thoughtmechanix.authentication.model.Role;
import com.thoughtmechanix.authentication.model.User;
import com.thoughtmechanix.authentication.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User member = userRepository.findOneByUserName(username);
		if (member == null) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
		}
		return new UserRepositoryUserDetails(member);
	}

	private final static class UserRepositoryUserDetails extends User implements UserDetails {
		private static final long serialVersionUID = 1L;

		private UserRepositoryUserDetails(User member) {
			super(member);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Role role = new Role();
			return role.getRoles();
		}

		@Override
		public String getUsername() {
			return super.getUserName();
		}
		
		@Override
		public String getPassword() {
			return super.getPassword();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}

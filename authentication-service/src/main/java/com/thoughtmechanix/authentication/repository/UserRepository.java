package com.thoughtmechanix.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.thoughtmechanix.authentication.model.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneByUserName(String username);
}
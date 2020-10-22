package com.appsdeveloperblog.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	//UserEntity findUserByEmail(String email);
	UserEntity findByEmail(String email);
	
	
	
	
	

}

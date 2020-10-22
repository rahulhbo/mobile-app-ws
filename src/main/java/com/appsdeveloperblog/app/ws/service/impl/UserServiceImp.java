package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

import com.appsdeveloperblog.app.ws.shared.Utils;

@Service
public class UserServiceImp implements UserService{
	
	@Autowired
	UserRepository userRepositery;
	
	@Autowired
	Utils utils;
   @Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override
	public UserDto createUser(UserDto user) {
    
		if(userRepositery.findByEmail(user.getEmail())!=null) throw new RuntimeException("Already Exist Exception");
		
		UserEntity userEntity=new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		String publicUserId=utils.generatetUserId(30);
		userEntity.setUserId(publicUserId);
		UserEntity storedUserDetails= userRepositery.save(userEntity);
		
		UserDto returnValue=new UserDto();
		 BeanUtils.copyProperties(storedUserDetails,returnValue);
		
		return returnValue;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity= userRepositery.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(), new ArrayList<>());
	}


	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity=userRepositery.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		UserDto returnValue=new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

}

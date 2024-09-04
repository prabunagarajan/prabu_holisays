package com.devar.cabs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devar.cabs.common.ErrorCode;
import com.devar.cabs.common.ErrorMessages;
import com.devar.cabs.common.ResponseMessageConstant;
import com.devar.cabs.entity.DriverDetailsEntity;
import com.devar.cabs.entity.UserEntity;
import com.devar.cabs.repository.UserRepository;
import com.devar.cabs.requestDTO.DriverDetailsRequestDTO;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.Library;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//	    public UserEntity createUser(UserEntity user) {
//	        user.setPassword(passwordEncoder.encode(user.getPassword()));
//	        return userRepository.save(user);
//	    }

	public GenericResponse createUser(UserEntity user) {
		Optional<UserEntity> userDetails = userRepository.findByUserName(user.getUserName());
		if (userDetails.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.ALREADY_EXIST.getErrorCode(),
					"This User Name is Already Exist");
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return Library.getSuccessfulResponse(userRepository.save(user), ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

	public Optional<UserEntity> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}


	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with userName: " + userName));
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());

	}

	public GenericResponse findById(Long id) {
		Optional<UserEntity> driverDetails = userRepository.findById(id);
		if (!driverDetails.isPresent()) {
		
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(driverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


}

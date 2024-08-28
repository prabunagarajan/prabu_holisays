package com.devar.cabs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.devar.cabs.entity.UserEntity;
import com.devar.cabs.requestDTO.LoginRequestDTO;
import com.devar.cabs.service.UserService;
import com.devar.cabs.utility.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Optional;

@Api(value = "User", description = "This controller contain all  operation of Driver Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtUtil jwtUtil;

	    @PostMapping("/signup")
	    public ResponseEntity<UserEntity> signup(@RequestBody UserEntity user) {
	        UserEntity createdUser = userService.createUser(user);
	        return ResponseEntity.ok(createdUser);
	    }

//	    @PostMapping("/login")
//	    public ResponseEntity<String> login(@RequestBody String username, String password) {
//	        Optional<UserEntity> userOpt = userService.findByUsername(username);
//	        if (userOpt.isPresent()) {
//	            UserEntity user = userOpt.get();
//	            if (userService.checkPassword(password, user.getPassword())) {
//	                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//	                final UserDetails userDetails = userService.loadUserByUsername(username);
//	                final String jwt = jwtUtil.generateToken(userDetails);
//	                return ResponseEntity.ok("Bearer " + jwt);
//	            } else {
//	                return ResponseEntity.status(401).body("Invalid credentials");
//	            }
//	        } else {
//	            return ResponseEntity.status(404).body("User not found");
//	        }
//	    }
	    
//	    @PostMapping("/login")
//	    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
//	        String username = loginRequest.getUsername();
//	        String password = loginRequest.getPassword();
//	        
//	        Optional<UserEntity> userOpt = userService.findByUsername(username);
//	        if (userOpt.isPresent()) {
//	            UserEntity user = userOpt.get();
//	            if (userService.checkPassword(password, user.getPassword())) {
//	                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//	                final UserDetails userDetails = userService.loadUserByUsername(username);
//	                final String jwt = jwtUtil.generateToken(userDetails);
//	                return ResponseEntity.ok("Bearer " + jwt);
//	            } else {
//	                return ResponseEntity.status(401).body("Invalid credentials");
//	            }
//	        } else {
//	            return ResponseEntity.status(404).body("User not found");
//	        }
//	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
	        String username = loginRequest.getUsername();
	        String password = loginRequest.getPassword();
	        
	        Optional<UserEntity> userOpt = userService.findByUsername(username);
	        if (userOpt.isPresent()) {
	            UserEntity user = userOpt.get();
	            if (userService.checkPassword(password, user.getPassword())) {
	                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	                final UserDetails userDetails = userService.loadUserByUsername(username);
	                final String jwt = jwtUtil.generateToken(userDetails, user);
	                return ResponseEntity.ok("Bearer " + jwt);
	            } else {
	                return ResponseEntity.status(401).body("Invalid credentials");
	            }
	        } else {
	            return ResponseEntity.status(404).body("User not found");
	        }
	    }


	    @GetMapping("/{id}")
	    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
	        Optional<UserEntity> userOpt = userService.findById(id);
	        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
	    }

}

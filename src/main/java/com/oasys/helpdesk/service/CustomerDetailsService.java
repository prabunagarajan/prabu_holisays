package com.oasys.helpdesk.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.entity.Activity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.RoleActivityRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.security.UserPrincipal;


@Service("userDetailsService")
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleActivityRepository roleActivityRepository;
    
    /**
     * This method will be used to load the user details based on the username or email
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailId)
            throws UsernameNotFoundException {
    	//Users user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
		Optional<UserEntity> user = userRepository.findByUsernameOrEmailIdIgnoreCase(emailId, emailId);
        if(!user.isPresent()) {
        	 throw new UsernameNotFoundException("User not found with email : " + emailId);
        }
		List<RoleMaster> roleMasterList = user.get().getRoles();
		List<String> roles = null;
		List<String> authorities = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roleMasterList)) {
			roles = roleMasterList.stream().map(r -> r.getRoleCode()).collect(Collectors.toList());
			roleMasterList.forEach(role -> {
				List<Activity> activities = roleActivityRepository.getActivityByRoleId(role.getId());
				if (!CollectionUtils.isEmpty(activities)) {
					authorities.addAll(activities.stream().map(a -> a.getCode()).collect(Collectors.toList()));
				}
			});
		}
		 UserPrincipal userPrincipal = new UserPrincipal(user.get().getId(), user.get().getUsername(),
				user.get().getFirstName(), user.get().getPassword(), user.get().getEmailId(),
				user.get().getPhoneNumber(), user.get().getEmployeeId(), authorities, roles);
        //return UserPrincipal.create(user);
    	
    	return userPrincipal;
    }

    /** this method used in JWTAuthenticationFilter to load the user details */
    public UserDetails loadUserById(Long id) {
    	UserEntity user = userRepository.findById(id).orElseThrow(
            () -> new UsernameNotFoundException("User not found with id : " + id)
        );
    	List<RoleMaster> roleMasterList = user.getRoles();
    	List<String> authorities = new ArrayList<>();
    	List<String> roles = null;
		if (!CollectionUtils.isEmpty(roleMasterList)) {
			roles = roleMasterList.stream().map(r -> r.getRoleCode()).collect(Collectors.toList());
			roleMasterList.forEach(role -> {
				List<Activity> activities = roleActivityRepository.getActivityByRoleId(role.getId());
				if (!CollectionUtils.isEmpty(activities)) {
					authorities.addAll(activities.stream().map(a -> a.getCode()).collect(Collectors.toList()));
				}
			});
		}
		UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getUsername(), user.getFirstName(),
				user.getPassword(), user.getEmailId(), user.getPhoneNumber(),user.getEmployeeId(),  authorities, roles);
        return userPrincipal;
    }
}

package com.oasys.helpdesk.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

//	UserRole getByUserMaster(User user);
	
	@Query("select ur from UserRole ur where ur.user.id=:userid ")
	UserRole getByUserMaster(@Param("userid") Long userId);
	
	
	@Query("select ur from UserRole ur where ur.roleMaster.id=:rolid ")
	Optional<UserRole> getByUser(@Param("rolid") Long userId);
	
	@Modifying
    @Transactional
	@Query(value="UPDATE user_role SET role_id=:roleID WHERE user_id =:userID", nativeQuery=true)
	public void updateAssociatedRecords(@Param("roleID") Long roleID,@Param("userID") Long userid);
}

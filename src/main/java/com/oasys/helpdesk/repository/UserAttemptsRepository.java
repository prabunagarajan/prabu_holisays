package com.oasys.helpdesk.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oasys.helpdesk.entity.UserAttempts;



@Repository
public interface UserAttemptsRepository extends JpaRepository<UserAttempts, Long> {

	@Transactional
    @Modifying
    @Query("Update UserAttempts userAttempts set userAttempts.attempts = 0,"
    		+ "userAttempts.lastModified=null where userAttempts.userId=:userId")
	void resetFailAttempts(@Param("userId") Long userId);

    @Query("SELECT userAttempts from UserAttempts userAttempts where userAttempts.userId=:userId")
    UserAttempts getUserAttempts(@Param("userId") Long userId);
}


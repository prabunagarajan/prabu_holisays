package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssociatedUserEntity;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;

@Repository
public interface AssociatedUserRepository extends JpaRepository<AssociatedUserEntity, Long> {

	@Query("SELECT distinct(a.associatedUserId) FROM AssociatedUserEntity a where a.user.id = :assignToOfficerId")
	public List<Long> getUserByInspectingOfficerId(@Param("assignToOfficerId") Long assignToOfficerId);
	
	@Query("SELECT a FROM AssociatedUserEntity a where  a.associatedUserId = :handlingOfficerId")
	public List<AssociatedUserEntity> getUserByHandlingOfficerId(@Param("handlingOfficerId") Long handlingOfficerId);
	
	//@Query("DELETE FROM AssociatedUserEntity a where a.user.id not in :assignToOfficerId and a.associatedUserId = :handlingOfficerId")
	@Modifying
    @Transactional
	@Query(value = "DELETE FROM associated_user a where a.associated_user_id = :handlingOfficerId and a.user_id not in :assignToOfficerId", nativeQuery=true)
	public void deleteAssociatedRecords(@Param("assignToOfficerId") Long assignToOfficerId, @Param("handlingOfficerId") Long handlingOfficerId);
	
	@Query("SELECT a FROM AssociatedUserEntity a where  a.associatedUserId = :handlingOfficerId and a.user.id = :assignToOfficerId")
	public Optional<AssociatedUserEntity> getUserByHandlingOfficerIdAndInspectingOfficerId(@Param("handlingOfficerId") Long handlingOfficerId, @Param("assignToOfficerId") Long assignToOfficerId);
	
	
//	@Query("select distinct user from AssociatedUserEntity")
//	public List<Long> findAllByOrderByModifiedDateDesc();
	
//	@Query("select distinct user,userName from AssociatedUserEntity")
//	public List<AssociatedUserEntity> findAllByOrderByModifiedDateDesc();
	
	@Query(value ="select distinct user_id,user_name from associated_user",nativeQuery = true)
	public List<Object> findAssociatedUser();
	
	//@Query("SELECT a FROM AssociatedUserEntity a where  a.user = :user")
//	@Query(value ="select a.* from associated_user a where a.user_id = user_id order by a.modified_date desc", nativeQuery=true)
//	public List<AssociatedUserEntity> findByUser(@Param("user_id") Long handlingOfficerId);
//	
	
	public List<AssociatedUserEntity> findAllByUserOrderByModifiedDateDesc(@Param("user") Long handlingOfficerId);
	
	
	@Query(value ="select * from associated_user a where  a.user_id = :user_id ",nativeQuery = true)
	public List<AssociatedUserEntity> findByUser(@Param("user_id") Long handlingOfficerId);
	
	
	@Query(value ="select * from associated_user a where  a.associated_user_id = :associated_user_id ",nativeQuery = true)
	public List<AssociatedUserEntity> findByAssociatedUserId(@Param("associated_user_id") Long asigntoofficerid);
	
	
	@Query("SELECT a FROM AssociatedUserEntity a where  a.associatedUserId = :associatedUserId")
	public Optional<AssociatedUserEntity> getByAssociatedUserId(@Param("associatedUserId") Long assignToOfficerId);
	
	
	@Query("SELECT a FROM AssociatedUserEntity a where  a.user = :user")
	public Optional<AssociatedUserEntity> getByUser(@Param("user") Long assignToOfficerId);
	
}

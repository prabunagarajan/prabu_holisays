package com.oasys.helpdesk.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserGroupEntity;
@Repository
public interface UsergroupRepository extends JpaRepository<UserGroupEntity, Long>{
	
	
	Optional<UserGroupEntity> findByUsergroupNameIgnoreCase(String usergroupname);
	
    Optional<UserGroupEntity> findByUsergroupCodeIgnoreCase(String code);
    
    List<UserGroupEntity> findAllByOrderByModifiedDateDesc();
    
    
    @Query("SELECT a FROM UserGroupEntity a where  (a.usergroupName) =:usergroup_name and a.id !=:id")
	Optional<UserGroupEntity> findByUsergroupNameIgnoreCaseNotInId(@Param("usergroup_name") String usergroup_name, @Param("id") Long id);
	
    
    @Query("SELECT a FROM UserGroupEntity a ")
	Page<UserGroupEntity> getAll(Pageable pageable);
    
    
    
    @Query("SELECT a FROM UserGroupEntity a where  a.usergroupName =:usergroup_name and a.role=:role and a.status=:status")
	Page<UserGroupEntity> getByUsergroupNameRoleAndStatus(@Param("usergroup_name") String usergroup_name,@Param("role") String role, @Param("status") Boolean status, Pageable pageable);
	
    
    @Query(value ="select a.* from usergroup_help a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<UserGroupEntity> findAllByStatusOrderByModifiedDateDesc();
	
    
    

}

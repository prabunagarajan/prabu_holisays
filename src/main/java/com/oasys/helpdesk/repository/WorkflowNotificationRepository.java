package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.workflowNotificationEntity;


public interface WorkflowNotificationRepository extends JpaRepository<workflowNotificationEntity, Long> {

 
 //@Query("SELECT a FROM workflowNotificationEntity a where  a.categoryId.id=:category order by a.modifiedDate desc")
 List<workflowNotificationEntity> findByCategoryId(Category categoryid);
 
 List<workflowNotificationEntity> findByRoleId(RoleMaster roleid);
	
 List<workflowNotificationEntity> findByRoleIdAndCategoryId(RoleMaster roleid,Category categoryid);

}

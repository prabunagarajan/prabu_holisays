package com.oasys.helpdesk.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	
	
	@Query(nativeQuery = true , value = "select r.* from activity r left join module m on r.module_id =m.id "
			+ "where r.active =true and (m.active = true or m.active is null) ORDER BY m.display_order,r.display_order")
	List<Activity> getActiveActivity();

	@Query(nativeQuery = true , value = "select r.* from activity r left join module m on r.module_id =m.id "
			+ "where r.active =true and (m.active = true or m.active is null) and m.code = :moduleCode ORDER BY m.display_order,r.display_order")
	List<Activity> getActiveActivityByModuleCode(@Param("moduleCode") String moduleCode);

	List<Activity> findByCode(String licenseManagementDashboardMenu);

	/**
	 * Find the activity based on the code or name
	 * @param searchInput
	 * @param searchInput2
	 * @param paging
	 * @return
	 */
	Page<Activity> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name, Pageable paging);

	/**
	 * find all active activity
	 * @param b
	 * @return
	 */
	List<Activity> findByActive(boolean isActive);

	/**
	 * get all active activity
	 * @param searchInput
	 * @param b
	 * @param paging
	 * @return
	 */
	Page<Activity> findByActiveAndNameContainingIgnoreCase(Boolean active,String searchInput, Pageable paging);
	
}

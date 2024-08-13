package com.oasys.helpdesk.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.AppModule;

public interface AppModuleRepository extends JpaRepository<AppModule, Long> {
	
	@Query("select r from AppModule r where r.id=:id ")
	AppModule getById(@Param("id") Long id);
	
	@Query("select a from AppModule a where a.active=true and a.parentModule=null ")
	List<AppModule> getActiveParentModule();
	
	@Query("select r from AppModule r where r.code=:code ")
	AppModule getAppModuleByCode(@Param("code") String code);
	
	@Query(nativeQuery = true, value = "SELECT m.* from module m "
			+ "join activity a on a.module_id=m.id or m.route_url is not null "
			+ "where m.parent_id is null and m.is_active=true GROUP by m.id order by display_order ")
	List<AppModule> getParentModuleLinkedToActivity();
	
	
	@Query(nativeQuery = true, value = "SELECT m.* from module m join activity a on a.module_id=m.id or m.route_url is not null where m.is_active=true GROUP by m.id order by display_order ")
	List<AppModule> getModuleLinkedToActivity();
	
	@Query("select a from AppModule a where a.active=true")
	List<AppModule> getAllActiveModule();

	/**
	 * find based on the name or code
	 * @param searchInput
	 * @param searchInput2
	 * @param paging
	 * @return
	 */
	Page<AppModule> findByCodeOrName(String searchInput, String searchInput2, Pageable paging);
	

}

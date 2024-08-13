package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.DistrictEntity;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.TicketStatusEntity;
@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {
	
	
	@Query("select i from IssueDetails i where i.id=:id")
	DistrictEntity getById(@Param("id") Long id);
	
	
	Optional<DistrictEntity> findByCodeIgnoreCase(String code);
	
	@Query(value ="select a.* from district_help a where a.status = true order by a.modified_date desc", nativeQuery=true)
    List<DistrictEntity> findAllByStatusOrderByModifiedDateDesc();
	
	
	Optional<DistrictEntity> findByCountrynameIgnoreCase(String countryname);
	
	@Query("SELECT a FROM DistrictEntity a where  (a.countryname) =:countryname and a.id !=:id")
	Optional<DistrictEntity> findByCountrynameIgnoreCaseNotInId(@Param("countryname") String countryname, @Param("id") Long id);
	
	
	List<DistrictEntity> findAllByOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM DistrictEntity a ")
	Page<DistrictEntity> getAll(Pageable pageable);
	
	@Query("SELECT a FROM DistrictEntity a where a.countryname =:countryname and a.state=:state and a.status=:status")
	Page<DistrictEntity> getByCountrynameAnstateAndStatus(@Param("countryname") String countryName,@Param("state") String state,
		@Param("status") String status, Pageable pageable);
	
	

}

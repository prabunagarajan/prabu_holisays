package com.devar.cabs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devar.cabs.entity.DriverDetailsEntity;




@Repository
public interface DriverDetailsRepository extends JpaRepository<DriverDetailsEntity, Long>{

	Optional<DriverDetailsEntity>  findByAadharNumber(String aadharNumber);

	@Query("SELECT a FROM DriverDetailsEntity a where  a.aadharNumber =:aadharNumber and a.id !=:id")
	Optional<DriverDetailsEntity> findByAadharNotInId(String aadharNumber, Long id);

	List<DriverDetailsEntity> findAllByOrderByIdDesc();

	List<DriverDetailsEntity> findByStatusOrderByModifiedDateDesc(Boolean true1);

}

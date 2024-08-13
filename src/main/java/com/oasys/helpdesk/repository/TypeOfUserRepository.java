package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.TypeOfUserEntity;

@Repository
public interface TypeOfUserRepository extends JpaRepository<TypeOfUserEntity, Long>{

	Optional<TypeOfUserEntity> findByCodeIgnoreCase(@Param("code") String upperCase);

	
	@Query("SELECT a FROM TypeOfUserEntity a where a.status=:status  and a.typeOfUser like %:typeOfUser%"  )
	Page<TypeOfUserEntity> getBySubStringAndStatus(@Param("typeOfUser") String typeOfUser, @Param("status")  Boolean status, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where  a.typeOfUser like %:typeOfUser%")
	Page<TypeOfUserEntity> getBySubString(@Param("typeOfUser") String name, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where  a.status=:status")
	Page<TypeOfUserEntity> getByStatus(@Param("status")  Boolean status, Pageable pageable);
	
//	@Query("SELECT a FROM TypeOfUserEntity a ")
//	Page<TypeOfUserEntity> getAll(Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where a.typeOfUser like %:typeOfUser%")
	Page<TypeOfUserEntity> getAllSubString(@Param("typeOfUser") String name, Pageable pageable);


	Optional<TypeOfUserEntity> findByTypeOfUserAndId(@Param("typeOfUser") String typeOfUser, Long id);

//	@Query("SELECT a FROM TypeOfUserEntity a where a.typeOfUser like %:typeOfUser%")
	List<TypeOfUserEntity> findAllByTypeOfUser(@Param("typeOfUser") String typeOfUser);


	List<TypeOfUserEntity> findAllByOrderByModifiedDateDesc();

//	@Query(value ="select a.* from TypeOfUserEntity a where a.status = :status order by a.modified_date desc", nativeQuery=true)
	List<TypeOfUserEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);
	
	Optional<TypeOfUserEntity> findByTypeOfUserIgnoreCase(@Param("typeOfUser") String typeOfUser);	
	
	@Query("SELECT a FROM TypeOfUserEntity a where upper(a.typeOfUser) like %:typeOfUser% and a.id !=:id")
	Optional<TypeOfUserEntity> findByTypeOfUserNotInId(@Param("typeOfUser") String typeOfUser, @Param("id") Long id);

	@Query("SELECT a FROM TypeOfUserEntity a where upper(a.typeOfUser) like %:typeOfUser% and a.id =:id and a.status=:status")
	Page<TypeOfUserEntity> getByIdStatusAndTypeOfUser(Long id, Boolean status, String typeOfUser,
			Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where upper(a.typeOfUser) like %:typeOfUser% and a.id =:id ")
	Page<TypeOfUserEntity> getByIdAndTypeOfUser(Long id, String typeOfUser, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where upper(a.typeOfUser) like %:typeOfUser% and  a.status=:status ")
	Page<TypeOfUserEntity> getByTypeOfUserAndStatus(String typeOfUser, Boolean status, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where a.id =:id ")
	Page<TypeOfUserEntity> getById(Long id, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a where upper(a.typeOfUser) like %:typeOfUser% ")
	Page<TypeOfUserEntity> getByTypeOfUser(String typeOfUser, Pageable pageable);
	
	@Query("SELECT a FROM TypeOfUserEntity a where a.id =:id and  a.status=:status  ")
	Page<TypeOfUserEntity> getByIdAndStatus(Long id, Boolean status, Pageable pageable);

	@Query("SELECT a FROM TypeOfUserEntity a ")
	Page<TypeOfUserEntity> getAll(Pageable pageable);	
}

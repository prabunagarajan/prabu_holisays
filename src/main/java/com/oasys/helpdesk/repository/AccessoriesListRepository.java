package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AccessoriesListData;

@Repository
public interface AccessoriesListRepository extends JpaRepository<AccessoriesListData, Long>{

	//	Optional<List<AccessoriesListData>> findByAccessoriesCodeIgnoreCase(String accessoriesCode);

	Optional<List<AccessoriesListData>> findByAccessoriesCodeIgnoreCase(String accessoriesCode);

	List<AccessoriesListData> findAllByOrderByModifiedDateDesc();


	Optional<AccessoriesListData> findByAccessoriesNameIdIgnoreCase(String accessoriesNameId);

	List<AccessoriesListData> findAllByStatusOrderByModifiedDateDesc(Boolean status);

	Optional<List<AccessoriesListData>> findByAccessoriesCode(String accessoriesCode);





}

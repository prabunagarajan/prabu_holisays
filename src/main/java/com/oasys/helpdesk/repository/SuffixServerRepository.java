package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SuffixServerEntity;

@Repository
public interface SuffixServerRepository extends JpaRepository<SuffixServerEntity, Long> {

}

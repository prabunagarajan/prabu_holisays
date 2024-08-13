package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.FeedBackEntity;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBackEntity, Long>{

}

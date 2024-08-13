package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.FeedBackComplaintEntity;

@Repository
public interface FeedBackComplaintRepository extends JpaRepository<FeedBackComplaintEntity, Long>{

}

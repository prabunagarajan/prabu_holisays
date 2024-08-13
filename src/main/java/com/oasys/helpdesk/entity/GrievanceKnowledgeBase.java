package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "grievance_knowledge_base")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceKnowledgeBase extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "gkb_code")
	private String code ;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_gkb_category_id"))
	private GrievanceCategoryEntity categoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_gkb_issue_details"))
	private GrievanceIssueDetails issueDetails;

	@Column(name = "priority")
	private String priority;

	@Column(name = "sla")
	private Long sla;

	@Column(name = "remark")
	private String remarks;

	@Column(name = "knowledge_solution")
	private String knowledgeSolution;
	
    @Column(name = "is_resolved")
	private boolean isResolved;
    
    @Column(name = "status")
	private boolean status;
    
    @Column(name = "count1")
	private Integer count;

    @Column(name = "type_of_user")
  	private String typeofUser;
    
    

}

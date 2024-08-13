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
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "problem_reported")
public class ProblemReported extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "problem")
	private String problem;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "priority_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_priority"))
//	private Priority priority;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ticket_sub_category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_sub_category"))
	private SubCategory subCategoryId;

    @Column(name = "is_active") 
	private boolean isActive;
    
    
    @Column(name = "pr_code")
	private String prCode;
    
    
    

}

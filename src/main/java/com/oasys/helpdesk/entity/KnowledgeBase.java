package com.oasys.helpdesk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "knowledge_base")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeBase extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "kb_id")
	private Long kbId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_kb_sub_category_id"))
	private SubCategory subcategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_kb_category_id"))
	private Category categoryId;

	@Column(name = "issue_details")
	private String issueDetails;

	@Column(name = "status")
	private String status;

	@Column(name = "priority")
	private String priority;

	@Column(name = "sla")
	private int sla;

	@Column(name = "count1")
	private Integer count;

	@Column(name = "remark")
	private String remarks;

	@Column(name = "knowledge_solution")
	private String knowledgeSolution;
	
    @Column(name = "is_resolved")
	private boolean isResolved;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_kb_category_id"))
	private IssueDetails issueDetailsEntity;

}

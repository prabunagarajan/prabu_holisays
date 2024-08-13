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
@Table(name = "help_desk_faq")
public class Faq extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "answer")
	private String answer;

    @Column(name = "is_deleted") 
	private Boolean deleted;
    
    @Column(name = "question")
	private String question;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdf_sub_category"))
	private SubCategory subCategoryId;
	
	@Column(name = "is_active")
	private Boolean status;
	
	@Column(name = "code")
	private String code;
    
    

}

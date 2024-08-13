package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkflowDTO implements Serializable {

	private String subModuleNameCode;
	
	private String moduleNameCode;
	
	private String applicationNumber;
	
	private String event;
	
	private String level;
	
	private String sendBackTo;
	
	private String forwardTo;
	
	private String comments;
	
	private String callbackURL;
}
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
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.LevelMaster;
import com.oasys.helpdesk.entity.RoleMaster;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class WorkflowNotificationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String workflowName;
	private String description;
	private Boolean push;
	private Boolean email;
	private Boolean sms;
	private Long levelId;
	private Long roleId;
	private Long categoryId;
	private Boolean active;
	private String levelName;
	private List<WorkflowNotDTO>  workflowDTO;


}
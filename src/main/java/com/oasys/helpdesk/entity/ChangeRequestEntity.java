package com.oasys.helpdesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.enums.ApplnStatus;
import com.oasys.helpdesk.enums.ChangereqStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Table(name = "change_request")
public class ChangeRequestEntity {
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "unit_name")
	private String unitName;

	@Column(name = "license_no")
	private String licenseNo;

	@Column(name = "license_status")
	private String licenseStatus;

	@Column(name = "address")
	private String address;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "shop_code")
	private String shopCode;

	@Column(name = "shop_name")
	private String shopName;

	@Column(name = "district")
	private String district;

	@Column(name = "changereq_appln_no")
	private String changereqApplnNo;

	@Column(name = "appln_status")
	private ApplnStatus applnStatus;

	@Column(name = "changereq_status")
	private ChangereqStatus changereqStatus;

	@Column(name = "description")
	private String description;

	@Column(name = "iescms_url")
	private String iescmsUrl;

	@Column(name = "iescms_uuid")
	private String iescmsUuid;

	@Column(name = "department_url")
	private String departmentUrl;

	@Column(name = "department_uuid")
	private String departmentUuid;

	@Column(name = "currently_workwith")
	private String currentlyWorkwith;

	@Column(name = "approved_by")
	private String approvedBy;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false)
	private ChangeRequestFeaturesEntity featureId;

	@Column(name = "raised_by")
	private String raisedBy;

	@Column(name = "user_mobile_number")
	private String userMobileNumber;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "district_code")
	private String districtCode;

	public ChangeRequestEntity() {
		createdDate = new Date();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assign_to", referencedColumnName = "id")
	private UserEntity assignTo;

}

package com.oasys.helpdesk.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.utility.EmploymentStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "user",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "email_id"
	        }),
	        @UniqueConstraint(columnNames = {
	            "phone_number"
	        }),
	        @UniqueConstraint(columnNames = {
		            "username"
		        })
})
@EqualsAndHashCode(callSuper=false)
public class UserEntity extends Trackable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8456161920720838558L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;	
	
	@Column(name = "middle_name")
	private String middleName;	
	
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "username")
	private String username;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	@JsonIgnore
	@Column(name = "password")
	private String password;
	
	@Column(name = "salutation_code")
	private String salutationCode;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "employee_id")
	private String employeeId;
	
	@Column(name = "employee_status")
	private EmploymentStatus employmentStatus;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",timezone ="Asia/Kolkata")
	@Column(name = "date_of_joining")
	private LocalDate dateOfJoining;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleMaster> roles = new ArrayList<>();
	
	@Column(name = "designation_code")
	private String designationCode;
	
	@Column(name = "is_dept_asset_applicable")
	private Boolean isDeptAssetApplicable;
	
	@JoinColumn(name = "device_id")
	private String deviceId;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private WorkLocationEntity workLocation;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private UserShiftConfigurationEntity shiftConfiguration;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private BackupUserEntity backupUser;
	
	@Column(name = "is_account_locked")
	private Boolean accountLocked =  Boolean.FALSE;
	
	@Column(name = "is_system_default_user")
	private Boolean isSystemDefaultUser =  Boolean.FALSE;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private List<AssociatedUserEntity> associatedUsers;
	
	public String getEmploymentStatus() {
		return this.employmentStatus.getType();
	}
}

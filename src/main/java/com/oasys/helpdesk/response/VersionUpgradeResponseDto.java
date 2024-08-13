package com.oasys.helpdesk.response;

import java.util.Date;

import lombok.Data;

@Data
public class VersionUpgradeResponseDto {
	private Long id;

	Integer version;

	String versionName;

	String location;

	Date releaseDate;

	String applicationType;

	String channel;
	
	String createdBy;
	
	String modifiedBy;
}

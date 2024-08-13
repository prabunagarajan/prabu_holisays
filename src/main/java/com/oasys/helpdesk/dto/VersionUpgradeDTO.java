package com.oasys.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class VersionUpgradeDTO {
	private Long id;

	Integer version;

	String versionName;

	String location;

	Date releaseDate;

	String applicationType;

	String channel;
}

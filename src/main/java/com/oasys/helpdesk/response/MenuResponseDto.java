package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MenuResponseDto implements Serializable{

	private Long id;
	
	private String code;
	
	private String name;
	
	private Long displayOrder;
	
	private String icon;
	
	@JsonProperty("url")
	private String routeUrl;
	
	private String type;
	
	@JsonProperty("children")
	private List<MenuResponseDto> childrenList = new ArrayList<MenuResponseDto>();
	
	@JsonProperty("pid")
	private Long parentModuleId;
	
	private Boolean active;
	
	private Boolean hasChild;
	
	private Boolean isChecked;
	
	
	
}

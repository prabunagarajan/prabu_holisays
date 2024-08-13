package com.oasys.helpdesk.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TollfreeDashboardDTO {

	int totalOpreatingHrs;
	int totalDownTimeHrs;
	int totalUptimeHrs;
	float uptimePercentage;

}

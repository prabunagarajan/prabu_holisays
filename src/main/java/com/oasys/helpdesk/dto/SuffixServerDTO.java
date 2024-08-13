package com.oasys.helpdesk.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SuffixServerDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private long serviceIds;
	private int sli;
	private int downTime;
	private long excludedDowntimes;
	private long errorBudget;
	private int upTime;
	private String periodFrom;
	private String periodTo;

}

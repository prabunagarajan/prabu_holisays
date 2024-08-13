/**
 * 
 */
package com.oasys.helpdesk.response;



import com.oasys.helpdesk.conf.Trackable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetails  {
	private String email;
	
	private String mobileNumber;
	
	private String name;

	private String licenceNumber;
}

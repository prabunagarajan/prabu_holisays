package com.oasys.helpdesk.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ContentPojo
{
		private String content;
		private List<String> attachments;
		public ContentPojo()
		{
			attachments=new LinkedList<String>();
		}
}

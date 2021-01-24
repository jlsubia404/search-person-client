package com.devoxs.searchperson.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactInfo {

	private String kind;
	
	private List<ContactValue> values;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public List<ContactValue> getValues() {
		return values;
	}

	public void setValues(List<ContactValue> values) {
		this.values = values;
	}
	
	
}

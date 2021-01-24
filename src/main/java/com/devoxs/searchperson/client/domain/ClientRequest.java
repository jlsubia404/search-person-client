package com.devoxs.searchperson.client.domain;

public class ClientRequest {

	private String documentId;
	
	private Integer apiVersion;

	
	public ClientRequest(String documentId, Integer apiVersion) {
		super();
		this.documentId = documentId;
		this.apiVersion = apiVersion;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public Integer getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}

	
}

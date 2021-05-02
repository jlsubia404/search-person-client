package com.devoxs.searchperson.client.domain;

public class Enterprise {
	
	 private String documentId;

	 private String  name ;

	 private String businessName;

	 private Boolean carryingAccount;

	 private String taxPayerKind;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Boolean getCarryingAccount() {
		return carryingAccount;
	}

	public void setCarryingAccount(Boolean carryingAccount) {
		this.carryingAccount = carryingAccount;
	}

	public String getTaxPayerKind() {
		return taxPayerKind;
	}

	public void setTaxPayerKind(String taxPayerKind) {
		this.taxPayerKind = taxPayerKind;
	}
	 
	 
	 
}

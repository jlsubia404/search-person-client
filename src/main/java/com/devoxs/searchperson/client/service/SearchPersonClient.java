package com.devoxs.searchperson.client.service;

import com.devoxs.searchperson.client.domain.Enterprise;
import com.devoxs.searchperson.client.domain.Person;

public interface SearchPersonClient {

	Person findPerson(String documentId);
	
	Enterprise finEnterprise(String documentId);
}

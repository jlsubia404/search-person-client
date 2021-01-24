package com.devoxs.searchperson.client.service;

import com.devoxs.searchperson.client.domain.Person;

public interface SearchPersonClient {

	Person findPerson(String documentId);
	
}

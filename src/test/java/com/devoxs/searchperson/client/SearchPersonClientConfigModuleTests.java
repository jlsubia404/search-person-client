package com.devoxs.searchperson.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.devoxs.searchperson.client.domain.Enterprise;
import com.devoxs.searchperson.client.domain.Person;
import com.devoxs.searchperson.client.service.SearchPersonClient;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchPersonClientConfigModule.class)
@TestPropertySource(locations="classpath:test.properties")
class SearchPersonClientConfigModuleTests {

	@Autowired
    private SearchPersonClient searchPersonClient;
	

	@Test
	void testPersonClient() {
		
		 Person result = searchPersonClient.findPerson("1234567890");
		 assertThat(result).isNotNull();
	}
	
	
	@Test
	void testEnterpriseClient() {
		
		 Enterprise result = searchPersonClient.finEnterprise("1234567890001");
		 assertThat(result).isNotNull();
	}
	
	
}

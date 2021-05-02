package com.devoxs.searchperson.client.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devoxs.searchperson.client.common.ClientConfig;
import com.devoxs.searchperson.client.common.Constants;
import com.devoxs.searchperson.client.domain.ClientRequest;
import com.devoxs.searchperson.client.domain.Enterprise;
import com.devoxs.searchperson.client.domain.Person;
import com.devoxs.searchperson.client.service.SearchPersonClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchPersonClientImpl implements SearchPersonClient {

	private static final Logger LOG = LoggerFactory.getLogger("SearchPersonClient");

	@Autowired
	private ClientConfig clientConfig;

	private CloseableHttpClient httpClient;

	@PostConstruct
	public void init() {
		initHttpClient();
	}

	@Override
	public Person findPerson(String documentId) {
		LOG.info("Dato a consulta externo findPerson {}", documentId);

		Person respuesta = null;
		HttpPost post = null;
		try{

			post = buildPostRequest(documentId, true);

			if (Boolean.TRUE.equals(clientConfig.getUseDummy())) {
				return (Person) getDummyData(true);
			}

			CloseableHttpResponse response = httpClient.execute(post);
			LOG.info("Codigo de respuesta {}", response.getStatusLine().getStatusCode());
			if (Constants.RESPONSE_CODE_OK == response.getStatusLine().getStatusCode()) {

				String respuestaStr = EntityUtils.toString(response.getEntity());
				if (LOG.isInfoEnabled()) {

					LOG.info("Response: {}", respuestaStr);
				}

				respuesta = (new ObjectMapper()).readValue(respuestaStr, Person.class);

			}

		} catch (IOException e) {
			LOG.error("Error de io consulta externo ", e);
		} catch (Exception e) {
			LOG.error("Error general consulta externo ", e);
		}finally {
			if(post != null) {				
				post.releaseConnection();
			}
		}
		
		return respuesta;
	}

	@Override
	public Enterprise finEnterprise(String documentId) {
		LOG.info("Dato a consulta externo finEnterprise {}", documentId);
		Enterprise respuesta = null;
		HttpPost post = null;
		try{

			post = buildPostRequest(documentId, false);

			if (Boolean.TRUE.equals(clientConfig.getUseDummy())) {
				return (Enterprise) getDummyData(false);
			}

			CloseableHttpResponse response = httpClient.execute(post);
			LOG.info("Codigo de respuesta {}", response.getStatusLine().getStatusCode());
			if (Constants.RESPONSE_CODE_OK == response.getStatusLine().getStatusCode()) {

				String respuestaStr = EntityUtils.toString(response.getEntity());
				if (LOG.isInfoEnabled()) {

					LOG.info("Response: {}", respuestaStr);
				}

				respuesta = (new ObjectMapper()).readValue(respuestaStr, Enterprise.class);

			}

		} catch (IOException e) {
			LOG.error("Error de io consulta externo ", e);
		} catch (Exception e) {
			LOG.error("Error general consulta externo ", e);
		}finally {
			if(post != null) {				
				post.releaseConnection();
			}
		}
		
		return respuesta;

	}
	
	private Object getDummyData(boolean isPerson) {
		String resourceName = isPerson? "dummy_response.txt": "dummy_response_ent.txt";
		URL resource = getClass().getClassLoader().getResource(resourceName);
		File dummyResponse;
		Scanner myReader = null;
		try {
			dummyResponse = new File(resource.toURI());

			StringBuilder strBUild = new StringBuilder();
			myReader = new Scanner(dummyResponse);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				strBUild.append(data.replace("\n", ""));
			}
			if(isPerson) {
				
				return (new ObjectMapper()).readValue(strBUild.toString(), Person.class);
			} else {
				return (new ObjectMapper()).readValue(strBUild.toString(), Enterprise.class);
			}

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			
			return null;
		} finally {
			if(myReader != null) {
				myReader.close();
			}
		}

	}

	private void initHttpClient() {

		SSLContext sslContext;
		try {

			sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			httpClient = HttpClients.custom().setSSLContext(sslContext).build();

		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOG.error("Error al configurar cliente de search person,", e);
		}
	}


	private HttpPost buildPostRequest(String documentId, boolean isPerson) throws UnsupportedCharsetException, JsonProcessingException {
		HttpPost post = null;
		post = new HttpPost(isPerson ? clientConfig.getUrlApi() : clientConfig.getUrlApiEnterprise());

		post.setEntity(new StringEntity((new ObjectMapper()).writeValueAsString(new ClientRequest(documentId, clientConfig.getApiVersion())),
				ContentType.APPLICATION_JSON));

		post.setHeader(Constants.DEVOXS_KEY_HEADER, clientConfig.getAccessKey());

		RequestConfig.Builder requestConfig = RequestConfig.custom();

		Integer timoeutMillis = clientConfig.getTimeoutSeconds() * 1000;
		
		requestConfig.setConnectTimeout(timoeutMillis).setConnectionRequestTimeout(timoeutMillis)
				.setSocketTimeout(timoeutMillis);

		post.setConfig(requestConfig.build());

		return post;
	}

}

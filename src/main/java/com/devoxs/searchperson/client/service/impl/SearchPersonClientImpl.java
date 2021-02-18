package com.devoxs.searchperson.client.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
import com.devoxs.searchperson.client.domain.ClientRequest;
import com.devoxs.searchperson.client.domain.Person;
import com.devoxs.searchperson.client.service.SearchPersonClient;
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
		LOG.info("Dato a consulta externo {}", documentId);

		Person respuesta = null;
		HttpPost post = null;
		try{

			post = new HttpPost(clientConfig.getUrlApi());

			post.setEntity(new StringEntity((new ObjectMapper()).writeValueAsString(new ClientRequest(documentId, clientConfig.getApiVersion())),
					ContentType.APPLICATION_JSON));

			post.setHeader("X-DVXS-KEY", clientConfig.getAccessKey());

			RequestConfig.Builder requestConfig = RequestConfig.custom();

			requestConfig.setConnectTimeout(30 * 1000).setConnectionRequestTimeout(30 * 1000)
					.setSocketTimeout(30 * 1000);

			post.setConfig(requestConfig.build());

			if (Boolean.TRUE.equals(clientConfig.getUseDummy())) {
				return getDummyData();
			}

			CloseableHttpResponse response = httpClient.execute(post);
			LOG.info("Codigo de respuesta {}", response.getStatusLine().getStatusCode());
			if (201 == response.getStatusLine().getStatusCode()) {

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

	private Person getDummyData() {
		URL resource = getClass().getClassLoader().getResource("dummy_response.txt");
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

			return (new ObjectMapper()).readValue(strBUild.toString(), Person.class);

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

}

package com.devoxs.searchperson.client.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientConfig {

	@Value("${search.api.url}")
	private String urlApi;
	
	@Value("${search.api.accessKey}")
	private String accessKey;
	
	@Value("${search.api.use.dummy}")
	private Boolean useDummy;

	@Value("${search.api.apiVersion}")
	private Integer apiVersion;
	
	@Value("${search.api.timeout}")
	private Integer timeoutSeconds;
	
	@Value("${search.api.url.ent}")
	private String urlApiEnterprise;

	public String getUrlApi() {
		return urlApi;
	}

	public void setUrlApi(String urlApi) {
		this.urlApi = urlApi;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Boolean getUseDummy() {
		return useDummy;
	}

	public void setUseDummy(Boolean useDummy) {
		this.useDummy = useDummy;
	}

	public Integer getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}

	public Integer getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setTimeoutSeconds(Integer timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	public String getUrlApiEnterprise() {
		return urlApiEnterprise;
	}

	public void setUrlApiEnterprise(String urlApiEnterprise) {
		this.urlApiEnterprise = urlApiEnterprise;
	}

}

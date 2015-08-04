package com.emc.isilon.ran;

import java.net.URI;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public class Client {

	// TODO:  Extract configuration
	private final String BASE = "https://172.16.57.101:8080/namespace/ifs/rest_user";
	private final String USER = "rest_user";
	private final String PASS = "Password123!";
	private UriBuilder uriBuilder;
	private javax.ws.rs.client.Client jerseyClient;

	public Client() {
		
		uriBuilder = UriBuilder.fromPath(BASE).path("{location}");

		HttpAuthenticationFeature authFeature = HttpAuthenticationFeature
				.basicBuilder().nonPreemptive().credentials(USER, PASS).build();

		/**
		 * Disable host name verification.  Should be able to configure the Isilon
		 * certificate with the correct host name.
		 **/
		HostnameVerifier hostnameVerifier = getHostnameVerifier();
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		jerseyClient = JerseyClientBuilder.newBuilder()
				.register(JacksonFeature.class)
				.register(authFeature)
				.hostnameVerifier(hostnameVerifier)
				.build();
	}

	public DirectoryListing getDirectoryListing(String dir) {
		URI uri = uriBuilder.build(dir);
		Response response = jerseyClient.target(uri).request()
				.accept("application/json").get();
		return response.readEntity(DirectoryListing.class);
	}
	
	public EntryAccessControl getAccessControlList(String entry) {
		URI uri = uriBuilder.queryParam("acl", true).build(entry);
		Response response = jerseyClient.target(uri).request().accept("applciation/json").get();
		return response.readEntity(EntryAccessControl.class);
	}
	
	public void setAccessControlList(EntryAccessControl acl, String entry) {
		URI uri = uriBuilder.queryParam("acl", true)
							.queryParam("access", true)
							.build(entry);
		jerseyClient.target(uri).request().put(Entity.json(acl));
	}
	
	public void deleteDirectory(String dir) {
		deleteDirectory(dir, false);
	}

	public void deleteDirectory(String dir, Boolean recursive) {
		URI uri = uriBuilder.queryParam("recursive", recursive).build(dir);
		jerseyClient.target(uri).request().delete();
	}

	public void createDirectory(String dir) {
		URI uri = uriBuilder.build(dir);
		jerseyClient.target(uri).request()
			.header("x-isi-ifs-target-type", "container")
			.put(Entity.json(""));
	}

	private HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

}

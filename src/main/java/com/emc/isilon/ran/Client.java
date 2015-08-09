package com.emc.isilon.ran;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public class Client {

	// TODO: Extract configuration
	private final String BASE = "https://172.16.57.101:8080/namespace/ifs/rest_user";
	private final String USER = "rest_user";
	private final String PASS = "Password123!";
	private javax.ws.rs.client.Client jerseyClient;

	public Client() {

		HttpAuthenticationFeature authFeature = HttpAuthenticationFeature
				.basicBuilder().nonPreemptive().credentials(USER, PASS).build();

		/**
		 * Disable host name verification. Should be able to configure the
		 * Isilon certificate with the correct host name.
		 **/
		HostnameVerifier hostnameVerifier = getHostnameVerifier();
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		jerseyClient = JerseyClientBuilder.newBuilder()
				.register(JacksonFeature.class).register(authFeature)
				.hostnameVerifier(hostnameVerifier).build();
	}

	public DirectoryListing getDirectoryListing(String relativePath)
			throws ClientException {
		URI uri = createUriBuilder(relativePath).build();
		Response response = jerseyClient.target(uri).request()
				.accept("application/json").get();
		handleResponse(response);
		return response.readEntity(DirectoryListing.class);
	}

	public EntryAccessControl getAccessControlList(String relativePath)
			throws ClientException {
		URI uri = createUriBuilder(relativePath).queryParam("acl", true)
				.build();
		Response response = jerseyClient.target(uri).request()
				.accept("applciation/json").get();
		handleResponse(response);
		return response.readEntity(EntryAccessControl.class);
	}

	public void setAccessControlList(EntryAccessControl acl, String relativePath)
			throws ClientException {
		URI uri = createUriBuilder(relativePath).queryParam("acl", true)
				.queryParam("access", true).build();
		Response response = jerseyClient.target(uri).request()
				.put(Entity.json(acl));
		handleResponse(response);
	}

	public void deleteDirectory(String relativePath) throws ClientException {
		deleteDirectory(relativePath, false);
	}

	public void deleteDirectory(String relativePath, Boolean recursive)
			throws ClientException {
		URI uri = createUriBuilder(relativePath).queryParam("recursive",
				recursive).build();
		Response response = jerseyClient.target(uri).request().delete();
		handleResponse(response);
	}

	public void createDirectory(String relativePath) throws ClientException {
		URI uri = createUriBuilder(relativePath).build();
		Response response = jerseyClient.target(uri).request()
				.header("x-isi-ifs-target-type", "container")
				.put(Entity.json(""));
		handleResponse(response);
	}

	public void deleteFile(String relativePath) throws ClientException {
		URI uri = createUriBuilder(relativePath).build();
		Response response = jerseyClient.target(uri).request().delete();
		handleResponse(response);
	}

	public void createFile(String relativePath, String localFilePath,
			MediaType mediaType) throws ClientException {
		createFile(relativePath, localFilePath, mediaType, false);
	}

	public void createFile(String relativePath, String localFilePath,
			MediaType mediaType, Boolean overwrite) throws ClientException {
		URI uri = createUriBuilder(relativePath).build();
		File file = new File(localFilePath);
		Response response = jerseyClient.target(uri)
				.queryParam("overwrite", overwrite).request()
				.header("x-isi-ifs-target-type", "object")
				.put(Entity.entity(file, mediaType));
		handleResponse(response);
	}

	public InputStream readFile(String relativePath, MediaType mediaType)
			throws ClientException {
		URI uri = createUriBuilder(relativePath).build();
		Response response = jerseyClient.target(uri)
				.request(new MediaType[] { mediaType }).get();
		handleResponse(response);
		return response.readEntity(InputStream.class);
	}

	private HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

	private UriBuilder createUriBuilder(String relativePath) {
		UriBuilder uriBuilder = UriBuilder.fromPath(BASE);
		for (String segment : relativePath.split("/")) {
			uriBuilder.segment(segment);
		}
		return uriBuilder;
	}

	private void handleResponse(Response response) throws ClientException {
		if (response.getStatus() > 399) {
			throw new ClientException(response.getStatusInfo().toString()
					+ ": " + response.readEntity(ErrorList.class));
		}
	}
}

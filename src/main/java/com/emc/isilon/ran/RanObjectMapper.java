package com.emc.isilon.ran;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class RanObjectMapper implements ContextResolver<ObjectMapper>{
	
	final ObjectMapper defaultObjectMapper;

	public RanObjectMapper() {
		defaultObjectMapper = createDefaultMapper();
	}

	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return defaultObjectMapper;
	}

	private ObjectMapper createDefaultMapper() {
		final ObjectMapper result = new ObjectMapper();
		return result;
	}

}

package com.emc.isilon.ran;

import java.util.Iterator;
import java.util.List;

public class ErrorList {
	List<Error> errors;

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public String toString() {
		String errorString = "";
		for (Iterator<Error> iterator = errors.iterator(); iterator.hasNext();) {
			Error error = (Error) iterator.next();
			errorString += error.toString();
			if (!iterator.hasNext()) errorString += " ";
		}
		return errorString;
	}
}

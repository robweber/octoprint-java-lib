package org.octoprint.api.exceptions;

/**
 * Created by Hendrik on 30.09.2017.
 */
public class InvalidApiKeyException extends OctoPrintAPIException {

	public static final int STATUS_CODE = 401;

	public InvalidApiKeyException(final String message) {
		super(STATUS_CODE, message);
	}

}

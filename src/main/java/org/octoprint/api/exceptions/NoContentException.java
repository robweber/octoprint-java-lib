package org.octoprint.api.exceptions;

/**
 * Created by Hendrik on 30.09.2017.
 */
public class NoContentException extends OctoPrintAPIException {

	public static final int STATUS_CODE = 204;

	public NoContentException(final String message) {
		super(STATUS_CODE, message);
	}

}

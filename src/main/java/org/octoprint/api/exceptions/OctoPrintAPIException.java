package org.octoprint.api.exceptions;

/**
 * Created by Hendrik on 30.09.2017.
 */
public class OctoPrintAPIException extends RuntimeException {

	private final int statusCode;

	public OctoPrintAPIException(final String message, final Throwable cause) {
		super(message, cause);
		this.statusCode = 0;
	}

	public OctoPrintAPIException(final int statusCode, final String message, final Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public OctoPrintAPIException(final int statusCode, final String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public final int getStatusCode() {
		return statusCode;
	}
}

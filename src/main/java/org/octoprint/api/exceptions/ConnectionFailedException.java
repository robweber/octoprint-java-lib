package org.octoprint.api.exceptions;

import java.io.IOException;

/**
 * Created by Hendrik on 30.09.2017.
 */
public class ConnectionFailedException extends OctoPrintAPIException {

	public ConnectionFailedException(final IOException cause) {
		super("Couldn't connect to server!", cause);
	}
}

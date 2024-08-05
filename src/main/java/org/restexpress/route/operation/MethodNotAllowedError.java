package org.restexpress.route.operation;

public class MethodNotAllowedError
extends RuntimeException
{
	private static final long serialVersionUID = 2777031033514754613L;

	public MethodNotAllowedError() {
		super();
	}

	public MethodNotAllowedError(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MethodNotAllowedError(String message, Throwable cause) {
		super(message, cause);
	}

	public MethodNotAllowedError(String message) {
		super(message);
	}

	public MethodNotAllowedError(Throwable cause) {
		super(cause);
	}
}

package org.restexpress.route.operation;

public class NotFoundError
extends RuntimeException
{
	private static final long serialVersionUID = -1368935140641618662L;

	public NotFoundError() {
		super();
	}

	public NotFoundError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundError(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundError(String message) {
		super(message);
	}

	public NotFoundError(Throwable cause) {
		super(cause);
	}
}

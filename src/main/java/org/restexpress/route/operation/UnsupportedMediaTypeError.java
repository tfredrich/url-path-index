package org.restexpress.route.operation;

public class UnsupportedMediaTypeError
extends RuntimeException
{
	private static final long serialVersionUID = 8641805079042807701L;

	public UnsupportedMediaTypeError() {
		super();
	}

	public UnsupportedMediaTypeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportedMediaTypeError(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedMediaTypeError(String message) {
		super(message);
	}

	public UnsupportedMediaTypeError(Throwable cause) {
		super(cause);
	}
}

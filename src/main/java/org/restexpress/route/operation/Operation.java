package org.restexpress.route.operation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operation
{
	/**
	 * The URL that is to be called on the origin server (e.g. if this is a proxy).
	 * May be parameterized using curly braces (e.g. '{userId}').
     */
	private String originUrl;

	/**
	 * The methods (and media types) supported by this operation.
	 */
	private Map<HttpMethod, OperationMethod> methods;

	public Operation() {
		super();
	}

	public Operation(String originUrl) {
		this();
		setOriginUrl(originUrl);
	}

	public Operation(String originUrl, HttpMethod method, List<String> mediaTypes) {
		this(originUrl);
		addMethod(method, mediaTypes);
	}

	public Operation(Operation that)
	{
		this.originUrl = that.originUrl;
		this.methods = (that.methods != null ? new HashMap<>(that.methods) : null);
	}

	public String getOriginUrl() {
		return originUrl;
	}

	public Operation setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
		return this;
	}

	public Operation addMethod(HttpMethod method, List<String> mediaTypes) {
		if (methods == null) {
			this.methods = new EnumMap<>(HttpMethod.class);
            this.methods.put(method, new OperationMethod(method, mediaTypes));
		}
		else {
			OperationMethod operationMethod = methods.get(method);
			if (operationMethod == null) {
				operationMethod = new OperationMethod(method, mediaTypes);
				methods.put(method, operationMethod);
			} else {
				operationMethod.addMediaTypes(mediaTypes);
			}
		}

        return this;
	}

	public boolean hasMethod(HttpMethod method) {
		return (methods != null && methods.containsKey(method));
	}

	public OperationMethod getMethod(HttpMethod method) {
		return (methods != null ? methods.get(method) : null);
	}

	public boolean hasMediaType(HttpMethod method, String mediaType) {
		return methods.get(method).hasMediaType(mediaType);
	}

	public List<HttpMethod> getMethods() {
		return (methods != null ? List.copyOf(methods.keySet()) : List.of());
	}

	public List<String> getMediaTypes(HttpMethod method) {
		return (hasMethod(method) ? methods.get(method).getMediaTypes() : List.of());
	}

	public String toString() {
		return "originUrl=" + originUrl + ", methods=" + methods;
	}

	public String formatOriginUrl(Map<String, String> identifiers) {
		String formatted = originUrl;

		if (formatted == null)
		{
			return "";
		}

        if (identifiers != null && !identifiers.isEmpty())
        {
            for (Map.Entry<String, String> entry : identifiers.entrySet())
            {
                formatted = formatted.replace(entry.getKey(), entry.getValue());
            }
        }

		return formatted;
	}
}

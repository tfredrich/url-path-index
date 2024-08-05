package org.restexpress.route.operation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OperationMethod {
	private HttpMethod method;
	private Set<String> mediaTypes;

	public OperationMethod(HttpMethod method, List<String> mediaTypes) {
		super();
		this.method = method;
		addMediaTypes(mediaTypes);
	}

	public boolean hasMediaType(String mediaType) {
		return mediaTypes.contains(mediaType);
	}

	public void addMediaTypes(List<String> mediaTypes) {
		if (this.mediaTypes == null) {
			this.mediaTypes = new HashSet<>();
		}

		this.mediaTypes.addAll(mediaTypes);
	}

	public List<String> getMediaTypes() {
		return (mediaTypes != null ? List.copyOf(mediaTypes) : List.of());
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String toString() {
		return method + ":" + mediaTypes;
	}
}
package org.restexpress.route.operation;

import org.restexpress.route.PathIndex;
import org.restexpress.route.SearchResults;

public class OperationIndex
{
	private PathIndex<Operation> pathIndex = new PathIndex<>();

	public OperationIndex()
	{
		super();
	}

	public void insert(String path, Operation operation)
	{
		pathIndex.insert(path, operation);
	}

	public SearchResults<Operation> search(String path, HttpMethod method, String mediaType)
	{
		SearchResults<Operation> r = pathIndex.search(path);

		if (r == null || !r.hasObject())
		{
			throw new NotFoundError("Path not found (404 not found): " + path);
		}

		Operation operation = r.getObject();

		if (!operation.hasMethod(method))
		{
			throw new MethodNotAllowedError("Valid methods are: " + operation.getMethods());
		}

		if (mediaType != null && !operation.hasMediaType(method, mediaType))
		{
			throw new UnsupportedMediaTypeError("Valid media types are: " + operation.getMediaTypes(method));
		}

		operation.setOriginUrl(operation.formatOriginUrl(r.getIdentifiers()));
		return r;
	}
}

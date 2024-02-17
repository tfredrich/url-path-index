package org.restexpress.route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

class SearchResults<T>
{
	private String requestedPath;
	private List<String> segments;
	private boolean success = true;
	private Map<String, String> identifiers;
	private T object;

	public SearchResults(String path)
	{
		super();
		this.requestedPath = path;
	}

	public void setFailed()
	{
		this.success = false;
	}

	public boolean matched()
	{
		return success;
	}

	public void addSegment(String segment)
	{
		if (segments == null)
		{
			segments = new ArrayList<>();
		}

		segments.add(segment);
	}

	public void addIdentifier(String parameterName, String segment)
	{
		addSegment(parameterName);

		if (identifiers == null)
		{
			identifiers = new HashMap<>();
		}

		identifiers.put(parameterName, segment);
	}

	public String getIdentifier(String parameterName)
	{
		return (hasIdentifiers() ? identifiers.get(parameterName) : null);
	}

	public boolean hasIdentifiers()
	{
		return (identifiers != null);
	}

	public T getObject()
	{
		return object;
	}

	public boolean hasIndexedObject()
	{
		return (object != null);
	}

	public void setObject(T object)
	{
		this.object = object;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(requestedPath);
		sb.append(": ");

		if (matched())
		{
			StringJoiner joiner = new StringJoiner("/", "/", "");
			segments.forEach(joiner::add);
			sb.append(joiner.toString());

			if (hasIdentifiers())
			{
				sb.append(" : ");
				sb.append(identifiers.toString());
			}

			if (hasIndexedObject())
			{
				sb.append(" (");
				sb.append(getObject().toString());
				sb.append(")");
			}
			return sb.toString();
		}

		sb.append("*** FAILED ***");
		return sb.toString();
	}
}

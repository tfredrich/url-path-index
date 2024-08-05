package org.restexpress.route;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SearchResults<T>
{
	// The path that was requested.
	private String path;

	// The segments that were extracted from the requested path
	private List<String> segments;

	// Whether the path matched or not.
	private boolean success = true;

	// The identifiers that were extracted from the requested path.
	private Map<String, String> identifiers;

	// The object that was indexed at the end of the path.
	private T object;

	public SearchResults(String path)
	{
		super();
		this.path = path;
	}

	public SearchResults(String path, List<String> segments, Map<String, String> identifiers, T object, boolean success)
	{
		super();
		this.path = path;

		if (segments != null) segments.stream().forEach(this::addSegment);
		if (identifiers != null) identifiers.entrySet().stream().forEach(e -> addIdentifier(e.getKey(), e.getValue()));
		setObject(object);
		this.success = success;
	}

	protected void setFailed()
	{
		this.success = false;
	}

	public boolean matched()
	{
		return success;
	}

	protected void addSegment(String segment)
	{
		if (segments == null)
		{
			segments = new ArrayList<>();
		}

		segments.add(segment);
	}

	protected void addIdentifier(String parameterName, String segment)
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

	public Map<String, String> getIdentifiers()
    {
        return (hasIdentifiers() ? Collections.unmodifiableMap(identifiers) : Collections.emptyMap());
    }

	public T getObject()
	{
		return object;
	}

	public boolean hasObject()
	{
		return (object != null);
	}

	public String getPath()
	{
		return path;
	}

	protected void setObject(T object)
	{
		this.object = object;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(path);
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

			if (hasObject())
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

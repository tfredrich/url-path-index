package org.restexpress.route;

/**
 * SlashSegment is a PathSegment that represents a slash ('/') in a URL pattern.
 * 
 * @param <T> the type of object indexed by this segment. It will be the same as the type of the parent index.
 */
class SlashSegment<T>
extends PathSegment<T>
{
	public static final String SLASH = "/";

	SlashSegment()
	{
		super(SLASH);
	}

	@Override
	public boolean isSlashNode()
	{
		return true;
	}
}

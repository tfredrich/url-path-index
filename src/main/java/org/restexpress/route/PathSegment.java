package org.restexpress.route;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PathSegment is a node in a tree structure representing a segment in the URI path. It can have
 * children nodes, and it can be a leaf node, in which case it holds an object that is indexed by
 * the path name.
 * 
 * @param <T> the type of object that is indexed/stored in this segment of the path.
 */
class PathSegment<T>
{
	private String segment;
	private Map<String, PathSegment<T>> childrenBySegment = new ConcurrentHashMap<>();
	private boolean isLeaf;

	// Populated on leaf nodes, this is the object being indexed by path name.
	private T indexedObject;

	public PathSegment(String segment)
	{
		this.segment = segment;
	}

	public PathSegment<T> addChild(PathSegment<T> child)
	{
		this.childrenBySegment.put(child.segment, child);
		return child;
	}

	public PathSegment<T> getChild(String segment)
	{
		return childrenBySegment.get(segment);
	}

	public boolean isLeaf()
	{
		return isLeaf;
	}

	public void setIsLeaf(boolean value)
	{
		this.isLeaf = value;
	}

	public boolean isIdentifierNode()
	{
		return false;
	}

	public boolean isSlashNode()
	{
		return false;
	}

	public void setIndexedObject(T o)
	{
		this.indexedObject = o;
	}

	public T getIndexedObject()
	{
		return indexedObject;
	}

	public boolean hasIndexedObject()
	{
		return (indexedObject != null);
	}

	@Override
	public String toString()
	{
		return String.valueOf(segment) + " (" + (isLeaf ? "leaf_node" : "internal_node") + ")";
	}
}

package org.restexpress.route;

import static org.restexpress.route.SlashSegment.SLASH;

import java.util.Arrays;

public class PathIndex<T>
{
	private PathSegment<T> root;

	public PathIndex()
	{
		super();
		this.root = new SlashSegment<>();
	}

	public void insert(String path, T object)
	{
		String[] pathSegments = asPathSegments(path);
		PathSegment<T> currentNode = root;

		for (String segment : pathSegments)
		{
			// This accounts for a leading slash ('/') on the path that creates an empty segment in the array.
			if (segment.isBlank()) continue;

			currentNode = getOrCreateSlashNode(currentNode);

			if (isIdentifier(segment))
			{
				currentNode = insertIdentifierSegment(currentNode, segment);
			}
			else
			{
				currentNode = insertPathSegment(currentNode, segment);
			}
		}

		currentNode.setIsLeaf(true);
		currentNode.setIndexedObject(object);
	}

	public SearchResults<T> search(String path)
	{
		String[] pathSegments = asPathSegments(path);
		PathSegment<T> currentNode = root;
		SearchResults<T> results = new SearchResults<>(path);

		for (String segment : Arrays.asList(pathSegments))
		{
			// This accounts for a leading slash ('/') on the path that creates an empty segment in the array.
			if (segment.isBlank()) continue;

			currentNode = traverse(currentNode, segment);

			if (currentNode != null)
			{
				if (currentNode.isIdentifierNode())
				{
					results.addIdentifier(((IdentifierSegment<T>) currentNode).getParameterName(), segment);
				}
				else
				{
					results.addSegment(segment);
				}
			}
			else
			{
				results.setFailed();
				return results;
			}
		}

		if (currentNode == null || !currentNode.isLeaf())
		{
			results.setFailed();
		}
		else if (currentNode.hasIndexedObject())
		{
			results.setObject(currentNode.getIndexedObject());
		}

		return results;
	}

	private PathSegment<T> traverse(PathSegment<T> node, String pathSegment)
	{
		if (node == null || pathSegment == null) return null;

		// If the current node is a slash node, move to its child node
		if (!node.isSlashNode())
	    {
	    	node = node.getChild(SLASH);
	    	if (node == null) return null;
	    }

		 // Find the child node that matches the segment
	    PathSegment<T> childNode = node.getChild(pathSegment);


	    // If there is not a segment match attempt to find an identifier segment
	    return childNode != null ? childNode : node.getChild(IdentifierSegment.WILDCARD);
	}

	private String[] asPathSegments(String path)
	{
		return path.split(SLASH);
	}

	private boolean isIdentifier(String segment)
	{
		return IdentifierSegment.isIdentifier(segment);
	}

	private PathSegment<T> insertIdentifierSegment(PathSegment<T> currentNode, String pathSegment)
	{
		IdentifierSegment<T> childNode = (IdentifierSegment<T>) currentNode.getChild(IdentifierSegment.WILDCARD);

		if (childNode == null)
		{
			childNode = new IdentifierSegment<>(pathSegment);
			currentNode.addChild(childNode);
		}

		return childNode;
	}

	private PathSegment<T> insertPathSegment(PathSegment<T> currentNode, String pathSegment)
	{
		PathSegment<T> childNode = currentNode.getChild(pathSegment);

		if (childNode == null)
		{
			childNode = currentNode.addChild(new PathSegment<>(pathSegment));
		}

		return childNode;
	}

	private PathSegment<T> getOrCreateSlashNode(PathSegment<T> currentNode)
	{
		if (!currentNode.isSlashNode())
		{
			PathSegment<T> slashNode = currentNode.getChild(SLASH);

			if (slashNode != null)
			{
				return slashNode;
			}
			else
			{
				return currentNode.addChild(new SlashSegment<>());
			}
		}

		return currentNode;
	}

	public static void main(String[] args)
	{
		PathIndex<Object> trie = new PathIndex<>();

		// Inserting some sample URLs into the trie
		trie.insert("/fee/fi/fo/fum", "I smell the blood of an Englishman!");
		trie.insert("/users/{userId}/posts", "user-posts");
		trie.insert("/usages/{usageId}/details", "usage-details");
		trie.insert("/products/{productId}/reviews", "product-reviews1");
		trie.insert("/products/{productId}/reviews/", "product-reviews2");
		trie.insert("/products/{productId}/reviews/", "product-reviews3");
		trie.insert("/accounts", "account-collection");
		trie.insert("/accounts/{accountId}", "an-account");
		trie.insert("/accounts/{accountId}/products", "account-products");
		trie.insert("/accounts/{accountId}/products/{productId}", "one-account-product");
		trie.insert("/accounts/{accountId}/products/{productId}/reviews", "account-product-reviews");

		// Searching for and printing matched URLs
		System.out.println("Should Succeed:");
		System.out.println(trie.search("/fee/fi/fo/fum"));
		System.out.println(trie.search("/accounts"));
		System.out.println(trie.search("/accounts/1234"));
		System.out.println(trie.search("/accounts/1234/products"));
		System.out.println(trie.search("/accounts/1234/products/4567"));
		System.out.println(trie.search("/accounts/1234/products/4567/reviews"));
		System.out.println(trie.search("/users/1234/posts"));
		System.out.println(trie.search("/usages/1234/details"));
		System.out.println(trie.search("/products/1234/reviews"));
		System.out.println(trie.search("/products/1234/reviews/"));
		System.out.println();
		System.out.println("Should Fail:");
		System.out.println(trie.search("/ / / "));
		System.out.println(trie.search("/feefifofum"));
		System.out.println(trie.search("/accounts1234"));
		System.out.println(trie.search("/users/1234"));
		System.out.println(trie.search("/usages/1234"));
		System.out.println(trie.search("/usage/1234"));
		System.out.println(trie.search("/accounts/1234/details"));
	}
}

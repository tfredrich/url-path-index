package org.restexpress.route;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PathIndexTest
{
	@Test
	void test()
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
		SearchResults<Object> results = trie.search("/fee/fi/fo/fum");
		assertTrue(results.matched());
		assertEquals("I smell the blood of an Englishman!", results.getObject());
		assertFalse(results.hasIdentifiers());
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
		assertFalse(trie.search("/ / / ").matched());
		System.out.println(trie.search("/ / / "));
		System.out.println(trie.search("/feefifofum"));
		System.out.println(trie.search("/accounts1234"));
		System.out.println(trie.search("/users/1234"));
		System.out.println(trie.search("/usages/1234"));
		System.out.println(trie.search("/usage/1234"));
		System.out.println(trie.search("/accounts/1234/details"));
	}
}

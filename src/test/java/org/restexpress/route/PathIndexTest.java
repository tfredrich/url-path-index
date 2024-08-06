package org.restexpress.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathIndexTest
{
	private PathIndex<String> index = new PathIndex<>();

	@BeforeEach
	void setUp()
	{
		index.insert("/fee/fi/fo/fum", "I smell the blood of an Englishman!");
		index.insert("/users/{userId}/posts", "user-posts");
		index.insert("/usages/{usageId}/details", "usage-details");
		index.insert("/products/{productId}/reviews", "product-reviews1");
		index.insert("/products/{productId}/reviews/", "product-reviews2");
		index.insert("/products/{productId}/reviews/", "product-reviews3");
		index.insert("/accounts", "account-collection");
		index.insert("/accounts/{accountId}", "an-account");
		index.insert("/accounts/{accountId}/products", "account-products");
		index.insert("/accounts/{accountId}/products/{productId}", "one-account-product");
		index.insert("/accounts/{accountId}/products/{productId}/reviews", "account-product-reviews");
	}

	@Test
	void shouldMatch()
	{
		assertSucceeds("/fee/fi/fo/fum", "I smell the blood of an Englishman!", null);
		assertSucceeds("/accounts", "account-collection", null);
		assertSucceeds("/accounts/1234", "an-account", Map.of("{accountId}", "1234"));
        assertSucceeds("/accounts/1234/products", "account-products", Map.of("{accountId}", "1234"));
        assertSucceeds("/accounts/1234/products/4567", "one-account-product", Map.of("{accountId}", "1234", "{productId}", "4567"));
        assertSucceeds("/accounts/1234/products/4567/reviews", "account-product-reviews", Map.of("{accountId}", "1234", "{productId}", "4567"));
        assertSucceeds("/users/1234/posts", "user-posts", Map.of("{userId}", "1234"));
        assertSucceeds("/usages/1234/details", "usage-details", Map.of("{usageId}", "1234"));
        assertSucceeds("/products/1234/reviews", "product-reviews3", Map.of("{productId}", "1234"));
        assertSucceeds("/products/1234/reviews/", "product-reviews3", Map.of("{productId}", "1234"));
	}

	@Test
	void shouldFail()
	{
        assertFails("/ / / ");
		assertFails("/feefifofum");
		assertFails("/accounts1234");
		assertFails("/users/1234");
		assertFails("/usages/1234");
		assertFails("/usage/1234");
		assertFails("/accounts/1234/details");
	}

	private void assertSucceeds(String path, String value, Map<String, String> parameters)
	{
		SearchResults<String> results = index.search(path);
		assertTrue(results.matched());
		assertEquals(value, results.getObject());

		if (parameters != null)
		{
			assertTrue(results.hasIdentifiers());
			assertEquals(parameters, results.getIdentifiers());
		}
		else
		{
			assertFalse(results.hasIdentifiers());
		}
	}

	private void assertFails(String path)
	{
		SearchResults<String> results = index.search(path);
		assertFalse(results.matched());
	}
}

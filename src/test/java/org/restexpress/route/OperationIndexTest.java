package org.restexpress.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.restexpress.route.operation.HttpMethod;
import org.restexpress.route.operation.MethodNotAllowedError;
import org.restexpress.route.operation.NotFoundError;
import org.restexpress.route.operation.Operation;
import org.restexpress.route.operation.OperationIndex;
import org.restexpress.route.operation.UnsupportedMediaTypeError;

class OperationIndexTest
{
	private static final String JSON = "application/json";
	private static final Operation COLLECTION = new Operation()
													.addMethod(HttpMethod.GET, List.of("application/json"))
													.addMethod(HttpMethod.POST, List.of("application/json"));
	private static final Operation ITEM = new Operation()
													.addMethod(HttpMethod.GET, List.of("application/json"))
													.addMethod(HttpMethod.PUT, List.of("application/json"))
													.addMethod(HttpMethod.DELETE, List.of("application/json"));
	@Test
	void test()
	{
		OperationIndex index = new OperationIndex();

		// Inserting some sample URLs into the index.
		index.insert("/fee/fi/fo/fum", new Operation(ITEM).setOriginUrl("https://127.0.0.1/internal/fee/fi/fo/fum"));
		index.insert("/users/{userId}/posts", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/users/{userId}/posts"));
		index.insert("/usages/{usageId}/details", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/usages/{usageId}/details"));
		index.insert("/products/{productId}/reviews", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/products/{productId}/reviews"));
		index.insert("/products/{productId}/reviews/", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/products/{productId}/reviews"));
		index.insert("/products/{productId}/reviews/", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/products/{productId}/reviews"));
		index.insert("/accounts", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/accounts"));
		index.insert("/accounts/{accountId}", new Operation(ITEM).setOriginUrl("https://127.0.0.1/internal/accounts/{accountId}"));
		index.insert("/accounts/{accountId}/products", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/accounts/{accountId}/products"));
		index.insert("/accounts/{accountId}/products/{productId}", new Operation(ITEM).setOriginUrl("https://127.0.0.1/internal/accounts/{accountId}/products/{productId}"));
		index.insert("/accounts/{accountId}/products/{productId}/reviews", new Operation(COLLECTION).setOriginUrl("https://127.0.0.1/internal/accounts/{accountId}/products/{productId}/reviews"));

		// Searching for and printing matched URLs
		assertSucceeds(index, "/fee/fi/fo/fum", HttpMethod.GET, null, false);
		assertSucceeds(index, "/fee/fi/fo/fum", HttpMethod.GET, JSON, false);
		assertSucceeds(index, "/fee/fi/fo/fum", HttpMethod.DELETE, JSON, false);
		assertSucceeds(index, "/accounts", HttpMethod.GET, JSON, false);
		assertSucceeds(index, "/accounts/1234", HttpMethod.PUT, JSON, true);
        assertSucceeds(index, "/accounts/1234/products", HttpMethod.POST, JSON, true);
        assertSucceeds(index, "/accounts/1234/products/4567", HttpMethod.GET, JSON, true);
        assertSucceeds(index, "/accounts/1234/products/4567/reviews", HttpMethod.GET, JSON, true);
        assertSucceeds(index, "/users/1234/posts", HttpMethod.GET, JSON, true);
        assertSucceeds(index, "/usages/1234/details", HttpMethod.GET, JSON, true);
        assertSucceeds(index, "/products/1234/reviews", HttpMethod.GET, JSON, true);
        assertSucceeds(index, "/products/1234/reviews/", HttpMethod.GET, JSON, true);

        // Not Found exceptions
		assertNotFound(index, "/ / / ", HttpMethod.GET, JSON);
		assertNotFound(index, "/feefifofum", HttpMethod.GET, JSON);
		assertNotFound(index, "/accounts1234", HttpMethod.GET, JSON);
		assertNotFound(index, "/users/1234", HttpMethod.GET, JSON);
		assertNotFound(index, "/usages/1234", HttpMethod.GET, JSON);
		assertNotFound(index, "/usage/1234", HttpMethod.GET, JSON);
		assertNotFound(index, "/accounts/1234/details", HttpMethod.GET, JSON);

		// Method Not Allowed exceptions
		assertMethodNotAllowed(index, "/fee/fi/fo/fum", HttpMethod.POST, JSON);
		assertMethodNotAllowed(index, "/accounts/1234", HttpMethod.POST, JSON);
		assertMethodNotAllowed(index, "/accounts/1234/products", HttpMethod.PUT, JSON);

		// Unsupported Media Type exceptions
		assertUnsupportedMediaType(index, "/fee/fi/fo/fum", HttpMethod.GET, "application/xml");
		assertUnsupportedMediaType(index, "/accounts/1234", HttpMethod.GET, "application/xml");
		assertUnsupportedMediaType(index, "/accounts/1234/products", HttpMethod.GET, "application/xml");
	}

	private void assertSucceeds(OperationIndex index, String path, HttpMethod method, String mediaType, boolean hasIdentifiers)
	{
		SearchResults<Operation> results = index.search(path, method, mediaType);
		assertTrue(results.matched());
		Operation operation = results.getObject();
		assertEquals(method, operation.getMethod(method).getMethod());
		assertEquals(normalizePath("https://127.0.0.1/internal" + path), operation.getOriginUrl().toString());
		assertEquals(hasIdentifiers, results.hasIdentifiers());		
	}

	private String normalizePath(String path)
	{
	    if (path != null && path.endsWith("/"))
	    {
	        return path.substring(0, path.length() - 1);
	    }

	    return path;
	}

	private void assertNotFound(OperationIndex index, String path, HttpMethod method, String mediaType)
	{
		try {
			index.search(path, method, mediaType);
		} catch (NotFoundError e) {
			return;
		}

		throw new AssertionError("Expected NotFoundError.");
	}

	private void assertMethodNotAllowed(OperationIndex index, String path, HttpMethod method, String mediaType)
	{
		try {
			index.search(path, method, mediaType);
		} catch (MethodNotAllowedError e) {
			return;
		}

		throw new AssertionError("Expected MethodNotAllowedError.");
	}

	private void assertUnsupportedMediaType(OperationIndex index, String path, HttpMethod method, String mediaType)
	{
		try {
			index.search(path, method, mediaType);
		} catch (UnsupportedMediaTypeError e) {
			return;
		}

		throw new AssertionError("Expected UnsupportedMediaTypeError.");
	}
}

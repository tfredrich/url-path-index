# url-path-index
An in-memory prefix index for URL segments with nanosecond response times for 1000s of URLs. This builds a semi-static prefix index of URL segments, including identifier segments, to process an incoming URL to parse the segments and identifiers from it and return an indexed object (such as routing metadata for the HTTP methods).

A successful search returns the individual URL segments and identifiers along with the indexed object. Failure is indicated by `SearchResults.matched()` returning false.

Currently, on my M1 Mac Mini, performance benchmarks show that searches take less than 300 **nano**seconds when conducting 100 million searches across approximately 1000 URLs.

## Usage

This simplistic example indexes string objects in the URL pattern, but likely you'll need to index something more complex.

```Java
PathIndex<String> index = new PathIndex<>();

// Index some URL patterns...
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
```

To search the index:

```Java
SearchResults<String> results = index.search("/accounts/1234/products/4567/reviews");

if (results.matched()) {
	String accountId = results.getIdentifier("accountId");	// "1234"
	String productId = results.getIdentifier("productId");	// "4567"
	String indexedObject = results.getObject();	// "account-product-reviews"
}
else {
	// perform matching failure logic here.
}
```

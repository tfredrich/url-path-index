# url-trie-index
A trie index for URL segments with nanosecond response times for 1000s of URLs. A successful search returns URL segments and identifiers along with the indexed object.

## Usage

This simplistic example indexes string objects in the URL pattern, but likely you'll need to index something more complex.

```Java
PathIndex<String> trie = new PathIndex<>();

// Index some URL patterns...
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
```

To search the index:

```Java
SearchResults<String> results = trie.search("/accounts/1234/products/4567/reviews");

if (results.matched()) {
	String accountId = results.getIdentifier("accountId");	// "1234"
	String productId = results.getIdentifier("productId");	// "4567"
	String indexedObject = results.getObject();				// "account-product-reviews"
}
else {
	// perform matching failure logic here.
}
```

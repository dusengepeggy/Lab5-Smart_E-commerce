## Response performance report

### Scope
- `GET /api/products`
  - Pagination: `page`, `size`
  - Sorting: `sortBy`, `sortDir`
  - Filtering: `categoryId`, `q`, `minPrice`, `maxPrice`, `inStock`

### Data retrieval strategy
- Uses a single SQL query with `JOIN Category` and `LEFT JOIN Inventory`.
- Sorting is executed in Postgres using a whitelisted `ORDER BY` column.
- Pagination uses `LIMIT/OFFSET`.
- Total count uses a separate `COUNT(*)` query with the same filters.

### Complexity
- Filtering reduces scanned rows based on index selectivity.
- Sorting cost is approximately \(O(n \log n)\) on the filtered result set unless an index supports the chosen order.
- `LIMIT` reduces returned rows to `size`, but `OFFSET` still requires skipping `offset` rows, so large offsets degrade performance.

### Index recommendations (Postgres)
- `Product(category_id)`
- `Product(price)`
- `Product(created_at)`
- `Product(name)` (or trigram/full-text if `q` is heavily used)
- `Inventory(product_id)` (ideally unique if 1 inventory row per product)

### Measurement methodology
- Record end-to-end latency at the controller boundary (start timestamp before service call, end after).
- Capture p50/p95/p99 for representative workloads:
  - No filters, default sort
  - Category filter + in-stock filter
  - Name search (`q`) with common terms
  - Worst-case offset (`page` large)

### SQL plan validation
Run for each representative workload:

```sql
EXPLAIN (ANALYZE, BUFFERS)
SELECT p.product_id,
       p.category_id,
       c.category_name,
       p.name,
       p.description,
       p.price,
       p.created_at,
       COALESCE(i.stock_quantity, 0) AS stock_quantity
FROM Product p
JOIN Category c ON p.category_id = c.category_id
LEFT JOIN Inventory i ON i.product_id = p.product_id
WHERE 1=1
ORDER BY p.name ASC
LIMIT 20 OFFSET 0;
```

Confirm:
- Index scans are used where expected (no accidental sequential scans).
- Sort does not spill to disk for typical workloads (if it does, tune indexes and/or Postgres memory settings).

### Expected performance notes
- For small to moderate datasets (tens of thousands of products), `LIMIT/OFFSET` with indexed filters typically remains fast.
- For very large datasets, prefer keyset pagination for the dominant sort order (e.g., `created_at` + `product_id`) to keep p95 stable.


# Time Complexity and Method Descriptions

## 1. `radiusSearch(centerLat, centerLon, radiusKm)`

### What it does

-   Validates the input coordinates and radius.
-   Initializes a result list.
-   Calls the recursive `radiusSearchHelper` to traverse the KD-tree.
-   Returns all nodes whose coordinates fall within the specified radius
    using Haversine distance for geographical accuracy.

### Time Complexity

-   **Average:** O(k + log n)
-   **Worst case:** O(n)

Where k is the number of nodes found and n is the total number of nodes in the KD-tree.

------------------------------------------------------------------------

## 2. `radiusSearchHelper(node, centerLat, centerLon, radiusKm, depth, result)`

### What it does

This method performs the actual KD-tree radius search.

For each node: 
1. Terminates if the node is null. 
2. Extracts its latitude and longitude. 
3. Computes the Haversine distance to the center point. 
4. Adds the node to the results if it is inside the radius. 
5. Determines the splitting axis (latitude or longitude) using `depth % 2`.
6. Chooses whether to explore the left or right subtree first. 
7. Computes the distance from the center point to the splitting plane. 
8. If the splitting plane intersects the search radius, explores the opposite subtree as well.

### Time Complexity

-   **Average:** O(k + log n)
-   **Worst case:** O(n)

Where k is the number of nodes found and n is the total number of nodes in the KD-tree.

------------------------------------------------------------------------

## 3. `getRadiusSearchSummary(centerLat, centerLon, radiusKm)`

### What it does

-   Calls `radiusSearch` to obtain nodes within the radius.
-   Groups all stations found by country.
-   Stores the distance to each station from the center point.
-   Sorts stations within each country.
-   Builds and returns a formatted summary containing:
    -   search center information
    -   list of countries
    -   stations per country
    -   distance, location, time zone, and station attributes

### Time Complexity

Let s be the total number of stations found: 
- Grouping and collecting stations → O(s) 
- Sorting stations → O(s log s)

**Total:** O(s log s)

------------------------------------------------------------------------

## 4. `getRadiusSearchAVL(centerLat, centerLon, radiusKm)`

### What it does

-   Executes the radius search.
-   For each resulting node:
    -   Creates a copy including the distance.
    -   Iterates through each station in that node:
        -   Searches for its coordinates in the AVL tree.
        -   If already in the AVL, adds the station to the existing
            node.
        -   Otherwise inserts a new node into the AVL.

Produces an **AVL tree that contains only the stations inside the
radius**.

### Time Complexity

Let s be the number of stations inserted:
- Each find/insert operation on the AVL → O(log s)

**Total:** O(s log s)

------------------------------------------------------------------------

## Summary Table

| Method                   | Description                              | Time Complexity                  |
|--------------------------|------------------------------------------|----------------------------------|
| `radiusSearch`           | Entry point for radius search            | O(k + log n) average, O(n) worst |
| `radiusSearchHelper`     | Recursive KD-tree traversal              | O(k + log n) average, O(n) worst |
| `getRadiusSearchSummary` | Builds formatted textual summary         | O(s log s)                       |
| `getRadiusSearchAVL`     | Builds an AVL containing result stations | O(s log s)                       |


Where:
- n = total nodes in the KD-tree
- k = nodes within radius
- s = total stations inside those nodes

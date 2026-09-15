# Code Complexity Analysis

## Index
- [Function 1: Main rangeSearch](#function-1-main-rangesearch)
- [Function 2: passesFilters](#function-2-passesfilters)
- [Function 3: Tree rangeSearch](#function-3-tree-rangesearch)
- [Function 4: rangeQueryRecursive](#function-4-rangequeryrecursive)

---

## 1. Pseudocode

### Function 1: rangeSearch
    FUNCTION rangeSearch(latMin, latMax, lonMin, lonMax, isCity, indexCountry, isMainStation):
    IF root IS NULL:
        THROW "Root is null"

    results = empty list of Station
    rangeQueryRecursive(results, root, latMin, latMax, lonMin, lonMax, 0, isCity, indexCountry, isMainStation)
    RETURN results
    END FUNCTION


---

### Function 2: rangeQueryRecursive
    FUNCTION rangeQueryRecursive(results, node, latMin, latMax, lonMin, lonMax, depth, isCity, indexCountry, isMainStation):
    IF node IS NULL:
        RETURN

    lat = node.element.coordinate.latitude
    lon = node.element.coordinate.longitude

    IF lat BETWEEN latMin AND latMax AND lon BETWEEN lonMin AND lonMax:
        passesFilters(results, node.element.stations, isCity, indexCountry, isMainStation)

    coord = lon IF depth MOD 2 == 0 ELSE lat
    minRange = lonMin IF depth MOD 2 == 0 ELSE latMin
    maxRange = lonMax IF depth MOD 2 == 0 ELSE latMax

    IF coord < minRange:
        rangeQueryRecursive(results, node.right, latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation)
    ELSE IF coord > maxRange:
        rangeQueryRecursive(results, node.left, latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation)
    ELSE:
        rangeQueryRecursive(results, node.left, latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation)
        rangeQueryRecursive(results, node.right, latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation)
    END FUNCTION


---

### Function 3: passesFilters
    FUNCTION passesFilters(filteredStations, candidateStations, isCity, indexCountry, isMainStation):
    FOR each station IN candidateStations:
        passes = TRUE

        IF isCity:
            passes = station.isCity()

        IF isMainStation:
            passes = passes AND station.isMainStation()

        SWITCH indexCountry:
            CASE 0:
                passes = passes AND (station.country.abbreviation == "PT")
            CASE 1:
                passes = passes AND (station.country.abbreviation == "ES")
            DEFAULT:
                (do nothing)

        IF passes:
            filteredStations.add(station)
    END FOR
    END FUNCTION


---

# 3. Time Complexity


### Table 1: `rangeSearch` Function

| Operation (pseudocode)                                                                                       | Description                      | Best Case | Average Case | Worst Case | Notes                                                              |
| ------------------------------------------------------------------------------------------------------------ | -------------------------------- | --------- | ------------ | ---------- | ------------------------------------------------------------------ |
| `results = empty list of Station`                                                                            | Initialize results list          | O(1)      | O(1)         | O(1)       | Simple memory allocation                                           |
| `rangeQueryRecursive(results, root, latMin, latMax, lonMin, lonMax, 0, isCity, indexCountry, isMainStation)` | Start recursive range search     | O(log n)  | O(√n + k·m)  | O(n·m)     | n = number of nodes, k = nodes inside range, m = stations per node |
| `RETURN results`                                                                                             | Return the final list            | O(1)      | O(1)         | O(1)       | Already constructed list                                           |
| **TOTAL**                                                                                                    | Complete `rangeSearch` operation | O(log n)  | O(√n + k·m)  | O(n·m)     | Includes filtering cost                                            |


### Table 2: `rangeQueryRecursive` Function

| Operation (pseudocode)                                                                                                                                       | Description                | Best Case      | Average Case | Worst Case | Notes                                                     |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------- | -------------- | ------------ | ---------- | --------------------------------------------------------- |
| `IF node IS NULL: RETURN`                                                                                                                                    | Base case of recursion     | O(1)           | O(1)         | O(1)       | Stops recursion for null nodes                            |
| `lat = node.element.coordinate.latitude` <br> `lon = node.element.coordinate.longitude`                                                                      | Extract coordinates        | O(1)           | O(1)         | O(1)       | Simple assignment                                         |
| `IF lat BETWEEN latMin AND latMax AND lon BETWEEN lonMin AND lonMax: passesFilters(...)`                                                                     | Filter stations in range   | O(m)           | O(m)         | O(m)       | Only called for nodes inside range; m = stations per node |
| `coord = lon IF depth MOD 2 == 0 ELSE lat` <br> `minRange = lonMin IF depth MOD 2 == 0 ELSE latMin` <br> `maxRange = lonMax IF depth MOD 2 == 0 ELSE latMax` | Select splitting dimension | O(1)           | O(1)         | O(1)       | Alternates between longitude and latitude                 |
| Recursive calls: <br> `rangeQueryRecursive(results, node.left, ...)` <br> `rangeQueryRecursive(results, node.right, ...)`                                    | Traverse tree              | O(log n)       | O(√n)        | O(n)       | √n expected for balanced k-d tree; worst-case degenerate  |
| **TOTAL**                                                                                                                                                    | Complete traversal of node | O(log n + k·m) | O(√n + k·m)  | O(n·m)     | k = nodes in range, m = stations per node                 |


### Table 3: `passesFilters` Function

| Operation (pseudocode)                                                                                                                      | Description                     | Best Case | Average Case | Worst Case | Notes                               |
| ------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------- | --------- | ------------ | ---------- | ----------------------------------- |
| `FOR each station IN candidateStations:`                                                                                                    | Loop over all stations in node  | O(m)      | O(m)         | O(m)       | m = stations per node               |
| `IF isCity: passes = station.isCity()` <br> `IF isMainStation: passes = passes AND station.isMainStation()` <br> `SWITCH indexCountry: ...` | Apply filtering conditions      | O(1)      | O(1)         | O(1)       | Constant-time checks per station    |
| `IF passes: filteredStations.add(station)`                                                                                                  | Add station to results          | O(1)      | O(1)         | O(1)       | Constant-time insertion per station |
| **TOTAL per node**                                                                                                                          | Cost of filtering node stations | O(m)      | O(m)         | O(m)       | Linear in stations per node         |



# 4. Math explication

## KD-Tree Range Query Recurrence Explanation

We want to count the number of **regions intersected by a vertical line** in a 2D kd-tree, which is important to understand the range query complexity.

---

## 1. The naive guess (incorrect)

At first glance, one might think:

> A vertical line intersects either the left or right child of the root, not both.

This would suggest the recurrence:

    Q(n) = 1 + Q(n/2)

**But this is incorrect.**

- The root splits vertically, but the next level splits horizontally.
- If the vertical line intersects a horizontal split, it may intersect both children of that node.
- Therefore, the situation at the next level is not the same as at the root, so we need to adjust the recursion.

---

## 2. Correct approach

- Redefine Q(n) as the number of intersected regions in a subtree whose root is a vertical split.
- Look **two levels down**:

    - After 2 levels, there are 4 regions (each with about n/4 points).
    - A vertical line intersects 2 of these 4 regions.
    - Adding the root and first child intersected gives the constant 2.
    - Recursively count the intersected subtrees.

---

## 3. Correct recurrence

    Q(n) = O(1), if n = 1
    Q(n) = 2 + 2 Q(n/4), if n > 1

- `2` → regions intersected at the current 2 levels
- `2 Q(n/4)` → recursively intersected regions in the two subtrees

---

## 4. Expanding the recurrence

    Q(n) = 2 + 2 Q(n/4)
           = 2 + 2 (2 + 2 Q(n/16))
           = 2 + 4 + 4 Q(n/16)
           = 6 + 4 Q(n/16)
           ...

After k steps:

    Q(n) = 2 * (2^k - 1) + 2^k Q(n / 4^k)

---

## 5. Stopping condition

Recursion stops when the subproblem has size 1(tree size = 1):

    n / 4^k = 1  =>  4^k = n  =>  k = log_4(n)

---

## 6. Solving the sum

    Q(n) = 2 * (2^k - 1) = 2 * (2^(log_4(n)) - 1) = 2 * (n^(1/2) - 1) = O(√(n))

- So a vertical line intersects O(√(n)) regions.
- Similarly, a horizontal line intersects O(√(n)) regions.
- Therefore, the total number of nodes visited by a rectangular query is also O(√(n)).

---

**Conclusion:**  
Even though a kd-tree node may store only a single point, its implicit region is the space defined by the splits from the root. By counting nodes intersected recursively, we obtain the classic 2D range query complexity:

    T(n) = O(√(n) + k)

where k is the number of reported points.

**Reference:**

Berg, M. de, Cheong, O., van Kreveld, M., & Overmars, M. (2008). *Computational Geometry: Algorithms and Applications* (3rd edition). Springer-Verlag.

- Specifically, Section 5.2 (“Kd-trees”) and Lemma 5.4 discuss range queries and the number of regions intersected by a line.


# 5. Experimental Example


```java
// Test for average-case behavior of KD-Tree
@Test
public void test64kAverageCase() {
    // --- Interval for the query ---
    double latMin = 40.0;
    double latMax = 50.0;
    double lonMin = -10.0;
    double lonMax = 10.0;

    // --- Filters ---
    boolean cityFilter = false;
    boolean mainFilter = false;
    int countryFilter = 2; // ALL countries

    // --- Reset visited nodes counter ---
    tree64k.resetVisitedNodes();

    // --- Perform range search ---
    List<Station> result = tree64k.rangeSearch(
            latMin, latMax, lonMin, lonMax,
            cityFilter, countryFilter, mainFilter
    );

    // --- Gather metrics ---
    int n = 61163;             // total nodes in the tree
    int k = result.size();     // nodes actually inside the query interval
    int visited = tree64k.getVisitedNodes(); // total nodes traversed

    // --- Output for verification ---
    System.out.println("n = " + n);
    System.out.println("k = " + k);
    System.out.println("visitedNodes = " + visited);
    System.out.println("sqrt(n) + k = " + ((int)Math.sqrt(n) + k));

    // --- Assertion: visited nodes should not exceed theoretical bound by too much ---
    assertTrue(visited <= Math.sqrt(n) + k * 2); 
}
```

## Results:
    n = 61163   
    k = 34421
    visitedNodes = 34766
    sqrt(n) + k (teórico) = 34668

The test confirms that the average-case performance of a 2D kd-tree range query matches the theoretical bound O(√n + k).  
For a tree with 61,163 nodes and 34,421 points returned, the number of nodes visited (34,766) is very close to the theoretical estimate (√n + k ≈ 34,668).  
This shows that even for large datasets, the kd-tree efficiently limits the number of nodes traversed, validating the expected asymptotic behavior.


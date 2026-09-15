# Code Complexity Analysis

## Index
- [Function 1: NearestNeighbors](#function-1-nearestNeighbors)
- [Function 2: Search](#function-2-search)
- [Function 3: Tree subSearch](#function-3-searchSubtrees)
- [Function 4: Get coordinate](#function-4-getCoordinate)
- [Function 5: Nearest Neighbors w/o filters](#function-5-nearestNeighbors)
- [Function 6: Nearest Neighbors w/ filters](#function-6-nearestNeighbors)
- [Function 7: Nearest Neighbors caller method](#function-7-nearestNeighbors)


---

## 1. Pseudocode

### Function 1: nearestNeighbors
    FUNCTION nearestNeighbors(lat, lon, n, filters...):
    IF root IS NULL OR n <= 0:
        RETURN empty list

    filterSet = SET(filters) IF filters NOT EMPTY ELSE empty set

    candidates = MAX-HEAP (size n) ordered by distance DESC

    search(root, lat, lon, n, 0, candidates, filterSet)

    result = empty list

    WHILE candidates NOT EMPTY:
        INSERT candidates.poll().nodeData AT FRONT OF result

    RETURN result
    END FUNCTION

---

### Function 2: search
    FUNCTION search(node, targetLat, targetLon, n, depth, candidates, filterSet):
    IF node IS NULL:
        RETURN

    nodeData = node.element

    IF filterSet NOT EMPTY:
        nodeTimeZone = nodeData.stations.first.timeZoneGroup
        IF nodeTimeZone IS NULL OR nodeTimeZone NOT IN filterSet:
            searchSubtrees(node, targetLat, targetLon, n, depth, candidates, filterSet)
            RETURN

    nodeLat = nodeData.coordinate.latitude
    nodeLon = nodeData.coordinate.longitude

    dist = haversineDistance(nodeLat, nodeLon, targetLat, targetLon)

    IF candidates.size < n:
        candidates.add(nodeData, dist)
    ELSE IF dist < candidates.peek().distance:
        candidates.poll()
        candidates.add(nodeData, dist)

    searchSubtrees(node, targetLat, targetLon, n, depth, candidates, filterSet)
    END FUNCTION
---

### Function 3: searchSubtrees
    FUNCTION searchSubtrees(node, targetLat, targetLon, n, depth, candidates, filterSet):
    compareLatitude = (depth MOD 2 == 0)

    nodeSplitValue = (latitude OR longitude BASED ON compareLatitude)
    targetSplitValue = targetLat IF compareLatitude ELSE targetLon

    targetIsLeft = (targetSplitValue < nodeSplitValue)

    nearSubtree = node.left  IF targetIsLeft ELSE node.right
    farSubtree  = node.right IF targetIsLeft ELSE node.left

    search(nearSubtree, targetLat, targetLon, n, depth + 1, candidates, filterSet)

    IF candidates NOT EMPTY:
        distanceToPlane = targetSplitValue - nodeSplitValue
        IF (distanceToPlane^2) < candidates.peek().distance:
            search(farSubtree, targetLat, targetLon, n, depth + 1, candidates, filterSet)
    END FUNCTION

---


### Function 4: getCoordinate
    FUNCTION getCoordinate(node, getLatitude):
    nodeData = node.element
    RETURN nodeData.coordinate.latitude IF getLatitude
           ELSE nodeData.coordinate.longitude
    END FUNCTION

### Function 5: nearestNeighbors
    FUNCTION nearestNeighbors(lat, lon, n):
    RETURN nearestNeighbors(lat, lon, n, empty TimeZoneGroup array)
    END FUNCTION

### Function 6: nearestNeighbors
    FUNCTION nearestNeighbor(lat, lon, filters...):
    neighbors = nearestNeighbors(lat, lon, 1, filters)

    IF neighbors IS EMPTY:
        RETURN null

    RETURN neighbors[0]
    END FUNCTION


### Function 7: nearestNeighbors
    FUNCTION nearestNeighbor(lat, lon):
    RETURN nearestNeighbor(lat, lon, empty TimeZoneGroup array)
    END FUNCTION

---

# 3. Time Complexity


### Table 1: `nearestNeighbors` Function

| Operation from pseudocode    | Description               | Best Case    | Average Case | Worst Case | Notes                     |
| ---------------------------- | ------------------------- | ------------ | ------------ | ---------- | ------------------------- |
| `filterSet = SET(filters)`   | Create a set from filters | O(1)         | O(1)         | O(1)       | Filters are constant-size |
| `candidates = MAX-HEAP`      | Initialize heap           | O(1)         | O(1)         | O(1)       | Heap grows to size *n*    |
| `search(root, ...)`          | KD-tree recursive search  | O(log n)     | O(log n)     | O(n)       | Depends on pruning        |
| `WHILE candidates NOT EMPTY` | Build final result list   | O(n)         | O(n)         | O(n)       | n = requested neighbors   |

### Table 2: `search` Function

| Operation from pseudocode       | Description                  | Best Case | Avg Case     | Worst Case | Notes             |
| ------------------------------- | ---------------------------- | --------- | ------------ | ---------- | ----------------- |
| `IF node IS NULL`               | Null check, stop recursion   | O(1)      | O(1)         | O(1)       | —                 |
| `IF filterSet NOT EMPTY`        | Time zone filter check       | O(1)      | O(1)         | O(1)       | Constant-time     |
| `dist = haversineDistance(...)` | Compute distance             | O(1)      | O(1)         | O(1)       | Always computed   |
| `candidates.add / poll / peek`  | Update heap                  | O(1)      | O(1)         | O(1)       | n is constant     |
| `searchSubtrees(...)`           | Continue recursion           | O(log n)  | O(log n)     | O(n)       | KD-tree traversal |



### Table 3: `searchSubtrees` Function

| Operation from pseudocode          | Description                   | Best Case | Avg Case     | Worst Case | Notes                   |
| ---------------------------------- | ----------------------------- | --------- | ------------ | ---------- | ----------------------- |
| `compareLatitude = (depth MOD 2…)` | Select axis                   | O(1)      | O(1)         | O(1)       | —                       |
| `targetIsLeft = (...)`             | Compare split values          | O(1)      | O(1)         | O(1)       | —                       |
| `search(nearSubtree, ...)`         | Always explore nearer subtree | O(1)      | O(log n)     | O(n)       |                         |
| `distanceToPlane = ...`            | Compute plane distance        | O(1)      | O(1)         | O(1)       |                         |
| `search(farSubtree, ...)`          | Explore far subtree if needed | O(0)      | O(log n)     | O(n)       | No pruning → worst case |

**Note:** O(0) represents a never executed code

### Table 4: `getCoordinate` Function

| Operation from pseudocode                 | Description  | Best | Avg  | Worst | Notes         |
| ----------------------------------------- | ------------ | ---- | ---- | ----- | ------------- |
| `RETURN coordinate.latitude OR longitude` | Access field | O(1) | O(1) | O(1)  | Simple lookup |


### Table 5: `nearestNeighbors(lat, lon, n)` Function

| Operation from pseudocode                      | Description  | Best | Avg  | Worst | Notes          |
| ---------------------------------------------- | ------------ | ---- | ---- | ----- | -------------- |
| `RETURN nearestNeighbors(lat, lon, n, empty…)` | Wrapper call | O(1) | O(1) | O(1)  | Delegates work |



### Table 6: `nearestNeighbor(lat, lon, filters...)` Function

| Operation from pseudocode                     | Description     | Best | Avg  | Worst | Notes               |
| --------------------------------------------- | --------------- | ---- | ---- | ----- | ------------------- |
| `neighbors = nearestNeighbors(... k = 1 ...)` | Delegate work   | O(1) | O(1) | O(1)  | Single nearest node |
| `RETURN neighbors[0]`                         | Constant return | O(1) | O(1) | O(1)  |                     |


### Table 7: `nearestNeighbor(lat, lon)` Function

| Operation from pseudocode                         | Description  | Best | Avg  | Worst | Notes |
| ------------------------------------------------- | ------------ | ---- | ---- | ----- | ----- |
| `RETURN nearestNeighbor(lat, lon, empty filters)` | Wrapper call | O(1) | O(1) | O(1)  |       |


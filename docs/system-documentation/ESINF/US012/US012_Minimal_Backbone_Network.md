# Code Complexity Analysis – US012 Minimal Backbone Network (Kruskal's MST)

## Index
- Function 1: `convertToUndirected`
- Function 2: `kruskal` (Kruskal's Algorithm)
- Function 3: `exportToGraphviz`
- Function 4: `generateDotContent`
- Function 5: `generateSVG`

---

## 1. Pseudocode

### Function 1: `convertToUndirected`
```
CREATE undirectedGraph as new Graph(directed=false, same type as input)

FOR each vertex v in directedGraph.vertices()
    ADD v to undirectedGraph

INITIALIZE addedEdges as HashSet<String>

FOR each edge in directedGraph.edges()
    u = edge.getVOrig()
    v = edge.getVDest()
    
    uHash = u.hashCode()
    vHash = v.hashCode()
    edgeKey = min(uHash, vHash) + "-" + max(uHash, vHash)
    
    IF edgeKey NOT IN addedEdges
        ADD edge(u, v, weight) to undirectedGraph
        ADD edgeKey to addedEdges

RETURN undirectedGraph
```

### Function 2: `kruskal` (Kruskal's Algorithm)
```
CREATE mst as new Graph(directed=false, same type as input)

FOR each vertex v in g.vertices()
    ADD v to mst

CREATE lstEdges as ArrayList from g.edges()

SORT lstEdges by edge.getWeight().getDistance() ASCENDING

FOR each edge e in lstEdges
    vOrig = e.getVOrig()
    vDest = e.getVDest()
    
    connectedVerts = DFS(mst, vOrig)
    
    IF connectedVerts is null OR vDest NOT IN connectedVerts
        ADD edge(vOrig, vDest, e.getWeight()) to mst

RETURN mst
```

### Function 3: `exportToGraphviz`
```
dotFile = filename + ".dot"
svgFile = filename + ".svg"

TRY
    dotContent = generateDotContent(mstGraph)
    
    WRITE dotContent to dotFile
    
    generateSVG(dotFile, svgFile)
    
CATCH IOException
    PRINT error message
```

### Function 4: `generateDotContent`
```
INITIALIZE StringBuilder sb

APPEND graph header and styling to sb

FOR each vertex v in graph.vertices()
    x = v.getCoordinates().getX()
    y = v.getCoordinates().getY()
    name = v.getStation().getName()
    
    APPEND vertex definition with coordinates to sb

INITIALIZE addedEdges as HashSet<String>

FOR each edge in graph.edges()
    id1 = edge.getVOrig().getStation().getId()
    id2 = edge.getVDest().getStation().getId()
    
    key = min(id1, id2) + "-" + max(id1, id2)
    
    IF key NOT IN addedEdges
        APPEND edge definition to sb
        ADD key to addedEdges

APPEND graph footer to sb
RETURN sb.toString()
```

### Function 5: `generateSVG`
```
CREATE ProcessBuilder with ["neato", "-Tsvg", dotPath, "-o", svgPath]

START process

WAIT for process completion

IF exitCode != 0
    PRINT warning about Graphviz
```


---

## 2. Time Complexity

### convertToUndirected
| Operation                                                      | Description                    | Time Complexity | Notes                           |
|----------------------------------------------------------------|--------------------------------|-----------------|---------------------------------|
| `Graph<V, TrackWeight> undirectedGraph = createEmptyGraph(...)` | Create empty graph             | 1               | Constant time                   |
| `for(V vertex : directedGraph.vertices())`                    | Iterate all vertices           | V               | Linear in vertices              |
| `undirectedGraph.addVertex(vertex)`                           | Add vertex to graph            | 1               | Assuming O(1) vertex insertion  |
| `Set<String> addedEdges = new HashSet<>()`                     | Initialize HashSet             | 1               | Constant time                   |
| `for(Edge<V, TrackWeight> edge : directedGraph.edges())`      | Iterate all edges              | E               | Linear in edges                 |
| `u.hashCode()` and `v.hashCode()`                             | Compute hash codes             | 1               | Assuming O(1) hash computation  |
| `Math.min(uHash, vHash)` and string concatenation             | Create edge key                | 1               | Constant string operations      |
| `addedEdges.contains(edgeKey)`                                 | HashSet lookup                 | 1               | Average O(1) for HashSet       |
| `undirectedGraph.addEdge(u, v, edge.getWeight())`            | Add edge to graph              | 1               | Assuming O(1) edge insertion    |
| `addedEdges.add(edgeKey)`                                      | Add to HashSet                 | 1               | Average O(1) for HashSet       |
| **Big(O)**                                                     |                                | **O(V + E)**    |                                 |

### kruskal (Kruskal's Algorithm)

| Operation | Description | Time Complexity                   | Notes                                  |
| :--- | :--- |:----------------------------------|:---------------------------------------|
| `Graph<V, TrackWeight> mst = createEmptyGraph(...)` | Create empty MST graph | 1                                 | Constant time                          |
| `for(V vertex : g.vertices())` | Copy all vertices | V                                 | Linear in vertices                     |
| `mst.addVertex(vertex)` | Add vertex to MST | V × 1 = V                         | V vertices, O(1) each                  |
| `List<Edge<V, TrackWeight>> lstEdges = new ArrayList(...)` | Create edge list | E                                 | Linear copy of edges                   |
| `lstEdges.sort(...)` | Sort edges (TimSort) | **Best: E**<br>**Worst: E log E** | Non Deterministic (faster if sorted).  |
| `for(Edge<V, TrackWeight> e : lstEdges)` | Iterate sorted edges | E                                 | Process each edge                      |
| `LinkedList<V> connectedVerts = DFS(mst, vOrig)` | Check connectivity via DFS | E × (V + E_mst)                   | **Major Bottleneck.** E_mst < V        |
| `connectedVerts.contains(vDest)` | Check specific connectivity | E × V_connected                   | **Linear Search** in the returned list |
| `mst.addEdge(...)` | Add edge to MST | 1                                 | Constant edge addition                 |
| **Big(O)** | | **O(EV)**                         |    |

### exportToGraphviz
| Operation                                    | Description              | Time Complexity | Notes                    |
|----------------------------------------------|--------------------------|-------------|--------------------------|
| `String dotContent = generateDotContent(mstGraph)` | Generate DOT content | V + E       | Linear in graph size     |
| `generateSVG(dotFile, svgFile)`             | Generate SVG via Graphviz| External    | Depends on Graphviz      |
| **Big(O)**                                   |                          | **O(V + E)** | Excluding external tools |

### generateDotContent
| Operation                                                | Description                   | Time Complexity | Notes                           |
|----------------------------------------------------------|-------------------------------|----------------|---------------------------------|
| `StringBuilder sb = new StringBuilder()`                 | Initialize string builder     | 1              | Constant time                   |
| `sb.append("graph minimal_backbone {...")` (headers)     | Append graph headers          | 1              | Constant string operations      |
| `for (StationVertex v : graph.vertices())`              | Iterate all vertices          | V              | Linear in vertices              |
| `v.getCoordinates().getX()` and `getY()`               | Get vertex coordinates        | V × 1 = V      | Constant coordinate access      |
| `v.getStation().getName().replace("\"", "\\\"")`       | Process station name          | V × N          | N = avg length of station names |
| `sb.append(String.format(...))`                        | Format and append vertex      | V × 1 = V      | String formatting per vertex    |
| `Set<String> addedEdges = new HashSet<>()`              | Initialize edge tracking      | 1              | Constant time                   |
| `for (Edge<StationVertex, TrackWeight> edge : graph.edges())` | Iterate all edges        | E              | Linear in edges                 |
| `Math.min(id1, id2) + "-" + Math.max(id1, id2)`        | Create unique edge key        | E × 1 = E      | Constant operations per edge    |
| `addedEdges.contains(key)`                              | Check for duplicate edge      | E × 1 = E      | Average O(1) HashSet lookup     |
| `sb.append(String.format("  \"%d\" -- \"%d\";\n", ...))`| Append edge to DOT           | E × 1 = E      | String formatting per edge      |
| `addedEdges.add(key)`                                   | Track added edge              | E × 1 = E      | Average O(1) HashSet insertion  |
| **Big(O)**                                              |                               | **O(V×N + E)** | N = avg station name length     |

### generateSVG
| Operation                                           | Description                 | Time Complexity | Notes                        |
|-----------------------------------------------------|-----------------------------|-----------------|------------------------------|
| `ProcessBuilder pb = new ProcessBuilder(...)`      | Create process builder      | 1               | Constant time                |
| `Process process = pb.start()`                     | Start Graphviz process      | External        | OS-dependent process spawn   |
| `int exitCode = process.waitFor()`                 | Wait for process completion | **External**    | **Depends on Graphviz neato** |
| **Total**                                          |                             | **External**    | Not under algorithm control  |
| **Big(O)**                                         |                             | **O(External)** | Graphviz "neato" complexity  |

---

## 3. Overall Algorithm Analysis

### Kruskal's MST Algorithm Complexity

**Our Implementation Complexity:** O(EV)
- **Bottleneck:** Using DFS for cycle detection instead of Union-Find
- **DFS per edge:** O(V + E_mst) where E_mst ≤ V-1
- **Total DFS cost:** O(E × V) in worst case

### Performance 

| Implementation | Sorting | Cycle Detection | Total Complexity |
|----------------|---------|-----------------|-----------|
| **Our Implementation** | O(E log E) | O(E × V) | **O(EV)** |

### Real-World Performance

For typical railway networks:
- **Sparse graphs:** E ≈ O(V), so our O(EV) = O(V²)
- **Dense graphs:** E ≈ O(V²), so our O(EV) = O(V³)

**Belgian Railway Network Example:**
- Vertices (Stations): V ≈ 200-500
- Edges (Connections): E ≈ 400-1000 (sparse)
- Our complexity: O(1000 × log(1000) + 1000 × 500) ≈ O(510,000)
- Optimal complexity: O(1000 × log(500)) ≈ O(9,000)


---

## Final Summary

**Overall Runtime Complexity for US012 Minimal Backbone Network:**
- **Graph Conversion:** O(V + E) - directed to undirected
- **MST Computation:** **O(EV)** - Kruskal with DFS *(bottleneck)*
- **Visualization:** O(V×N + E) - DOT generation + External SVG


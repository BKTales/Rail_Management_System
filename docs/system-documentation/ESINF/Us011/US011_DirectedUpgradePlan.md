# Code Complexity Analysis – DirectedLineUpgradePlan (Kosaraju & Topological Sort)

## Index
- Function 1: `computeUpgradePlan`
- Function 2: `kosarajuAlgorithm`
- Function 3: `DFSFinishTime` (recursive DFS)
- Function 4: `matrixTranspose`

---

## 1. Pseudocode

### Function 1: `computeUpgradePlan`
```
IF graph is null OR graph is not directed
    THROW IllegalArgumentException

WCCs = CALL getWeaklyConnectedComponents(graph)
INITIALIZE finalTopologicalOrders as List of Lists

FOR EACH wcc IN WCCs
    INITIALIZE finishStack as LinkedList
    INITIALIZE cycleResult as LinkedList
    
    CALL kosarajuAlgorithm(wcc, cycleResult, finishStack)
    
    IF cycleResult IS NOT EMPTY
        RETURN new UpgradePlanResult(true, null, cycleResult)
    
    REVERSE finishStack
    ADD finishStack TO finalTopologicalOrders

RETURN new UpgradePlanResult(false, finalTopologicalOrders, null)
```

### Function 2: `kosarajuAlgorithm`
```
INITIALIZE visited as Boolean Array [graph.numVertices]
CALL computeFinishTimes(graph, visited, finishStack)

tGraph = CALL reverseGraph(graph)
CALL findSCCsAndCycle(tGraph, finishStack, cycle, visited)
```
### Function 3: `computeFinishTimes`
```
FOR EACH vertex v IN graph.vertices
    IF visited[graph.key(v)] IS false
        CALL DFSFinishTime(graph, v, visited, finishStack)
```

### Function 4: `findSCCsAndCycle`
```
INITIALIZE visited as Boolean Array [tGraph.numVertices]
INITIALIZE tempStack as Copy of finishStack

WHILE tempStack IS NOT EMPTY
    v = tempStack.pop() // remove last
    IF visited[tGraph.key(v)] IS false
        INITIALIZE SCC as LinkedList
        CALL DFSFinishTime(tGraph, v, visited, SCC)
        
        IF SCC.size > 1
            ADD ALL SCC nodes TO cycle
            RETURN
```

### Function 5: `DFSFinishTime`
```
vKey = g.key(vOrig)
visited[vKey] = true

FOR EACH neighbor IN g.adjVertices(vOrig)
    neighborKey = g.key(neighbor)
    IF visited[neighborKey] IS false
        CALL DFSFinishTime(g, neighbor, visited, qdfs)

ADD vOrig TO qdfs // Tempo de finalização
```

### Function 6: `reverseGraph`
```
CREATE tGraph AS NEW MapGraph(directed=true)

FOR EACH v IN graph.vertices
    tGraph.addVertex(v)

FOR EACH edge IN graph.edges
    tGraph.addEdge(edge.dest, edge.origin, edge.weight)

RETURN tGraph
```

### Function 7: `getWeaklyConnectedComponents`
```
undirectedGraph = CALL buildUndirectedGraph(graph)
INITIALIZE visited as Boolean Array
INITIALIZE wccs as List of Graphs

FOR EACH vertex v IN undirectedGraph
    IF visited[v.key] IS false
        INITIALIZE componentVertices as Set
        CALL dfs(undirectedGraph, v, visited, componentVertices)
        
        wccGraph = CALL reconstructWCCGraph(graph, componentVertices)
        ADD wccGraph TO wccs

RETURN wccs
```

### Function 8: `buildUndirectedGraph`
```
CREATE undirectedGraph AS NEW MapGraph(directed=false)

FOR EACH v IN graph.vertices
    undirectedGraph.addVertex(v)

FOR EACH edge IN graph.edges
    undirectedGraph.addEdge(edge.origin, edge.dest, edge.weight)

RETURN undirectedGraph
```

### Function 9: `reconstructWCCGraph`
```
CREATE wccGraph AS NEW MapGraph(directed=true)

FOR EACH cv IN componentVertices
    wccGraph.addVertex(cv)

FOR EACH edge IN originalGraph.edges
    IF componentVertices CONTAINS edge.origin AND componentVertices CONTAINS edge.dest
        wccGraph.addEdge(edge.origin, edge.dest, edge.weight)

RETURN wccGraph
```

### Function 10: `dfs`
```
vKey = graph.key(v)
visited[vKey] = true
ADD v TO component

FOR EACH neighbor IN graph.adjVertices(v)
    IF visited[graph.key(neighbor)] IS false
        CALL dfs(graph, neighbor, visited, component)
```

---

## 2. Time Complexity

### computeUpgradePlan 
| **Operation**                                      | **Description**                      | **Time Complexity**             | **Notes**                  |   |
|----------------------------------------------------|--------------------------------------|---------------------------------|----------------------------| - |
| ` IF graph is null...`                             | Throw exception if null/not directed | $1$                             | Constant                   |   |
| `CALL getWeaklyConnectedComponents(graph)`         | Compute WCCs                         | $V + E$                         |                            |   |
| `INITIALIZE finalOrders `                          |                                      | $1$                             |                            |   |
| `FOR EACH wcc IN WCCs`                             | Iterate all components               | $k$                             | $k$ is the number of WCCs. |   |
| `new LinkedList()`                                 | Initialize finishStack & cycleResult | $2k$                            |                            |   |
| `kosarajuAlgorithm(wcc, cycleResult, finishStack)` | Find SCCs and cycle in component     | $V + E$                         |                            |   |
| `IF cycleResult NOT EMPTY`                         | Check for cycles                     | $k$                             |                            |   |
| `REVERSE finishStack`                              | Reverse topological order            | $V * k$                         |                            |   |
| `ADD finishStack TO finalTopologicalOrders`        | Store topological order              | $V * k$                         |                            |   |
| **Total**                                          |                                      | **$2V + 2E + 2V * k + 4k + 2$** |                            |   |
| **Big(O)**                                         |                                      | **$O(V + E)$**                  |                            |   |

### kosarajuAlgorithm 
| **Operation**                                           | **Description**                          | **Time Complexity**    | **Notes** |
|---------------------------------------------------------| ---------------------------------------- |------------------------|-----------|
| `INITIALIZE visited`                                    | Create visited array                     | $V$                    |           |
| `CALL computeFinishTimes(graph, visited, finishStack)`  | First DFS to compute finish times        | $V + E$                |           |
| `tGraph = CALL reverseGraph`                            | Transpose the graph                      | $\sum Vi + Ei = V + E$ |           |
| `findSCCsAndCycle(tGraph, finishStack, cycle, visited)` | Second DFS to find SCCs and detect cycle | $V + E$                |           |
| **Total**                                               |                                          | **$4V + 3E)$**         |           |
| **Big(O)**                                              |                                          | **O(V + E)**           |           |


### computeFinishTimes
| **Operation**                                   | **Description**                  | **Time Complexity**    | **Notes**            |
|-------------------------------------------------| -------------------------------- |------------------------|----------------------|
| `FOR EACH vertex v `                            | Iterate all vertices             | $V$                    |                      |
| `graph.key(v)`                                  | Get vertex by index              | $V$                    |                      |
| `IF visited[...] IS false`                      | Check if vertex visited          | $V$                    |                      |
| `DFSFinishTime(graph, v, visited, finishStack)` | DFS starting at unvisited vertex | $\sum Vi + Ei = V + E$ | Total over all calls |
| **Total**                                       |                                  | **$4V + E) $**         |                      |
| **Big(O)**                                      |                                  | **$O(V + E)$**         |                      |


### findSCCsAndCycle
| **Operation**                            | **Description**          | **Time Complexity**    | **Notes**                                                         |
|------------------------------------------| ------------------------ |------------------------|-------------------------------------------------------------------|
| `INITIALIZE visited`                     | Reset visited array      | $V$                    |                                                                   |
| `INITIALIZE tempStack `                  | Copy finish stack        | $V$                    |                                                                   |
| `WHILE tempStack NOT empty`              | Iterate all vertices     | $V$                    | Each vertex popped once                                           |
| `tempStack.removeLast()`                 | Remove vertex from stack | $V$                    | O(1) per pop                                                      |
| `IF visited[...] IS false`               | Check if visited         | $V$                    | O(1)                                                              |
| `INITIALIZE SCC`                         | Create SCC list          | $k$                    | Created once per SCC ($k$).                                       |
| `DFSFinishTime(tGraph, v, visited, SCC)` | DFS on transposed graph  | $\sum Vi + Ei = V + E$ |                                                                   |
| `IF SCC.size > 1`                        | Check component size     | $k$                    |                                                                   |
| `cycle.addAll(SCC)`                      | Add SCC to cycle list    | $V$                    | Only once (early return). In the worst case, all nodes are added. |
| **Total**                                |                          | **$7V + 2k + E)$**     | Dominant terms                                                    |
| **Big(O)**                               |                          | **$O(V + E)$**         |                                                                   |


### getWeaklyConnectedComponents
| **Operation**                                        | **Description**                      | **Time Complexity**    | **Notes**                       |
|------------------------------------------------------|--------------------------------------|------------------------|---------------------------------|
| `CALL buildUndirectedGraph`                          | Convert directed to undirected graph | $4V + 3E + 1$          |                                 |
| `INITIALIZE visited`                                 | Create visited array                 | $V$                    | Initialized to false            |
| `INITIALIZE wccs`                                    | Create list of components            | $1$                    |                                 |
| `FOR EACH vertex v`                                  | Iterate all vertices                 | $V$                    |                                 |
| `undirectedGraph.vertex(i)`                          | Get vertex by index                  | $V$                    |                                 |
| `IF visited[v.key] IS false`                         | Check visited                        | $V$                    | O(1)                            |
| `INITIALIZE componentVertices set`                   |                                      | $k$                    |                                 |
| `CALL dfs(undirectedGraph, v, ...)`                  | DFS traversal                        | $\sum Vi + Ei = V + E$ | Total over all calls $O(V + E)$ |
| `CALL reconstructWCCGraph(graph, componentVertices)` | Build WCC subgraph                   | $V + E$                |                                 |
| `wccs.add(wccGraph)`                                 | Add component to list                | $k$                    | One per component               |
| **Total**                                            |                                      | **$7V + 5E + 2k + 2$** | Dominant terms                  |
| **Big(O)**                                           |                                      | **$O(V + E)$**         |                                 |

**Note:** $\sum (E * Vi) = E * \sum (Vi) = E * V$

### buildUndirectedGraph
| **Operation**                                                   | **Description**               | **Time Complexity** | **Notes** |
|-----------------------------------------------------------------|-------------------------------|---------------------|-----------|
| `CREATE undirectedGraph`                                        | Create empty undirected graph | $1$                 | Constant  |
| `graph.vertices() `                                             |                               | $V$                 |           |
| `FOR EACH v IN graph.vertices`                                  | Iterate all vertices          | $V$                 |           |
| `undirectedGraph.addVertex(v)`                                  | Add vertex to graph           | $V$                 |           |
| `graph.edges() `                                                |                               | $V + E$             |           |
| `FOR EACH edge`                                                 | Iterate all edges             | $E$                 |           |
| `undirectedGraph.addEdge(edge.origin, edge.dest, edge.weight)`  | Add edge to graph             | $E$                 |           |
| **Total**                                                       |                               | **$4V + 3E + 1$**   |           |
| **Big(O)**                                                      |                               | **$O(V + E)$**      |           |
 

### reconstructWCCGraph
| **Operation**                                 | **Description**                | **Time Complexity**   | **Notes**                                  |
|-----------------------------------------------|--------------------------------|-----------------------|--------------------------------------------|
| `CREATE wccGraph`                             | Create empty graph             | $1$                   | Constant                                   |
| `FOR EACH cv IN componentVertices`            | Iterate component vertices     | $Vi$                  | Vi is the number of vertices in the WCC.   |
| `wccGraph.addVertex(cv)`                      | Add vertex to sub-graph        | $Vi$                  |                                            |
| `originalGraph.edges()`                       | Get all edges                  | $V + E$               | This retrieves edges from the whole graph. |
| `FOR EACH edge  `                             | Iterate all edges              | $E$                   |                                            |
| `componentVertices.contains(edge.getVOrig())` | Check origin in component      | $1$                   |                                            |
| `componentVertices.contains(edge.getVDest())` | Check destination in component | $1$                   |                                            |
| `wccGraph.addEdge(...)`                       | Add edge to graph              | $Ei$                  |                                            |
| **Total**                                     |                                | **$V + 2E + Ei + 1$** |                                            |
| **Big(O)**                                    |                                | **$O(V + E)$**        | Dominant term                              |


### DFSFinishTime 
| **Operation**                      | **Description**                      | **Time Complexity** | **Notes**                     |
|------------------------------------|--------------------------------------|---------------------|-------------------------------|
| `vKey = g.key(vOrig) `             | Get vertice key                      | $Vi$                |                               |
| `visited[vKey] = true`             | Mark the current vertex as visited ` | $Vi$                |                               |
| `FOR EACH neighbor`                | Iterate over all neighbors(edges)    | $Ei$                | Σ deg(v) = E (directed graph) |
| `neighborKey = g.key(neighbor)`    | Get index/key of neighbor            | $Ei$                |                               |
| `IF visited[neighborKey] IS false` | Check if neighbor is visited         | $Ei$                |                               |
| `CALL DFSFinishTime(...) `         | Recursive DFS call                   | $Ei$                |                               |
| `ADD vOrig TO qdfs`                | Add current vertex to finish stack   | $Vi$                |                               |
| **Total **                         |                                      | **$3Vi + 4Ei$**     |                               |
| **Big(O)**                         |                                      | **$O(Vi + Ei)$**    |                               |

### reverseGraph 
| Operation                                                                 | Description           | Time Complexity     | Notes |
|---------------------------------------------------------------------------|-----------------------|---------------------|-------|
| `new MapGraph<>(graph.isDirected())`                                      | Create empty MapGraph | $1$                 |       |
| `graph.vertices()`                                                        | Get all vertices      | $Vi$                |       |
| `for (StationVertex v : graph.vertices()) t.graph.addVertice(v)`          | Add all vertices      | $Vi$                |       |
| `graph.edges()`                                                           | Get all edges         | $Vi + Ei$           |       |
| `for (Edge<StationVertex,TrackWeight> edge : edges) tGraph.addEdge(...) ` | Add reversed edges    | $Ei$                |       |
| `return tGraph`                                                           | Return graph          | $1$                 |       |
| **Total**                                                                 |                       | **$3Vi + 2Ei + 2$** |       |
| **Big(O)**                                                                |                       | **$O(Vi + Ei)$**    |       |

### dfs
| Operation                                         | Description      | Time Complexity | Notes |
|---------------------------------------------------|------------------|-----------------|-------|
| `vKey = graph.key(v)`                             | Get vertice key  | $Vi$            |       |
| `visited[vKey] = true`                            | Mark as visited  | $Vi$            |       |
| `component.add(v)`                                |                  | $Vi$            |       |
| `FOR EACH neighbor IN graph.adjVertices(v)`       | Iterate edges    | $Ei$            |       |
| `IF visited[graph.key(neighbor)] IS false `       | Check condition  | $Ei$            |       |
| `CALL dfs(graph, neighbor, visited, component)  ` |                  | $Ei$            |       |
| **Total**                                         |                  | **3Vi + 3Ei**   |       |
| **Big(O)**                                        |                  | **O(Vi + Ei)**  |       |
---

# DFS Time Complexity Analysis 

## Explanation

**What is the running time of DFS?** The loops on lines 1-3 and lines 5-7 of DFS take time Θ(V), exclusive of the time to execute the calls to DFS-Visit. As we did for breadth-first search, we use aggregate analysis. The procedure DFS-Visit is called exactly once for each vertex v ∈ V, since the vertex u on which DFS-Visit is invoked must be white and the first thing DFS-Visit does is paint vertex u gray. During an execution of DFS-Visit(G, v), the loop on lines 4-7 executes |Adj[v]| times. Since

\[
\sum_{v \in V} |Adj[v]| = \Theta(E)
\]

the total cost of executing lines 4-7 of DFS-Visit is Θ(E). The running time of DFS is therefore Θ(V + E).

## Key Points:

1. **Initialization phase:** Θ(V) - marking all vertices as unvisited
2. **Main DFS loop:** Θ(V) - checking each vertex for visitation status
3. **DFS-Visit calls:** Θ(E) - processing all edges across all vertices
4. **Total complexity:** Θ(V + E) - additive combination of vertex and edge processing

(Source: Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). Introduction to Algorithms (3rd ed.). MIT Press.)

## Mathematical Basis:
- Each vertex visited exactly once → Θ(V)
- Each edge examined exactly once → Θ(E)
- Sum of all adjacency list sizes = 2E (undirected) or E (directed)

## Application to Our Implementation:
Our `DFSFinishTime` follows this exact pattern with deterministic Θ(V + E) complexity, as it processes each vertex once and each edge once, regardless of graph structure.
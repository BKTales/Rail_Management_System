# Code Complexity Analysis Us007

## Index
- [Function 1: TwoDTree](#function-1-TwoDTree)
- [Function 2: insertHelper](#function-3-insertHelper)
- [Function 3: inOrderTransversalData](#function-2-inOrderTransversalData)
- [Function 4: inOrderTransversalHelperData](#function-3-inOrderTransversalHelperData)

---

## 1. Pseudocode

### Function 1: TwoDTree
    FUNCTION TwoDTree(AVL tree):

    List of node datas From AVL = avl.inOrderTransversalData()
    Sort by first comparator(x)

    root = insertHelper(node list, depth)
    END FUNCTION


1
### Function 2: insertHelper
    FUNCTION insertHelper(NodeData list, depth):
        IF list is NULL || List is over 
            END

        Increment size
        Sort List according to depth(x or y)

        Get midian index and create this node

        split the list in half(using median index)

        node.setLeft (insertHelper(leftSubList, depth + 1))
        node.setRight (insertHelper(rightSubList, depth + 1))
    END FUNCTION


---

### Function 3: inOrderTransversalData
    FUNCTION inOrderTransversalData():
    Start list of nodes 

    call helper(inOrderTransversalHelperData(list, tree_root))
    END FUNCTION



---

### Function 4: inOrderTransversalHelperData
    Functions inOrderTransversalHelperData(List nodes, Node node):
        IF node is NULL
            END

        inOrderTransversalHelperData(List, left node)
        nodes.add(node element)
        inOrderTransversalHelperData(List, Right node)
    END FUNCTION


---

# 3. Time Complexity


## Table 1: `TwoDTree` Function

| Operation                | Description                    | Average Case   | Notes                 |
|--------------------------|--------------------------------|----------------|-----------------------|
| `inOrderTransversalData` | Extract all nodes from AVL     | n              | Linear traversal      |
| `insertHelper`           | Build 2D tree                  | n log n        | One visit per element |
| Sum:                     |                                | n log n + n    |                       |
| **TOTAL**                | **Complete tree construction** | **O(n log n)** | Sorting dominates     |

---

## Table 2: `insertHelper` Function

| Operation           | Description                    | Average Case          | Notes                           |
|---------------------|--------------------------------|-----------------------|---------------------------------|
| Base case           | Empty list check               | 1                     | Stops recursion                 |
| Sorting list        | Sort by selected axis          | n log n               | Happens at each recursive level |
| Build node          | Create median node             | 1                     | Constant                        |
| Split list          | Partition into halves          | n                     | Dependent on list size          |
| Recursive calls     | Two subtree constructions      | n log n               | Height × sort cost              |
| Sum:                |                                | 2 * (n log n) + n + 2 |                                 |
| **TOTAL**           | **Recursive kd-tree building** | **O(n log n)**        |                                 |

---

## Table 3: `inOrderTransversalData` Function

| Operation            | Description                  | Avg      | Notes            |
|----------------------|------------------------------|----------|------------------|
| Init list            | Create empty list            | 1        | Constant         |
| Call helper          | Traverse entire AVL          | n        | Visits all nodes |
| Return list          | Return result                | 1        | Constant         |
| Sum:                 |                              | n + 2    |                  |
| **TOTAL**            | **Retrieve AVL sorted data** | **O(n)** | Linear time      |

---

## Table 4: `inOrderTransversalHelperData` Function

| Operation           | Description                | Avg         | Notes                   |
|---------------------|----------------------------|-------------|-------------------------|
| Null check          | Base case                  | 1           | Constant                |
| Left recursion      | Visit left subtree         | n           | Part of full traversal  |
| Add element         | Insert node data           | 1           | Constant                |
| Right recursion     | Visit right subtree        | n           | Part of full traversal  |
| Sum:                |                            | 2 * (n + 1) |                         |
| **TOTAL**           | **Full inorder traversal** | **O(n)**    | Every node visited once |

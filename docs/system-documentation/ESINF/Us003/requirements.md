# US003 - As a planner, I want to pack the allocation rows produced by USEI02 into capacity-bounded trolleys, choosing one of the packing heuristics. So pickers can complete runs without overloading trolleys.

## 1. Requirements Engineering

### 1.1. User Story Description
As a planner, I want to pack the allocation rows produced by USEI02 into capacity-bounded trolleys, choosing one of the packing heuristics (First Fit, First Fit Decreasing, Best Fit Decreasing), so pickers can complete runs without overloading trolleys.
### 1.2. Customer Specifications and Clarifications

#### From the client clarifications
> **Q:**
>
> **A:**

### 1.3. Acceptance Criteria

- AC1: The system must allow the planner to select one of the heuristics (FF, FFD, BFD).

- AC2: There is no limit of trolleys.

- AC3: Trolly capacity should be defined by the planner.


- AC4: Orders must be dispatched into trolleys without exceeding the defined capacity.

- AC5: If a line cannot fit into the current trolley:
	- Split the line into a “partial allocation”, or
	- Defer the full line, logging it as “skipped due to capacity.”

- AC6: For each heuristic execution, the system must display:
	- Total number of trolleys used.
	- Utilisation of each trolley (`usedWeight / capacityWeight`, e.g. 85%).
	- The picking plan per trolley, detailing: `orderId, lineNo, aisle, bay, boxId, SKU, quantity`.


### 1.4. Found out Dependencies
No dependencies

### 1.5 Input and Output Data
**Inputs:**
- Trolley capacity (kg).
- Allocation rows from USEI02: `(orderId, lineNo, sku, qty, boxId, aisle, bay, unitWeight)`.
- Selected packing heuristic (FF, FFD, BFD).

**Outputs:**
- Number of trolleys used.
- For each trolley:
	- Used weight, remaining capacity, % utilisation.
	- List of allocations (picking plan).
- Log of partial or deferred allocations.


### 1.6. System Sequence Diagram (SSD)


### 1.7 Other Relevant Remarks
No other relevant remarks

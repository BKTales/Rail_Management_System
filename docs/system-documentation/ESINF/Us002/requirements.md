# US002 - As a warehouse planner, when I receive open orders, I want the system to examine current inventory and allocate quantities from boxe

## 1. Requirements Engineering

### 1.1. User Story Description
As a warehouse planner, when I receive open orders, I want the system to examine
 current inventory and allocate quantities from boxes in FEFO order, and produce
 per-line statuses (**ELIGIBLE**, **PARTIAL**, **UNDISPATCHABLE**) and a list of
 allocation rows with box and bay information.

### 1.2. Customer Specifications and Clarifications

#### From the client clarifications
> **Q:**
>
> **A:**

### 1.3. Acceptance Criteria

- AC1: Orders are processed by:
	> - priority ASC,
	> - dueDate ASC,
	> - orderId ASC.
- AC2: Within an order, lines are processed by lineNo ASC
- AC3: The system supports two modes: flag eligibility strict, partial, the default is strict.
	> **Strict**: its only **ELIGIBLE** only if its fully allocated otherwise
	> it is **UNDISPATCHABLE**.
	>
	> **Partial**:  any item allocated makes it **PARTIAL** and none **UNDISPATCHABLE**.
- AC4: Give the wanted output

### 1.4. Found out Dependencies
No dependencies

### 1.5 Input and Output Data
Inputs:
- Array of Orders

Outputs:
- A list of orders eligibility
- Output allocations
- Per-line status

### 1.6. System Sequence Diagram (SSD)


### 1.7 Other Relevant Remarks
No other relevant remarks

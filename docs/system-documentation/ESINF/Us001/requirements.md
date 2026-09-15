# US001 - As a terminal operator, I want unloading operations of wagons to automatically store inventory

## 1. Requirements Engineering

### 1.1. User Story Description

As a terminal operator, I want unloading operations of wagons to automatically
 store inventory using FEFO and/or FIFO logic, so that I can ensure
 the correct dispatch order and minimize product spoilage

### 1.2. Customer Specifications and Clarifications

#### From the client clarifications
> **Q:**
>
> **A:**

### 1.3. Acceptance Criteria

- AC1:ach wagon’s contents should be assigned to an aisle/bay, and their
 products must be inserted into the correct position inside the bay accordingly
 to the following rule:
	> - **expiryDate** (earliest first; null last);
	> - **receivedAt** (oldest first);
	> - **boxId ASC** (tie-break);
- AC2: The operation dispatch must always consume stock from the “front” of the
 bay list, guaranteeing FEFO/FIFO behaviour.
- AC3:If a bay becomes empty, it remains in the WMS (the bay still exists), but
 the box list is empty.
- AC4: The operation relocation must update a box’s warehouseId/aisle/bay only (Relocation).
- AC5: The array of products should not be null.
- AC6: Give the wanted output


### 1.4. Found out Dependencies
- Warehouse
- Isle
- Box
- Bay

### 1.5 Input and Output Data
Inputs:
- Array of Products

Outputs:
- Storaged and sorted items inside the warehouse

### 1.6. System Sequence Diagram (SSD)


### 1.7 Other Relevant Remarks
No other relevant remarks.

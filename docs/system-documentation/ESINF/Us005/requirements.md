# US005 - As a quality operator, I want returned goods to be placed in quarantine

## 1. Requirements Engineering

### 1.1. User Story Description
As a quality operator, I want returned goods to be placed in quarantine so that I can inspect them in the reverse order they arrived, latest first (descending by timestamp, ties by returnId ASC), process them, either discard or restock them, and create an audit file detailing the processing done to each product.


### 1.2. Customer Specifications and Clarifications

#### From the client clarifications
> **Q:**
>
> **A:**

### 1.3. Acceptance Criteria

- **AC1:** Ensure that the quarantine exists.

- **AC2:** All customer returns are first placed in **quarantine**.

- **AC3:** Each returned item must be **inspected** ,in the reverse order they arrived (latest first), to decide whether it will be restocked or discarded.

- **AC4:**
    - If **Discarded** → the item is flagged as **unusable** and will not be returned to stock.
    - If **Restocked**, a new box must be created with:
        - `boxId = "RET-" + returnId`
        - `receivedAt = now()`
        - `expiryDate = provided value` or `null` if unknown

- **AC5:** The new box is inserted into the warehouse following **FEFO/FIFO rules** as defined in **USEI01**.

- **AC6:** Each inspection writes a line into an **external audit log file** in the format:
  - `timestamp`: Date and time of the inspection
  - `returnId`: Return identifier
  - `sku`: Product identifier
  - `action`: Action taken (`Restocked` or `Discarded`)
  - `qty`: Total inspected quantity
  - `qtyRestocked` and `qtyDiscarded`: (if applicable) when items are partially restocked and partially discarded

    #### Examples:

  - 2025-09-22 14:32 | returnId=R105 | sku=SKU123 | action=Restocked | qty=8  
  - 2025-09-22 14:40 | returnId=R106 | sku=SKU200 | action=Discarded

### 1.4. Found out Dependencies

No dependencies

### 1.5 Input and Output Data
Inputs:
- Array of returned goods;

Outputs:
- Log file with info from each inspections;

### 1.6. System Sequence Diagram (SSD)

### 1.7 Other Relevant Remarks
No other relevant remarks.

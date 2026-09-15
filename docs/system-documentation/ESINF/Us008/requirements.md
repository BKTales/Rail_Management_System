# USEI08 – Search by Geographical Area

## 1. Requirements Engineering

### 1.1. User Story Description
As a planner, I want to query a 2D-tree (KD-tree) for all stations within a latitude/longitude rectangle, optionally filtered by type or country, so that I can quickly extract all relevant stations without scanning the entire dataset.

### 1.2. Customer Specifications and Clarifications

#### From client clarifications
> **Q:** 
> **A:** 

### 1.3. Acceptance Criteria

- **AC1:** Validate that latitude and longitude of each stations are within [latMin, latMax] and [lonMin, lonMax], inclusive.
- **AC2:** Search must return all stations inside the specified rectangle (inclusive).
- **AC3:** Optional filters can be applied:
    - `isCity` = true | false
    - `isMainStation` = true | false
    - `country` = PT | ES | all
- **AC4:** KD-tree pruning must be used to avoid scanning the entire dataset.

### 1.4. Dependencies
- KD-tree structure must already exist with all stations inserted.

### 1.5. Input and Output Data
**Inputs:**
- Rectangle bounds: `latMin`, `latMax`, `lonMin`, `lonMax`
- Optional filters: `isCity`, `isMainStation`, `country`

**Outputs:**
- List of stations satisfying rectangle bounds and optional filters, sorted as specified.

### 1.6. System Sequence Diagram (SSD)

### 1.7 Other Relevant Remarks
- Analysis of temporal complexity and sample queries/tests will be necessary.

